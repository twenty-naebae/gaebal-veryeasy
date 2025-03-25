package com.gaebal_easy.client.hub.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaebal_easy.client.hub.application.dto.*;
import com.gaebal_easy.client.hub.domain.entity.Hub;
import com.gaebal_easy.client.hub.domain.entity.HubDirectMovementInfo;
import com.gaebal_easy.client.hub.domain.repository.HubDirectRepository;
import com.gaebal_easy.client.hub.domain.repository.HubRepository;
import gaebal_easy.common.global.exception.CanNotFindHubNameException;
import gaebal_easy.common.global.exception.Code;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class HubMovementService {

    private final HubDirectRepository hubDirectRepository;
    private final HubRepository hubRepository;
    private final NaverDirectionApiService naverDirectionApiService;
    private final HubDirectRedisService hubDirectRedisService;
    private final HubRouteRedisService hubRouteService;
    private final Map<String, List<EdgeDto>> graph = new HashMap<>();

    public HubDirectDto getDirectHub(String depart, String arrive) {
        // 앞뒤 공백 제거
        depart = depart.replaceAll("\\s+", "");
        arrive = arrive.replaceAll("\\s+", "");
        HubDirectDto hubDirectDto = new HubDirectDto();
        System.out.println("@@@@@@@@@depart : " + depart + " arrive : " + arrive);

        if(depart.equals(arrive)) return hubDirectDto;

        List<HubDirectMovementInfo> hubDirectMovementInfos = hubDirectRepository.findAll();
        List<Hub> hubList = hubRepository.findAll();

        // 허브 이름 유효성 검증
        if(!checkValidHubName(depart,arrive,hubList)) throw new CanNotFindHubNameException(Code.HUB_CAN_NOT_FIND_HUB_NAME);

        for(HubDirectMovementInfo hubDirectMovementInfo : hubDirectMovementInfos){
            double departLatitude = 0;double departLongitude = 0;double arriveLatitude = 0;double arriveLongitude = 0;
            for(Hub hub : hubList){
                String address = hub.getHubLocation().getName().substring(0,4);
                if(address.equals(hubDirectMovementInfo.getDepart())){
                    departLatitude = hub.getHubLocation().getLatitude();
                    departLongitude = hub.getHubLocation().getLongitude();
                }else if(address.equals(hubDirectMovementInfo.getArrive())){
                    arriveLatitude = hub.getHubLocation().getLatitude();
                    arriveLongitude = hub.getHubLocation().getLongitude();
                }
            }

            String json = naverDirectionApiService.getDirection(departLongitude,departLatitude,arriveLongitude,arriveLatitude);

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                NaverRouteResponse response = objectMapper.readValue(json, NaverRouteResponse.class);
                double distance = response.getRoute().getTrafast().get(0).getSummary().getDistance();
                int duration = response.getRoute().getTrafast().get(0).getSummary().getDuration();
                hubDirectRepository.save(new HubDirectMovementInfo(hubDirectMovementInfo.getId(),hubDirectMovementInfo.getDepart(),hubDirectMovementInfo.getArrive(),duration,distance));
                System.out.println("거리: " + distance + "m");
                System.out.println("소요 시간: " + duration + "초");
                if((hubDirectMovementInfo.getDepart().equals(depart)&&hubDirectMovementInfo.getArrive().equals(arrive)) ||
                        (hubDirectMovementInfo.getDepart().equals(arrive)&&hubDirectMovementInfo.getArrive().equals(depart))
                ){
                    hubDirectDto = HubDirectDto.of(new HubDirectMovementInfo(hubDirectMovementInfo.getId(),depart, arrive, duration, distance));
                }
                hubDirectRedisService.saveRedis(new HubDirectDto(hubDirectMovementInfo.getDepart(),hubDirectMovementInfo.getArrive(),duration,distance));
                hubDirectRedisService.saveRedis(new HubDirectDto(hubDirectMovementInfo.getArrive(),hubDirectMovementInfo.getDepart(),duration,distance));
            } catch (JsonProcessingException e) {
                System.out.println("JSON 파싱 오류: " + e.getMessage());
            }
        }
        return hubDirectDto;
    }

    public HubRouteDto getHubRoute(String depart, String arrive) {
        HubRouteDto hubRouteDto = new HubRouteDto();
        if(depart.equals(arrive)) return hubRouteDto;
        if(hubDirectRedisService.getDirectRedis(depart,arrive)==null) getDirectHub(depart, arrive);
        List<HubDirectMovementInfo> hubDirectMovementInfos = hubDirectRepository.findAll();
        List<Hub> hubList = hubRepository.findAll();
        for(HubDirectMovementInfo info : hubDirectMovementInfos){
            makeGraph(info.getDepart(), info.getArrive(), info.getRequiredTime(), info.getDistance());
            makeGraph(info.getArrive(), info.getDepart(), info.getRequiredTime(), info.getDistance());
        }

        for(Hub hub : hubList){
            String start = hub.getHubLocation().getName().substring(0,4);
            Map<String, HubRouteDto> routeDtoMap = dijkstra(start);

            for (HubRouteDto hubRoute : routeDtoMap.values()) {
                if(hubRoute.getDepart().equals(depart)&&hubRoute.getArrive().equals(arrive))
                    hubRouteDto = hubRoute;
                hubRouteService.saveRedis(hubRoute);
            }
        }

        return hubRouteDto;
    }

    private Map<String, HubRouteDto> dijkstra(String start) {
        Map<String, Double> totalDistance = new HashMap<>();
        Map<String, Integer> totalTime = new HashMap<>();
        Map<String, String> preEdge = new HashMap<>();
        PriorityQueue<NodeDto> pq = new PriorityQueue<>((o1,o2)->{
            return (int) (o1.getTotalDist()-o2.getTotalDist());
        });

        for (String name : graph.keySet()) {
            totalDistance.put(name, 2000000000D);
            totalTime.put(name, 2000000000);
        }

        totalDistance.put(start, 0D);
        totalTime.put(start,0);
        pq.offer(new NodeDto(start, 0,0D));

        while (!pq.isEmpty()) {
            NodeDto cur = pq.poll();
            String now = cur.getName();

            if (cur.getTotalDist()>totalDistance.get(now)) continue;

            for (EdgeDto edge : graph.getOrDefault(now, new ArrayList<>())) {
                double newDist = totalDistance.get(now) + edge.getDist();
                int newTime = totalTime.get(now)+edge.getTime();
                if (newDist < totalDistance.getOrDefault(edge.getName(), 2000000000D)) {
                    totalDistance.put(edge.getName(), newDist);
                    totalTime.put(edge.getName(), newTime);
                    preEdge.put(edge.getName(), now);
                    pq.offer(new NodeDto(edge.getName(), newTime,newDist));
                }
            }
        }

        Map<String, HubRouteDto> result = new HashMap<>();
        for (String end : totalDistance.keySet()) {
            if (start.equals(end)) continue;

            List<String> visitHub = new ArrayList<>();

            String cur = end;
            while (preEdge.containsKey(cur)) {
                visitHub.add(0, cur);
                cur = preEdge.get(cur);
            }
            visitHub.add(0, start); // 시작 지점 추가

            result.put(end, new HubRouteDto(start, end,totalTime.get(end), totalDistance.get(end), visitHub));
        }
        return result;
    }


    private void makeGraph(String depart, String arrive, int time, double dist) {
        List<EdgeDto> list = graph.get(depart);
        if (list == null) list = new ArrayList<>();
        list.add(new EdgeDto(arrive, time, dist));
        graph.put(depart, list);
    }

    private boolean checkValidHubName(String depart, String arrive, List<Hub> hubList){
        boolean checkValidDepart = false;
        boolean checkValidArrive = false;
        for(Hub hub : hubList){
            String hubName = hub.getHubLocation().getName().substring(0,4);
            System.out.println("hubName : " + hubName);
            if(hubName.equals(depart)) {
                System.out.println("depart 걸림!!!");
                checkValidDepart=true;
            }
            if(hubName.equals(arrive)) {
                System.out.println("arrive 걸림!!!");
                checkValidArrive=true;
            }
        }
        return checkValidDepart && checkValidArrive;
    }

}

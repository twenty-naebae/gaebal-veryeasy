package com.gaebal_easy.client.hub.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaebal_easy.client.hub.application.dto.HubDirectDto;
import com.gaebal_easy.client.hub.application.dto.HubResponseDto;
import com.gaebal_easy.client.hub.application.dto.NaverRouteResponse;
import com.gaebal_easy.client.hub.application.dto.ProductResponseDto;
import com.gaebal_easy.client.hub.domain.entity.Hub;
import com.gaebal_easy.client.hub.domain.entity.HubDirectMovementInfo;
import com.gaebal_easy.client.hub.domain.entity.HubProductList;
import com.gaebal_easy.client.hub.domain.repository.HubDirectRepository;
import com.gaebal_easy.client.hub.domain.repository.HubProductListRepository;
import com.gaebal_easy.client.hub.domain.repository.HubRepository;
import com.gaebal_easy.client.hub.presentation.dto.HubRequestDto;
import gaebal_easy.common.global.exception.CanNotFindProductInHubException;
import gaebal_easy.common.global.exception.Code;
import gaebal_easy.common.global.exception.HubNotFoundException;
import gaebal_easy.common.global.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubService {

    private final HubRepository hubRepository;
    private final HubProductListRepository hubProductListRepository;
    private final HubDirectRepository hubDirectRepository;
    private final NaverDirectionApiService naverDirectionApiService;
    private final HubDirectRedisService hubDirectRedisService;

    public ProductResponseDto getProduct(UUID productId, UUID hubId) {
        Hub hub = getHub(hubId);
        HubProductList hubProductList = getHubProductList(productId);
        if(!hubProductList.getHub().getId().equals(hub.getId()))
            throw new CanNotFindProductInHubException(Code.HUB_CAN_NOT_FIND_PRODUCT_IN_HUB);
        return ProductResponseDto.of(hubProductList,hub);
    }

    public void deleteHub(UUID id) {
        Hub hub = getHub(id);
        // TODO : 유저네임 넘겨주기, 이미 삭제된 허브가 아닌지 판별 deletedAtIsNotNull
        hub.delete("deleted by");
        hubRepository.save(hub);
    }

    public HubResponseDto requireHub(UUID id) {
        Hub hub = getHub(id);
        return HubResponseDto.of(hub);
    }

    private Hub getHub(UUID id){
        return hubRepository.getHub(id).orElseThrow(() -> new HubNotFoundException(Code.HUB_NOT_FOUND));
    }
    private HubProductList getHubProductList(UUID id){
        return hubProductListRepository.getProduct(id).orElseThrow(() -> new ProductNotFoundException(Code.HUB_PRODUCT_NOT_FOUND));
    }


    public HubDirectDto getDirectHub(String depart, String arrive) {
        HubDirectDto hubDirectDto = new HubDirectDto();
        List<HubDirectMovementInfo> hubDirectMovementInfos = hubDirectRepository.findAll();
        List<Hub> hubList = hubRepository.findAll();
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
            System.out.println("출발 좌표: " + departLatitude + ", " + departLongitude);
            System.out.println("도착 좌표: " + arriveLatitude + ", " + arriveLongitude);

            String json = naverDirectionApiService.getDirection(departLongitude,departLatitude,arriveLongitude,arriveLatitude);
            System.out.println(json);
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                NaverRouteResponse response = objectMapper.readValue(json, NaverRouteResponse.class);
                int distance = response.getRoute().getTrafast().get(0).getSummary().getDistance();
                int duration = response.getRoute().getTrafast().get(0).getSummary().getDuration();
                hubDirectRepository.save(new HubDirectMovementInfo(hubDirectMovementInfo.getId(),hubDirectMovementInfo.getDepart(),hubDirectMovementInfo.getArrive(),duration,distance));
                System.out.println("거리: " + distance + "m");
                System.out.println("소요 시간: " + duration + "초");
                if(hubDirectMovementInfo.getDepart().equals(depart)&&hubDirectMovementInfo.getArrive().equals(arrive) ||
                        hubDirectMovementInfo.getDepart().equals(arrive)&&hubDirectMovementInfo.getArrive().equals(depart)
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
}

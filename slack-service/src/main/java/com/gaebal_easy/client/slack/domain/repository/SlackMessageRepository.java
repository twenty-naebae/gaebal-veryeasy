package com.gaebal_easy.client.slack.domain.repository;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gaebal_easy.client.slack.domain.entity.SlackMessage;

public interface SlackMessageRepository extends JpaRepository<SlackMessage, UUID> {
	Page<SlackMessage> findAllByReceiveId(UUID receiveId, Pageable pageable);
}

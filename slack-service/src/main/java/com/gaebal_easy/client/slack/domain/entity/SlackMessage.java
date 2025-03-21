package com.gaebal_easy.client.slack.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import gaebal_easy.common.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_slack_message")
public class SlackMessage extends BaseTimeEntity {
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "VARCHAR(36)")
	private UUID id;

	@Column(nullable = false)
	private UUID receiveId;

	@Column(nullable = false, columnDefinition = "LONGTEXT")
	private String message;

	@Column(nullable = false)
	private LocalDateTime sendTime;

	public SlackMessage(UUID receiveId, String message, LocalDateTime sendTime) {
		this.receiveId = receiveId;
		this.message = message;
		this.sendTime = sendTime;
	}
}

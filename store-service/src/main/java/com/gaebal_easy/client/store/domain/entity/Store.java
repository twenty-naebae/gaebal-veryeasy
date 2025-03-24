package com.gaebal_easy.client.store.domain.entity;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.gaebal_easy.client.store.presentation.dto.StoreRequest;

import gaebal_easy.common.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_store")
public class Store extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private StoreType type;

	@Column(nullable = false)
	private UUID hubId;

	private String address;

	@Column(nullable = false)
	private Long managerId;

	public static Store create(UUID hubId, Long managerId, StoreRequest.postStoreRequest request) {
		return Store.builder()
			.name(request.getName())
			.type(request.getType())
			.hubId(hubId)
			.address(request.getAddress())
			.managerId(managerId)
			.build();
	}

	public void update(StoreRequest.updateStoreRequest request) {
		this.name = request.getName();
		this.type = request.getType();
		this.hubId = request.getHubId();
		this.address = request.getAddress();
		this.managerId = request.getManagerId();
	}

}

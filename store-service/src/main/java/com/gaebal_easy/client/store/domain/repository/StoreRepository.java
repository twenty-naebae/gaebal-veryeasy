package com.gaebal_easy.client.store.domain.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gaebal_easy.client.store.domain.entity.Store;


@Repository("storeRepositoryJpa")
public interface StoreRepository extends JpaRepository<Store, UUID> {
	Page<Store> findAllByHubId(UUID hubId, Pageable pageable);

	@Query("SELECT s FROM Store s WHERE s.name LIKE %:name% OR s.address LIKE %:address%")
	Page<Store> findByNameContainingOrAddressContaining(@Param("name") String name,
		@Param("address") String address,
		Pageable pageable);
}

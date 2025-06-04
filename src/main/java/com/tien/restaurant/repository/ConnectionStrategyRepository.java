package com.tien.restaurant.repository;

import com.tien.restaurant.entity.ConnectionStrategy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionStrategyRepository extends JpaRepository<ConnectionStrategy, Long> {
    List<ConnectionStrategy> findByDeviceId(Long deviceId);
}
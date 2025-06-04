package com.tien.restaurant.repository;

import com.tien.restaurant.entity.ConnectionStrategy;
import com.tien.restaurant.entity.ConnectionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionStrategyRepository extends JpaRepository<ConnectionStrategy, Long> {
    List<ConnectionStrategy> findByDeviceId(Long deviceId);

    Optional<ConnectionStrategy> findByConnectionTypeAndAgentId(ConnectionType type, String agentId);
    Optional<ConnectionStrategy> findByConnectionTypeAndIpAddressAndPort(ConnectionType type, String ip, Integer port);
}
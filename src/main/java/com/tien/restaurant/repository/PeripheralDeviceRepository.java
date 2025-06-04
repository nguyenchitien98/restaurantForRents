package com.tien.restaurant.repository;

import com.tien.restaurant.entity.DeviceStatus;
import com.tien.restaurant.entity.DeviceType;
import com.tien.restaurant.entity.PeripheralDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeripheralDeviceRepository extends JpaRepository<PeripheralDevice, Long> {
    List<PeripheralDevice> findByType(DeviceType type);
    List<PeripheralDevice> findByStatus(DeviceStatus status);
}
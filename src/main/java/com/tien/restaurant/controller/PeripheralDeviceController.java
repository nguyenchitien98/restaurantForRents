package com.tien.restaurant.controller;

import com.tien.restaurant.dto.PeripheralDeviceDTO;
import com.tien.restaurant.dto.request.CreatePeripheralDeviceRequest;
import com.tien.restaurant.dto.request.UpdatePeripheralDeviceRequest;
import com.tien.restaurant.entity.ConnectionStrategy;
import com.tien.restaurant.entity.PeripheralDevice;
import com.tien.restaurant.service.PeripheralDeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class PeripheralDeviceController {

    private final PeripheralDeviceService deviceService;

    // Devices
    @GetMapping
    public ResponseEntity<List<PeripheralDeviceDTO>> getAll() {
        return ResponseEntity.ok(deviceService.getAllDevices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeripheralDeviceDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(deviceService.getById(id));
    }

    @PostMapping
    public ResponseEntity<PeripheralDeviceDTO> create(@RequestBody CreatePeripheralDeviceRequest request) {
        return ResponseEntity.ok(deviceService.createDevice(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PeripheralDeviceDTO> update(@PathVariable Long id, @RequestBody UpdatePeripheralDeviceRequest request) {
        return ResponseEntity.ok(deviceService.updateDevice(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }

    // Connection strategies
    @GetMapping("/{deviceId}/connections")
    public List<ConnectionStrategy> getConnections(@PathVariable Long deviceId) {
        return deviceService.getConnections(deviceId);
    }

    @PostMapping("/{deviceId}/connections")
    public ConnectionStrategy addConnection(@PathVariable Long deviceId, @RequestBody ConnectionStrategy strategy) {
        return deviceService.addConnection(deviceId, strategy);
    }

    @DeleteMapping("/connections/{strategyId}")
    public ResponseEntity<Void> deleteConnection(@PathVariable Long strategyId) {
        deviceService.deleteConnection(strategyId);
        return ResponseEntity.noContent().build();
    }
}

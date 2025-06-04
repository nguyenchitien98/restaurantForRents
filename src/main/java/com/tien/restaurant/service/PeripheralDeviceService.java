package com.tien.restaurant.service;

import com.tien.multitenancy.config.TenantContext;
import com.tien.restaurant.dto.PeripheralDeviceDTO;
import com.tien.restaurant.dto.request.ConnectionCheckRequest;
import com.tien.restaurant.dto.request.CreatePeripheralDeviceRequest;
import com.tien.restaurant.dto.request.UpdatePeripheralDeviceRequest;
import com.tien.restaurant.entity.ConnectionStrategy;
import com.tien.restaurant.entity.ConnectionType;
import com.tien.restaurant.entity.DeviceStatus;
import com.tien.restaurant.entity.PeripheralDevice;
import com.tien.restaurant.mapper.PeripheralDeviceMapper;
import com.tien.restaurant.repository.ConnectionStrategyRepository;
import com.tien.restaurant.repository.PeripheralDeviceRepository;
import com.tien.tenant.service.TenantService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PeripheralDeviceService {

    private final PeripheralDeviceRepository deviceRepository;
    private final ConnectionStrategyRepository connectionRepository;
    private final TenantService tenantPlanService;

    public List<PeripheralDeviceDTO> getAllDevices() {
        return deviceRepository.findAll()
                .stream()
                .map(PeripheralDeviceMapper::toDTO)
                .toList();
    }

    public PeripheralDeviceDTO getById(Long id) {
        PeripheralDevice device = deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thiết bị"));
        return PeripheralDeviceMapper.toDTO(device);
    }

    @Transactional
    public PeripheralDeviceDTO createDevice(CreatePeripheralDeviceRequest request) {
        String tenantId = TenantContext.getTenant();
        String plan = tenantPlanService.getPlanByTenantId(tenantId);

        if ("basic".equalsIgnoreCase(plan)) {
            long count = deviceRepository.count();
            if (count >= 3) {
                throw new IllegalStateException("Gói Basic chỉ cho phép tối đa 3 thiết bị");
            }
        }

        PeripheralDevice device = PeripheralDeviceMapper.fromCreateRequest(request);
        PeripheralDevice savedDevice = deviceRepository.save(device);

        if (request.getConnection() != null) {
            ConnectionStrategy strategy = PeripheralDeviceMapper.fromRequest(request.getConnection());
            strategy.setDevice(savedDevice);
            connectionRepository.save(strategy);
            savedDevice.setConnectionStrategies(List.of(strategy));
        }

        return PeripheralDeviceMapper.toDTO(savedDevice);
    }

    @Transactional
    public PeripheralDeviceDTO updateDevice(Long id, UpdatePeripheralDeviceRequest request) {
        PeripheralDevice device = deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thiết bị"));

        PeripheralDeviceMapper.updateFromRequest(device, request);
        deviceRepository.save(device);

        // xử lý connection: giả định chỉ sửa connection đầu tiên (nếu có)
        if (request.getConnection() != null) {
            List<ConnectionStrategy> existingConnections = device.getConnectionStrategies();

            if (existingConnections != null && !existingConnections.isEmpty()) {
                ConnectionStrategy existing = existingConnections.get(0);
                PeripheralDeviceMapper.updateConnection(existing, request.getConnection());
                connectionRepository.save(existing);
            } else {
                ConnectionStrategy strategy = PeripheralDeviceMapper.fromRequest(request.getConnection());
                strategy.setDevice(device);
                connectionRepository.save(strategy);
                device.setConnectionStrategies(List.of(strategy));
            }
        }

        return PeripheralDeviceMapper.toDTO(device);
    }

    public void deleteDevice(Long id) {
        deviceRepository.deleteById(id);
    }

    // Connection strategies
    public ConnectionStrategy addConnection(Long deviceId, ConnectionStrategy strategy) {
        PeripheralDevice device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thiết bị"));

        strategy.setDevice(device);
        ConnectionStrategy saved = connectionRepository.save(strategy);

        // Đồng bộ 2 chiều nếu cần
        device.getConnectionStrategies().add(saved);
        return saved;
    }

    public List<ConnectionStrategy> getConnections(Long deviceId) {
        return connectionRepository.findByDeviceId(deviceId);
    }

    public void deleteConnection(Long strategyId) {
        connectionRepository.deleteById(strategyId);
    }

    @Transactional
    public Optional<PeripheralDevice> verifyAndUpdateDevice(ConnectionCheckRequest request) {
        Optional<ConnectionStrategy> strategyOptional = Optional.empty();

        if (request.getConnectionType() == ConnectionType.AGENT) {
            strategyOptional = connectionRepository
                    .findByConnectionTypeAndAgentId(ConnectionType.AGENT, request.getAgentId());
        } else if (request.getConnectionType() == ConnectionType.LAN) {
            strategyOptional = connectionRepository
                    .findByConnectionTypeAndIpAddressAndPort(
                            ConnectionType.LAN,
                            request.getIpAddress(),
                            request.getPort()
                    );
        }

        if (strategyOptional.isEmpty()) return Optional.empty();

        ConnectionStrategy strategy = strategyOptional.get();
        PeripheralDevice device = strategy.getDevice();
        device.setStatus(DeviceStatus.CONNECTED);
        deviceRepository.save(device);

        return Optional.of(device);
    }
}
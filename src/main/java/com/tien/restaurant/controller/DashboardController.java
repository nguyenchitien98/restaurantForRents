package com.tien.restaurant.controller;

import com.tien.restaurant.dto.response.DailyOrderStatResponse;
import com.tien.restaurant.dto.response.DashboardStatResponse;
import com.tien.restaurant.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public ResponseEntity<List<DashboardStatResponse>> getSummary() {
        return ResponseEntity.ok(dashboardService.getDashboardSummary());
    }

    @GetMapping("/orders-per-day")
    public ResponseEntity<List<DailyOrderStatResponse>> getOrdersPerDay() {
        return ResponseEntity.ok(dashboardService.getOrdersPerDay());
    }
}
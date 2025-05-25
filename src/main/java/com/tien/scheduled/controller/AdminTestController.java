package com.tien.scheduled.controller;

import com.tien.scheduled.service.RetentionJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// This controller for test
@RestController
@RequestMapping("/admin/test")
@RequiredArgsConstructor
public class AdminTestController {

    private final RetentionJobService dataRetentionJob;

    @GetMapping("/notify")
    public void testNotify() {
        dataRetentionJob.notifyExpiringTenants();
    }

    @GetMapping("/remind")
    public void testRemind() {
        dataRetentionJob.remindFinalWarningTenants();
    }

    @GetMapping("/delete")
    public void testDelete() {
        dataRetentionJob.deleteExpiredData();
    }
}
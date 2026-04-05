package com.junaid.finance_backend.controller;

import com.junaid.finance_backend.model.DashboardResponse;
import com.junaid.finance_backend.model.TransactionRecord;
import com.junaid.finance_backend.service.DashboardService;
import com.junaid.finance_backend.service.RecordService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@SecurityRequirement(name = "basicAuth")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private RecordService recordService;

    @GetMapping("/summary")
    public ResponseEntity<DashboardResponse> getSummary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }

    @GetMapping("/category")
    public Map<String, Double> getCategorySummary() {
        return recordService.getCategorySummary();
    }

    @GetMapping("/recent")
    public List<TransactionRecord> getRecentActivity(
            @RequestParam(defaultValue = "10") int limit) {
        return dashboardService.getRecentActivity(limit);
    }
}

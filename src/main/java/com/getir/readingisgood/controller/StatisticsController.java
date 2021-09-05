package com.getir.readingisgood.controller;

import com.getir.readingisgood.model.response.MonthlyStatsResponse;
import com.getir.readingisgood.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/getMonthlyStatistics")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<List<MonthlyStatsResponse>> getMonthlyStatistics() {
        return ResponseEntity.ok(statisticsService.getMonthlyStatistics());
    }

}

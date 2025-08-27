package com.example.crm.controller;

import com.example.crm.dto.SellerDTO;
import com.example.crm.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/most-productive")
    public SellerDTO getMostProductiveSeller(@RequestParam String period) {
        return analyticsService.getMostProductiveSeller(period);
    }

    @GetMapping("/sellers-less-than")
    public List<SellerDTO> getSellersWithAmountLessThan(@RequestParam BigDecimal amount, @RequestParam String period) {
        return analyticsService.getSellersWithAmountLessThan(amount, period);
    }

    @GetMapping("/best-period/{sellerId}")
    public Map<String, Object> getBestPeriodForSeller(@PathVariable Long sellerId) {
        return analyticsService.getBestPeriodForSeller(sellerId);
    }
}
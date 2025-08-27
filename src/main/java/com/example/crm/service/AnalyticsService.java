package com.example.crm.service;

import com.example.crm.dto.SellerDTO;
import com.example.crm.entity.Seller;
import com.example.crm.entity.Transaction;
import com.example.crm.repository.SellerRepository;
import com.example.crm.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
public class AnalyticsService {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public SellerDTO getMostProductiveSeller(String period) {
        LocalDateTime start = getStartDateForPeriod(period);
        LocalDateTime end = LocalDateTime.now();

        List<Object[]> results = sellerRepository.findSellersWithTotalAmountBetween(start, end);
        if (results.isEmpty()) {
            return null;
        }
        Seller seller = (Seller) results.get(0)[0];
        return convertToDTO(seller);
    }

    public List<SellerDTO> getSellersWithAmountLessThan(BigDecimal amount, String period) {
        LocalDateTime start = getStartDateForPeriod(period);
        LocalDateTime end = LocalDateTime.now();

        List<Object[]> results = sellerRepository.findSellersWithTotalAmountBetween(start, end);
        List<SellerDTO> filtered = new ArrayList<>();
        for (Object[] row : results) {
            Seller seller = (Seller) row[0];
            BigDecimal total = (BigDecimal) row[1];
            if (total.compareTo(amount) < 0) {
                filtered.add(convertToDTO(seller));
            }
        }
        return filtered;
    }

    public Map<String, Object> getBestPeriodForSeller(Long sellerId) {
        List<Transaction> transactions = transactionRepository.findBySellerId(sellerId);
        if (transactions.isEmpty()) {
            return null;
        }
        //Здесь использую метод скользящего окна на 30 дней
        transactions.sort(Comparator.comparing(Transaction::getTransactionDate));

        int maxCount = 0;
        LocalDateTime bestStart = null;
        LocalDateTime bestEnd = null;

        int left = 0;
        for (int right = 0; right < transactions.size(); right++) {
            while (left < right && transactions.get(right).getTransactionDate().minusDays(30).isAfter(transactions.get(left).getTransactionDate())) {
                left++;
            }
            int currentCount = right - left + 1;
            if (currentCount > maxCount) {
                maxCount = currentCount;
                bestStart = transactions.get(left).getTransactionDate();
                bestEnd = transactions.get(right).getTransactionDate();
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("start", bestStart);
        result.put("end", bestEnd);
        result.put("count", maxCount);
        return result;
    }

    private LocalDateTime getStartDateForPeriod(String period) {
        LocalDate today = LocalDate.now();
        return switch (period.toUpperCase()) {
            case "DAY" -> today.atStartOfDay();
            case "MONTH" -> today.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
            case "QUARTER" -> today.with(today.getMonth().firstMonthOfQuarter()).with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
            case "YEAR" -> today.with(TemporalAdjusters.firstDayOfYear()).atStartOfDay();
            default -> throw new IllegalArgumentException("Invalid period: " + period);
        };
    }

    private SellerDTO convertToDTO(Seller seller) {
        SellerDTO dto = new SellerDTO();
        dto.setId(seller.getId());
        dto.setName(seller.getName());
        dto.setContactInfo(seller.getContactInfo());
        dto.setRegistrationDate(seller.getRegistrationDate());
        return dto;
    }
}
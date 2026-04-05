package com.junaid.finance_backend.service;

import com.junaid.finance_backend.model.DashboardResponse;
import com.junaid.finance_backend.model.TransactionRecord;
import com.junaid.finance_backend.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private RecordRepository recordRepository;

    public DashboardResponse getSummary() {
        Double income = recordRepository.getTotalIncome();
        Double expense = recordRepository.getTotalExpense();
        if (income == null) {
            income = 0.0;
        }
        if (expense == null) {
            expense = 0.0;
        }
        Double balance = income - expense;
        return new DashboardResponse(income, expense, balance);
    }

    /**
     * Most recent records by date (newest first). Limit is clamped between 1 and 50.
     */
    public List<TransactionRecord> getRecentActivity(int limit) {
        int cap = Math.min(Math.max(limit, 1), 50);
        return recordRepository.findAll(PageRequest.of(0, cap, Sort.by(Sort.Direction.DESC, "date")))
                .getContent();
    }
}

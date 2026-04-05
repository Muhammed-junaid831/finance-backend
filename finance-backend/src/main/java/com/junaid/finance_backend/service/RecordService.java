package com.junaid.finance_backend.service;

import com.junaid.finance_backend.exception.ResourceNotFoundException;
import com.junaid.finance_backend.model.TransactionRecord;
import com.junaid.finance_backend.model.Type;
import com.junaid.finance_backend.repository.RecordRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class RecordService {

    @Autowired
    private RecordRepository recordRepo;

    public TransactionRecord save(TransactionRecord record) {
        return recordRepo.save(record);
    }

    public List<TransactionRecord> getAll() {
        return recordRepo.findAll();
    }

    public List<TransactionRecord> findWithFilters(Type type, String category, java.time.LocalDate from, java.time.LocalDate to) {
        return recordRepo.findAll(buildSpec(type, category, from, to));
    }

    public TransactionRecord findById(Long id) {
        return recordRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found: id=" + id));
    }

    public TransactionRecord update(Long id, TransactionRecord incoming) {
        TransactionRecord existing = findById(id);
        existing.setAmount(incoming.getAmount());
        existing.setType(incoming.getType());
        existing.setCategory(incoming.getCategory());
        existing.setDate(incoming.getDate());
        existing.setNote(incoming.getNote());
        return recordRepo.save(existing);
    }

    public void delete(Long id) {
        if (!recordRepo.existsById(id)) {
            throw new ResourceNotFoundException("Record not found: id=" + id);
        }
        recordRepo.deleteById(id);
    }

    private Specification<TransactionRecord> buildSpec(Type type, String category, java.time.LocalDate from, java.time.LocalDate to) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (type != null) {
                predicates.add(cb.equal(root.get("type"), type));
            }
            if (category != null && !category.isBlank()) {
                predicates.add(cb.equal(cb.lower(root.get("category")), category.toLowerCase(Locale.ROOT).trim()));
            }
            if (from != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), from));
            }
            if (to != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), to));
            }
            if (predicates.isEmpty()) {
                return cb.conjunction();
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    public Map<String, Double> getCategorySummary() {
        List<Object[]> results = recordRepo.getExpenseByCategory();
        Map<String, Double> summary = new HashMap<>();
        for (Object[] row : results) {
            String cat = (String) row[0];
            Double total = (Double) row[1];
            summary.put(cat, total);
        }
        return summary;
    }

    public Map<String, Double> getSummary(String type) {
        Map<String, Double> summary = new HashMap<>();
        List<Object[]> results;

        if ("monthly".equalsIgnoreCase(type)) {
            results = recordRepo.getMonthlySummary();
            for (Object[] row : results) {
                Integer monthNumber = (Integer) row[0];
                Double total = (Double) row[1];
                String monthName = Month.of(monthNumber).getDisplayName(TextStyle.FULL, Locale.ENGLISH);
                summary.put(monthName, total);
            }
        } else if ("weekly".equalsIgnoreCase(type)) {
            results = recordRepo.getWeeklySummary();
            for (Object[] row : results) {
                Integer weekNumber = (Integer) row[0];
                Double total = (Double) row[1];
                summary.put("Week " + weekNumber, total);
            }
        } else {
            throw new IllegalArgumentException("Invalid type: use 'monthly' or 'weekly'");
        }
        return summary;
    }
}

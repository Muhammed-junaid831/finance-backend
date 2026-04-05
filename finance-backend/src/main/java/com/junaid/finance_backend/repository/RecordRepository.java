package com.junaid.finance_backend.repository;

import com.junaid.finance_backend.model.TransactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecordRepository extends JpaRepository<TransactionRecord, Long>, JpaSpecificationExecutor<TransactionRecord> {

    @Query("SELECT SUM(r.amount) FROM TransactionRecord r WHERE r.type = 'INCOME'")
    Double getTotalIncome();

    @Query("SELECT SUM(r.amount) FROM TransactionRecord r WHERE r.type = 'EXPENSE'")
    Double getTotalExpense();

    @Query("SELECT r.category, SUM(r.amount) FROM TransactionRecord r WHERE r.type = 'EXPENSE' GROUP BY r.category")
    List<Object[]> getExpenseByCategory();

    @Query("SELECT MONTH(r.date), SUM(r.amount) FROM TransactionRecord r GROUP BY MONTH(r.date)")
    List<Object[]> getMonthlySummary();

    @Query("SELECT WEEK(r.date), SUM(r.amount) FROM TransactionRecord r GROUP BY WEEK(r.date)")
    List<Object[]> getWeeklySummary();
}

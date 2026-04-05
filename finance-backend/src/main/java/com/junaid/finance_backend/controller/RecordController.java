package com.junaid.finance_backend.controller;

import com.junaid.finance_backend.model.TransactionRecord;
import com.junaid.finance_backend.model.Type;
import com.junaid.finance_backend.service.RecordService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/records")
@SecurityRequirement(name = "basicAuth")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @PostMapping
    public ResponseEntity<TransactionRecord> create(@Valid @RequestBody TransactionRecord record) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recordService.save(record));
    }

    @GetMapping
    public List<TransactionRecord> list(
            @RequestParam(required = false) Type type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return recordService.findWithFilters(type, category, from, to);
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Double>> getSummary(@RequestParam String type) {
        return ResponseEntity.ok(recordService.getSummary(type));
    }

    @GetMapping("/{id}")
    public TransactionRecord get(@PathVariable Long id) {
        return recordService.findById(id);
    }

    @PutMapping("/{id}")
    public TransactionRecord update(@PathVariable Long id, @Valid @RequestBody TransactionRecord record) {
        return recordService.update(id, record);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        recordService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

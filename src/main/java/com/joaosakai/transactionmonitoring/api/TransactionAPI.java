package com.joaosakai.transactionmonitoring.api;

import com.joaosakai.transactionmonitoring.model.Statistic;
import com.joaosakai.transactionmonitoring.model.Transaction;
import com.joaosakai.transactionmonitoring.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
public class TransactionAPI {

    private StatisticService service;

    @Autowired
    public TransactionAPI(StatisticService service) {
        this.service = service;
    }

    @PostMapping(path = "/transactions")
    public ResponseEntity<String> add(@RequestBody Transaction transaction) {
        if(service.addTransaction(transaction)) {
           return new ResponseEntity<>(CREATED);
        }
        return new ResponseEntity<>(NO_CONTENT);
    }

    @GetMapping(path = "/statistics")
    public Statistic getMetrics() {
        return service.getStatistics();
    }

}

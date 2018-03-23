package com.joaosakai.transactionmonitoring.service;

import com.joaosakai.transactionmonitoring.model.Statistic;
import com.joaosakai.transactionmonitoring.model.Transaction;
import com.joaosakai.transactionmonitoring.repository.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticService {

    @Autowired
    private StatisticsRepository statisticsRepository;

    /**
     * Add a transaction in the Transaction repository
     * @param transaction transaction to be added
     */
    public boolean addTransaction(final Transaction transaction) {
        return statisticsRepository.addTransaction(transaction);
    }

    /**
     * Get statistics according to the last one minute
     * @return Statistics related to all transaction in the one minute time frame
     */
    public Statistic getStatistics() {
        return statisticsRepository.getStatistics();
    }
}

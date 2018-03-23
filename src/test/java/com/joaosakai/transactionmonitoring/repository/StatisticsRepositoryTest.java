package com.joaosakai.transactionmonitoring.repository;

import com.joaosakai.transactionmonitoring.model.Statistic;
import com.joaosakai.transactionmonitoring.model.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Clock;

import static org.junit.Assert.assertTrue;

@SpringBootTest
public class StatisticsRepositoryTest {

    private StatisticsRepository statisticsRepository;
    private Clock clock;

    @Before
    public void setup() {
        statisticsRepository = new StatisticsRepository();
        clock = Clock.systemUTC();
    }

    @Test
    public void testStatisticCreation() {
        statisticsRepository.addTransaction(createTransaction(10d, 10));
        statisticsRepository.addTransaction(createTransaction(15d, 20));

        Statistic statistic = statisticsRepository.getStatistics();

        assertTrue(statistic.getSum() == 25);
        assertTrue(statistic.getMax() == 15);
        assertTrue(statistic.getMin() == 10);
        assertTrue(statistic.getCount() == 2);
    }

    @Test
    public void testTransactionBeforeOneMinute() {
        statisticsRepository.addTransaction(createTransaction(10d, 10));
        statisticsRepository.addTransaction(createTransaction(15d, 100));

        Statistic statistic = statisticsRepository.getStatistics();

        assertTrue(statistic.getSum() == 10);
        assertTrue(statistic.getMax() == 10);
        assertTrue(statistic.getMin() == 10);
        assertTrue(statistic.getCount() == 1);

    }


    @Test
    public void testTransactionInTheFuture() {
        statisticsRepository.addTransaction(createTransaction(20d, 10));
        statisticsRepository.addTransaction(createTransaction(15d, -100));

        Statistic statistic = statisticsRepository.getStatistics();

        assertTrue(statistic.getSum() == 20);
        assertTrue(statistic.getMax() == 20);
        assertTrue(statistic.getMin() == 20);
        assertTrue(statistic.getCount() == 1);

    }

    @Test
    public void testNegativeTransactions() {
        statisticsRepository.addTransaction(createTransaction(20d, 10));
        statisticsRepository.addTransaction(createTransaction(20d, 9));
        statisticsRepository.addTransaction(createTransaction(10d, 8));
        statisticsRepository.addTransaction(createTransaction(-40d, 7));

        Statistic statistic = statisticsRepository.getStatistics();

        assertTrue(statistic.getSum() == 10);
        assertTrue(statistic.getMax() == 20);
        assertTrue(statistic.getMin() == -40);
        assertTrue(statistic.getCount() == 4);
    }


    private Transaction createTransaction(double amount, int seconds) {
        return new Transaction(amount, clock.millis() - seconds * 1000);
    }

}

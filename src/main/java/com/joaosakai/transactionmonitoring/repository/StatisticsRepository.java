package com.joaosakai.transactionmonitoring.repository;

import com.joaosakai.transactionmonitoring.model.Statistic;
import com.joaosakai.transactionmonitoring.model.Transaction;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.LinkedList;
import java.util.List;

@Component
public class StatisticsRepository {

    private static final int TIME_LIMIT_MILLIS = 60000;

    private static final int TIME_LIMIT_SECONDS = TIME_LIMIT_MILLIS / 1000;

    private Statistic[] transactionStatistics = new Statistic[TIME_LIMIT_SECONDS];

    private long[] transactionsTimestamps = new long[TIME_LIMIT_SECONDS];

    private final Clock clock;

    public StatisticsRepository() {
        this.clock = Clock.systemUTC();
    }

    /**
     * Add transaction in the transaction repository. this method is synchronized to ensure one thread execution per time
     * @param transaction transaction object to be included;
     */
    public synchronized boolean addTransaction(final Transaction transaction) {
        if(isInvalidTransaction(transaction.getTimestamp())) {
            return false;
        }

        if(shouldResetTransactionsStatistics(transaction)) {
            setTransactionStatistics(transaction, new Statistic());
        }

        updateTransactionsTimestamp(transaction);
        setTransactionStatistics(transaction, getTransactionStatistic(transaction).add(transaction));

        return true;
    }

    /**
     * Verify if the transactionTimestamp is between the one minute time range
     * @param transactionTimestamp timestamp of the transaction
     * @return validation of the transaction
     */
    private boolean isInvalidTransaction(final long transactionTimestamp) {
        return clock.millis() - transactionTimestamp > TIME_LIMIT_MILLIS || clock.millis() < transactionTimestamp;
    }

    /**
     * Verify if the transaction already exists and if the index key is different then the current transaction timestamp
     *
     * @param transaction Transaction to be verified
     * @return true if the transaction need to be reseted
     */
    private boolean shouldResetTransactionsStatistics(Transaction transaction) {
        return getTransactionStatistic(transaction) == null
                || getTransactionTimestamp(transaction) != transaction.getBasedTimestamp();
    }

    private Statistic getTransactionStatistic(Transaction transaction) {
        return transactionStatistics[getTransactionStatisticIndex(transaction)];
    }

    private long getTransactionTimestamp(Transaction transaction) {
        return transactionsTimestamps[getTransactionStatisticIndex(transaction)];
    }

    private void updateTransactionsTimestamp(Transaction transaction) {
        transactionsTimestamps[getTransactionStatisticIndex(transaction)] = transaction.getBasedTimestamp();
    }

    private void setTransactionStatistics(Transaction transaction, Statistic statistic) {
        transactionStatistics[getTransactionStatisticIndex(transaction)] = statistic;
    }

    /**
     * Get an transaction index based on the transaction timestamp
     * @param transaction Transaction to be verified
     * @return Index position
     */
    private int getTransactionStatisticIndex(Transaction transaction) {
        return (int) (transaction.getTimestamp() / 1000) % TIME_LIMIT_SECONDS;
    }


    /**
     * Get all statistics from transactionStatistics array based on the one minute rule;
     * @return Statistic object with all necessary information
     */
    public Statistic getStatistics() {
        return Statistic.build(getTransactionsInTimeRange());
    }

    private List<Statistic> getTransactionsInTimeRange() {
        long oneMinuteAgo = clock.millis() - TIME_LIMIT_MILLIS;
        LinkedList<Statistic> relevantBuckets = new LinkedList<>();

        for (int i = 0; i < transactionStatistics.length; i++) {
            if (transactionStatistics[i] == null) {
                continue;
            }

            if (transactionsTimestamps[i] >= oneMinuteAgo) {
                relevantBuckets.push(transactionStatistics[i]);
            }
        }

        return relevantBuckets;
    }

}

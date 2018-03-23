package com.joaosakai.transactionmonitoring.model;

import java.util.List;

public class Statistic {

    private double sum;
    private double avg;
    private double max;
    private double min;
    private long count;

    public Statistic(double sum, double avg, double max, double min, long count) {
        this.sum = sum;
        this.avg = avg;
        this.max = max;
        this.min = min;
        this.count = count;
    }

    /**
     * Build a empty Statistic object with default values
     */
    public Statistic() {
        this(0, 0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);
    }

    /**
     * Create a new immutable statistic object to be used in the transactionStatistic array
     * @param transaction transaction to be used to calculate the statistics
     * @return new Statistic object
     */
    public Statistic add(Transaction transaction) {
        long tCount = count + 1;
        double tAvg = avg + ((transaction.getAmount() - avg) / tCount);
        double tMax = Math.max(max, transaction.getAmount());
        double tMin = Math.min(min, transaction.getAmount());
        double tSum = sum + transaction.getAmount();

        return new Statistic(tSum, tAvg, tMax, tMin, tCount);
    }

    /**
     * Calculate the statistics based on the statistics list
     * @param statistics list of statistics to be calculated
     * @return Statistic object containing the transaction metrics;
     */
    public static Statistic build(final List<Statistic> statistics) {
        long finalCount = 0;
        double finalMax = Double.NEGATIVE_INFINITY;
        double finalMin = Double.POSITIVE_INFINITY;
        double finalSum = 0;

        for (Statistic statistic : statistics) {
            if(statistic == null) {
                continue;
            }

            finalCount += statistic.getCount();
            finalMax = Math.max(finalMax, statistic.getMax());
            finalMin = Math.min(finalMin, statistic.getMin());
            finalSum += statistic.getSum();
        }

        if (finalCount == 0) {
            return new Statistic();
        }

        return new Statistic(finalSum, finalSum / finalCount, finalMax, finalMin, finalCount);

    }

    public double getSum() {
        return sum;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public long getCount() {
        return count;
    }

}

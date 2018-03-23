package com.joaosakai.transactionmonitoring.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transaction {

    private double amount;
    private long timestamp;

    public Transaction() {}

    public Transaction(@JsonProperty double amount, @JsonProperty long timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getBasedTimestamp() {
        return (timestamp / 1000) * 1000;
    }


    @Override
    public String toString() {
        return "Transaction info: " +
                "timestamp: " + this.timestamp +
                " amount: " + this.amount;


    }
}

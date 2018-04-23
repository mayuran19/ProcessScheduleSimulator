package com.mayuran19.nus.os.simulator;

public class BurstTime {
    private double previousBurstTime;
    private double previousPredictedBurstTime;

    public double getPreviousBurstTime() {
        return previousBurstTime;
    }

    public void setPreviousBurstTime(double previousBurstTime) {
        this.previousBurstTime = previousBurstTime;
    }

    public double getPreviousPredictedBurstTime() {
        return previousPredictedBurstTime;
    }

    public void setPreviousPredictedBurstTime(double previousPredictedBurstTime) {
        this.previousPredictedBurstTime = previousPredictedBurstTime;
    }
}

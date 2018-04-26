package com.mayuran19.nus.os.simulator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Process {
    public static double currentTime = 0;
    private String processId;
    private double arrivingTime;
    private double burstTime;
    private double quantumProcessedTime;
    private boolean isCompleted;
    private double waitTime;
    private double executionTime;
    private double completionTime;
    private double predictedBurstTime;

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public double getArrivingTime() {
        return arrivingTime;
    }

    public void setArrivingTime(double arrivingTime) {
        this.arrivingTime = arrivingTime;
    }

    public double getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(double burstTime) {
        this.burstTime = burstTime;
    }

    public double getQuantumProcessedTime() {
        return quantumProcessedTime;
    }

    public void setQuantumProcessedTime(double quantumProcessedTime) {
        this.quantumProcessedTime = quantumProcessedTime;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public double getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(double waitTime) {
        this.waitTime = waitTime;
    }

    public double getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(double executionTime) {
        this.executionTime = executionTime;
    }

    public double getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(double completionTime) {
        this.completionTime = completionTime;
    }

    public double getPredictedBurstTime() {
        return predictedBurstTime;
    }

    public void setPredictedBurstTime(double predictedBurstTime) {
        this.predictedBurstTime = predictedBurstTime;
    }

    @Override
    public String toString() {
        return "Process{" +
                "processId='" + processId + '\'' +
                ", arrivingTime=" + arrivingTime +
                ", burstTime=" + burstTime +
                ", quantumProcessedTime=" + quantumProcessedTime +
                ", isCompleted=" + isCompleted +
                ", waitTime=" + waitTime +
                ", executionTime=" + executionTime +
                ", completionTime=" + completionTime +
                '}';
    }

    public static List<Process> getProcesses(String path) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path));
        List<Process> processes = new ArrayList<>();
        lines.forEach(line -> {
            Process process = new Process();
            String[] array = line.split(" ");
            process.setProcessId(array[0]);
            process.setArrivingTime(Integer.valueOf(array[1]));
            process.setBurstTime(Integer.valueOf(array[2]));
            process.setExecutionTime(Integer.valueOf(array[2]));
            processes.add(process);
        });

        return processes;
    }

    public static Process getProcessWithMinimumBurstTime(List<Process> processes) {
        Process mininumBurstTimeProcess = null;
        for (Process process : processes) {
            if (process.getArrivingTime() > Process.currentTime) {
                break;
            } else {
                if (process.getBurstTime() > 0) {
                    if (mininumBurstTimeProcess == null) {
                        mininumBurstTimeProcess = process;
                    } else {
                        if (process.getBurstTime() < mininumBurstTimeProcess.getBurstTime()) {
                            mininumBurstTimeProcess = process;
                        }
                    }
                }
            }
        }

        return mininumBurstTimeProcess;
    }

    public static Process getProcessWithMinimumPredictedBurstTime(List<Process> processes, Map<String, Double> map) {
        Process mininumBurstTimeProcess = null;
        for (Process process : processes) {
            if (process.getArrivingTime() > Process.currentTime) {
                break;
            } else {
                if(map.get(process.getProcessId()) ==  null){
                    map.put(process.getProcessId(), 5d);
                }
                if (process.getBurstTime() > 0) {
                    if (mininumBurstTimeProcess == null) {
                        mininumBurstTimeProcess = process;
                    } else {
                        if (map.get(process.getProcessId()) < map.get(mininumBurstTimeProcess.getProcessId())) {
                            mininumBurstTimeProcess = process;
                        }
                    }
                }
            }
        }

        return mininumBurstTimeProcess;
    }
}

package com.mayuran19.nus.os.simulator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SJFSimulation {
    public static void run(List<Process> processes, File file){
        Map<String, BurstTime> burstMap = new HashMap<>();

        Process previousExecutedProcess = null;
        while (!isAllProcessesCompleted(processes)){
            Process processToExecute = Process.getProcessWithMinimumBurstTime(processes);
            if(processToExecute == null){
                //Increase the current time
                Process.currentTime++;
                continue;
            }else {
                if (previousExecutedProcess == null || !previousExecutedProcess.getProcessId().equals(processToExecute.getProcessId())) {
                    System.out.println("(" + Process.currentTime + "," + processToExecute.getProcessId() + ")");
                }
                previousExecutedProcess = executeProcess(processToExecute, burstMap);
            }
        }

        double totalWaitTime = 0;
        double totalProcess = 0;
        for(Process process : processes){
            //System.out.println(process.getProcessId() + ": Completion time: " + process.getCompletionTime() + ", wait time: " + (process.getCompletionTime() - process.getArrivingTime() - process.getExecutionTime()));
            totalWaitTime = totalWaitTime + ((process.getCompletionTime() - process.getArrivingTime() - process.getExecutionTime()));
            totalProcess = totalProcess + 1;
            //System.out.println(process.toString());
        }

        System.out.println("average waiting time: " + (totalWaitTime/totalProcess));
    }

    public static Process executeProcess(Process process, Map<String, BurstTime> burstMap){
        double currentBurstTime = process.getBurstTime();
        if(burstMap.get(process.getProcessId()) != null){
            //
            BurstTime burstTimeObj = burstMap.get(process.getProcessId());
            double predictedBurstTime = 0.5 * burstTimeObj.getPreviousBurstTime() + (1 - 0.5) * burstTimeObj.getPreviousPredictedBurstTime();

            if(currentBurstTime > predictedBurstTime){
                process.setBurstTime(currentBurstTime - predictedBurstTime);
                Process.currentTime += predictedBurstTime;
            }else{
                process.setBurstTime(0);
                Process.currentTime += currentBurstTime;
                process.setCompletionTime(Process.currentTime);
            }
            burstTimeObj.setPreviousBurstTime(currentBurstTime);
            burstTimeObj.setPreviousPredictedBurstTime(predictedBurstTime);
        }else{
            double predictedBurstTime = 5;
            if(currentBurstTime > predictedBurstTime){
                process.setBurstTime(currentBurstTime - predictedBurstTime);
                Process.currentTime += predictedBurstTime;
            }else{
                process.setBurstTime(0);
                Process.currentTime += currentBurstTime;
                process.setCompletionTime(Process.currentTime);
            }
            BurstTime burstTimeObj = new BurstTime();
            burstTimeObj.setPreviousBurstTime(currentBurstTime);
            burstTimeObj.setPreviousPredictedBurstTime(predictedBurstTime);
            burstMap.put(process.getProcessId(), burstTimeObj);
        }

        return process;
    }

    public static boolean isAllProcessesCompleted(List<Process> processes) {
        return processes.stream().allMatch(process -> process.getBurstTime() == 0);
    }
}

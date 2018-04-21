package com.mayuran19.nus.os.simulator;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SJFSimulation {
    public static void main(String args[]) throws IOException {
        List<Process> processes = Process.getProcesses(args[0]);
        Map<String, Double> burstMap = new HashMap<>();

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
    }

    public static Process executeProcess(Process process, Map<String, Double> burstMap){
        double burstTime = process.getBurstTime();
        process.setBurstTime(0);
        Process.currentTime += burstTime;
        burstMap.put(process.getProcessId(), burstTime);
        process.setCompletionTime(Process.currentTime);

        return process;
    }

    public static boolean isAllProcessesCompleted(List<Process> processes) {
        return processes.stream().allMatch(process -> process.getBurstTime() == 0);
    }
}

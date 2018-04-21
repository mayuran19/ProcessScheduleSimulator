package com.mayuran19.nus.os.simulator;

import java.io.IOException;
import java.util.List;

public class SRTFSimulator {
    public static void main(String[] args) throws IOException {
        List<Process> processes = Process.getProcesses(args[0]);
        Process previousExecutedProcess = null;
        while (!isAllProcessesCompleted(processes)) {
            Process processToExecute = Process.getProcessWithMinimumBurstTime(processes);
            if(processToExecute == null){
                //Increase the current time
                Process.currentTime++;
                continue;
            }else{
                if (previousExecutedProcess == null || !previousExecutedProcess.getProcessId().equals(processToExecute.getProcessId())) {
                    System.out.println("(" + Process.currentTime + "," + processToExecute.getProcessId() + ")");
                }
                previousExecutedProcess = executeProcess(processToExecute);
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

        System.out.println("Average wait time: " + (totalWaitTime/totalProcess));
    }

    public static Process executeProcess(Process process) {
        process.setBurstTime(process.getBurstTime() - 1);
        Process.currentTime++;
        if(process.getBurstTime() == 0){
            process.setCompletionTime(Process.currentTime);
        }

        return process;
    }

    public static boolean isAllProcessesCompleted(List<Process> processes) {
        return processes.stream().allMatch(process -> process.getBurstTime() == 0);
    }
}

package com.mayuran19.nus.os.simulator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class SRTFSimulator {
    public static void run(List<Process> processes, File file) throws IOException {
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
                    Files.write(file.toPath(), ("(" + Process.currentTime + "," + processToExecute.getProcessId() + ")" + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
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

        System.out.println("average waiting time: " + (totalWaitTime/totalProcess));
        Files.write(file.toPath(), ("average waiting time: " + (totalWaitTime/totalProcess) + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
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

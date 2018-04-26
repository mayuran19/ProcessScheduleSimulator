package com.mayuran19.nus.os.simulator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SJFSimulation {
    public static void main(String args[]) throws IOException {
        Process.currentTime = 0;
        List<Process> processes = Process.getProcesses(args[0]);
        Path sjfOutputPath = Paths.get(Paths.get(args[0]).getParent().toString(), "SJF.txt");
        Files.write(sjfOutputPath, "".getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        System.out.println("simulating SJF ----");
        SJFSimulation.run(processes, 0.5, sjfOutputPath.toFile());
        System.out.println();
    }

    public static void run(List<Process> processes, double alpha, File file) throws IOException {
        Map<String, BurstTime> burstMap = new HashMap<>();
        Map<String, Double> map = new HashMap<>();
        Process previousExecutedProcess = null;
        while (!isAllProcessesCompleted(processes)){
            Process processToExecute = Process.getProcessWithMinimumPredictedBurstTime(processes, map);
            if(processToExecute == null){
                //Increase the current time
                Process.currentTime++;
                continue;
            }else {
                if (previousExecutedProcess == null || !previousExecutedProcess.getProcessId().equals(processToExecute.getProcessId())) {
                    System.out.println("(" + Process.currentTime + "," + processToExecute.getProcessId() + ")");
                    Files.write(file.toPath(), ("(" + Process.currentTime + "," + processToExecute.getProcessId() + ")" + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
                }
                previousExecutedProcess = executeProcess(processToExecute, map, alpha);
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

    public static Process executeProcess(Process process, Map<String, Double> map, double alpha){
        double currentBurstTime = process.getBurstTime();
        process.setBurstTime(0);
        Process.currentTime += currentBurstTime;
        process.setCompletionTime(Process.currentTime);
        double nextBurstTime = alpha * currentBurstTime + (1 - alpha) * map.get(process.getProcessId());
        map.put(process.getProcessId(), nextBurstTime);

        return process;
    }

    public static boolean isAllProcessesCompleted(List<Process> processes) {
        return processes.stream().allMatch(process -> process.getBurstTime() == 0);
    }
}

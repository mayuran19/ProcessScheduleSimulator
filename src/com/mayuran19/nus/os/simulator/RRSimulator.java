package com.mayuran19.nus.os.simulator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class RRSimulator {
    public static void main(String args[]) throws IOException {
        List<Process> processes = Process.getProcesses(args[0]);
        Queue<Process> queue = new LinkedList<>();
        processes.forEach(process -> {
            queue.add(process);
        });
        //RRScheduling(queue);
        RRSchedule(queue, 4, processes);
    }

    public static void RRSchedule(Queue<Process> taskQueue, int quantum, List<Process> processes) {
        Queue<Process> waitQueue = new LinkedList<>();
        waitQueue.add(taskQueue.poll());
        Process previousProcessedTask = null;
        List<Process> waitTimeList = new ArrayList<>();
        while (taskQueue.size() > 0 || waitQueue.size() > 0) {
            while (waitQueue.size() > 0 || taskQueue.size() > 0) {
                Process processInWaitQueue = waitQueue.poll();
                previousProcessedTask = processTask(processInWaitQueue, quantum, waitQueue, previousProcessedTask);
                waitTimeList.add(previousProcessedTask);
                while (taskQueue.size() > 0) {
                    Process process = taskQueue.peek();
                    if (process.getArrivingTime() <= Process.currentTime) {
                        waitQueue.add(process);
                        taskQueue.remove(process);
                    } else {
                        break;
                    }
                }
                if (processInWaitQueue.getBurstTime() > 0) {
                    waitQueue.add(processInWaitQueue);
                }
                Process processInTaskQueue = taskQueue.peek();
                if (waitQueue.size() == 0 && taskQueue.size() > 0 && processInTaskQueue.getArrivingTime() > Process.currentTime) {
                    Process.currentTime = processInTaskQueue.getArrivingTime();
                    waitQueue.add(taskQueue.poll());
                }
            }
        }
        int totalWaitTime = 0;
        int totalWaitProcess = 0;
        for (Process p : processes) {
            //System.out.println(p.getProcessId() + ": Completion time:" + p.getCompletionTime() + ", arriving time: " + p.getArrivingTime());
            totalWaitTime = totalWaitTime + p.getCompletionTime() - p.getArrivingTime() - p.getExecutionTime();
            totalWaitProcess = totalWaitProcess + 1;
        }
        //System.out.println("Wait process:" + totalWaitProcess);
        //System.out.println("Wait time:" + totalWaitTime);
        System.out.println("Average wait time:" + (Double.valueOf(totalWaitTime)/Double.valueOf(totalWaitProcess)));
    }

    public static Process processTask(Process process, int quantum, Queue<Process> queue, Process previousProcessedTask) {
        if (previousProcessedTask != null && previousProcessedTask.getProcessId().equals(process.getProcessId())) {

        } else {
            System.out.println("(" + Process.currentTime + "," + process.getProcessId() + ")");
        }

        /*if(process.getArrivingTime() == Process.currentTime){
            process.setWaitTime(0);
        }else if(process.getArrivingTime() < Process.currentTime){
            process.setWaitTime(Process.currentTime - process.getArrivingTime());
        }*/
        if (process.getBurstTime() > quantum) {
            process.setBurstTime(process.getBurstTime() - quantum);
            Process.currentTime = Process.currentTime + quantum;
        } else {
            Process.currentTime = Process.currentTime + process.getBurstTime();
            process.setBurstTime(0);
            process.setCompletionTime(Process.currentTime);
            process.setWaitTime(Process.currentTime - process.getArrivingTime() - process.getExecutionTime());
        }

        return process;
    }

    public static void SRTFSchedule(){

    }
}

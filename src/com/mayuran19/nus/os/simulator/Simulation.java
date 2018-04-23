package com.mayuran19.nus.os.simulator;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Simulation {
    public static void main(String args[]) throws IOException {
        List<Process> processes = Process.getProcesses(args[0]);
        System.out.println("printing input ----");
        processes.forEach(process -> {
            System.out.println(String.join(" ", process.getProcessId(), String.valueOf(process.getArrivingTime()), String.valueOf(process.getBurstTime())));
        });
        System.out.println();



        Process.currentTime = 0;
        processes = Process.getProcesses(args[0]);
        Path rrOutputPath = Paths.get(Paths.get(args[0]).getParent().toString(), "RR.txt");
        System.out.println("simulating RR ----");
        RRSimulator.run(processes, 2, rrOutputPath.toFile());
        System.out.println();

        Process.currentTime = 0;
        processes = Process.getProcesses(args[0]);
        Path srtfOutputPath = Paths.get(Paths.get(args[0]).getParent().toString(), "SRTF.txt");
        System.out.println("simulating SRTF ----");
        SRTFSimulator.run(processes, srtfOutputPath.toFile());
        System.out.println();

        Process.currentTime = 0;
        processes = Process.getProcesses(args[0]);
        Path sjfOutputPath = Paths.get(Paths.get(args[0]).getParent().toString(), "'SJF.txt");
        System.out.println("simulating SJF ----");
        SJFSimulation.run(processes, sjfOutputPath.toFile());
        System.out.println();
    }
}

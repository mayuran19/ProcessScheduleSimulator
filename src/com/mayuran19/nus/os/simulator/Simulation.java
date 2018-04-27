package com.mayuran19.nus.os.simulator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Simulation {
    public static void main(String args[]) throws IOException {
        List<Process> processes = Process.getProcesses(args[0]);
        System.out.println("printing input ----");
        processes.forEach(process -> {
            System.out.println(String.join(" ", process.getProcessId(), String.valueOf(process.getArrivingTime()), String.valueOf(process.getBurstTime())));
        });
        System.out.println();

        Path rrOutputPath = Paths.get(Paths.get(args[0]).getParent().toString(), "RR.txt");
        Files.write(rrOutputPath, "".getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        for(int i = 1; i < 50; i++){
            Process.currentTime = 0;
            processes = Process.getProcesses(args[0]);
            System.out.println("simulating RR ----");
            System.out.println("Running for quantum: " + i);
            RRSimulator.run(processes, i, rrOutputPath.toFile());
            System.out.println();
        }
        System.out.println("end RR");
        //RRSimulator.run(processes, 8, rrOutputPath.toFile());
        System.out.println();

        Process.currentTime = 0;
        processes = Process.getProcesses(args[0]);
        Path srtfOutputPath = Paths.get(Paths.get(args[0]).getParent().toString(), "SRTF.txt");
        Files.write(srtfOutputPath, "".getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        System.out.println("simulating SRTF ----");
        SRTFSimulator.run(processes, srtfOutputPath.toFile());
        System.out.println();

        Path sjfOutputPath = Paths.get(Paths.get(args[0]).getParent().toString(), "SJF.txt");
        Files.write(sjfOutputPath, "".getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        for(double i = 0; i <=1; i = i + 0.1){
            Process.currentTime = 0;
            processes = Process.getProcesses(args[0]);
            System.out.println("simulating SJF ----");
            System.out.println("For alpha: " + i);
            SJFSimulation.run(processes, i, sjfOutputPath.toFile());
            System.out.println();
        }
        System.out.println();
    }
}

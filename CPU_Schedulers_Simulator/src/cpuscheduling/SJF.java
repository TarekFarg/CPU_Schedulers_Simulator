package cpuscheduling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SJF {

    public void execute(List<Processe> processList) {
        // Sort by Arrival Time first, then Burst Time
        Collections.sort(processList, Comparator.comparingInt(Processe::getArrivalTime)
                .thenComparingInt(Processe::getBurstTime));

        int currentTime = 0;
        int totalProcesses = processList.size();
        List<Processe> completedProcesses = new ArrayList<>();

        while (completedProcesses.size() < totalProcesses) {
            // Get processes that have arrived and are not yet executed
            List<Processe> readyQueue = new ArrayList<>();
            for (Processe p : processList) {
                if (p.getArrivalTime() <= currentTime && !completedProcesses.contains(p)) {
                    readyQueue.add(p);
                }
            }

            if (!readyQueue.isEmpty()) {
                // Select the process with the shortest burst time
                Processe currentProcess = Collections.min(readyQueue, Comparator.comparingInt(Processe::getBurstTime));
                currentProcess.setWaitingTime(currentTime - currentProcess.getArrivalTime());
                currentTime += currentProcess.getBurstTime();
                currentProcess.setTurnaroundTime(currentProcess.getWaitingTime() + currentProcess.getBurstTime());
                completedProcesses.add(currentProcess);
            } else {
                // No process is ready; increment the current time
                currentTime++;
            }
        }

        printResults(completedProcesses);
    }

    private void printResults(List<Processe> processList) {
        System.out.println("Process\tArrival\tBurst\tWaiting\tTurnaround");
        for (Processe p : processList) {
            System.out.println(
                    p.getName() + "\t" +
                            p.getArrivalTime() + "\t" +
                            p.getBurstTime() + "\t" +
                            p.getWaitingTime() + "\t" +
                            p.getTurnaroundTime());
        }

        // Calculate and print average waiting and turnaround times
        double totalWaitingTime = processList.stream().mapToInt(Processe::getWaitingTime).sum();
        double totalTurnaroundTime = processList.stream().mapToInt(Processe::getTurnaroundTime).sum();
        System.out.println("Average Waiting Time: " + (totalWaitingTime / processList.size()));
        System.out.println("Average Turnaround Time: " + (totalTurnaroundTime / processList.size()));
    }
}

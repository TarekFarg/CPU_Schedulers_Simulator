import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SRTF {

    public void execute(List<Processe> processList, int contextSwitching) {
        // Sort processes by Arrival Time initially
        Collections.sort(processList, Comparator.comparingInt(Processe::getArrivalTime));

        int currentTime = 0;
        int totalProcesses = processList.size();
        List<Processe> completedProcesses = new ArrayList<>();

        // Keep a copy of original burst times
        List<Integer> originalBurstTimes = new ArrayList<>();
        for (Processe p : processList) {
            originalBurstTimes.add(p.getBurstTime());
        }

        while (completedProcesses.size() < totalProcesses) {
            // Get ready processes (those that have arrived and are not yet completed)
            List<Processe> readyQueue = new ArrayList<>();
            for (Processe p : processList) {
                if (p.getArrivalTime() <= currentTime && p.getBurstTime() > 0 && !completedProcesses.contains(p)) {
                    readyQueue.add(p);
                }
            }

            if (!readyQueue.isEmpty()) {
                // Select process with the shortest remaining burst time
                Processe currentProcess = Collections.min(readyQueue, Comparator.comparingInt(Processe::getBurstTime));

                // Simulate process execution for 1 time unit
                currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
                currentTime++;

                // If the process finishes, calculate completion, turnaround, and waiting times
                if (currentProcess.getBurstTime() == 0) {
                    completedProcesses.add(currentProcess);

                    // Completion Time = Current Time
                    int completionTime = currentTime;

                    // Turnaround Time = Completion Time - Arrival Time
                    int turnaroundTime = completionTime - currentProcess.getArrivalTime();
                    currentProcess.setTurnaroundTime(turnaroundTime);

                    // Waiting Time = Turnaround Time - Original Burst Time
                    int originalBurstTime = originalBurstTimes.get(processList.indexOf(currentProcess));
                    int waitingTime = turnaroundTime - originalBurstTime;
                    currentProcess.setWaitingTime(waitingTime);

                    // Add context switching time after process completion
                    currentTime += contextSwitching;
                }
            } else {
                // No process is ready to execute; increment time
                currentTime++;
            }
        }

        printResults(processList);
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

        // Calculate average waiting and turnaround times
        double totalWaitingTime = processList.stream().mapToInt(Processe::getWaitingTime).sum();
        double totalTurnaroundTime = processList.stream().mapToInt(Processe::getTurnaroundTime).sum();
        System.out.println("Average Waiting Time: " + (totalWaitingTime / processList.size()));
        System.out.println("Average Turnaround Time: " + (totalTurnaroundTime / processList.size()));
    }
}

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Priority {

    public void execute(List<Processe> processList) {
        processList.sort(Comparator.comparingInt(Processe::getPriorityNumber));

        int currentTime = 0;
        for (Processe process : processList) {
            process.setWaitingTime(currentTime - process.getArrivalTime());
            if (process.getWaitingTime() < 0) {
                process.setWaitingTime(0); // Ensure waiting time isn't negative
            }

            currentTime = Math.max(currentTime, process.getArrivalTime()) + process.getBurstTime();

            process.setTurnaroundTime(process.getWaitingTime() + process.getBurstTime());

            System.out.println("Process " + process.getName() + " executed:");
            System.out.println("\tArrival Time: " + process.getArrivalTime());
            System.out.println("\tPriority: " + process.getPriorityNumber());
            System.out.println("\tBurst Time: " + process.getBurstTime());
            System.out.println("\tWaiting Time: " + process.getWaitingTime());
            System.out.println("\tTurnaround Time: " + process.getTurnaroundTime());
        }

        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        for (Processe process : processList) {
            totalWaitingTime += process.getWaitingTime();
            totalTurnaroundTime += process.getTurnaroundTime();
        }

        System.out.println("\nAverage Waiting Time: " + (totalWaitingTime / processList.size()));
        System.out.println("Average Turnaround Time: " + (totalTurnaroundTime / processList.size()));
    }
}

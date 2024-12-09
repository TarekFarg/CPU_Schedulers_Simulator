import java.util.List;

public class SRTF {

    public void runSRTF(List<Processe> processes) {
        int n = processes.size();
        int[] remainingBurstTime = new int[n];
        int[] waitingTime = new int[n];
        int[] turnaroundTime = new int[n];

        // Copy burst times
        for (int i = 0; i < n; i++) {
            remainingBurstTime[i] = processes.get(i).getBurstTime();
        }

        int complete = 0, time = 0, shortest = 0, min = Integer.MAX_VALUE;
        boolean check = false;

        while (complete != n) {
            for (int j = 0; j < n; j++) {
                if (processes.get(j).getArrivalTime() <= time &&
                    remainingBurstTime[j] < min && remainingBurstTime[j] > 0) {
                    shortest = j;
                    min = remainingBurstTime[j];
                    check = true;
                }
            }

            if (!check) {
                time++;
                continue;
            }

            remainingBurstTime[shortest]--;
            min = remainingBurstTime[shortest];
            if (min == 0) min = Integer.MAX_VALUE;

            if (remainingBurstTime[shortest] == 0) {
                complete++;
                check = false;

                int finishTime = time + 1;
                waitingTime[shortest] = finishTime - processes.get(shortest).getBurstTime() - processes.get(shortest).getArrivalTime();
                if (waitingTime[shortest] < 0) waitingTime[shortest] = 0;
                
                processes.get(shortest).setWaitingTime(waitingTime[shortest]);
                processes.get(shortest).setTurnaroundTime(finishTime - processes.get(shortest).getArrivalTime());
                processes.get(shortest).setFinishedTime(finishTime);
            }
            time++;
        }

        // Assign calculated turnaround times to processes
        for (int i = 0; i < n; i++) {
            turnaroundTime[i] = processes.get(i).getTurnaroundTime();
        }
    }

    public void printResults(List<Processe> processes) {
        System.out.println("Process\tArrival\tBurst\tWaiting\tTurnaround\tFinished");
        for (Processe process : processes) {
            System.out.println(
                process.getName() + "\t" +
                process.getArrivalTime() + "\t" +
                process.getBurstTime() + "\t" +
                process.getWaitingTime() + "\t" +
                process.getTurnaroundTime() + "\t" + "\t" +
                process.getFinishedTime()
            );
        }
    }
}

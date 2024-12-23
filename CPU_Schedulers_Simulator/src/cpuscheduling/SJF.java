package cpuscheduling;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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

        showResultsInGUI(completedProcesses);
    }

    private void showResultsInGUI(List<Processe> processList) {
        // Create a JFrame to display results
        JFrame resultFrame = new JFrame("SJF Scheduling Results");
        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultFrame.setSize(800, 600);

        // Create a JTable to display process details
        String[] columnNames = {"Process", "Arrival Time", "Burst Time", "Waiting Time", "Turnaround Time"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable resultTable = new JTable(tableModel);

        // Populate the table model
        for (Processe p : processList) {
            tableModel.addRow(new Object[]{
                    p.getName(),
                    p.getArrivalTime(),
                    p.getBurstTime(),
                    p.getWaitingTime(),
                    p.getTurnaroundTime()
            });
        }

        // Calculate average waiting and turnaround times
        double totalWaitingTime = processList.stream().mapToInt(Processe::getWaitingTime).sum();
        double totalTurnaroundTime = processList.stream().mapToInt(Processe::getTurnaroundTime).sum();
        double avgWaitingTime = totalWaitingTime / processList.size();
        double avgTurnaroundTime = totalTurnaroundTime / processList.size();

        // Create labels for average times
        JLabel avgWaitingTimeLabel = new JLabel("Average Waiting Time: " + avgWaitingTime);
        JLabel avgTurnaroundTimeLabel = new JLabel("Average Turnaround Time: " + avgTurnaroundTime);

        // Add components to the main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JScrollPane tableScrollPane = new JScrollPane(resultTable);

        mainPanel.add(tableScrollPane);
        mainPanel.add(avgWaitingTimeLabel);
        mainPanel.add(avgTurnaroundTimeLabel);

        resultFrame.add(mainPanel);
        resultFrame.setVisible(true);
    }
}

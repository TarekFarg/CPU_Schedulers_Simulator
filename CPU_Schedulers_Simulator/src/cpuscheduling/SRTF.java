package cpuscheduling;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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

        // Track the remaining burst times
        List<Integer> remainingBurstTimes = new ArrayList<>(originalBurstTimes);

        // Gantt chart timeline
        List<GanttEntry> ganttTimeline = new ArrayList<>();

        Processe lastProcess = null; // To track the last executed process

        while (completedProcesses.size() < totalProcesses) {
            // Get ready processes (those that have arrived and are not yet completed)
            List<Processe> readyQueue = new ArrayList<>();
            for (Processe p : processList) {
                int remainingBurstTime = remainingBurstTimes.get(processList.indexOf(p));
                if (p.getArrivalTime() <= currentTime && remainingBurstTime > 0 && !completedProcesses.contains(p)) {
                    readyQueue.add(p);
                }
            }

            if (!readyQueue.isEmpty()) {
                // Select process with the shortest remaining burst time
                Processe currentProcess = Collections.min(readyQueue, Comparator.comparingInt(p -> remainingBurstTimes.get(processList.indexOf(p))));

                // Get the index of the current process
                int processIndex = processList.indexOf(currentProcess);

                // Add context switching time if switching to a new process
                if (lastProcess != null && lastProcess != currentProcess) {
                    currentTime += contextSwitching;
                }

                // Simulate process execution for 1 time unit
                remainingBurstTimes.set(processIndex, remainingBurstTimes.get(processIndex) - 1);
                ganttTimeline.add(new GanttEntry(currentProcess.getName(), currentTime, currentTime + 1));
                currentTime++;

                // If the process finishes, calculate completion, turnaround, and waiting times
                if (remainingBurstTimes.get(processIndex) == 0) {
                    completedProcesses.add(currentProcess);

                    // Completion Time = Current Time
                    int completionTime = currentTime;

                    // Turnaround Time = Completion Time - Arrival Time
                    int turnaroundTime = completionTime - currentProcess.getArrivalTime();
                    currentProcess.setTurnaroundTime(turnaroundTime);

                    // Waiting Time = Turnaround Time - Original Burst Time
                    int originalBurstTime = originalBurstTimes.get(processIndex);
                    int waitingTime = turnaroundTime - originalBurstTime;
                    currentProcess.setWaitingTime(waitingTime);
                }

                lastProcess = currentProcess;
            } else {
                // No process is ready to execute; increment time
                currentTime++;
            }
        }

        showResultsInGUI(processList, ganttTimeline);
    }

    private void showResultsInGUI(List<Processe> processList, List<GanttEntry> ganttTimeline) {
        // Create a JFrame to display results
        JFrame resultFrame = new JFrame("Scheduling Results");
        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultFrame.setSize(800, 600);

        // Create a JTable to display process details
        String[] columnNames = {"Process", "Arrival Time", "Burst Time", "Waiting Time", "Turnaround Time"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable resultTable = new JTable(tableModel);

        // Populate the table model and ensure calculations are correct
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

        // Create a Gantt chart panel
        JPanel ganttChartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                int startX = 50; // Starting X position for the chart
                int startY = 50; // Starting Y position
                int barHeight = 40; // Height of each process bar
                int timeUnitWidth = 30; // Width for one unit of time

                for (GanttEntry entry : ganttTimeline) {
                    int barStartX = startX + entry.getStartTime() * timeUnitWidth;
                    int barWidth = (entry.getEndTime() - entry.getStartTime()) * timeUnitWidth;
                    int barY = startY;

                    // Set unique colors for each process
                    Color[] colors = {Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.RED, Color.ORANGE, Color.LIGHT_GRAY};
                    g.setColor(colors[entry.getProcessName().charAt(1) - '1']);

                    // Draw process bar
                    g.fillRect(barStartX, barY, barWidth, barHeight);

                    // Draw process name and start/end times
                    g.setColor(Color.BLACK);
                    g.drawRect(barStartX, barY, barWidth, barHeight);
                    g.drawString(entry.getProcessName(), barStartX + 5, barY + barHeight / 2 + 5);
                    g.drawString(String.valueOf(entry.getStartTime()), barStartX - 10, barY + barHeight + 15);
                    g.drawString(String.valueOf(entry.getEndTime()), barStartX + barWidth - 10, barY + barHeight + 15);
                }
            }
        };
        ganttChartPanel.setPreferredSize(new Dimension(700, 300));

        // Add components to the main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JScrollPane tableScrollPane = new JScrollPane(resultTable);
        JScrollPane chartScrollPane = new JScrollPane(ganttChartPanel);

        mainPanel.add(tableScrollPane);
        mainPanel.add(chartScrollPane);
        mainPanel.add(avgWaitingTimeLabel);
        mainPanel.add(avgTurnaroundTimeLabel);

        resultFrame.add(mainPanel);
        resultFrame.setVisible(true);
    }

    private static class GanttEntry {
        private final String processName;
        private final int startTime;
        private final int endTime;

        public GanttEntry(String processName, int startTime, int endTime) {
            this.processName = processName;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public String getProcessName() {
            return processName;
        }

        public int getStartTime() {
            return startTime;
        }

        public int getEndTime() {
            return endTime;
        }
    }
}

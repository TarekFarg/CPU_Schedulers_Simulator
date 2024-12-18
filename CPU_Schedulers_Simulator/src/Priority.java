import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class Priority {
    private int contextSwitchingTime;
    public Priority(int contextSwitchingTime) {
        this.contextSwitchingTime = contextSwitchingTime;
    }

    public void execute(List<Processe> processList) {
        processList.sort((p1, p2) -> Integer.compare(p1.getPriorityNumber(), p2.getPriorityNumber()));

        int currentTime = 0;

        for (Processe process : processList) {
            process.setWaitingTime(currentTime - process.getArrivalTime());
            if (process.getWaitingTime() < 0) {
                process.setWaitingTime(0);
                currentTime = process.getArrivalTime();
            }

            process.setTurnaroundTime(process.getWaitingTime() + process.getBurstTime());

            currentTime += process.getBurstTime() + contextSwitchingTime;
        }

        showResultsInGUI(processList);
    }

    private void showResultsInGUI(List<Processe> processList) {
        JFrame frame = new JFrame("Priority Scheduling Results (With Context Switching)");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 400);

        String[] columnNames = {
                "Process Name", "Arrival Time", "Burst Time", "Priority", "Waiting Time", "Turnaround Time"
        };

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Processe process : processList) {
            Object[] row = {
                    process.getName(),
                    process.getArrivalTime(),
                    process.getBurstTime(),
                    process.getPriorityNumber(),
                    process.getWaitingTime(),
                    process.getTurnaroundTime()
            };
            model.addRow(row);
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        frame.add(scrollPane);

        frame.setVisible(true);
    }
}

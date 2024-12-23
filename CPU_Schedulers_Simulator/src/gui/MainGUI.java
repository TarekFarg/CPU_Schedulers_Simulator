package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import cpuscheduling.Processe;
import cpuscheduling.Priority;
import cpuscheduling.FCAI;

public class MainGUI {
    public JFrame frame;
    private List<cpuscheduling.Processe> processList;
    private JTextField numProcessesField, quantumField, contextSwitchingField;
    private JComboBox<String> schedulingTypeBox;

    public MainGUI() {
        frame = new JFrame("CPU Scheduling Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Scheduling Parameters"));

        inputPanel.add(new JLabel("Number of Processes:"));
        numProcessesField = new JTextField();
        inputPanel.add(numProcessesField);

        inputPanel.add(new JLabel("Round Robin Time Quantum (if needed):"));
        quantumField = new JTextField();
        inputPanel.add(quantumField);

        inputPanel.add(new JLabel("Context Switching Time:"));
        contextSwitchingField = new JTextField();
        inputPanel.add(contextSwitchingField);

        inputPanel.add(new JLabel("Scheduling Algorithm:"));
        schedulingTypeBox = new JComboBox<>(new String[]{"SJF", "SRTF", "Priority", "FCAI"});
        inputPanel.add(schedulingTypeBox);

        frame.add(inputPanel, BorderLayout.NORTH);

        // Process Input Button
        JButton processInputButton = new JButton("Enter Process Details");
        frame.add(processInputButton, BorderLayout.CENTER);

        // Event Listener for Process Input
        processInputButton.addActionListener(e -> handleProcessInput());
    }

    private void handleProcessInput() {
        int numProcesses;
        int quantum;
        int contextSwitching;

        try {
            numProcesses = Integer.parseInt(numProcessesField.getText());
            quantum = Integer.parseInt(quantumField.getText());
            contextSwitching = Integer.parseInt(contextSwitchingField.getText());
        } catch (NumberFormatException e) {
            showError("Please enter valid numeric values.");
            return;
        }

        processList = new ArrayList<>();
        for (int i = 0; i < numProcesses; i++) {
            JTextField nameField = new JTextField();
            JTextField arrivalTimeField = new JTextField();
            JTextField burstTimeField = new JTextField();
            JTextField priorityField = new JTextField();
            JTextField quantumTimeField = new JTextField();

            Object[] fields = {
                    "Process Name:", nameField,
                    "Arrival Time:", arrivalTimeField,
                    "Burst Time:", burstTimeField,
                    "Priority Number (if needed):", priorityField,
                    "Quantum Time (if needed):", quantumTimeField
            };

            int option = JOptionPane.showConfirmDialog(frame, fields, "Enter Details for Process " + (i + 1), JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    cpuscheduling.Processe p = new cpuscheduling.Processe();
                    p.setName(nameField.getText());
                    p.setArrivalTime(Integer.parseInt(arrivalTimeField.getText()));
                    p.setBurstTime(Integer.parseInt(burstTimeField.getText()));
                    p.setPriorityNumber(Integer.parseInt(priorityField.getText()));
                    p.setQuantumTime(Integer.parseInt(quantumTimeField.getText()));
                    processList.add(p);
                } catch (NumberFormatException ex) {
                    showError("Invalid input for process details. Please try again.");
                    return;
                }
            } else {
                return;
            }
        }

        executeSchedulingAlgorithm(quantum, contextSwitching);
    }

    private void executeSchedulingAlgorithm(int quantum, int contextSwitching) {
        String schedulingType = (String) schedulingTypeBox.getSelectedItem();

        switch (schedulingType) {
            case "SJF":
                cpuscheduling.SJF sjf = new cpuscheduling.SJF();
                sjf.execute(processList);
                break;
            case "SRTF":
                cpuscheduling.SRTF srtf = new cpuscheduling.SRTF();
                srtf.execute(processList, contextSwitching);
                break;
            case "Priority":
                cpuscheduling.Priority pr = new Priority(contextSwitching);
                pr.execute(processList);
                break;
            case "FCAI":
                cpuscheduling.FCAI fcai = new FCAI(processList, processList.size());
                break;
            default:
                showError("Invalid scheduling algorithm selected.");
        }
    }


    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void show() {
        frame.setVisible(true);
    }
}


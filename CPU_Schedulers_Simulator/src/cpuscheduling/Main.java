package cpuscheduling;

import gui.MainGUI;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGUI app = new MainGUI();
            app.frame.setVisible(true);
        });
    }
}
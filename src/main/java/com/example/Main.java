package com.example;

import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Paper ReSearch");
            Gui gui = new Gui();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setContentPane(gui.getPanel());
            frame.setVisible(true);
        });
    }
}

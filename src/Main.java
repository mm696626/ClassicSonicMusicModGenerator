// Sonic 1 Forever and Sonic 2 Absolute Music Mod Generator by Matt McCullough
// This is to automate and streamline music mod creation for Sonic 1 Forever and Sonic 2 Absolute

import gamechooserui.GameChooserUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        GameChooserUI gameChooserUI = new GameChooserUI();
        gameChooserUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameChooserUI.pack();
        gameChooserUI.setVisible(true);
    }
}
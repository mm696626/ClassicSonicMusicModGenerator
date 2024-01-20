// Classic Sonic Music Mod Generator by Matt McCullough
// This is to automate and streamline music mod creation for Sonic 1 Forever, Sonic 2 Absolute, Sonic CD (2011), Sonic 3 AIR, and Sonic Mania

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
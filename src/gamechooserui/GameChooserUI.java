package gamechooserui;

import sonic1and2.ui.MusicModGeneratorUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameChooserUI extends JFrame implements ActionListener {


    private JButton sonic1, sonic2;
    GridBagConstraints gridBagConstraints = null;

    public GameChooserUI()
    {
        setTitle("Choose a Game");

        sonic1 = new JButton("Sonic 1 Forever");
        sonic1.addActionListener(this);

        sonic2 = new JButton("Sonic 2 Absolute");
        sonic2.addActionListener(this);

        setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.gridx=0;
        gridBagConstraints.gridy=0;
        add(sonic1, gridBagConstraints);

        gridBagConstraints.gridx=1;
        gridBagConstraints.gridy=0;
        add(sonic2, gridBagConstraints);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == sonic1) {
            setVisible(false);
            MusicModGeneratorUI musicModGeneratorUI = new MusicModGeneratorUI(true);
            musicModGeneratorUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            musicModGeneratorUI.pack();
            musicModGeneratorUI.setVisible(true);
        }

        if (e.getSource() == sonic2) {
            setVisible(false);
            MusicModGeneratorUI musicModGeneratorUI = new MusicModGeneratorUI(false);
            musicModGeneratorUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            musicModGeneratorUI.pack();
            musicModGeneratorUI.setVisible(true);
        }
    }
}
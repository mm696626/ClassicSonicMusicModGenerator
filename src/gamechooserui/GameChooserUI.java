package gamechooserui;

import sonic1and2.ui.Sonic1And2MusicModGeneratorUI;
import sonic3.ui.Sonic3AIRMusicModGeneratorUI;
import soniccd.ui.SonicCDMusicModGeneratorUI;
import sonicmania.ui.SonicManiaMusicModGeneratorUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameChooserUI extends JFrame implements ActionListener {


    private JButton sonic1, sonic2, sonic3, sonicCD, sonicMania;
    GridBagConstraints gridBagConstraints = null;

    public GameChooserUI()
    {
        setTitle("Choose a Game");

        sonic1 = new JButton("Sonic 1 Forever");
        sonic1.addActionListener(this);

        sonic2 = new JButton("Sonic 2 Absolute");
        sonic2.addActionListener(this);

        sonic3 = new JButton("Sonic 3 AIR");
        sonic3.addActionListener(this);

        sonicCD = new JButton("Sonic CD (2011)");
        sonicCD.addActionListener(this);

        sonicMania = new JButton("Sonic Mania");
        sonicMania.addActionListener(this);

        setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.gridx=0;
        gridBagConstraints.gridy=0;
        add(sonic1, gridBagConstraints);

        gridBagConstraints.gridx=2;
        gridBagConstraints.gridy=0;
        add(sonic2, gridBagConstraints);

        gridBagConstraints.gridx=0;
        gridBagConstraints.gridy=1;
        add(sonic3, gridBagConstraints);

        gridBagConstraints.gridx=2;
        gridBagConstraints.gridy=1;
        add(sonicCD, gridBagConstraints);

        gridBagConstraints.gridx=1;
        gridBagConstraints.gridy=2;
        add(sonicMania, gridBagConstraints);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == sonic1) {
            setVisible(false);
            Sonic1And2MusicModGeneratorUI sonic1And2MusicModGeneratorUI = new Sonic1And2MusicModGeneratorUI(true);
            sonic1And2MusicModGeneratorUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            sonic1And2MusicModGeneratorUI.pack();
            sonic1And2MusicModGeneratorUI.setVisible(true);
        }

        if (e.getSource() == sonic2) {
            setVisible(false);
            Sonic1And2MusicModGeneratorUI sonic1And2MusicModGeneratorUI = new Sonic1And2MusicModGeneratorUI(false);
            sonic1And2MusicModGeneratorUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            sonic1And2MusicModGeneratorUI.pack();
            sonic1And2MusicModGeneratorUI.setVisible(true);
        }

        if (e.getSource() == sonic3) {
            setVisible(false);
            Sonic3AIRMusicModGeneratorUI sonic3AIRMusicModGeneratorUI = new Sonic3AIRMusicModGeneratorUI();
            sonic3AIRMusicModGeneratorUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            sonic3AIRMusicModGeneratorUI.pack();
            sonic3AIRMusicModGeneratorUI.setVisible(true);
        }

        if (e.getSource() == sonicCD) {
            setVisible(false);
            SonicCDMusicModGeneratorUI sonicCDMusicModGeneratorUI = new SonicCDMusicModGeneratorUI();
            sonicCDMusicModGeneratorUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            sonicCDMusicModGeneratorUI.pack();
            sonicCDMusicModGeneratorUI.setVisible(true);
        }

        if (e.getSource() == sonicMania) {
            setVisible(false);
            SonicManiaMusicModGeneratorUI sonicManiaMusicModGeneratorUI = new SonicManiaMusicModGeneratorUI();
            sonicManiaMusicModGeneratorUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            sonicManiaMusicModGeneratorUI.pack();
            sonicManiaMusicModGeneratorUI.setVisible(true);
        }
    }
}
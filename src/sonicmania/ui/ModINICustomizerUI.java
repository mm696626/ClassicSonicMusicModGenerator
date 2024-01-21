package sonicmania.ui;

import sonicmania.io.ModINIGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ModINICustomizerUI extends JFrame implements ActionListener {

    private final String[] INIFieldNames = {"Name", "Author", "Description" ,"Version"};
    private JLabel[] iniFieldNameLabels = new JLabel[INIFieldNames.length];
    private JTextField[] iniFieldNameFields = new JTextField[INIFieldNames.length];
    private JButton saveModINI;
    GridBagConstraints gridBagConstraints = null;

    public ModINICustomizerUI()
    {
        setTitle("Mod INI Customizer");
        generateUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == saveModINI) {
            ModINIGenerator modINIGenerator = new ModINIGenerator();
            modINIGenerator.generateModJSON(INIFieldNames, iniFieldNameFields);
            setVisible(false);
        }

    }

    private void generateUI() {

        setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();

        int rowNum = 0;

        //draw labels for the UI
        for (int i = 0; i< INIFieldNames.length; i++) {

            iniFieldNameLabels[i] = new JLabel(INIFieldNames[i]);

            gridBagConstraints.gridx=0;
            gridBagConstraints.gridy=rowNum;
            add(iniFieldNameLabels[i], gridBagConstraints);

            iniFieldNameFields[i] = new JTextField(10);

            gridBagConstraints.gridx=1;
            gridBagConstraints.gridy=rowNum;
            add(iniFieldNameFields[i], gridBagConstraints);

            rowNum++;
        }

        saveModINI = new JButton("Save Mod INI");
        saveModINI.addActionListener(this);

        gridBagConstraints.gridx=0;
        gridBagConstraints.gridy=rowNum;
        add(saveModINI, gridBagConstraints);
    }
}
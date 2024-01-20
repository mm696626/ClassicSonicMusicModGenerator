package sonic3.ui;

import sonic3.io.ModJSONGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ModJSONCustomizerUI extends JFrame implements ActionListener {

    private final String[] JSONFieldNames = {"Name", "Author", "Description" ,"ModVersion"};
    private JLabel[] jsonFieldNameLabels = new JLabel[JSONFieldNames.length];
    private JTextField[] jsonFieldNameFields = new JTextField[JSONFieldNames.length];
    private JButton saveModJSON;
    GridBagConstraints gridBagConstraints = null;

    public ModJSONCustomizerUI()
    {
        setTitle("Mod JSON Customizer");
        generateUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == saveModJSON) {
            ModJSONGenerator modJSONGenerator = new ModJSONGenerator();
            modJSONGenerator.generateModJSON(JSONFieldNames, jsonFieldNameFields);
            setVisible(false);
        }

    }

    private void generateUI() {

        setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();

        int rowNum = 0;

        //draw labels for the UI
        for (int i = 0; i< JSONFieldNames.length; i++) {

            jsonFieldNameLabels[i] = new JLabel(JSONFieldNames[i]);

            gridBagConstraints.gridx=0;
            gridBagConstraints.gridy=rowNum;
            add(jsonFieldNameLabels[i], gridBagConstraints);

            jsonFieldNameFields[i] = new JTextField(10);

            gridBagConstraints.gridx=1;
            gridBagConstraints.gridy=rowNum;
            add(jsonFieldNameFields[i], gridBagConstraints);

            rowNum++;
        }

        saveModJSON = new JButton("Save Mod JSON");
        saveModJSON.addActionListener(this);

        gridBagConstraints.gridx=0;
        gridBagConstraints.gridy=rowNum;
        add(saveModJSON, gridBagConstraints);
    }
}
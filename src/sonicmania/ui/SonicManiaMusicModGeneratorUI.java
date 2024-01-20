package sonicmania.ui;

import sonicmania.io.ModFolderGenerator;
import sonicmania.io.OldFileCleaner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SonicManiaMusicModGeneratorUI extends JFrame implements ActionListener {


    //where to grab ogg files from
    private String audioFolderPath = "";
    private JButton generateModINI, generateModFolder, pickMusic;
    GridBagConstraints gridBagConstraints = null;

    public SonicManiaMusicModGeneratorUI()
    {
        setTitle("Sonic Mania Music Mod Generator");

        pickMusic = new JButton("Pick Music for Mod");
        pickMusic.addActionListener(this);

        generateModINI = new JButton("Generate Mod INI");
        generateModINI.addActionListener(this);

        generateModFolder = new JButton("Generate Mod Folder");
        generateModFolder.addActionListener(this);

        setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.gridx=0;
        gridBagConstraints.gridy=0;
        add(pickMusic, gridBagConstraints);

        gridBagConstraints.gridx=1;
        gridBagConstraints.gridy=0;
        add(generateModINI, gridBagConstraints);

        gridBagConstraints.gridx=3;
        gridBagConstraints.gridy=0;
        add(generateModFolder, gridBagConstraints);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == pickMusic) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int response = fileChooser.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                audioFolderPath = fileChooser.getSelectedFile().getAbsolutePath();
            } else {
                return;
            }

            MusicOptionsPickerUI musicOptionsPickerUI = new MusicOptionsPickerUI(audioFolderPath);
            musicOptionsPickerUI.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            musicOptionsPickerUI.pack();

            if (musicOptionsPickerUI.getAudioFileNameLength() > 0) {
                musicOptionsPickerUI.setVisible(true);
            }
        }

        if (e.getSource() == generateModINI) {
            ModINICustomizerUI modINICustomizerUI = new ModINICustomizerUI();
            modINICustomizerUI.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            modINICustomizerUI.pack();
            modINICustomizerUI.setVisible(true);
        }

        if (e.getSource() == generateModFolder) {
            File modINI = new File("mod.ini");
            File oggFilePaths = new File("oggFilePaths.txt");
            File musicChoices = new File("musicChoices.txt");
            if (modINI.exists() && oggFilePaths.exists() && musicChoices.exists()) {

                String modFolderName = getModFolderName();
                String modBaseDir = getModBaseDir();
                String modFolderPath = modBaseDir + modFolderName;

                File modFolder = new File(modFolderPath);
                ModFolderGenerator modFolderGenerator = new ModFolderGenerator();

                boolean generateFolder = false;

                if (modFolder.exists()) {
                    int overwriteModFolderDialogResult = JOptionPane.showConfirmDialog(this, "A mod folder already exists at " + modFolder.getAbsolutePath() + ". Would you like to overwrite it?");
                    if (overwriteModFolderDialogResult == JOptionPane.YES_OPTION){

                        if (deleteDirectory(modFolderPath))  {
                            generateFolder = true;
                        }
                    }
                }

                else {
                    generateFolder = true;
                }

                try {
                    if (generateFolder) {
                        modFolderGenerator.generateModFolder();
                        OldFileCleaner oldFileCleaner = new OldFileCleaner();
                        oldFileCleaner.cleanFiles();
                        JOptionPane.showMessageDialog(this, "Mod successfully generated!");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Something went wrong when generating the mod folder");
                }
            }
            else {
                JOptionPane.showMessageDialog(this, "You haven't picked music or generated a mod json file!");
            }
        }
    }

    private String getModBaseDir() {
        File modJSON = new File("mod.ini");
        String modJSONPath = modJSON.getAbsolutePath();
        return modJSONPath.substring(0, modJSONPath.lastIndexOf("mod.ini"));
    }

    private String getModFolderName() {
        String modFolderName = "";

        Scanner inputStream = null;

        try {
            inputStream = new Scanner (new FileInputStream("mod.ini"));
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Mod JSON File does not exist");
            return null;
        }

        String line = "";
        while (inputStream.hasNextLine()) {
            line = inputStream.nextLine();
            if (line.contains("Name=")) {
                modFolderName = line.substring(line.lastIndexOf("=")+1);
                break;
            }
        }

        modFolderName = modFolderName.replaceAll("[^a-zA-Z0-9]", "");
        inputStream.close();
        return modFolderName;
    }

    private boolean deleteDirectory(String folderPath) {

        File[] folderFileList = new File(folderPath).listFiles();

        //Grab all files and check subfolders if the file is a directory
        for (File file: folderFileList) {
            if (file.isDirectory()) {
                deleteDirectory(file.getAbsolutePath());
            }

            file.delete();
        }

        File folder = new File(folderPath);
        return folder.delete();
    }
}
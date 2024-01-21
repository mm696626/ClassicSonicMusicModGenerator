package sonic3.ui;

import gamechooserui.GameChooserUI;
import sonic3.io.ModFolderGenerator;
import sonic3.io.OldFileCleaner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class Sonic3AIRMusicModGeneratorUI extends JFrame implements ActionListener {


    //where to grab ogg files from
    private String audioFolderPath = "";
    private JButton generateModJSON, generateModFolder, pickMusic, back;
    GridBagConstraints gridBagConstraints = null;

    public Sonic3AIRMusicModGeneratorUI()
    {
        setTitle("Sonic 3 AIR Music Mod Generator");

        pickMusic = new JButton("Pick Music for Mod");
        pickMusic.addActionListener(this);

        generateModJSON = new JButton("Generate Mod JSON");
        generateModJSON.addActionListener(this);

        generateModFolder = new JButton("Generate Mod Folder");
        generateModFolder.addActionListener(this);

        back = new JButton("Go Back to Game Selection");
        back.addActionListener(this);

        setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.gridx=0;
        gridBagConstraints.gridy=0;
        add(back, gridBagConstraints);

        gridBagConstraints.gridx=1;
        gridBagConstraints.gridy=0;
        add(pickMusic, gridBagConstraints);

        gridBagConstraints.gridx=2;
        gridBagConstraints.gridy=0;
        add(generateModJSON, gridBagConstraints);

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

        if (e.getSource() == generateModJSON) {
            ModJSONCustomizerUI modJSONCustomizerUI = new ModJSONCustomizerUI();
            modJSONCustomizerUI.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            modJSONCustomizerUI.pack();
            modJSONCustomizerUI.setVisible(true);
        }

        if (e.getSource() == generateModFolder) {
            File modJSON = new File("mod.json");
            File audioReplacementsJSON = new File("audio_replacements.json");
            File oggFilePaths = new File("oggFilePaths.txt");
            if (modJSON.exists() && audioReplacementsJSON.exists() && oggFilePaths.exists()) {

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

                        //copy music choices file
                        File musicChoicesFile = new File("musicChoices.txt");
                        File chosenMusicFile = new File("chosenMusic.txt");
                        Files.copy(musicChoicesFile.toPath(), chosenMusicFile.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);

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

        if (e.getSource() == back) {
            setVisible(false);
            OldFileCleaner oldFileCleaner = new OldFileCleaner();
            oldFileCleaner.cleanFiles();
            GameChooserUI gameChooserUI = new GameChooserUI();
            gameChooserUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gameChooserUI.pack();
            gameChooserUI.setVisible(true);
        }
    }

    private String getModBaseDir() {
        File modJSON = new File("mod.json");
        String modJSONPath = modJSON.getAbsolutePath();
        return modJSONPath.substring(0, modJSONPath.lastIndexOf("mod.json"));
    }

    private String getModFolderName() {
        String modFolderName = "";

        Scanner inputStream = null;

        try {
            inputStream = new Scanner (new FileInputStream("mod.json"));
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Mod JSON File does not exist");
            return null;
        }

        String line = "";
        while (inputStream.hasNextLine()) {
            line = inputStream.nextLine();
            if (line.contains("\"Name\"")) {
                modFolderName = line.substring(line.indexOf(":") + 3, line.lastIndexOf("\""));
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
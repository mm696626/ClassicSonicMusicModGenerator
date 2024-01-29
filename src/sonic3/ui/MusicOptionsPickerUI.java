package sonic3.ui;

import sonic3.io.AudioReplacementJSONGenerator;
import sonic3.io.MusicChoiceSaver;
import sonic3.io.MusicNameLoader;
import sonic3.io.OGGFilePathSaver;
import sonic3.validation.ExtensionValidator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class MusicOptionsPickerUI extends JFrame implements ActionListener {


    //where to grab ogg files from
    private String audioFolderPath = "";
    private ArrayList<String> musicNames;

    private int musicTrackCount;
    private ArrayList<JPanel> jPanels = new ArrayList<>();

    private JLabel[] musicTrackNameLabels;
    private JComboBox[] musicTrackPickers;
    private ArrayList<JButton> saveMusicChoicesButtons = new ArrayList<>();
    private ArrayList<JButton> randomizeMusicChoicesButtons = new ArrayList<>();
    private String[] audioFileNames;
    private ArrayList<File> audioFiles;



    public MusicOptionsPickerUI(String audioFolderPath)
    {
        this.audioFolderPath = audioFolderPath;
        MusicNameLoader musicNameLoader = new MusicNameLoader();
        this.musicNames =  musicNameLoader.getMusicNamesFromIDSFile();
        this.musicTrackCount = musicNames.size();
        this.musicTrackNameLabels = new JLabel[musicTrackCount];
        this.musicTrackPickers = new JComboBox[musicTrackCount];
        setTitle("Pick Music Options");
        generateUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (checkIfSaveButtonWasPressed(e)) {
            MusicChoiceSaver musicChoiceSaver = new MusicChoiceSaver();
            musicChoiceSaver.saveMusicChoicesToMusicChoicesFile(musicNames, musicTrackPickers, audioFileNames);
            generateAudioReplacementsJSON();
            setVisible(false);
        }

        if (checkIfRandomizeButtonWasPressed(e)) {
            MusicChoiceSaver musicChoiceSaver = new MusicChoiceSaver();

            boolean removeDuplicates = false;
            boolean canceledDialog = false;

            int removeDuplicatesDialogResult = JOptionPane.showConfirmDialog(this, "Would you like to try to remove duplicate music tracks when randomizing? Pressing No will simply pick at pure random.");
            if (removeDuplicatesDialogResult == JOptionPane.YES_OPTION){
                removeDuplicates = true;
            }
            else if (removeDuplicatesDialogResult == JOptionPane.NO_OPTION) {
                removeDuplicates = false;
            }
            else {
                canceledDialog = true;
            }

            if (!canceledDialog) {
                musicChoiceSaver.saveRandomMusicChoicesToMusicChoicesFile(musicNames, audioFileNames, removeDuplicates);
                generateAudioReplacementsJSON();
            }

            setVisible(false);
        }

    }

    private boolean checkIfSaveButtonWasPressed(ActionEvent e) {
        return e.getSource() == saveMusicChoicesButtons.get(0)
                || e.getSource() == saveMusicChoicesButtons.get(1)
                || e.getSource() == saveMusicChoicesButtons.get(2)
                || e.getSource() == saveMusicChoicesButtons.get(3)
                || e.getSource() == saveMusicChoicesButtons.get(4)
                || e.getSource() == saveMusicChoicesButtons.get(5);
    }

    private boolean checkIfRandomizeButtonWasPressed(ActionEvent e) {
        return e.getSource() == randomizeMusicChoicesButtons.get(0)
                || e.getSource() == randomizeMusicChoicesButtons.get(1)
                || e.getSource() == randomizeMusicChoicesButtons.get(2)
                || e.getSource() == randomizeMusicChoicesButtons.get(3)
                || e.getSource() == randomizeMusicChoicesButtons.get(4)
                || e.getSource() == randomizeMusicChoicesButtons.get(5);
    }

    private void generateUI() {

        int[] numRows = new int[6];
        numRows[0] = getNumRowsForJPanel("Act 1 Boss");
        numRows[1] = getNumRowsForJPanel("Title");
        numRows[2] = getNumRowsForJPanel("Competition Menu");
        numRows[3] = getNumRowsForJPanel("Bonus Stage: Magnetic Orbs");
        numRows[4] = getNumRowsForJPanel("Game Over");
        numRows[5] = getNumRowsForJPanel("Drowning") + 1;

        JPanel zonePanel = new JPanel();
        GridLayout zoneGridLayout = new GridLayout(numRows[0] + 1, 2);
        zonePanel.setLayout(zoneGridLayout);

        JPanel bossesPanel = new JPanel();
        GridLayout bossesGridLayout = new GridLayout((numRows[1] - numRows[0]) + 1,2);
        bossesPanel.setLayout(bossesGridLayout);

        JPanel menuEndingsPanel = new JPanel();
        GridLayout menuEndingsGridLayout = new GridLayout((numRows[2] - numRows[1]) + 1,2);
        menuEndingsPanel.setLayout(menuEndingsGridLayout);

        JPanel competitionPanel = new JPanel();
        GridLayout competitionGridLayout = new GridLayout((numRows[3] - numRows[2]) + 1,2);
        competitionPanel.setLayout(competitionGridLayout);

        JPanel miscPanel = new JPanel();
        GridLayout miscGridLayout = new GridLayout((numRows[4] - numRows[3]) + 1,2);
        miscPanel.setLayout(miscGridLayout);

        JPanel jinglesPanel = new JPanel();
        GridLayout jinglesGridLayout = new GridLayout((numRows[5] - numRows[4]) + 1,2);
        jinglesPanel.setLayout(jinglesGridLayout);

        jPanels.add(zonePanel);
        jPanels.add(bossesPanel);
        jPanels.add(menuEndingsPanel);
        jPanels.add(competitionPanel);
        jPanels.add(miscPanel);
        jPanels.add(jinglesPanel);

        //draw labels for the UI
        for (int i=0; i<musicNames.size(); i++) {
            musicTrackNameLabels[i] = new JLabel(musicNames.get(i));
        }

        for (int i=0; i<musicTrackNameLabels.length; i++) {

            //grab .ogg files from the directory chosen
            audioFiles = getAudioFileList(audioFolderPath);
            audioFileNames = getAudioFileListFileNames(audioFiles);

            //write all file paths to file
            OGGFilePathSaver oggFilePathSaver = new OGGFilePathSaver();
            oggFilePathSaver.writeOGGFilePathsToFile(audioFiles);

            //check if there are no OGG files
            if (audioFileNames.length == 0) {
                JOptionPane.showMessageDialog(this, "No .ogg files were found");
                return;
            }

            //add found .ogg files to the UI
            String[] musicTrackChoices = new String[audioFileNames.length + 1];

            musicTrackChoices[0] = "Do Not Change";

            for (int musicTrackIndex=1; musicTrackIndex<musicTrackChoices.length; musicTrackIndex++) {
                musicTrackChoices[musicTrackIndex] = audioFileNames[musicTrackIndex-1];
            }

            musicTrackPickers[i] = new JComboBox(musicTrackChoices);
            musicTrackPickers[i].setSelectedIndex(0);
            musicTrackPickers[i].addActionListener(this);
        }

        int startIndex = 0;
        for (int i=0; i<jPanels.size(); i++) {
            addToPanel(jPanels.get(i), startIndex, numRows[i]);
            startIndex = numRows[i];
            saveMusicChoicesButtons.add(new JButton("Save Music Choices"));
            saveMusicChoicesButtons.get(i).addActionListener(this);
            randomizeMusicChoicesButtons.add(new JButton("Randomize Music Choices"));
            randomizeMusicChoicesButtons.get(i).addActionListener(this);
            jPanels.get(i).add(saveMusicChoicesButtons.get(i));
            jPanels.get(i).add(randomizeMusicChoicesButtons.get(i));
        }

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Zone Themes", jPanels.get(0));
        tabbedPane.add("Boss Themes", jPanels.get(1));
        tabbedPane.add("Menu/Ending Themes", jPanels.get(2));
        tabbedPane.add("Competition Mode Themes", jPanels.get(3));
        tabbedPane.add("Misc Themes/Theme Variants", jPanels.get(4));
        tabbedPane.add("Super Themes/Jingles", jPanels.get(5));
        add(tabbedPane);
    }

    private void addToPanel(JPanel jPanel, int startIndex, int endIndex) {

        for (int i=startIndex; i<endIndex; i++) {
            jPanel.add(musicTrackNameLabels[i]);
            jPanel.add(musicTrackPickers[i]);
        }
    }

    private int getNumRowsForJPanel(String findStoppingPoint) {

        for (int i=0; i<musicNames.size(); i++) {
            String trackName = musicNames.get(i);
            if (trackName.contains(findStoppingPoint)) {
                return i;
            }
        }

        return 0;
    }

    private ArrayList<File> getAudioFileList(String audioFolderPath) {

        File[] audioFolderFileList = new File(audioFolderPath).listFiles();
        ExtensionValidator extensionValidator = new ExtensionValidator();

        ArrayList<File> oggFileList = new ArrayList<>();

        //Grab all .ogg files and check subfolders if the file  is a directory
        for (File file: audioFolderFileList) {
            String fileName = file.getName();
            if (extensionValidator.isExtensionValid(fileName) && !file.isDirectory()) {
                oggFileList.add(file);
            }
            else if (file.isDirectory()) {
                oggFileList.addAll(getAudioFileList(file.getAbsolutePath()));
            }
        }

        return oggFileList;
    }

    private String[] getAudioFileListFileNames(ArrayList<File> oggFileList) {
        String[] oggFileNames = new String[oggFileList.size()];

        for (int i=0; i<oggFileList.size(); i++) {
            oggFileNames[i] = oggFileList.get(i).getName();
        }

        return oggFileNames;
    }

    private void generateAudioReplacementsJSON() {
        File musicChoicesFile = new File("musicChoices.txt");
        if (musicChoicesFile.exists()) {
            AudioReplacementJSONGenerator audioReplacementJSONGenerator = new AudioReplacementJSONGenerator();

            if (!audioReplacementJSONGenerator.generateJSON()) {
                JOptionPane.showMessageDialog(this, "No music tracks are changed, so nothing was generated");
            }
        }
        else {
            JOptionPane.showMessageDialog(this, "You haven't saved music selections yet. Please pick some music for your mod!");
        }
    }

    public int getAudioFileNameLength() {
        return audioFileNames.length;
    }

}
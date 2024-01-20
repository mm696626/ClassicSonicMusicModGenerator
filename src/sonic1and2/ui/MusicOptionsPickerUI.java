package sonic1and2.ui;

import sonic1and2.io.MusicChoiceSaver;
import sonic1and2.io.MusicNameLoader;
import sonic1and2.io.OGGFilePathSaver;
import sonic1and2.validation.ExtensionValidator;

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
    private JLabel[] musicTrackNameLabels;
    private JComboBox[] musicTrackPickers;
    private JButton saveMusicChoices, randomizeMusicChoices;
    private String[] audioFileNames;
    private ArrayList<File> audioFiles;
    private boolean isSonic1;
    private Container container;


    public MusicOptionsPickerUI(String audioFolderPath, boolean isSonic1)
    {
        this.audioFolderPath = audioFolderPath;
        this.isSonic1 = isSonic1;
        MusicNameLoader musicNameLoader = new MusicNameLoader();
        this.musicNames =  musicNameLoader.getMusicNamesFromIDSFile(isSonic1);
        this.musicTrackCount = musicNames.size();
        this.musicTrackNameLabels = new JLabel[musicTrackCount];
        this.musicTrackPickers = new JComboBox[musicTrackCount];
        setTitle("Pick Music Options");
        this.container = getContentPane();
        container.setLayout(new BorderLayout());
        generateUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == saveMusicChoices) {
            MusicChoiceSaver musicChoiceSaver = new MusicChoiceSaver(isSonic1);
            musicChoiceSaver.saveMusicChoicesToMusicChoicesFile(musicNames, musicTrackPickers, audioFileNames);
            setVisible(false);
        }

        if (e.getSource() == randomizeMusicChoices) {
            MusicChoiceSaver musicChoiceSaver = new MusicChoiceSaver(isSonic1);

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
            }

            setVisible(false);
        }

    }

    private void generateUI() {

        JPanel jPanel = new JPanel();
        GridLayout gridLayout = new GridLayout(musicTrackCount+1, 2);
        jPanel.setLayout(gridLayout);

        //draw labels for the UI
        for (int i=0; i<musicNames.size(); i++) {

            if (musicNames.get(i).contains(" (has fast variant)")) {
                musicTrackNameLabels[i] = new JLabel(musicNames.get(i).substring(0, musicNames.get(i).lastIndexOf(" (has fast variant)")));
            }
            else {
                musicTrackNameLabels[i] = new JLabel(musicNames.get(i));
            }
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

            jPanel.add(musicTrackNameLabels[i]);
            jPanel.add(musicTrackPickers[i]);
        }

        saveMusicChoices = new JButton("Save Music Choices");
        saveMusicChoices.addActionListener(this);
        jPanel.add(saveMusicChoices);

        randomizeMusicChoices = new JButton("Randomize Music Choices");
        randomizeMusicChoices.addActionListener(this);
        jPanel.add(randomizeMusicChoices);

        JScrollPane jScrollPane = new JScrollPane(jPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        container.add(jScrollPane);
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

    public int getAudioFileNameLength() {
        return audioFileNames.length;
    }

}
package sonic1and2.io;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class MusicChoiceSaver {

    private boolean isSonic1;

    public MusicChoiceSaver(boolean isSonic1) {
        this.isSonic1 = isSonic1;
    }

    public void saveMusicChoicesToMusicChoicesFile(ArrayList<String> musicNames, JComboBox[] musicTrackPickers, String[] audioFileNames)
    {
        PrintWriter outputStream = null;

        try{
            outputStream = new PrintWriter( new FileOutputStream("musicChoices.txt"));
        }
        catch (FileNotFoundException f) {
            System.out.println("File does not exist");
            System.exit(0);
        }


        for (int i=0; i<musicTrackPickers.length; i++) {
            if (musicTrackPickers[i].getSelectedIndex() == 0) {
                //If the user does not want to change a specific theme, then put a DONTCHANGE to signify that
                outputStream.println(getTrackFileName(musicNames.get(i)) + " DONTCHANGE");
            }
            else {
                //If the user wants music replaced, and it has a fast variant add it to the choices file
                if (musicNames.get(i).contains(" (has fast variant)")) {
                    outputStream.println(getTrackFileName(musicNames.get(i)) + "_F " + audioFileNames[musicTrackPickers[i].getSelectedIndex() - 1]);
                }
                outputStream.println(getTrackFileName(musicNames.get(i)) + " " + audioFileNames[musicTrackPickers[i].getSelectedIndex() - 1]);
            }
        }

        outputStream.close();
    }

    public void saveRandomMusicChoicesToMusicChoicesFile(ArrayList<String> musicNames, String[] audioFileNames, boolean removeDuplicates)
    {
        PrintWriter outputStream = null;

        try{
            outputStream = new PrintWriter( new FileOutputStream("musicChoices.txt"));
        }
        catch (FileNotFoundException f) {
            System.out.println("File does not exist");
            System.exit(0);
        }

        ArrayList<Integer> duplicates = new ArrayList<>();

        Random rng = new Random();
        for (int i=0; i<musicNames.size(); i++) {
            int randNum = rng.nextInt(audioFileNames.length);

            if (removeDuplicates) {
                while(isDuplicate(randNum, duplicates)) {
                    randNum = rng.nextInt(audioFileNames.length);
                }
                duplicates.add(randNum);

                if (duplicates.size() == audioFileNames.length) {
                    duplicates = new ArrayList<>();
                }
            }
            //If the user wants music replaced, and it has a fast variant add it to the choices file
            if (musicNames.get(i).contains(" (has fast variant)")) {
                outputStream.println(getTrackFileName(musicNames.get(i)) + "_F " + audioFileNames[randNum]);
            }
            outputStream.println(getTrackFileName(musicNames.get(i)) + " " + audioFileNames[randNum]);
        }

        outputStream.close();
    }

    private boolean isDuplicate(int randNum, ArrayList<Integer> duplicates) {
        for (int i=0; i<duplicates.size(); i++) {
            if (randNum == duplicates.get(i)) {
                return true;
            }
        }

        return false;
    }

    private String getTrackFileName(String trackName) {
        //Gets the appropriate ogg file name for the track
        Scanner inputStream = null;

        String fileNameToLoadFrom = "";

        if (isSonic1) {
            fileNameToLoadFrom = "s1MusicIDS.txt";
        }
        else {
            fileNameToLoadFrom = "s2MusicIDS.txt";
        }

        try {
            inputStream = new Scanner (new FileInputStream(fileNameToLoadFrom));
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Music ID file does not exist");
            return "";
        }

        String trackID = "";
        while (inputStream.hasNextLine()) {
            String line = inputStream.nextLine();

            if (line.contains(trackName)) {
                trackID = line.split(":")[0];
                inputStream.close();
                return trackID;
            }
        }

        inputStream.close();
        return trackID;
    }
}

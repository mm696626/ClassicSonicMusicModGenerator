package sonic1and2.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class MusicNameLoader {

    public ArrayList<String> getMusicNamesFromIDSFile(boolean isSonic1, boolean perActMusic) {
        ArrayList<String> musicNames = new ArrayList<>();
        Scanner inputStream = null;

        String fileNameToLoadFrom = "";

        fileNameToLoadFrom = getFileNameToLoadFrom(isSonic1, perActMusic);

        try {
            inputStream = new Scanner (new FileInputStream(fileNameToLoadFrom));
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Music ID file does not exist");
            return null;
        }

        String trackName = "";
        while (inputStream.hasNextLine()) {
            String line = inputStream.nextLine();
            trackName = line.substring(line.indexOf(":")+1);
            musicNames.add(trackName);
        }

        inputStream.close();
        return musicNames;
    }

    private String getFileNameToLoadFrom(boolean isSonic1, boolean perActMusic) {
        String fileNameToLoadFrom;

        if (isSonic1 && !perActMusic) {
            fileNameToLoadFrom = "s1MusicIDS.txt";
        }
        else if (isSonic1 && perActMusic) {
            fileNameToLoadFrom = "s1PerActMusicIDS.txt";
        }
        else if (!isSonic1 && !perActMusic) {
            fileNameToLoadFrom = "s2MusicIDS.txt";
        }
        else {
            fileNameToLoadFrom = "s2PerActMusicIDS.txt";
        }
        return fileNameToLoadFrom;
    }
}

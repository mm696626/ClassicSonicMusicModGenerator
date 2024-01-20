package sonic1and2.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class MusicNameLoader {

    public ArrayList<String> getMusicNamesFromIDSFile(boolean isSonic1) {
        ArrayList<String> musicNames = new ArrayList<>();
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
}

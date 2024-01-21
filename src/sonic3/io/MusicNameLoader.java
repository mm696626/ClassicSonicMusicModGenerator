package sonic3.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class MusicNameLoader {

    public ArrayList<String> getMusicNamesFromIDSFile() {
        ArrayList<String> musicNames = new ArrayList<>();
        Scanner inputStream = null;

        try {
            inputStream = new Scanner (new FileInputStream("s3MusicIDS.txt"));
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

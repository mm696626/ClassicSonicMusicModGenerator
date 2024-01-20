package soniccd.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class MusicChoiceLoader {

    public ArrayList<String> getMusicChoicesFromFile() {
        ArrayList<String> musicChoices = new ArrayList<>();
        Scanner inputStream = null;

        try {
            inputStream = new Scanner (new FileInputStream("musicChoices.txt"));
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Music Choices file does not exist");
            return null;
        }

        while (inputStream.hasNextLine()) {
            String line = inputStream.nextLine();
            if (line.length() > 0) {
                musicChoices.add(line);
            }
        }

        inputStream.close();
        return musicChoices;
    }
}

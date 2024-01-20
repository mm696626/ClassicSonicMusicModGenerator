package sonic3.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class AudioReplacementJSONGenerator {

    private ArrayList<String> songFileNames;

    //"01": { "File": "my_music.ogg", "Type": "Music", "LoopStart": "X" }
    //file has to be called audio_replacements.json
    public boolean generateJSON() {
        songFileNames = getSongFileNames();
        if (areAllSongsUnchanged(songFileNames)) {
            return false;
        }
        generateAudioReplacementsJSON();
        return true;
    }

    private void generateAudioReplacementsJSON() {
        PrintWriter outputStream = null;

        try {
            outputStream = new PrintWriter(new FileOutputStream("audio_replacements.json"));
        }
        catch (FileNotFoundException f) {
            System.out.println("File does not exist");
            return;
        }

        Scanner inputStream = null;

        try {
            inputStream = new Scanner (new FileInputStream("musicChoices.txt"));
        }
        catch (FileNotFoundException e)
        {
            System.out.println("No music choices file exists");
            return;
        }

        outputStream.println("{");


        int index = 0;

        while (inputStream.hasNextLine()) {
            String line = inputStream.nextLine();
            String ID = line.substring(0, line.indexOf(" "));
            String fileName = line.substring(line.indexOf(" ") + 1);

            if (!fileName.equals("DONTCHANGE")) {
                //Example JSON Line: "01": { "File": "my_music.ogg", "Type": "Music", "LoopStart": "X" }
                String jsonLine = "\t" + "\"" + ID + "\": { \"File\": \"" + fileName + "\", \"Type\": \"Music\", \"LoopStart\": \"0\" }";

                //Blue Spheres is an exception to the rule, so we need to hardcode its two other IDs
                if (ID.equals("1C")) {
                    String blueSpheresSpeedUp = "\t" + "\"" + "1C_speedup" + "\": { \"File\": \"" + fileName + "\", \"Type\": \"Music\", \"LoopStart\": \"0\" },";
                    String blueSpheresSpeedUp45 = "\t" + "\"" + "1C_speedup45" + "\": { \"File\": \"" + fileName + "\", \"Type\": \"Music\", \"LoopStart\": \"0\" },";
                    outputStream.println(blueSpheresSpeedUp);
                    outputStream.println(blueSpheresSpeedUp45);
                }

                if (inputStream.hasNextLine() && !isLastChangedSong(index)) {
                    jsonLine = jsonLine + ",";
                }

                outputStream.println(jsonLine);
            }

            //check if at EOF or at the last changed song
            if (isLastChangedSong(index)) {
                break;
            }
            index++;
        }

        outputStream.println("}");
        inputStream.close();
        outputStream.close();
    }
    private ArrayList<String> getSongFileNames() {
        Scanner inputStream = null;
        ArrayList<String> fileNames = new ArrayList<>();

        try {
            inputStream = new Scanner (new FileInputStream("musicChoices.txt"));
        }
        catch (FileNotFoundException e)
        {
            System.out.println("No music choices file exists");
            return new ArrayList<>();
        }

        while (inputStream.hasNextLine()) {
            String[] lineContents = inputStream.nextLine().split(" ");
            String fileName = lineContents[1];
            fileNames.add(fileName);
        }

        inputStream.close();
        return fileNames;
    }

    private boolean isLastChangedSong(int index) {
        for (int i=index+1; i<songFileNames.size(); i++) {
            if (!songFileNames.get(i).equals("DONTCHANGE")) {
                return false;
            }
        }

        return true;
    }

    private boolean areAllSongsUnchanged(ArrayList<String> songFileNames) {
        for (int i=0; i<songFileNames.size(); i++) {
            if (!songFileNames.get(i).equals("DONTCHANGE"))
                return false;
        }

        return true;
    }
}

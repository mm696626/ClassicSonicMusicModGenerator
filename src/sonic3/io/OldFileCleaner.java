package sonic3.io;

import java.io.File;

public class OldFileCleaner {

    public void cleanFiles() {
        File audioReplacementsFile = new File("audio_replacements.json");
        File modJSONFile = new File("mod.json");
        File musicChoicesFile = new File("musicChoices.txt");
        File oggFilePathsFile = new File("oggFilePaths.txt");

        if (!audioReplacementsFile.delete()) {
            System.out.println("Audio Replacements File could not be deleted");
        }

        if (!modJSONFile.delete()) {
            System.out.println("Mod JSON File could not be deleted");
        }

        if (!musicChoicesFile.delete()) {
            System.out.println("Music Choices File could not be deleted");
        }

        if (!oggFilePathsFile.delete()) {
            System.out.println("OGG Paths File could not be deleted");
        }

    }
}

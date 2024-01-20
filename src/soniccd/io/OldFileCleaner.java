package soniccd.io;

import java.io.File;

public class OldFileCleaner {

    public void cleanFiles() {
        File modINIFile = new File("mod.ini");
        File musicChoicesFile = new File("musicChoices.txt");
        File oggFilePathsFile = new File("oggFilePaths.txt");

        if (!modINIFile.delete()) {
            System.out.println("Mod INI File could not be deleted");
        }

        if (!musicChoicesFile.delete()) {
            System.out.println("Music Choices File could not be deleted");
        }

        if (!oggFilePathsFile.delete()) {
            System.out.println("OGG Paths File could not be deleted");
        }

    }
}

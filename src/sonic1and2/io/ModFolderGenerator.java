package sonic1and2.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Scanner;

public class ModFolderGenerator {

    public void generateModFolder() throws IOException {

        ArrayList<File> files = getFilePathsFromFile();

        MusicChoiceLoader musicChoiceLoader = new MusicChoiceLoader();
        ArrayList<String> musicChoices = musicChoiceLoader.getMusicChoicesFromFile();

        String modFolderName = getModFolderName();
        String modBaseDir = getModBaseDir();
        String filePathSeparator = modBaseDir.substring(modBaseDir.length()-1);
        String modFolderPath = modBaseDir + modFolderName;

        File modFolder = new File(modFolderPath);

        if (modFolder.mkdirs()) {

            //copy mod.ini
            File modJSON = new File("mod.ini");
            String copiedModJSONPath = modFolder.getAbsolutePath() + filePathSeparator + "mod.ini";
            File copiedModJSON = new File(copiedModJSONPath);
            Files.copy(modJSON.toPath(), copiedModJSON.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);

            String audioFolderPath = modFolderPath + filePathSeparator + "Data" + filePathSeparator + "Music";

            File audioFolder = new File(audioFolderPath);
            if (audioFolder.mkdirs()) {
                copyAudioFiles(files, audioFolder.getAbsolutePath() + filePathSeparator, musicChoices);
            }
        }
    }

    private String getModBaseDir() {
        File modJSON = new File("mod.ini");
        String modJSONPath = modJSON.getAbsolutePath();
        return modJSONPath.substring(0, modJSONPath.lastIndexOf("mod.ini"));
    }

    private ArrayList<File> getFilePathsFromFile() {
        ArrayList<File> files = new ArrayList<>();

        MusicChoiceLoader musicChoiceLoader = new MusicChoiceLoader();
        ArrayList<String> musicChoices = musicChoiceLoader.getMusicChoicesFromFile();

        for (int i=0; i<musicChoices.size(); i++) {
            String filePath = getFilePathForMusicChoice(musicChoices.get(i));
            files.add(new File(filePath));
        }

        return files;
    }

    private String getFilePathForMusicChoice(String musicChoice) {
        Scanner inputStream = null;

        try {
            inputStream = new Scanner (new FileInputStream("oggFilePaths.txt"));
        }
        catch (FileNotFoundException e)
        {
            System.out.println("OGG File Paths file does not exist");
            return null;
        }

        while (inputStream.hasNextLine()) {
            String filePath = inputStream.nextLine();
            String fileName = musicChoice.substring(musicChoice.indexOf(" ") + 1);
            if (fileName.equals("DONTCHANGE")) {
                inputStream.close();
                return "DONTCHANGE";
            }

            if (filePath.endsWith(fileName)) {
                inputStream.close();
                return filePath;
            }
        }

        inputStream.close();
        return null;
    }

    private String getModFolderName() {
        String modFolderName = "";

        Scanner inputStream = null;

        try {
            inputStream = new Scanner (new FileInputStream("mod.ini"));
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Mod JSON File does not exist");
            return null;
        }

        String line = "";
        while (inputStream.hasNextLine()) {
            line = inputStream.nextLine();
            if (line.contains("Name=")) {
                modFolderName = line.substring(line.lastIndexOf("=")+1);
                break;
            }
        }

        modFolderName = modFolderName.replaceAll("[^a-zA-Z0-9]", "");
        inputStream.close();
        return modFolderName;
    }

    private void copyAudioFiles(ArrayList<File> files, String destinationPath, ArrayList<String> musicChoices) throws IOException {

        for (int i=0; i<files.size(); i++) {
            String audioFilePath = files.get(i).getAbsolutePath();
            String copiedAudioFilePath = destinationPath + musicChoices.get(i).split(" ")[0] + ".ogg";
            File copiedAudioFile = new File(copiedAudioFilePath);

            if (!audioFilePath.endsWith("DONTCHANGE")) {
                Files.copy(files.get(i).toPath(), copiedAudioFile.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
            }
        }
    }
}

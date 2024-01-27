package sonic1and2.io;

import sonic1and2.validation.ExtensionValidator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Scanner;

public class ModFolderGenerator {

    public void generateModFolder(boolean isSonic1, boolean perActMusic) throws IOException {

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


            //copy scripts if per act music was chosen
            if (perActMusic) {
                String scriptsFolderPath = createScriptsFolder(modFolderPath, filePathSeparator);

                if (scriptsFolderPath != null) {
                    copyScriptsToScriptsFolder(isSonic1, scriptsFolderPath);
                }
            }

            String audioFolderPath = modFolderPath + filePathSeparator + "Data" + filePathSeparator + "Music";

            File audioFolder = new File(audioFolderPath);
            if (audioFolder.mkdirs()) {
                copyAudioFiles(files, audioFolder.getAbsolutePath() + filePathSeparator, musicChoices);
            }
        }
    }

    private String createScriptsFolder(String modFolderPath, String filePathSeparator) {
        String scriptsFolderPath = modFolderPath + filePathSeparator + "Scripts";
        File scriptsFolder = new File(scriptsFolderPath);
        if (scriptsFolder.mkdirs()) {
            return scriptsFolderPath;
        }

        return null;
    }

    private void copyScriptsToScriptsFolder(boolean isSonic1, String scriptsFolderPath) throws IOException {
        File scriptFolder = new File(getPathOfScriptFolder(isSonic1));
        ArrayList<File> scriptFiles = getScriptFileList(scriptFolder.getAbsolutePath());

        for (int i=0; i< scriptFiles.size(); i++) {
            String scriptFilePath = scriptFiles.get(i).getAbsolutePath();
            String destinationScriptPath = getDestinationPath(scriptFilePath, scriptsFolderPath, isSonic1);

            String copiedScriptFilePath = destinationScriptPath;
            File copiedScriptFile = new File(copiedScriptFilePath);
            Files.copy(scriptFiles.get(i).toPath(), copiedScriptFile.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
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

    private ArrayList<File> getScriptFileList(String scriptFolderPath) {

        File[] scriptFolderFileList = new File(scriptFolderPath).listFiles();
        ExtensionValidator extensionValidator = new ExtensionValidator();

        ArrayList<File> scriptFileList = new ArrayList<>();

        //Grab all script files and check subfolders if the file is a directory
        for (File file: scriptFolderFileList) {
            String fileName = file.getName();
            if (extensionValidator.isScriptFileExtensionValid(fileName) && !file.isDirectory()) {
                scriptFileList.add(file);
            }
            else if (file.isDirectory()) {
                scriptFileList.addAll(getScriptFileList(file.getAbsolutePath()));
            }
        }

        return scriptFileList;
    }

    private String getPathOfScriptFolder(boolean isSonic1) {
        String baseDir = getModBaseDir();

        if (isSonic1) {
            return baseDir + "s1Scripts";
        }
        else  {
            return baseDir + "s2Scripts";
        }
    }

    private String getDestinationPath(String scriptFilePath, String modFolderScriptsPath, boolean isSonic1) {
        String[] sonic1Scripts = {"GHZ", "MZ", "SYZ", "LZ", "SLZ", "SBZ"};
        String[] sonic2Scripts = {"EHZ", "CPZ", "ARZ", "CNZ", "HTZ", "MCZ", "OOZ", "MPZ"};
        String destinationPath;

        String modBaseDir = getModBaseDir();
        String filePathSeparator = modBaseDir.substring(modBaseDir.length()-1);

        int numScripts = 0;

        if (isSonic1) {
            numScripts = sonic1Scripts.length;
        }

        else {
            numScripts = sonic2Scripts.length;
        }

        for (int i=0; i<numScripts; i++) {

            if (isSonic1) {

                String s1ScriptsPath = scriptFilePath.substring(scriptFilePath.indexOf("s1Scripts"));
                s1ScriptsPath = s1ScriptsPath.substring(s1ScriptsPath.indexOf(filePathSeparator) + 1, s1ScriptsPath.lastIndexOf(filePathSeparator));

                if (s1ScriptsPath.equals(sonic1Scripts[i])) {
                    destinationPath = modFolderScriptsPath + scriptFilePath.substring(scriptFilePath.indexOf(sonic1Scripts[i]) - 1);

                    String scriptDestinationFolder = modFolderScriptsPath + filePathSeparator + sonic1Scripts[i];
                    File destinationFolder = new File(scriptDestinationFolder);

                    if (!destinationFolder.exists()) {
                        destinationFolder.mkdirs();
                    }

                    return destinationPath;
                }
            }

            else {

                String s2ScriptsPath = scriptFilePath.substring(scriptFilePath.indexOf("s2Scripts"));
                s2ScriptsPath = s2ScriptsPath.substring(s2ScriptsPath.indexOf(filePathSeparator) + 1, s2ScriptsPath.lastIndexOf(filePathSeparator));

                if (s2ScriptsPath.equals(sonic2Scripts[i])) {
                    destinationPath = modFolderScriptsPath + scriptFilePath.substring(scriptFilePath.indexOf(sonic2Scripts[i]) - 1);

                    String scriptDestinationFolder = modFolderScriptsPath + filePathSeparator + sonic2Scripts[i];
                    File destinationFolder = new File(scriptDestinationFolder);

                    if (!destinationFolder.exists()) {
                        destinationFolder.mkdirs();
                    }

                    return destinationPath;
                }
            }
        }

        return null;
    }
}

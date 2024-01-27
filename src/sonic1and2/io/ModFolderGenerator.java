package sonic1and2.io;

import sonic1and2.validation.ExtensionValidator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
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

                    String modScriptFolderPath = modFolderPath + filePathSeparator + "Scripts";
                    modifyScriptsInScriptsFolder(modScriptFolderPath, isSonic1);
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

    private void modifyScriptsInScriptsFolder(String scriptsFolderPath, boolean isSonic1) {
        File scriptFolder = new File(scriptsFolderPath);
        ArrayList<File> scriptFiles = getScriptFileList(scriptFolder.getAbsolutePath());

        for (int i=0; i<scriptFiles.size(); i++) {
            String zoneName = scriptFiles.get(i).getName();
            zoneName = zoneName.substring(0, zoneName.indexOf("Setup.txt"));

            ArrayList<String> scriptFileData = getScriptFileData(scriptFiles.get(i));
            modifyScriptFile(scriptFileData, scriptFiles.get(i), isSonic1, zoneName);
        }
    }

    private ArrayList<String> getScriptFileData(File scriptFile) {
        ArrayList<String> fileData = new ArrayList<>();

        Scanner inputStream = null;

        try {
            inputStream = new Scanner (new FileInputStream(scriptFile.getAbsolutePath()));
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Mod JSON File does not exist");
            return null;
        }

        while (inputStream.hasNextLine()) {
            fileData.add(inputStream.nextLine());
        }

        inputStream.close();
        return fileData;
    }

    private void modifyScriptFile(ArrayList<String> fileData, File scriptFile, boolean isSonic1, String zoneName) {

        PrintWriter outputStream = null;

        try {
            outputStream = new PrintWriter( new FileOutputStream(scriptFile.getAbsolutePath()));
        }
        catch (FileNotFoundException f) {
            System.out.println("File does not exist");
            System.exit(0);
        }

        for (int i=0; i<fileData.size(); i++) {
            String line = fileData.get(i);
            if (line.contains("SetMusicTrack(") || line.contains("SwapMusicTrack(")) {
                String musicTrackName = line.substring(line.indexOf("\"")+1, line.lastIndexOf("\""));
                musicTrackName = musicTrackName.substring(0, musicTrackName.indexOf("."));

                if (isActMusic(isSonic1, musicTrackName)) {
                    String newLine;
                    String musicChoice = getMusicChoiceForScript(musicTrackName);
                    if (musicChoice.equals("DONTCHANGE")) {

                        String zoneReplacement = getZoneReplacement(zoneName, isSonic1);
                        if (musicTrackName.endsWith("_F")) {
                            zoneReplacement = zoneReplacement + "_F";
                        }
                        newLine = line.replace(musicTrackName, zoneReplacement);
                        outputStream.println(newLine);
                    }
                    else {
                        outputStream.println(fileData.get(i));
                    }
                }
                else {
                    outputStream.println(fileData.get(i));
                }
            }
            else {
                outputStream.println(fileData.get(i));
            }
        }

        outputStream.close();
    }

    private String getZoneReplacement(String zoneName, boolean isSonic1) {
        String[] sonic1ZoneNames = {"GHZ", "MZ", "SYZ", "LZ", "SLZ", "SBZ"};
        String[] sonic2ZoneNames = {"EHZ", "CPZ", "ARZ", "CNZ", "HTZ", "MCZ", "OOZ", "MPZ"};

        String[] sonic1Zones = {"GreenHill", "Marble", "SpringYard", "Labyrinth", "StarLight", "ScrapBrain"};
        String[] sonic2Zones = {"EmeraldHill", "ChemicalPlant", "AquaticRuin", "CasinoNight", "HillTop", "MysticCave", "OilOcean", "Metropolis"};

        int numZones = 0;

        if (isSonic1) {
            numZones = sonic1Zones.length;
        }

        else {
            numZones = sonic2Zones.length;
        }

        for (int i=0; i<numZones; i++) {
            if (isSonic1) {
                if (zoneName.equals(sonic1ZoneNames[i])) {
                    return sonic1Zones[i];
                }
            }

            else {
                if (zoneName.equals(sonic2ZoneNames[i])) {
                    return sonic2Zones[i];
                }
            }
        }

        return "";
    }

    private String getMusicChoiceForScript(String musicTrackName) {
        MusicChoiceLoader musicChoiceLoader = new MusicChoiceLoader();
        ArrayList<String> musicChoices = musicChoiceLoader.getMusicChoicesFromFile();

        for (int i=0; i<musicChoices.size(); i++) {
            String musicTrack = musicChoices.get(i).split(" ")[0];
            String musicChoice = musicChoices.get(i).split(" ")[1];

            if (musicTrack.equals(musicTrackName)) {
                return musicChoice;
            }
        }
        return "DONTCHANGE";
    }

    private boolean isActMusic(boolean isSonic1, String musicTrackName) {
        String[] sonic1Acts = {"GHZA1", "GHZA2", "GHZA3", "MZA1", "MZA2", "MZA3", "SYZA1", "SYZA2", "SYZA3", "LZA1", "LZA2", "LZA3"
        , "SLZA1", "SLZA2", "SLZA3", "SBZA1", "SBZA2", "SBZA3"};

        String[] sonic2Acts = {"EmeraldHillA1", "EmeraldHillA2", "ChemicalPlantA1", "ChemicalPlantA2"
        , "AquaticRuinA1", "AquaticRuinA2", "CasinoNightA1", "CasinoNightA2", "HillTopA1", "HillTopA2", "MysticCaveA1", "MysticCaveA2"
        , "OilOceanA1", "OilOceanA2", "MetropolisA1", "MetropolisA2", "MetropolisA3"};

        int numActs = 0;

        if (isSonic1) {
            numActs = sonic1Acts.length;
        }

        else {
            numActs = sonic2Acts.length;
        }

        for (int i=0; i<numActs; i++) {
            if (isSonic1) {
                if (musicTrackName.contains(sonic1Acts[i])) {
                    return true;
                }
            }

            else {
                if (musicTrackName.contains(sonic2Acts[i])) {
                    return true;
                }
            }
        }

        return false;
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

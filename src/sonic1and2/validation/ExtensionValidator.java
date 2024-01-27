package sonic1and2.validation;

public class ExtensionValidator {

    public boolean isExtensionValid(String fileName) {
        return fileName.endsWith("ogg");
    }
    public boolean isScriptFileExtensionValid(String fileName) {
        return fileName.endsWith("txt");
    }
}

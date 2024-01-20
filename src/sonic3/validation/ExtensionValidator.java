package sonic3.validation;

public class ExtensionValidator {

    public boolean isExtensionValid(String fileName) {
        return fileName.endsWith("ogg");
    }
}

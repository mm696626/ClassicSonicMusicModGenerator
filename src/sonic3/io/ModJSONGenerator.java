package sonic3.io;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class ModJSONGenerator {

    public void generateModJSON(String[] JSONFieldNames, JTextField[] jsonFieldNameFields)
    {
        PrintWriter outputStream = null;

        try{
            outputStream = new PrintWriter( new FileOutputStream("mod.json"));
        }
        catch (FileNotFoundException f) {
            System.out.println("File does not exist");
            System.exit(0);
        }

        outputStream.println("{");
        outputStream.println("\t" + "\"Metadata\":");
        outputStream.println("\t" + "{");

        for (int i=0; i<JSONFieldNames.length; i++) {

            String jsonFieldNameFieldValue = jsonFieldNameFields[i].getText().trim();

            String jsonLine = "";

            if (jsonFieldNameFieldValue.length() > 0) {
                jsonLine = "\t\t" + "\"" + JSONFieldNames[i] + "\": \"" + jsonFieldNameFieldValue + "\",";
            }
            else {
                jsonLine = "\t\t" + "\"" + JSONFieldNames[i] + "\": \"" + "Nothing was provided here" + "\",";
            }

            if (i == JSONFieldNames.length - 1)  {
                jsonLine = jsonLine.substring(0, jsonLine.lastIndexOf(","));
            }

            outputStream.println(jsonLine);
        }

        outputStream.println("\t" + "}");
        outputStream.println("}");
        outputStream.close();
    }
}

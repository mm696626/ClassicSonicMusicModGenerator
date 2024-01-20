package sonic1and2.io;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class ModINIGenerator {

    public void generateModJSON(String[] INIFieldNames, JTextField[] INIFieldNameFields)
    {
        PrintWriter outputStream = null;

        try{
            outputStream = new PrintWriter( new FileOutputStream("mod.ini"));
        }
        catch (FileNotFoundException f) {
            System.out.println("File does not exist");
            System.exit(0);
        }

        for (int i=0; i<INIFieldNames.length; i++) {

            String iniFieldNameFieldValue = INIFieldNameFields[i].getText().trim();

            String iniLine = "";

            if (iniFieldNameFieldValue.length() > 0) {
                iniLine = INIFieldNames[i] + "=" + iniFieldNameFieldValue;
            }
            else {
                iniLine = INIFieldNames[i] + "=" + "Nothing was provided here";
            }

            outputStream.println(iniLine);
        }

        outputStream.close();
    }
}

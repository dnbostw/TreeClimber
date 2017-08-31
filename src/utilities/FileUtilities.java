/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author dnbostw
 */
public class FileUtilities {

    public static FileWriter log;

    public FileUtilities() {
    }

    public static void copyFile(File source, File destination) throws FileNotFoundException, IOException {
        copyFile(source.getAbsoluteFile(),destination.getAbsoluteFile());
    }
    
    public static void copyFile(String source, String destination) throws FileNotFoundException, IOException {

        BufferedReader inFile = openReadOnlyFile(source);
        FileWriter     outFile= openWriteOnlyFile(destination);
        String line = null;
        while ((line=inFile.readLine()) != null) {
            outFile.write(line.concat("\n"));
        }
        closeReadOnlyFile(inFile);
        closeWriteOnlyFile(outFile);
    }
    
    public static BufferedReader openReadOnlyFile(String filename) throws FileNotFoundException {
        BufferedReader file = new BufferedReader(new FileReader(new File(filename)));
        return file;
    }

    public static void openLog(String fileName) throws IOException {
        log = new FileWriter(new File(fileName));
    }

    public static void log(String string) throws IOException {
        if (string == null) {
            return;
        }
        if (log == null) {
            System.out.println(string);
        } else {
            log.write(string.concat("\n"));
        }
    }

    public static void closeLog() throws IOException {
        if (log != null) {
            log.flush();
            log.close();
        }
        log=null;
    }
    

    public static FileWriter openWriteOnlyFile(String filename) throws IOException {
        deleteFile(filename);
        File file = new File(filename);
        file.createNewFile();
        FileWriter filew = new FileWriter(file);
        return filew;
    }

    public static void closeReadOnlyFile(BufferedReader file) throws IOException {
        if (file != null) {
            file.close();
        }
    }

    public static void closeWriteOnlyFile(FileWriter file) throws IOException {
        file.flush();
        file.close();
    }

    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
}
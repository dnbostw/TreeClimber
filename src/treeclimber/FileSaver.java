/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treeclimber;

import java.io.File;
import java.io.IOException;
import javafx.stage.FileChooser;
import utilities.FileUtilities;

/**
 *
 * @author dnbostw
 */
public class FileSaver {
    private FileChooser fileChooser;

    public FileSaver(String title) {
        fileChooser = new FileChooser();
        fileChooser.setTitle(title);
    }
    
    public FileSaver(String title, String extensionDescription, String[] extensions) {
        this(title);
        if (extensionDescription != null) {
            FileChooser.ExtensionFilter extFilter = 
                        new FileChooser.ExtensionFilter(extensionDescription, extensions);
            fileChooser.getExtensionFilters().add(extFilter);
        }
    }
    
    public void saveFile(String pathOfFileToSave) throws IOException {
        String saveDirectory = System.getProperty("user.home");
        fileChooser.setInitialDirectory(new File(saveDirectory));
        String detaultInputFileName = pathOfFileToSave.lastIndexOf("/") > 0 
                ? pathOfFileToSave.substring(pathOfFileToSave.lastIndexOf("/")+1) 
                : pathOfFileToSave;
        fileChooser.setInitialFileName(detaultInputFileName);
        File saveFile = fileChooser.showSaveDialog(Core.getMainStage()) ; // OpenDialog(Core.getMainStage());
        
        if (saveFile != null) {
            FileUtilities.copyFile(pathOfFileToSave, saveFile.getAbsolutePath());
        }
    }
        
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treeclimber.components;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import treeclimber.Context;
import treeclimber.Core;
import static treeclimber.Core.mainStage;

/**
 *
 * @author dnbostw
 */
public class ContextPanel extends QuickDialog<ContextData> { // BorderPane {
    
   @FXML TextField contextName, sourceFilepath, fieldParserName;
   @FXML TextField excludeEditText;
   @FXML ListView<String>  excludeListView;
   
   @FXML Button defaultButton, cancelButton, saveButton, addButton, removeButton;
    
   @FXML public void handleSourcePath(ActionEvent event) {
        FileChooser fc = new FileChooser();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Project File for Import");
        String defaultDirectory = System.getProperty("user.home");
        fileChooser.setInitialDirectory(new File(defaultDirectory));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Jar/War Files", "*.jar", "*.war"));
        File selectedFile = fileChooser.showOpenDialog(mainStage);
        sourceFilepath.setText(selectedFile.getAbsolutePath());
//        if (selectedFile != null) {
//            status.setText(selectedFile.getName());
//        }
       
   }
   
   @FXML  public void handleAddButton(ActionEvent event) { 
       String textToAdd = excludeEditText.getText();
       if (textToAdd.equals("")) return;
       Set<String> tmpValues = new TreeSet<String>(excludeListView.getItems());
       tmpValues.add(textToAdd);
       excludeListView.getItems().setAll(tmpValues);
       excludeEditText.setText("");
   }
   
   @FXML  public void handleRemoveButton(ActionEvent event) { 
       ObservableList<String> removeItems = excludeListView.getSelectionModel().getSelectedItems();
       if (removeItems != null && removeItems.size() > 0) {
           excludeListView.getItems().removeAll(removeItems);
       }
       excludeEditText.setText("");
   }
   
   @FXML  public void resetDefaultValues(ActionEvent event) { 
        restoreInstance();
        updateViewFromInstance();
   }
   
   @FXML  public void saveEdits(ActionEvent event) {
        updateInstanceFromView();
        if (isValid()) {
            dialogStage.close();
        }
   }
   
   @FXML  public void cancelEdits(ActionEvent event) {
       setInstance(null);
       restoreInstance();
       dialogStage.close();
   }
 
   @Override
   public void updateViewFromInstance() {
       contextName.setText(getInstance().getContextName());
       sourceFilepath.setText(getInstance().getSourceFilepath());
       fieldParserName.setText(getInstance().getFieldParserName());
   }
   
   @Override
   public void updateInstanceFromView() {
       getInstance().setContextName(contextName.getText().trim().toLowerCase());
       getInstance().setSourceFilepath(sourceFilepath.getText());
       getInstance().setFieldParserName(fieldParserName.getText());
   }
   
   @Override
   public void setDefaultDisplayFields() {
       contextName.setText("<context name>");
       sourceFilepath.setText("*.jar, *.war");
       fieldParserName.setText("treeclimber.parsers.DefaultLineParser");
   }
   
   @Override
   public void setDefaultInstance() {
       getInstance().restoreDefaults("");
   }
   
    /**
     *
     * @author dnbostw
     */
   
    public ContextPanel(Window parentWindow, ContextData data) throws IOException {
        super(parentWindow, data, "components/ContextPanel.fxml");
    }

    public ContextPanel(ContextData data) throws IOException {
        super(Core.getMainWindow(), data, "components/ContextPanel.fxml");
    }

    @Override
    public void initialize() {
    }

    @Override
    public String validate(ContextData instance) {
        String title = "Error occurred while validating Context data";
        String result = null;
        if (contextName.getText() == null || contextName.getText().trim().equals("")) {
            return QuickDialog.formatValidationErrorMessage(title, "Missing Context Name", "Context name must be non-blank");
        }
        if (sourceFilepath.getText() == null || sourceFilepath.getText().trim().equals("")) {
            return QuickDialog.formatValidationErrorMessage(title, "Missing Source Jar/War Name", "Source Jar/War name must be non-blank");
        }
        File testFile = new File(sourceFilepath.getText().trim());
        if (!testFile.exists()) {
            return QuickDialog.formatValidationErrorMessage(title, "Source Jar/War doesn't exist.", sourceFilepath.getText().concat(" not found."));
        }
        if (fieldParserName.getText() == null || fieldParserName.getText().trim().equals("")) {
            return QuickDialog.formatValidationErrorMessage(title, "Missing Field Parser Name", "Field parser class name must be non-blank");
        }
        Object testParser = null;
        try {
            testParser = Context.loadFieldParser(fieldParserName.getText().trim());
        } catch (Exception ex) {
            return QuickDialog.formatValidationErrorMessage(title, "Error Loading Field Parser", ex.getMessage());
        }
        if (testParser == null) {
            return QuickDialog.formatValidationErrorMessage(title, "Error Loading Field Parser", "Check the field parser name spelling");
        }
        return null;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treeclimber.components;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import treeclimber.Core;

/**
 *
 * @author dnbostw
 */
public class EditExcludesPanel extends QuickDialog<EditExcludesData> { // BorderPane {
    
   @FXML TextField excludeEditText;
   @FXML ListView<String>  excludeListView;
   
   @FXML Button defaultButton, cancelButton, saveButton, addButton, removeButton;
    
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
       excludeEditText.setText(getInstance().getEditText());
       excludeListView.getItems().setAll(FXCollections.observableArrayList(getInstance().getList()));
   }
   
   @Override
   public void updateInstanceFromView() {
       getInstance().setEditText(excludeEditText.getText());
       getInstance().getList().setAll(FXCollections.observableArrayList(excludeListView.getItems()));
   }
   
   @Override
   public void setDefaultDisplayFields() {
       excludeEditText.setText("");
       excludeListView.setItems(getInstance().getList());
   }
   
   @Override
   public void setDefaultInstance() {
       getInstance().setEditText("");
       getInstance().setList(FXCollections.observableArrayList());
   }
   
    /**
     *
     * @author dnbostw
     */
   
    public EditExcludesPanel(Window parentWindow, EditExcludesData data) throws IOException {
        super(Core.getMainWindow(), data, "components/EditExcludesPanel.fxml");
    }

    @Override
    public void initialize() {
    }

    @Override
    public String validate(EditExcludesData instance) {
        return null;
    }
}

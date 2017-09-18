/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treeclimber.components;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import treeclimber.Core;
import treeclimber.TreeClimber;
import treeclimber.newrelic.Editable;

/**
 *
 * @author dnbostw
 */
public abstract class  QuickDialog<T> extends BorderPane {

    protected Stage dialogStage;
    protected T instance;
    protected T saveInstance;

    /** Set value that will be the edit target.  This is also the return value **/
    public void setInstance(T t) {
        instance = t;
    }

    /** Return the edit Target **/
    public T getInstance() {
        return instance;
    }

    /** Backup the original edit target in case user Cancels **/
    public void saveInstance() {
        saveInstance = (T) ((Editable)instance).copy(null);
    }

    public void restoreInstance() {
        ((Editable)saveInstance).copy((Editable)instance);
    }

    public void initInstance(T editTarget) {
        setInstance(editTarget);
        saveInstance();
        updateViewFromInstance();
    }

    public abstract String validate(T instance);
    
    public abstract void updateViewFromInstance();

    public abstract void updateInstanceFromView();
    
    public abstract void setDefaultDisplayFields();

    public abstract void setDefaultInstance();
    
    public abstract void initialize();
    
    public QuickDialog() { }
    
    public QuickDialog(Window parentWindow, T editTarget, String fxmlFilePath) throws IOException {
        // Load the fxml file and create a new stage for the popup dialog.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TreeClimber.class.getResource(fxmlFilePath));
        loader.setController(this);
        BorderPane page = (BorderPane) loader.load();
        page.getStylesheets().add("file:css/stylein.css");
        // Create the dialog Stage.
        dialogStage = new Stage();
        dialogStage.setTitle("Edit Person");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(parentWindow);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        initInstance(editTarget);
        initialize();
    }

    public T show() {
        dialogStage.showAndWait();
        return instance;
    }
    
    public final static String formatValidationErrorMessage(String title, String header, String message) {
        return String.format(Core.VALIDATION_ERROR_MESSSAGE,title,header,message);
    }
    
    public final static String[] decodeValidationErrorMessage(String message) {
        if (message == null) return new String[2];
        String[] result = message.split("#");
        return result;
    }
    
    protected boolean isValid() {
        String error = validate(getInstance());
        if (error != null) {
            String[] text = decodeValidationErrorMessage(error);
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle(text[Core.VALIDATION_ERROR_TITLE]);
            alert.setHeaderText(text[Core.VALIDATION_ERROR_HEADER]);
            alert.setContentText(text[Core.VALIDATION_ERROR_MESSAGE]);
            alert.showAndWait();
            return false;
        }
        return true;
    }
    
    public void close() {
        dialogStage.close();
    }

}

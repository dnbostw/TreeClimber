/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treeclimber.components;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
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
    
    public void close() {
        dialogStage.close();
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treeclimber.components;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import treeclimber.Core;
import treeclimber.newrelic.InstrumentationDescriptor;

/**
 *
 * @author dnbostw
 */
public class InstrumentationPanel extends QuickDialog<InstrumentationDescriptor> { // BorderPane {
    
   private final static String DEFAULT_EXTENSION_NAME="defaultExtension";
   private final static String DEFAULT_VERSION="1.0";
   private final static String DEFAULT_METRIC_PREFIX="";
   private final static boolean DEFAULT_ENABLED=true;
   
   @FXML TextField extensionNameText, versionText, metricPrefixText;
   @FXML CheckBox  enabledCheckbox;

   @FXML Button defaultButton, cancelButton, saveButton;
    
   @FXML  public void resetDefaultValues(ActionEvent event) { 
        setDefaultDisplayFields();
        updateInstanceFromView();
   }
   @FXML  public void saveEdits(ActionEvent event) {
        updateInstanceFromView();
        close();
   }
   @FXML  public void cancelEdits(ActionEvent event) {
       restoreInstance();
       close();
//       setInstance(null);
   }
 
   @Override
   public void updateViewFromInstance() {
        extensionNameText.setText(getInstance().getExtensionName());
        versionText.setText(getInstance().getVersion());
        metricPrefixText.setText(getInstance().getMetricPrefix());
        enabledCheckbox.setSelected(getInstance().isEnabled());
   }
   
   @Override
   public void updateInstanceFromView() {
       getInstance().setExtensionName(extensionNameText.getText().trim());
       getInstance().setVersion(versionText.getText().trim());
       getInstance().setMetricPrefix(metricPrefixText.getText());
       getInstance().setEnabled(enabledCheckbox.isSelected());
   }
   
   @Override
   public void setDefaultDisplayFields() {
       extensionNameText.setText(DEFAULT_EXTENSION_NAME);
       versionText.setText(DEFAULT_VERSION);
       metricPrefixText.setText(DEFAULT_METRIC_PREFIX);
       enabledCheckbox.setSelected(DEFAULT_ENABLED);
   }
   
   @Override
   public void setDefaultInstance() {
       getInstance().setExtensionName(DEFAULT_EXTENSION_NAME);
       getInstance().setVersion(DEFAULT_VERSION);
       getInstance().setMetricPrefix(DEFAULT_METRIC_PREFIX);
       getInstance().setEnabled(DEFAULT_ENABLED);
   }
   
    /**
     *
     * @author dnbostw
     */
   
    public InstrumentationPanel(Window parentWindow, InstrumentationDescriptor instrument) throws IOException {
        super(Core.getMainWindow(), instrument, "components/InstrumentationPanel.fxml");
    }

    @Override
    public void initialize() {
    }
}

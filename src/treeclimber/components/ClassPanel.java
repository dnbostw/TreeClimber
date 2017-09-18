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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import treeclimber.Core;
import treeclimber.newrelic.ClassDescriptor;

/**
 *
 * @author dnbostw
 */
public class ClassPanel extends QuickDialog<ClassDescriptor> { // BorderPane {
    
   private static final String DEFAULT_INTERFACE_NAME="";
   private static final String DEFAULT_METRIC_NAME_FORMAT_PREFIX="";
   private static final String DEFAULT_METHOD_ANNOTATION="";
   private static final String DEFAULT_NAME_TRANSACTION="";
   private static final String DEFAULT_TRANSACTION_TYPE="background";  // or "web"
   private static final boolean DEFAULT_EXCLUDE_FROM_TRANSACTION=false;
   private static final boolean DEFAULT_TRANSACTION_START_POINT=false;
   private static final boolean DEFAULT_IGNORE_TRANSACTION=false;
   
   @FXML TextField interfaceNameText, methodAnnotationText, metricNameFormatText, nameTransactionText;
   @FXML ComboBox  transactionTypeComboBox;
   @FXML CheckBox  interfaceCheckbox, excludeFromTransactionCheckbox, transactionStartPointCheckbox, ignoreTransactionCheckbox;
   @FXML Label     classNameLabel;
   
   @FXML Button defaultButton, cancelButton, saveButton;
    
   @FXML  public void resetDefaultValues(ActionEvent event) { 
       setDefaultDisplayFields();
       updateInstanceFromView();
   }
   @FXML  public void saveEdits(ActionEvent event) {
       updateInstanceFromView();
       if (isValid()) {
            close();
       }
   }
   @FXML  public void cancelEdits(ActionEvent event) {
       restoreInstance();
       close();
   }
 
   @Override
   public void updateViewFromInstance() {
       classNameLabel.setText(getInstance().getClassName());
       interfaceCheckbox.setSelected(getInstance().getInterfaceName()!=null);
       methodAnnotationText.setText(getInstance().getMethodAnnotation());
       metricNameFormatText.setText(getInstance().getMetricNameFormat());
       nameTransactionText.setText(getInstance().getNameTransaction());
       excludeFromTransactionCheckbox.setSelected(getInstance().isExcludeFromTransactionTrace());
       transactionStartPointCheckbox.setSelected(getInstance().isTransactionStartPoint());
       ignoreTransactionCheckbox.setSelected(getInstance().isIgnoreTransaction());
   }
   
   @Override
   public void updateInstanceFromView() {
       // Class name comes in with the constructor and cannot be changed
//       getInstance().setClassName(classNameText.getText());
       if (interfaceCheckbox.isSelected()) {
           getInstance().setInterfaceName(interfaceNameText.getText());
       } else {
           getInstance().setInterfaceName("");
       }
       getInstance().setMethodAnnotation(methodAnnotationText.getText());
       getInstance().setMetricNameFormat(metricNameFormatText.getText());
       getInstance().setNameTransaction(nameTransactionText.getText());
       getInstance().setExcludeFromTransactionTrace(excludeFromTransactionCheckbox.isSelected());
       getInstance().setTransactionStartPoint(transactionStartPointCheckbox.isSelected());
       getInstance().setIgnoreTransaction(ignoreTransactionCheckbox.isSelected());
   }
   
   @Override
   public void setDefaultDisplayFields() {
       interfaceCheckbox.setSelected(false);
       interfaceNameText.setText(DEFAULT_INTERFACE_NAME);
       methodAnnotationText.setText(DEFAULT_METHOD_ANNOTATION);
       metricNameFormatText.setText(DEFAULT_METRIC_NAME_FORMAT_PREFIX);
       nameTransactionText.setText(DEFAULT_NAME_TRANSACTION);
       excludeFromTransactionCheckbox.setSelected(DEFAULT_EXCLUDE_FROM_TRANSACTION);
       transactionStartPointCheckbox.setSelected(DEFAULT_TRANSACTION_START_POINT);
       ignoreTransactionCheckbox.setSelected(DEFAULT_IGNORE_TRANSACTION);
   }
   
   @Override
   public void setDefaultInstance() {
       getInstance().setInterfaceName(DEFAULT_INTERFACE_NAME);
       getInstance().setMethodAnnotation(DEFAULT_METHOD_ANNOTATION);
       getInstance().setMetricNameFormat(DEFAULT_METRIC_NAME_FORMAT_PREFIX);
       getInstance().setNameTransaction(DEFAULT_NAME_TRANSACTION);
       getInstance().setExcludeFromTransactionTrace(DEFAULT_EXCLUDE_FROM_TRANSACTION);
       getInstance().setTransactionStartPoint(DEFAULT_TRANSACTION_START_POINT);
       getInstance().setIgnoreTransaction(DEFAULT_IGNORE_TRANSACTION);
   }
   
    /**
     *
     * @author dnbostw
     */
   
    public ClassPanel(Window parentWindow, ClassDescriptor instrument) throws IOException {
        super(Core.getMainWindow(), instrument, "components/ClassPanel.fxml");
    }
    
    @Override
    public void initialize() {
        transactionTypeComboBox.setValue("background");
    }

    @Override
    public String validate(ClassDescriptor instance) {
        return null;
    }
}

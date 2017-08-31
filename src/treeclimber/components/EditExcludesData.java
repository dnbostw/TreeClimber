/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treeclimber.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import treeclimber.newrelic.Editable;

/**
 *
 * @author dnbostw
 */
public class EditExcludesData implements Editable<EditExcludesData> {

    ObservableList<String> list = FXCollections.observableArrayList();
    String editText;

    public EditExcludesData() { 
    }
    
    public EditExcludesData(String editText, ObservableList<String> list) {
        this.editText = editText;
        this.list = FXCollections.observableArrayList();
        for (String item : list) {
            this.list.add(item);
        }
    }
    
    @Override
    public EditExcludesData copy(EditExcludesData eed) {
        if (eed == null) {
            eed = new EditExcludesData();
        }
        eed.setEditText(getEditText());
        eed.setList(FXCollections.observableArrayList(getList()));
        return eed;
    }
    
    public void clearList() {
        getList().clear();
    }

    public ObservableList<String> getList() {
        if (list == null) {
            list = FXCollections.observableArrayList();
        }
        return list;
    }

    public void setList(ObservableList<String> list) {
        this.list = list;
    }

    public String getEditText() {
        return editText;
    }

    public void setEditText(String editText) {
        this.editText = editText;
    }
    
}

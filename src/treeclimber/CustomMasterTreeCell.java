/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treeclimber;

import javafx.scene.control.TreeCell;

/**
 *
 * @author djehuty
 */
public class CustomMasterTreeCell extends TreeCell<TreeItemValue> {

    public CustomMasterTreeCell() {
    }

    
    @Override
    protected void updateItem(TreeItemValue item, boolean empty) {

        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(item.toString());
        }
    }
    
    
}

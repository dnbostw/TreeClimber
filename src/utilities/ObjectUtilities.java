/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;

/**
 *
 * @author dnbostw
 */
public class ObjectUtilities<T extends Node> {
    
//        public T getObject(Parent parent, Class lookingFor, String id) {
//            if (parent == null) return null;
//            if (parent.getId().equals(id)) {
//                return (T)parent;
//            }
//            if (parent.getChildrenUnmodifiable() != null) {
//                for (Node node : parent.getChildrenUnmodifiable()) {
//                    if (node.getId().equals(id)) return (T)node;
//                    if (Parent.class.isAssignableFrom(node.getClass())) {
//                        T _obj = getObject((Parent) node, lookingFor, id);
//                        if (_obj != null) return (T)_obj;
//                    }
//                }
//            }
//            return null;
//        }
        
        public T getObject(Parent parent, Class clazz, String name) {
            if (parent == null) return null;
            if (parent.getId().equals(name)) {
                return (T)parent;
            }
            ObservableList<Node> nodes = null;
            if (ScrollPane.class.isAssignableFrom(parent.getClass())) {
                    if ( ((Node)((ScrollPane)parent).getContent()).getId().equals(name)) {
                        return (T) ((ScrollPane)parent).getContent();
                    } else {
                        return null;
                    }
            }
            if ((SplitPane.class.isAssignableFrom(parent.getClass()))) {
                nodes = ((SplitPane)parent).getItems();
            }
            else {
                nodes = parent.getChildrenUnmodifiable();
            }
            for (Node node : nodes) {
                if (ScrollPane.class.isAssignableFrom(node.getClass())) {
                    if ( ((Node)((ScrollPane)node).getContent()).getId().equals(name)) {
                        return (T) ((ScrollPane)node).getContent();
                    }
                }
                if (Parent.class.isAssignableFrom(node.getClass())) {
                    String id = node.getId();
                    if (id != null && id.equals(name)) return (T) node;
                    T _obj = getObject((Parent) node, clazz, name);
                    if (_obj != null) return (T) _obj;
                } else {
                    T control = (T) node;
                    if (control.getId() != null && control.getId().equals(name)) {
                        return (T) control;
                    }
                }
            }
            return null;
        }

}

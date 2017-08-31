/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treeclimber;

/**
 *
 * @author djehuty
 */
public class TreeItemValue {
    public static int TREE_ITEM_PACKAGE=0x01;
    public static int TREE_ITEM_CLASS=0x02;
    public static int TREE_ITEM_METHOD= 0x04;
    
    private String fullName;
    private String name;
    private int type;

    public TreeItemValue(String fullName, String name, int type) {
        this.fullName = fullName;
        this.name = name;
        this.type = type;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
    public boolean isPackage() {
        return type == TREE_ITEM_PACKAGE;
    }    
    
    public boolean isClass() {
        return type == TREE_ITEM_CLASS;
    }
    
    public boolean isMethod() {
        return type == TREE_ITEM_METHOD;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    
}

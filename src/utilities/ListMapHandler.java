/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dnbostw
 */
public class ListMapHandler extends HashMap {
    private Map<String,List<String>> map = new HashMap<String,List<String>>();

    public ListMapHandler() {
    }
    
    public Map<String,List<String>> getMap() {
        return map;
    }
    
    public boolean contains(String keyName, String listKey) {
        if (map.containsKey(keyName)) {
            List<String> list = map.get(keyName);
            return list.contains(listKey);
        }
        return false;
    }
    
    public void add(String keyName, String listKey) {
        if (map.containsKey(keyName)) {
            List<String> list = map.get(keyName);
            list.add(listKey);
        } else {
            List<String> list = new LinkedList<String>();
            list.add(listKey);
            map.put(keyName, list);
        }
    }
    
    public void remove(String keyName, String listKey) {
        if (map.containsKey(keyName)) {
            List<String> list = map.get(keyName);
            list.remove(listKey);
        }
    }
    
    public List<String> getAll(String keyName) {
        if (map.containsKey(keyName)) {
            List<String> list = map.get(keyName);
            return list;
        }
        return null;
    }
    
    public void put(String keyName) {
        map.put(keyName,new LinkedList<String>());
    }
    
    public void put(String keyName, String listKey) {
        add(keyName, listKey);
    }
    
    public List<String> get(String keyName) {
        return map.get(keyName);
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder("ListMapHandler {\n");
        for (Map.Entry<String,List<String>> entry : map.entrySet()) {
            sb.append("\t"+entry.getKey()+" {\n");
            for (String listKey : entry.getValue()) {
                sb.append("\t\t"+listKey+"\n");
            }
            sb.append("\t}\n");
        }
        sb.append("}\n");
        return sb.toString();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author dnbostw
 */
public class ListUtilities {

    public final static String listToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        if (list != null) {
            for (String item : list) {
                sb.append(item);
                if (i < (list.size() - 1)) {
                    sb.append(",");
                }
                ++i;
            }
        } else {
            sb.append("");
        }
        return sb.toString();
    }

    public final static List<String> stringToList(String input) {
        if (input != null) {
            String[] array = input.split(",");
            for (int i = 0; i < array.length; i++) {
                array[i] = array[i].trim();
            }
            return Arrays.asList(array);
        } else {
            return new LinkedList<String>();
        }
    }

    public final static Set<String> stringToSet(String input) {
        if (input == null) {
            String[] array = input.split(",");
            for (int i = 0; i < array.length; i++) {
                array[i] = array[i].trim();
            }
            return new TreeSet(Arrays.asList(array));
        } else {
            return new TreeSet<String>();
        }
    }

}

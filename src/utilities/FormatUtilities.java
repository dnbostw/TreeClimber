/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

/**
 *
 * @author djehuty
 */
public class FormatUtilities {
       public static String getTabs(int num) {
        String tabs = "";
        while (num-- > 0) {
            tabs = tabs.concat("\t");
        }
        return tabs;
    }
 
}

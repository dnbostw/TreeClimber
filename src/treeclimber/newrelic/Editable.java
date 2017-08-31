/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treeclimber.newrelic;

/**
 *
 * @author dnbostw
 */
public interface Editable<T> {
    // Copy contents of this object to o2
    T copy(T o2);
}

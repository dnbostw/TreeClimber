/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treeclimber.parsers;

/**
 *
 * @author dnbostw
 */
public abstract class FieldParser {

    public FieldParser() {
    }

    public abstract String parseRawField(String line, int field);
    public abstract String parseField(String line, int fieldNum);
    public abstract String parseMethodName(String field, int fieldNum);
    public abstract String parseFullMethodName(String field, int fieldNum);
    public abstract String parseClass(String line);
    public abstract String parsePackage(String line);
    
}

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
public class RapidFieldParser extends FieldParser {

    public RapidFieldParser() {
        super();
    }

    @Override
    public String parseRawField(String line, int field) {
        int inx = line.indexOf(" ");
        if (inx >= 0) {
            if (field == 1) {
                return (inx >= 0 ? line.substring(0, line.indexOf(" ")) : line.substring(line.indexOf(" ") + 1));
            } else {
                return (inx >= 0 ? line.substring(inx + 1) : null);
            }
        }
        return null;
    }

    @Override
    public String parseField(String line, int fieldNum) {
        String result = parseRawField(line, fieldNum);
        if (result == null) {
            return null;
        }
        if (fieldNum == 1) {
            if (result.startsWith("M:") || result.startsWith("S:") || result.startsWith("O:")) {
                return result.substring(2);
            }
        }
        if (fieldNum == 2) {
            if (!line.contains(" ")) {
                return null;
            }
            if (!result.startsWith("(I)")) { // (result.startsWith("(M)") || result.startsWith("(S)") || result.startsWith("(O)"))) {
                return result.substring(3);
            }
        }
        return result;

    }

    @Override
    public String parseMethodName(String field, int fieldNum) {
        String method = parseFullMethodName(field, fieldNum);
        if (method != null) {
            return method.substring(method.lastIndexOf(":")+1);
        }
        return null;
    }

    @Override
    public String parseFullMethodName(String field, int fieldNum) {
        if (field.contains(" ")) {
            return parseField(field, fieldNum);
        }

        if (fieldNum == 1) {
            if (field.startsWith("M:")) {
                return field.substring(2);
            } else {
                return field;
            }
        }
        if (fieldNum == 2) {
            if (field.startsWith("(")) {
                return field.substring(3);
            } else {
                return field;
            }
        }
        return null;
    }

    @Override
    public String parseClass(String line) {
        String field = parseFullMethodName(parseRawField(line, 1), 1);
        String tmp0 = field.substring(0,field.indexOf(":"));
        return tmp0;
    }
    
    // Assumes package in field 1
    @Override
    public String parsePackage(String line) {
        String field = parseFullMethodName(parseRawField(line, 1), 1);
        if (field != null) {
            String _package = field.substring(0,field.indexOf("("));
            _package = _package.substring(0,_package.lastIndexOf("."));
            return _package;
        }
        return null;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treeclimber;

import java.io.IOException;
import treeclimber.parsers.FieldParser;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author dnbostw
 */
public class LineParser {

    public static int PARSE_METHOD = 0x01;
    public static int PARSE_PACKAGE = 0x02;
    public static int PARSE_CLASSES = 0x04;
    public static int PARSE_FIELD = 0x08;
    public static int PARSE_FULL_METHOD = 0x10;
    public static int PARSE_STARTS_WITH = 0x20;

    private Map<String, Context> contexts = new HashMap<String, Context>();
    private FieldParser fieldParser = null; // new FieldParser("default");
    private Context context;

    public LineParser() {
    }

    public List<String> parseLinesStartingWith(List<String> input, String matchString) {
        List<String> result = new LinkedList<String>();
        boolean start = false;
        for (String string : input) {
            System.out.println(string);
            if (string.startsWith(matchString)) {
                start = true;
                result.add(string);
            } else if (start) {
                break;
            }
        }
        return result;
    }

    public List<String> parseLines(List<String> input, String rootMethod, int type) {
        List<String> result = new LinkedList<String>();
        result = new LinkedList<String>();
        boolean start = false;

//        System.out.println("Parsing " + rootMethod);
        for (String line : input) {
            String mName = null;
            if (type == PARSE_METHOD) {
                mName = getFieldParser().parseMethodName(line, 1);
            }
            if (type == PARSE_FULL_METHOD) {
                mName = getFieldParser().parseFullMethodName(line, 1);
            }
            if (type == PARSE_PACKAGE) {
                mName = getFieldParser().parsePackage(line);
            }
            if (type == PARSE_CLASSES) {
                mName = getFieldParser().parseClass(line);
            }
            if (type == PARSE_FIELD) {
                mName = getFieldParser().parseField(line, 1);
            }
            if (type == PARSE_STARTS_WITH) {
                if (line.startsWith(rootMethod)) {
                    start = true;
                    result.add(line);
                } else {
                    if (start) {
                        break;
                    }
                }
            } else {
                if (mName.equals(rootMethod)) {
                    start = true;
                    result.add(line);
                } else {
                    if (start) {
                        break;
                    }
                }
            }
        }
        return result;
    }

    public Set<String> getPackages(List<String> reader) {
        Set<String> result = new TreeSet<String>();
        if (fieldParser == null) {
            return result;
        }
        for (String line : reader) {
            String pkg = fieldParser.parsePackage(line);
            result.add(pkg);
        }
        return result;
    }

    public Set<String> getClasses(List<String> reader) {
        Set<String> result = new TreeSet<String>();
        if (fieldParser == null) {
            return result;
        }
        for (String line : reader) {
            String pkg = fieldParser.parseClass(line);
            result.add(pkg);
        }
        return result;
    }

    public String parseField(String line, int fieldNum) {
        return "";
    }

    public FieldParser getFieldParser() {
        return fieldParser;
    }

    public void setContext(String context) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        Context ctx = null;
        if (contexts.containsKey(context)) {
            ctx = contexts.get(context);
        } else {
            ctx = new Context(context);
            contexts.put(context, ctx);
        }
        fieldParser = ctx.getFieldParser();
        this.context = ctx;
    }

    public Context getContext() {
        return context;
    }

}

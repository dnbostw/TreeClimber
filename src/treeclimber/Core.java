/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treeclimber;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import utilities.FileUtilities;

/**
 *
 * @author dnbostw
 */
public class Core {

    public final static String TMP_DIRECTORY="/tmp";
    public final static String BASE_FILE_DIRECTORY="./files/";
    public final static String PARSERS_DIRECTORY=BASE_FILE_DIRECTORY.concat("parsers/");
    public final static String CONTEXT_DIRECTORY=BASE_FILE_DIRECTORY.concat("context/");
    public final static String CONTEXT_PROPERTIES_FILE="%sContext.properties";
    public final static String CONTEXT_INPUT_TREE_FILE="%sInputTree.tree";
    public final static String CONTEXT_OUTPUT_TREE_FILE="%sOutputTree.tree";
    public final static String LOG_DIRECTORY=BASE_FILE_DIRECTORY.concat("log/");
    public final static String LOG_FILE_NAME="TreeClimber.log";
    public final static String LOG_SELECTED_ITEMS="Selected.log";
    public final static String NEW_RELIC_DIRECTORY=BASE_FILE_DIRECTORY.concat("newrelic/");
    public final static String NEW_RELIC_DEFAULT_FILE_NAME="%sInstrumentation.xml";
    public final static String DEFAULT_PARSER_NAME="treeclimber.parsers.DefaultFieldParser";
    public final static String VALIDATION_ERROR_MESSSAGE="%s#%s#%s"; // error title # error header # error message
    public final static int    VALIDATION_ERROR_TITLE=0;
    public final static int    VALIDATION_ERROR_HEADER=1;
    public final static int    VALIDATION_ERROR_MESSAGE=2;
    
    
    private static LineParser lineParser = new LineParser();

    private static Set<String> packages;

    private static Map<String, Set<String>> classes;

    private static Map<String, Set<String>> methods;

    public static Stack<String> stack;

    public static FileWriter outputFile;

    public static Stage mainStage;
    
    private static int maxNestingLevel=10;

    public static int getMaxNestingLevel() {
        return maxNestingLevel;
    }

    public static void setMaxNestingLevel(int maxNestingLevel) {
        Core.maxNestingLevel = maxNestingLevel;
    }
    

    private static int nestingLevel = 8;

    public static int getNestingLevel() {
        return nestingLevel;
    }

    public static void setNestingLevel(int nestingLevel) {
        Core.nestingLevel = nestingLevel;
    }

    public static void pushIt(String item) {
        getStack().push(item);
    }

    public static String popIt() {
        String result = stack.pop();
        return result;
    }

    public static boolean inStack(String item) {
        return getStack().contains(item);
    }

    public static LineParser getLineParser() {
        return lineParser;
    }

    public static Context getContext() {
        return lineParser.getContext();
    }

    public static String changeContext(String context) throws IOException {
        try {
            getPackages().clear();
            getClasses().clear();
            getMethods().clear();

            lineParser.setContext(context);
            if (lineParser.getContext().getRawInput().size() == 0) {
                processRawTree();
            }

            processPackages(lineParser.getContext().getRawInput());
            processClasses(packages);
            processMethods(classes);

        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
            System.out.println(ex.getMessage());
            return "Error loading FieldParser for context ".concat(context);
        }
        return "Context set to ".concat(context);
    }

    public static List<String> climbTree(String startingMethod) throws IOException {
        return climbTree(startingMethod, 1);
    }

    public static List<String> climbTree(String startingMethod, int tabLevel) throws IOException {
        List<String> result = new LinkedList<String>();
        List<String> methodList = null;
        List<String> _methodList = lineParser.parseLines(getContext().getRawInput(), startingMethod, LineParser.PARSE_FULL_METHOD);

        if (tabLevel > maxNestingLevel) {
            maxNestingLevel=tabLevel;
        }
        
        methodList = (_methodList == null ? new LinkedList<String>() : _methodList);

        boolean firstLine = true;
        int tab = tabLevel;
        for (String line : methodList) {
            String field1 = lineParser.getFieldParser().parseField(line, 1);
            if (field1 == null || inStack(field1)) {
                return result;
            }
            pushIt(field1);
            String sfield1 = null;
            if (firstLine) {
                sfield1 = tab + ">" + field1;
//                FileUtilities.log(sfield1 + "\n");
                result.add(sfield1);
                firstLine = false;
                if (!lineParser.getContext().isInPackageWhitelist(field1)
                        || lineParser.getContext().isInPackageBlacklist(field1)
                        || lineParser.getContext().isInMethodExcludeList(field1)) {
                    popIt();
                    continue;
                }
            }
            String field2 = lineParser.getFieldParser().parseField(line, 2);
            if (field2 == null || field1.equals(field2) || lineParser.getContext().isInPackageBlacklist(field2)
                    || lineParser.getContext().isInMethodExcludeList(field2)) {
                popIt();
                continue;
            }
            String sfield2 = (tab + 1) + ">" + field2;
            if (lineParser.getContext().isInPackageWhitelist(field2) 
                    && !lineParser.getContext().isInPackageBlacklist(field2) 
                    && !lineParser.getContext().isInMethodExcludeList(field2) 
                    && !startingMethod.equals(field2)) {
                if (getNestingLevel() >= (tab+1)) {
                    result.addAll(climbTree(field2, tab + 1));
                }
            } else {
//                FileUtilities.log(sfield2 + "\n");
                result.add(sfield2);
            }
            popIt();
        }
        return result;
    }

//    public static List<String> climbTree(String startingMethod, int tabLevel) throws IOException {
//        List<String> result = new LinkedList<String>();
//        List<String> methodList = null;
//        List<String> _methodList = lineParser.parseLines(getContext().getRawInput(), startingMethod, LineParser.PARSE_FULL_METHOD);
//
//        methodList = (_methodList == null ? new LinkedList<String>() : _methodList);
//        
//        boolean firstLine = true;
//        int tab = tabLevel;
//        for (String line : methodList) {
//            String field1 = lineParser.getFieldParser().parseField(line, 1);
//            if (field1 == null || inStack(field1)) {
//                return result;
//            }
//            pushIt(field1);
//            String sfield1 = null;
//            if (firstLine) {
//                sfield1 = tab+">"+field1; // FormatUtilities.getTabs(tab) + field1;
////				System.out.println(sfield1);
//                FileUtilities.log(sfield1+"\n");
//                result.add(sfield1);
//                tab += 1;
//                firstLine = false;
//            }
//            String field2 = lineParser.getFieldParser().parseField(line, 2);
//            if (field2 == null || field1.equals(field2)) {
//                popIt();
//                continue;
//            }
//            String sfield2 = tab+">"+field2; // getTabs(tab) + field2;
//            if (lineParser.getContext().isInPackageWhitelist(field2) && !startingMethod.equals(field2)) {
////				System.out.println(sfield2);
//                FileUtilities.log(sfield2+"\n");
//                result.addAll(climbTree(field2, tab));
//            }
//            popIt();
//        }
//        return result;
//    }
    public static Stack<String> getStack() {
        if (stack == null) {
            stack = new Stack<String>();
        }
        return stack;
    }

    public static void processRawTree() throws IOException {
        BufferedReader br = null;
        List<String> rawInput = null;
        try {
            br = FileUtilities.openReadOnlyFile(lineParser.getContext().getInputTreePath());
            rawInput = lineParser.getContext().getRawInput();
            rawInput.clear();
            String line = null;
            while ((line = br.readLine()) != null) {
                String field1 = lineParser.getFieldParser().parseRawField(line, 1);
                String field2 = lineParser.getFieldParser().parseRawField(line, 2);
                if (field1 != null || field2 != null) {
                    if ((field1.startsWith("M:") || field1.startsWith("S:") || field1.startsWith("O:"))
                            && (field2.startsWith("(M)") || field2.startsWith("(S)") || field2.startsWith("(O)"))) {
                        rawInput.add(line);
                    }
                }
            }
        } finally {
            FileUtilities.closeReadOnlyFile(br);
        }
    }

    private static void processPackages(List<String> rawInput) {
        for (String line : rawInput) {
            String pkg = lineParser.getFieldParser().parsePackage(line);
            getPackages().add(pkg);
        }
//        for (String _pkg : packages) {
//            System.out.println(_pkg);
//        }
    }

    private static void processClasses(Set<String> packages) {
        for (String pkg : packages) {
            List<String> allClasses = lineParser.parseLines(lineParser.getContext().getRawInput(), pkg, LineParser.PARSE_PACKAGE);
            Set<String> _classes = new TreeSet<String>();
            for (String line : allClasses) {
                String clazz = lineParser.getFieldParser().parseClass(line);
                _classes.add(clazz);
            }
            getClasses().put(pkg, _classes);
        }
//        for (Map.Entry<String, Set<String>> entry : getClasses().entrySet()) {
//            System.out.println(entry.getKey());
//            for (String cls : entry.getValue()) {
//                System.out.println("\t".concat(cls));
//            }
//        }
    }

    private static void processMethods(Map<String, Set<String>> classes) {
        for (Map.Entry<String, Set<String>> entry : getClasses().entrySet()) {
            Set<String> _classes = classes.get(entry.getKey());
            for (String cls : _classes) {
                List<String> allMethods = lineParser.parseLines(lineParser.getContext().getRawInput(), cls, LineParser.PARSE_CLASSES);
                Set<String> _methods = new TreeSet<String>();
                for (String line : allMethods) {
                    String method = lineParser.getFieldParser().parseMethodName(line, 1);
                    _methods.add(method);
                }
                getMethods().put(cls, _methods);
            }
        }
//        for (Map.Entry<String, Set<String>> entry : getMethods().entrySet()) {
//            System.out.println(entry.getKey());
//            for (String cls : entry.getValue()) {
//                System.out.println("\t".concat(cls));
//            }
//        }
    }

    public static Set<String> getPackages() {
        if (packages == null) {
            packages = new TreeSet<String>();
        }
        return packages;
    }

    public static Map<String, Set<String>> getClasses() {
        if (classes == null) {
            classes = new HashMap<String, Set<String>>();
        }
        return classes;
    }

    public static Map<String, Set<String>> getMethods() {
        if (methods == null) {
            methods = new HashMap<String, Set<String>>();
        }
        return methods;
    }

    private static Scene mainScene;
    
    public static void setMainScene(Scene scene) {
        mainScene = scene;
    }
    
    public static Scene getMainScene() {
        return mainScene;
    }
    
    public static Window getMainWindow() {
        return getMainScene().getWindow();
    }
    
    public static Stage getMainStage() {
        return mainStage;
    }

    public static void setMainStage(Stage mainStage) {
        Core.mainStage = mainStage;
    }

}

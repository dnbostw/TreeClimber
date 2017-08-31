/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treeclimber;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import treeclimber.parsers.FieldParser;
import utilities.FileUtilities;

/**
 *
 * @author djehuty
 */
public class Context {

    private String context;
    private FieldParser fieldParser;
    private Set<String>  methodExcludeList;
    private Set<String>  packageBlackList;
    private Set<String>  packageWhitelist;
    private List<String> rawInput;
  
    Context(String context) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
       setContext(context);
    }

    private String inputContextFileName;

    public String getInputContextFileName() {
        return inputContextFileName;
    }

    public void setInputContextFileName(String inputContextFileName) {
        this.inputContextFileName = inputContextFileName;
    }
    
    public String getContext() {
        return context;
    }
    private String inputMethodTreeFileName;

    public String getInputMethodTreeFileName() {
        return inputMethodTreeFileName;
    }

    public void setInputMethodTreeFileName(String inputMethodTreeFileName) {
        this.inputMethodTreeFileName = inputMethodTreeFileName;
    }

    public String getOutputMethodTreeFileName() {
        return outputMethodTreeFileName;
    }

    public void setOutputMethodTreeFileName(String outputMethodTreeFileName) {
        this.outputMethodTreeFileName = outputMethodTreeFileName;
    }

    public String getFieldParserName() {
        return fieldParserName;
    }

    public void setFieldParserName(String fieldParserName) {
        this.fieldParserName = fieldParserName;
    }
    
    private String outputMethodTreeFileName;
    private String fieldParserName;

    public void setContext(String context) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        loadContext(context);
    }

    private void loadContext(String context) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        this.context=context;
        initializeContext(context);
//        createInputMethodTreeFileName();
//        createOutputMethodTreeFileName();
//        createFieldParserName();
    }

    private Properties config = new Properties();

    public Properties getConfig() {
        return config;
    }
    
    private void initializeContext(String context) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        //  Example:  CommonsInputTree.txt
        inputContextFileName = "./files/context/".concat(context.substring(0, 1).toUpperCase().concat(context.substring(1).toLowerCase()).concat("Context.properties"));
        Properties config = new Properties();
        Reader in = (Reader)FileUtilities.openReadOnlyFile(inputContextFileName);
        config.load(in);
        this.fieldParserName=config.getProperty("fieldParserName");
        this.inputMethodTreeFileName=config.getProperty("inputMethodTreeFileName");
        this.outputMethodTreeFileName=config.getProperty("outputMethodTreeFileName");
        loadFieldParser();
        String[] _methodExcludeList = config.getProperty("methodExcludeList").split(",");
        clearMethodExcludeList();
        for (String _item : _methodExcludeList) {
            addToMethodExcludeList(_item);
        }
        String[] _blackList = config.getProperty("blackList").split(",");
        clearPackageBlacklist();
        for (String _item : _blackList) {
            addToPackageBlacklist(_item);
        }
        String[] _whiteList = config.getProperty("whiteList").split(",");
        clearPackageWhitelist();
        for (String _item : _whiteList) {
            addToPackageWhitelist(_item);
        }
    }
    
    public void saveContext() throws IOException {
        FileWriter fw = FileUtilities.openWriteOnlyFile(inputContextFileName);
        fw.write("contextName=".concat(context).concat("\n"));
        fw.write("inputMethodTreeFileName=".concat(inputMethodTreeFileName).concat("\n"));
        fw.write("outputMethodTreeFileName=".concat(outputMethodTreeFileName).concat("\n"));
        fw.write("fieldParserName=".concat(fieldParserName).concat("\n"));
        int i = 0;
        fw.write("methodExcludeList=");
        for (String _item : getMethodExcludelist()) {
            fw.write(_item);
            if (i < (getMethodExcludelist().size()-1)) {
                fw.write(",");
            }
            ++i;
        }
        fw.write("\n");
        fw.write("blackList=");
        for (String _item : getPackageBlacklist()) {
            fw.write(_item);
            if (i < (getPackageBlacklist().size()-1)) {
                fw.write(",");
            }
            ++i;
        }
        fw.write("\n");
        i = 0;
        fw.write("whiteList=");
        for (String _item : getPackageWhitelist()) {
            fw.write(_item);
            if (i < (getPackageWhitelist().size()-1)) {
                fw.write(",");
            }
            ++i;
        }
        fw.write("\n");
        FileUtilities.closeWriteOnlyFile(fw);
        
    }

    private void createInputMethodTreeFileName() {
        //  Example:  CommonsInputTree.txt
        inputMethodTreeFileName = "./files/".concat(context.substring(0, 1).toUpperCase().concat(context.substring(1).toLowerCase()).concat("InputTree.txt"));
    }

    private void createOutputMethodTreeFileName() {
        // Example: CommonsMethodTree.txt
        outputMethodTreeFileName = "/tmp/".concat(context.substring(0, 1).toUpperCase().concat(context.substring(1).toLowerCase()).concat("MethodTree.txt"));
    }

    private void createFieldParserName() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        // Example parsers.CommonsFieldParser
        fieldParserName = "treeclimber.parsers.".concat(context.substring(0, 1).toUpperCase()).concat(context.substring(1)).concat("FieldParser");
        loadFieldParser();
    }

    public FieldParser getFieldParser() {
        return fieldParser;
    }

    public void loadFieldParser() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
            Class _f = Class.forName(fieldParserName);
            FieldParser _fp = (FieldParser) _f.newInstance();
            fieldParser = _fp;
    }

    public void setMethodExcludeList(Set<String> methodExcludeList) {
        if (this.methodExcludeList != null) {
            this.methodExcludeList.clear();
            this.methodExcludeList=null;
        }
        this.methodExcludeList = methodExcludeList;
    }

    public void setPackageBlackList(Set<String> packageBlackList) {
        if (this.packageBlackList != null) {
            this.packageBlackList.clear();
            this.packageBlackList=null;
        }
        this.packageBlackList = packageBlackList;
    }

    public void setPackageWhitelist(Set<String> packageWhitelist) {
        if (this.packageWhitelist != null) {
            this.packageWhitelist.clear();
            this.packageWhitelist=null;
        }
        this.packageWhitelist = packageWhitelist;
    }

    public Set<String> getPackageWhitelist() {
        if (packageWhitelist == null) {
            packageWhitelist = new TreeSet<String>();
        }
        return packageWhitelist;
    }
    
    public void addToPackageWhitelist(String pkg) {
       Set<String> wl = getPackageWhitelist();
       wl.add(pkg);
    }
    
    public void removeFromPackageWhitelist(String pkg) {
       Set<String> wl = getPackageWhitelist();
       wl.remove(pkg);
    }

    public void clearPackageWhitelist() {
       Set<String> wl = getPackageWhitelist();
       wl.clear();
    }
    
    public boolean isInPackageWhitelist(String pkg) {
       for (String value : getPackageWhitelist()) {
           if (pkg.startsWith(value)) return true;
       }
       return false;
    }

    public Set<String> getMethodExcludelist() {
        if (methodExcludeList == null) {
            methodExcludeList = new TreeSet<String>();
        }
        return methodExcludeList;
    }
    
    public Set<String> getPackageBlacklist() {
        if (packageBlackList == null) {
            packageBlackList = new TreeSet<String>();
        }
        return packageBlackList;
    }
    
    public void addToPackageBlacklist(String pkg) {
       Set<String> wl = getPackageBlacklist();
       wl.add(pkg);
    }
    
    public void removeFromPackageBlacklist(String pkg) {
       Set<String> wl = getPackageBlacklist();
       wl.remove(pkg);
    }

    public void clearPackageBlacklist() {
       Set<String> wl = getPackageBlacklist();
       wl.clear();
    }
    
    public boolean isInPackageBlacklist(String pkg) {
       for (String value : getPackageBlacklist()) {
           if (pkg.startsWith(value)) return true;
       }
       return false;
    }

    public Set<String> getMethodExcludeList() {
        if (methodExcludeList == null) {
            methodExcludeList = new TreeSet<String>();
        }
        return methodExcludeList;
    }
    
    public void addToMethodExcludeList(String pkg) {
       Set<String> wl = getMethodExcludeList();
       wl.add(pkg);
    }
    
    public void removeFromMethodExcludeList(String pkg) {
       Set<String> wl = getMethodExcludeList();
       wl.remove(pkg);
    }

    public void clearMethodExcludeList() {
       Set<String> wl = getMethodExcludeList();
       wl.clear();
    }
    
    public boolean isInMethodExcludeList(String pkg) {
       for (String value : getMethodExcludeList()) {
           if (pkg.contains(value)) return true;
       }
       return false;
    }

    public List<String> getRawInput() {
        if (rawInput == null) {
            rawInput = new LinkedList<String>();
        }
        return rawInput;
    }

    public void setRawInput(List<String> rawInput) {
        this.rawInput = rawInput;
    }
       
}

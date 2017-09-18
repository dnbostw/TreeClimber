/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treeclimber;

import com.newrelic.agent.deps.com.google.common.util.concurrent.SettableFuture;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import treeclimber.components.ContextData;
import treeclimber.parsers.FieldParser;
import utilities.FileUtilities;

/**
 *
 * @author djehuty
 */
public class Context {

    private ContextData data;
    
    private FieldParser fieldParser;
//    private Set<String>  methodExcludeList;
//    private Set<String>  packageBlackList;
//    private Set<String>  packageWhitelist;
    private List<String> rawInput;
  
    Context() {
        data = new ContextData();
    }
    
    Context(String context) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        this();
        setContext(context);
    }

    private String inputContextFileName;

    public String getInputContextFileName() {
        return inputContextFileName;
    }

    public void setInputContextFileName(String inputContextFileName) {
        this.inputContextFileName = inputContextFileName;
    }
    
    public void setContextName(String name) {
        data.setContextName(name);
    }
    
    public String getContextName() {
        return data.getContextName(); // name;
    }
    
//    private String inputTreePath;

    public String getInputTreePath() {
        return data.getInputTreepath(); // inputTreePath;
    }

    public void setInputTreePath(String inputTreePath) {
        data.setInputTreepath(inputTreePath); // this.inputTreePath = inputTreePath;
    }

    public String getOutputTreePath() {
        return data.getOutputTreepath();
//        return outputTreePath;
    }

    public void setOutputTreePath(String outputTreePath) {
        data.setOutputTreepath(outputTreePath);
//        this.outputTreePath = outputTreePath;
    }

    public String getFieldParserName() {
        return data.getFieldParserName();
//        return fieldParserName;
    }

    public void setFieldParserName(String fieldParserName) {
        data.setFieldParserName(fieldParserName);
//        this.fieldParserName = fieldParserName;
    }
    
//    private String outputTreePath;
//    private String fieldParserName;

    public void setContext(String context) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        loadContext(context);
    }

    private void loadContext(String context) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        initializeContext(context);
    }

    private Properties config = new Properties();

    public Properties getConfig() {
        return config;
    }
    
    private void initializeContext(String context) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        //  Example:  CommonsInputTree.txt
        ContextData _data = ContextData.parse(context);
        
        this.inputContextFileName=_data.getContextPropertiesFileName();
        setInputContextFileName(_data.getContextPropertiesFileName());
        setFieldParserName(_data.getFieldParserName());
        setInputTreePath(_data.getInputTreepath());
        setOutputTreePath(_data.getOutputTreepath());
        setMethodExcludeList(_data.getMethodExcludelist());
        setPackageBlacklist(_data.getBlacklist());
        setPackageWhitelist(_data.getWhitelist());
        loadFieldParser();
        
    }
    
    public void saveContext() throws IOException {
        ContextData.save(data);
    }

    public FieldParser getFieldParser() {
        return fieldParser;
    }

    public final static FieldParser loadFieldParser(String fieldParserName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
            Class _f = Class.forName(fieldParserName);
            FieldParser result = (FieldParser) _f.newInstance();
            return result;
    }
    
    public void loadFieldParser() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
            FieldParser _fp = loadFieldParser(getFieldParserName());
            fieldParser = _fp;
    }

    public void setMethodExcludeList(Set<String> methodExcludeList) {
        getMethodExcludeList().clear();
        data.setMethodExcludelist(methodExcludeList);
    }
    
    public void setMethodExcludeList(List<String> list) {
        setMethodExcludeList(new TreeSet<String>(list));
    }

    public void setPackageBlackList(Set<String> packageBlackList) {
        data.setBlacklist(packageBlackList);
    }

    public void setPackageWhitelist(List<String> list) {
        data.setWhitelist(new TreeSet<String>(list));
    }

    public void setPackageBlacklist(List<String> list) {
        setPackageBlackList(new TreeSet<String>(list));
    }

    public void setPackageWhitelist(Set<String> packageWhitelist) {
        getPackageWhitelist().clear();
        setPackageWhitelist(data.getWhitelist());
    }

    public Set<String> getPackageWhitelist() {
        return new TreeSet<String>(data.getWhitelist());
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
        return new TreeSet<String>(data.getMethodExcludelist());
    }
    
    public Set<String> getPackageBlacklist() {
        return new TreeSet<String>(data.getBlacklist());
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
        return new TreeSet<String>(data.getMethodExcludelist());
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
       
    public ContextData getData() {
        return this.data;
    }
}

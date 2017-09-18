/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treeclimber.components;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import treeclimber.Core;
import treeclimber.newrelic.Editable;
import utilities.FileUtilities;
import utilities.ListUtilities;

/**
 *
 * @author dnbostw
 */
public class ContextData implements Editable<ContextData> {

    private final static String[] defaultExcludeMethods = {"getInstance(","<init>("};
    private final static String[] defaultBlacklist = {"com.google.","java.","javax.","org.jboss","commons."};
    private final static String[] defaultWhitelist = {};
    
    private boolean editing;
    private String contextName;
    private String sourceFilepath;
    private String inputTreepath;
    private String outputTreepath;
    private String fieldParsername;
    private ObservableList<String> methodExcludeList;
    private ObservableList<String> blackList;
    private ObservableList<String> whiteList;

    private String contextPropertiesFileName;
    
    public ContextData() {
        restoreDefaults();
    }

    public ContextData(String context) {
        restoreDefaults(context);
    }
    
    public ContextData(ContextData source) {
        source.copy(this);
    }

    @Override
    public ContextData copy(ContextData eed) {
        if (eed == null) {
            eed = new ContextData();
        }
        eed.setContextName(getContextName());
        eed.setSourceFilepath(getSourceFilepath());
        eed.setInputTreepath(getInputTreepath());
        eed.setOutputTreepath(getOutputTreepath());
        eed.setFieldParserName(getFieldParserName());
        eed.setMethodExcludelist(FXCollections.observableArrayList(getMethodExcludelist()));
        eed.setBlacklist(FXCollections.observableArrayList(getBlacklist()));
        eed.setWhitelist(FXCollections.observableArrayList(getWhitelist()));

        return eed;
    }
    
    public void restoreDefaults() {
        restoreDefaults("default");
    }
    
    public void restoreDefaults(String context) {
        setContextName(context);
        contextPropertiesFileName = Core.CONTEXT_DIRECTORY.concat(String.format(Core.CONTEXT_PROPERTIES_FILE,context));
        inputTreepath = Core.CONTEXT_DIRECTORY.concat(String.format(Core.CONTEXT_INPUT_TREE_FILE, context));
        outputTreepath = Core.CONTEXT_DIRECTORY.concat(String.format(Core.CONTEXT_OUTPUT_TREE_FILE, context));
        fieldParsername = "treeclimber.parsers.DefaultFieldParser";
    
        for (String m : defaultExcludeMethods) {
            getMethodExcludelist().add(m);
        }
        for (String b : defaultBlacklist) {
            getBlacklist().add(b);
        }
        for (String w : defaultWhitelist) {
            getWhitelist().add(w);
        }
    }
    
    public final static void save(ContextData data) throws IOException {
        if (data.getContextPropertiesFileName() == null) {
            data.setContextPropertiesFileName(ContextData.getContextPropertiesFilePath(data.getContextName()));
        }
        FileWriter fw = FileUtilities.openWriteOnlyFile(data.getContextPropertiesFileName());
        String output = data.toString();
        fw.write(output+"\n");
        fw.flush();
        FileUtilities.closeWriteOnlyFile(fw);
    }
    
    public final static String getContextPropertiesFilePath(String contextName) {
        return Core.CONTEXT_DIRECTORY.concat(String.format(Core.CONTEXT_PROPERTIES_FILE, contextName));
    }

    public final static ContextData parse(String contextName) throws FileNotFoundException, IOException {
        String contextPropertiesFile = getContextPropertiesFilePath(contextName);
        Properties config = new Properties();
        Reader in = (Reader)FileUtilities.openReadOnlyFile(contextPropertiesFile);
        config.load(in);
        return parse(contextName,config);
    }

    private final static ContextData parse(String contextName, Properties config) {
        ContextData result = new ContextData(contextName);
        result.setContextName(config.getProperty("contextName"));
        result.setInputTreepath(config.getProperty("inputTreePath"));
        result.setOutputTreepath(config.getProperty("outputTreePath"));
        result.setFieldParserName(config.getProperty("fieldParserName"));
        result.setMethodExcludelist(FXCollections.observableArrayList(ListUtilities.stringToList(config.getProperty("methodExcludeList"))));
        result.setBlacklist(FXCollections.observableArrayList(ListUtilities.stringToList(config.getProperty("blackList"))));
        result.setWhitelist(FXCollections.observableArrayList(ListUtilities.stringToList(config.getProperty("whiteList"))));
        return result;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("contextName=%s\n", contextName));
        sb.append(String.format("inputTreePath=%s%sInputTree.tree\n", Core.CONTEXT_DIRECTORY, contextName));
        sb.append(String.format("ouputTreePath=%s%sOutputTree.tree\n", Core.LOG_DIRECTORY, contextName));
        sb.append(String.format("fieldParserName=%s\n",fieldParsername));
        sb.append(String.format("methodExcludeList=%s\n",ListUtilities.listToString(methodExcludeList)));
        sb.append(String.format("blackList=%s\n",ListUtilities.listToString(blackList)));
        sb.append(String.format("whiteList=%s\n",ListUtilities.listToString(whiteList)));
        return sb.toString();
    }

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String context) {
        this.contextName = context;
        this.contextName = context == null ? "" : context;
        contextPropertiesFileName = Core.CONTEXT_DIRECTORY.concat(String.format(Core.CONTEXT_PROPERTIES_FILE,this.contextName));
    }

    public String getSourceFilepath() {
        return sourceFilepath;
    }

    public void setSourceFilepath(String sourcefilepath) {
        this.sourceFilepath = sourcefilepath;
    }

    public String getInputTreepath() {
        return inputTreepath;
    }

    public void setInputTreepath(String inputtreepath) {
        this.inputTreepath = inputtreepath;
    }

    public String getOutputTreepath() {
        return outputTreepath;
    }

    public void setOutputTreepath(String outputtreepath) {
        this.outputTreepath = outputtreepath;
    }

    public String getFieldParserName() {
        return fieldParsername;
    }

    public void setFieldParserName(String fieldparsername) {
        this.fieldParsername = fieldparsername;
    }

    public void addToExcludeMethodList(String method) {
        getMethodExcludelist().add(method);
    }

    public void clearMethodExcludeList() {
        getMethodExcludelist().clear();
    }

    public ObservableList<String> getMethodExcludelist() {
        methodExcludeList = (methodExcludeList == null ? FXCollections.observableArrayList() : methodExcludeList);
        return methodExcludeList;
    }

    public void setMethodExcludelist(Set<String> methodexcludelist) {
        this.methodExcludeList = FXCollections.observableArrayList(methodexcludelist);
    }
    
    public void setMethodExcludelist(ObservableList<String> methodexcludelist) {
        this.methodExcludeList = methodexcludelist;
    }

    public void addToBlacklist(String item) {
        getBlacklist().add(item);
    }

    public void clearBlacklist() {
        getBlacklist().clear();
    }

    public ObservableList<String> getBlacklist() {
        blackList = (blackList == null ? FXCollections.observableArrayList() : blackList);
        return blackList;
    }

    public void setBlacklist(Set<String> blacklist) {
        this.blackList = FXCollections.observableArrayList(blacklist);
    }
    
    public void setBlacklist(ObservableList<String> blacklist) {
        this.blackList = blacklist;
    }

    public void addToWhitelist(String item) {
        getWhitelist().add(item);
    }

    public void clearWhitelist() {
        getWhitelist().clear();
    }

    public ObservableList<String> getWhitelist() {
        whiteList = (whiteList == null ? FXCollections.observableArrayList() : whiteList);
        return whiteList;
    }

    public void setWhitelist(Set<String> whitelist) {
        this.whiteList = FXCollections.observableArrayList(whitelist);
    }
    
    public void setWhitelist(ObservableList<String> whitelist) {
        this.whiteList = whitelist;
    }

    public boolean isEditing() {
        return editing;
    }

    public void setEditing(boolean editing) {
        this.editing = editing;
    }

    public String getContextPropertiesFileName() {
        return contextPropertiesFileName;
    }

    public void setContextPropertiesFileName(String contextPropertiesFileName) {
        this.contextPropertiesFileName = contextPropertiesFileName;
    }

}

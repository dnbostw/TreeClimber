/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treeclimber;

import com.sun.javafx.scene.control.skin.LabeledText;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import treebuilder.TreeBuilder;
import treeclimber.components.ClassPanel;
import treeclimber.components.ContextData;
import treeclimber.components.ContextPanel;
import treeclimber.components.EditExcludesData;
import treeclimber.components.EditExcludesPanel;
import treeclimber.components.InstrumentationPanel;
import treeclimber.newrelic.ClassDescriptor;
import treeclimber.newrelic.InstrumentationDescriptor;
import utilities.FileUtilities;
import utilities.FormatUtilities;
import utilities.ListMapHandler;

/**
 *
 * @author dnbostw
 */
public class TreeClimberController implements Initializable {

    @FXML
    private BorderPane rootNode;

    @FXML
    private Pane masterPane, detailPane, headerPane, footerPane;

    @FXML
    private ScrollPane masterScrollPane, detailScrollPane;

    @FXML
    private SplitPane splitPane;

    @FXML
    Text detailText;

    @FXML
    private TreeView masterTree;

    @FXML
    private TreeView detailTree;

    @FXML
    private ComboBox context, nestingLimit;

    @FXML
    private Label status;

    @FXML
    private ListView packageList;

    @FXML
    private RadioButton includes;

    @FXML
    private RadioButton excludes;

    @FXML
    private RadioButton methods;

    private Button editPackages, refreshDetailButton, importContext, editContext;

    @FXML
    public void handleIncludeList(ActionEvent event) {
        packageList.getItems().setAll(Core.getContext().getPackageWhitelist());
    }

    @FXML
    public void handleExcludeList(ActionEvent event) {
        packageList.getItems().setAll(Core.getContext().getPackageBlacklist());
    }

    public void handleMethodList(ActionEvent event) {
        packageList.getItems().setAll(Core.getContext().getMethodExcludelist());
    }

    @FXML
    public void handleEditPackages(ActionEvent event) throws IOException {
        EditExcludesData data = new EditExcludesData("", packageList.getItems());
        try {
            EditExcludesPanel panel = new EditExcludesPanel(Core.getMainWindow(), data);
            EditExcludesData result = panel.show();
            if (result != null) {
                packageList.setItems(result.getList());
                if (includes.isSelected()) {
                    Core.getContext().setPackageWhitelist(new TreeSet(packageList.getItems()));
                } else if (excludes.isSelected()) {
                    Core.getContext().setPackageBlackList(new TreeSet(packageList.getItems()));
                } else if (methods.isSelected()) {
                    Core.getContext().setMethodExcludeList(new TreeSet(packageList.getItems()));
                }
                Core.getContext().saveContext();
            }
        } catch (IOException iox) {
            status.setText("Could not create the Edit dialog");
        }
    }

    private Set<String> getSelectedDetailItems(CheckBoxTreeItem parent) {
        Set<String> result = new HashSet<String>();
        if (parent.isSelected()) {
            result.add((String) parent.getValue());
        }
        if (parent.getChildren() != null) {
            for (CheckBoxTreeItem child : (ObservableList<CheckBoxTreeItem>) parent.getChildren()) {
                if (child.isSelected()) {
                    result.add((String) child.getValue());
                }
                result.addAll(getSelectedDetailItems(child));
            }
        }
        return result;
    }

    @FXML
    public void handleGenerateInstrumentation(ActionEvent event) throws IOException {
        Set<String> selected = getSelectedDetailItems((CheckBoxTreeItem) detailTree.getRoot());
        InstrumentationDescriptor document = new InstrumentationDescriptor(((String) context.getValue()).concat("Extension"), true, "2.0");
        document.setExtensionName((String) context.getValue() + "Extension");
        document.setEnabled(true);
        document.setVersion("1.0");
        InstrumentationPanel dialog = new InstrumentationPanel(Core.getMainWindow(), document);
        document = dialog.show();
        if (document == null) {
            return;
        }
        FileUtilities.openLog(Core.LOG_DIRECTORY.concat(Core.LOG_SELECTED_ITEMS));

        for (String item : selected) {
            FileUtilities.log(item);
        }
        FileUtilities.closeLog();

        ListMapHandler classMap = new ListMapHandler();

        BufferedReader mainTemplateReader = FileUtilities.openReadOnlyFile(Core.BASE_FILE_DIRECTORY.concat("Selected.log"));
        String mainLine = null;
        while ((mainLine = mainTemplateReader.readLine()) != null) {
            String className = mainLine.substring(0, mainLine.indexOf(":"));
            String methodName = mainLine.substring(mainLine.indexOf(":") + 1);
            classMap.put(className, methodName);
        }
        FileUtilities.closeReadOnlyFile(mainTemplateReader);

        for (Map.Entry<String, List<String>> entry : classMap.getMap().entrySet()) {
            ClassDescriptor classDescriptor = new ClassDescriptor(entry.getKey(), entry.getValue());
            ClassPanel classDialog = new ClassPanel(Core.getMainWindow(), classDescriptor);
            classDescriptor = classDialog.show();
            document.addPointcut(classDescriptor);
        }
        String newRelicfile = Core.NEW_RELIC_DIRECTORY.concat(String.format(Core.NEW_RELIC_DEFAULT_FILE_NAME, Core.getContext().getContextName()));
        FileUtilities.openLog(newRelicfile);
        FileUtilities.log(document.toString());
        FileUtilities.closeLog();
        String[] extensions = {"*.xml"};
        FileSaver fileSaver = new FileSaver("Save New Relic Instrumentation File",
                "XML Instrumentation Files (*.xml)",
                extensions);
        fileSaver.saveFile(newRelicfile);

        // java -Djava.ext.dirs=/path/to/jarred/classes -jar newrelic.jar instrument -file /path/to/file.xml -debug true
    }

    @FXML
    public void handleDetailRefresh(ActionEvent event) {
        refreshMethodTree(masterTree);
    }

    @FXML
    public void handleEditContext(ActionEvent event) {
        // Build edit object and show edit dialog
        ContextData data = new ContextData(Core.getContext().getData());
        try {
            ContextPanel panel = new ContextPanel(Core.getMainWindow(), data);
            // Get Context data values
            data = panel.show();
            panel.close();
        } catch (Exception ex) {
            
        }

    }

    @FXML
    public void handleImportContext(ActionEvent event) {
        ContextData data = new ContextData();
        try {
            ContextPanel panel = new ContextPanel(Core.getMainWindow(), data);
            // Get Context data values
            data = panel.show();
            panel.close();

         String workingdirectory = Core.BASE_FILE_DIRECTORY.concat(data.getContextName()).concat(File.separator);
         String jarfilepath = workingdirectory.concat(data.getContextName().concat("Temp.jar"));
         String treefilepath= workingdirectory.concat(data.getContextName().concat("InputTree.tree"));
         
//        this.jarfilepath = workingdirectory.concat(projectname).concat(".jar");
//        this.treefilepath = workingdirectory.concat(projectname).concat(".tree");

            
            // Build tree in default location
            TreeBuilder builder = new TreeBuilder(Core.BASE_FILE_DIRECTORY
                    , data.getContextPropertiesFileName()
                    , workingdirectory
                    , data.getSourceFilepath()
                    , jarfilepath
                    , treefilepath);
//            TreeBuilder builder = new TreeBuilder(data.getContextName(),Core.BASE_FILE_DIRECTORY, data.getSourceFilepath());
            
            builder.importProject();
            ContextData.save(data);
            FileUtilities.copyBinaryFile(treefilepath
                    , Core.CONTEXT_DIRECTORY.concat(String.format(Core.CONTEXT_INPUT_TREE_FILE,data.getContextName())));
            
            FileUtilities.deleteDir(new File(workingdirectory));
            
//            String contextFilename = Core.CONTEXT_DIRECTORY.concat(String.format(Core.CONTEXT_PROPERTIES_FILE,data.getContextName()));
//            File   contextFile = new File(contextFilename);
            updateContextMenu();
            context.getSelectionModel().select(data.getContextName());
            
        } catch (Exception ex) {
            status.setText(ex.getMessage().substring(0, 128));
        }
    }

    private Stack<CheckBoxTreeItem> itemStack = new Stack<CheckBoxTreeItem>();

    public void refreshMethodTree(TreeView tree) {
        CheckBoxTreeItem[] roots = new CheckBoxTreeItem[20];
        ObservableList<TreeItem> items = tree.getSelectionModel().getSelectedItems();
        TreeItemValue value = null;
        TreeItem item = items.get(0);
        Stack<CheckBoxTreeItem> rootStack = new Stack<CheckBoxTreeItem>();
        CheckBoxTreeItem rootItem = null;
        if (item != null) {
            if (item.getValue() instanceof TreeItemValue) {
                value = (TreeItemValue) item.getValue();
                if (value.isMethod()) {
                    try {
                        List<String> methodList = Core.climbTree(value.getFullName());
                        int currentLevel = 0;
                        boolean first = true;
                        int maxLevel = 0;
                        if (methodList != null) {
                            FileUtilities.openLog(Core.LOG_DIRECTORY.concat(Core.LOG_FILE_NAME));
                            for (String line : methodList) {
                                FileUtilities.log(line);
                                int lvl = Integer.valueOf(line.substring(0, line.indexOf(">")));
                                if (lvl > maxLevel) {
                                    maxLevel = lvl;
                                }
                            }
                            FileUtilities.closeLog();

                            String nestLevel = (String) nestingLimit.getValue();

                            nestingLimit.getItems().clear();
                            for (int i = 1; i <= Core.getMaxNestingLevel(); i++) {
                                nestingLimit.getItems().add("" + i);
                            }
                            if (Core.getMaxNestingLevel() / 2 == 0) {
                                nestingLimit.setValue("1");
                            } else {
                                nestingLimit.setValue("" + Core.getMaxNestingLevel());
                            }

                            nestingLimit.setValue(nestLevel);

                            int level = 0;
                            String text = "";
                            for (String string : methodList) {
                                level = Integer.valueOf(string.substring(0, string.indexOf(">")));
                                text = string.substring(string.indexOf(">") + 1);
                                CheckBoxTreeItem newItem = new CheckBoxTreeItem(text, null, false);
                                if (rootStack.size() == 0) {
                                    rootStack.push(newItem);
                                    currentLevel = level;
                                    rootItem = newItem;
                                    continue;
                                }
                                if (level < currentLevel) {
                                    while (level < currentLevel) {
                                        --currentLevel;
                                        if (level > 1) {
                                            rootStack.pop();
                                        }
                                    }
                                } else if (level > currentLevel) {

                                    CheckBoxTreeItem lastItem = null;
                                    if (rootStack.peek().getChildren() != null) {
                                        if (rootStack.peek().getChildren().size() > 0) {
                                            lastItem = (CheckBoxTreeItem) rootStack.peek().getChildren().get(rootStack.peek().getChildren().size() - 1);
                                            lastItem.getChildren().add(newItem);
                                            rootStack.push(lastItem);
                                        } else {
                                            lastItem = rootStack.peek();
                                            lastItem.getChildren().add(newItem);
                                        }
                                    }
                                    currentLevel = level;
                                    continue;
                                }
                                rootStack.peek().getChildren().add(newItem);
                            }
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(TreeClimberController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            if (detailTree.getRoot() != null && detailTree.getRoot().getChildren() != null) {
                detailTree.getRoot().getChildren().clear();
            }
            detailTree.setVisible(false);
            detailTree.setRoot(null);
            detailTree.setRoot(rootItem);
            detailTree.setVisible(true);

            status.setText(value != null ? value.getName() : "");
        }
    }

    @FXML
    public void handleMasterTreeClicked(Event event) throws IOException {
        if (event.getSource() instanceof LabeledText) {
            status.setText(((LabeledText) event.getSource()).getText());
        }
        if ((event.getSource() instanceof TreeView)) {
            refreshMethodTree(masterTree);
        }
    }

    @FXML
    public void handleNestingLimitChange(Event event) throws IOException {
        ComboBox box = (ComboBox) event.getTarget();
        String value = (String) box.getValue();
        String message = null;
        if (value != null) {
            int nl = Integer.valueOf(value);
            Core.setNestingLevel(nl);
//            refreshMethodTree(detailTree);
        }
    }

    @FXML
    public void handleContextChange(Event event) {
        ComboBox box = (ComboBox) event.getTarget();
        String value = (String) box.getValue();
        String message = null;
        try {
            message = Core.changeContext(value);
            includes.setSelected(true);
            ObservableList<String> pkgItems = FXCollections.observableArrayList(Core.getContext().getPackageWhitelist());
            packageList.setItems(pkgItems);
            masterTree.setVisible(false);
            clearTree(masterTree.getRoot());
//            masterTree.getRoot().getChildren().clear();
            masterTree.setRoot(null);
            masterTree.setRoot(new TreeItem("Code info for " + value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase()));
            masterTree.getRoot().getChildren().clear();
//            masterTree.getRoot().setValue("Code info for " + value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase());
            for (String pkg : Core.getPackages()) {
                TreeItem<String> pkgItem = new TreeItem(new TreeItemValue(pkg, pkg, TreeItemValue.TREE_ITEM_PACKAGE));
                Set<String> clsItems = Core.getClasses().get(pkg);

                for (String _class : clsItems) {
                    TreeItem<String> clsItem = new TreeItem(new TreeItemValue(_class, _class, TreeItemValue.TREE_ITEM_CLASS));

                    Set<String> methItems = Core.getMethods().get(_class);
                    for (String methItem : methItems) {
                        TreeItem<String> mItem = new TreeItem(new TreeItemValue(_class.concat(":").concat(methItem), methItem, TreeItemValue.TREE_ITEM_METHOD));
                        clsItem.getChildren().add(mItem);
                    }
                    pkgItem.getChildren().add(clsItem);
                }
                masterTree.getRoot().getChildren().add(pkgItem);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            message = "Error changing Context";
        }
        masterTree.setVisible(true);
//        status.setText("Writing package and class files.");
//        writeInfoFile((String) value);
        status.setText(message);
    }

    private void clearTree(TreeItem tree) {
        for (Iterator it = tree.getChildren().iterator(); it.hasNext();) {
            TreeItem item = (TreeItem) it.next();
            if (item.getChildren() != null) {
                clearTree(item);
            } else {
                item.getChildren().clear();
            }
        }
    }

    private void writeInfoFile(String context) {
        FileWriter clsFile = null;
        try {
            clsFile = FileUtilities.openWriteOnlyFile("files/" + context + "Classes.txt");
            for (Map.Entry<String, Set<String>> entry : Core.getClasses().entrySet()) {
                clsFile.write(entry.getKey() + "\n");
                for (String value : entry.getValue()) {
                    clsFile.write("\t" + value + "\n");
                    Set<String> methods = Core.getMethods().get(value);
//                    Core.getLineParser().getContext().addToPackageBlacklist("java.");
//                    Core.getLineParser().getContext().addToPackageBlacklist("com.google.");
//                    Core.getLineParser().getContext().addToPackageBlacklist("org.jboss.");
                    for (String method : methods) {
                        clsFile.write("\t\t" + method + "\n");
                        String fullPath = value.concat(":").concat(method);
                        List<String> calls = Core.getLineParser().parseLines(Core.getLineParser().getContext().getRawInput(), fullPath, LineParser.PARSE_FULL_METHOD);
                        System.out.println("" + calls.size() + " entries for " + fullPath + " found.");
                        for (String call : calls) {
                            String calledMethod = Core.getLineParser().getFieldParser().parseField(call, 2);
                            if (Core.getLineParser().getContext().isInPackageBlacklist(calledMethod)) {
                                continue;
                            }
                            clsFile.write("\t\t\t" + calledMethod + "\n"); // 
                        }
                    }
                }
            }
        } catch (IOException iox) {
            Logger.getLogger(TreeClimberController.class.getName()).log(Level.SEVERE, null, iox);
        } finally {
            try {
                FileUtilities.closeWriteOnlyFile(clsFile);
            } catch (IOException ex) {
                Logger.getLogger(TreeClimberController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void shutdown() {
        try {
            FileUtilities.closeLog();
        } catch (IOException ex) {
            Logger.getLogger(TreeClimberController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateContextMenu() {
        List<String> c = FileUtilities.findFilesContaining("Context.properties", Core.CONTEXT_DIRECTORY);
        Set<String> contextNames = new TreeSet<String>();
        for (String s : c) {
            contextNames.add(FormatUtilities.stripAllExcept(s, "Context.properties"));
        }
        ObservableList<String> contexts = FXCollections.observableArrayList(contextNames);
        context.setItems(contexts);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ToggleGroup group = new ToggleGroup();
        includes.setToggleGroup(group);
        excludes.setToggleGroup(group);
        methods.setToggleGroup(group);
        includes.setSelected(true);
        nestingLimit.setValue("5");
//treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle); 
//        masterTree.setCellFactory(tree -> {
//            TreeCell<TreeItemValue> cell;
//            cell = new TreeCell<TreeItemValue>();
//            ((Node) cell).addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//                public void handle(MouseEvent event) {
//                    TreeItem item = (TreeItem) event.getTarget();
//                    TreeItemValue value = (TreeItemValue) item.getValue();
//                    status.setText(value.getName());
//                }
//            });
//            return cell;
//
//            @Override
//            protected void updateItem(TreeItemValue item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty || item == null) {
//                    item.setName("");
//                    item.setType(TreeItemValue.TREE_ITEM_PACKAGE);
//                } else {
//                    item.setName(item.toString());
//                }
//            }
//          );
//        TreeItem masterRootItem = new TreeItem(new TreeItemValue("", 0));
        masterTree.addEventHandler(TreeItem.branchCollapsedEvent(), new MasterTreeMouseEventHandler());
        masterTree.addEventHandler(TreeItem.branchExpandedEvent(), new MasterTreeMouseEventHandler());
        detailTree.setCellFactory(CheckBoxTreeCell.<TreeItemValue>forTreeView());

//        masterRootItem.addEventHandler(EventType.ROOT, new MasterTreeMouseEventHandler());
        masterTree.setRoot(
                new TreeItem(new TreeItemValue("", "", 0)));

        masterTree.setVisible(
                true);
        detailTree.setVisible(
                true);

        updateContextMenu();
        try {
            FileUtilities.openLog(Core.LOG_FILE_NAME.concat(Core.LOG_FILE_NAME));
        } catch (IOException ex) {
            Logger.getLogger(TreeClimberController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public class MasterTreeMouseEventHandler implements EventHandler<TreeItem.TreeModificationEvent> {

        @Override
        public void handle(TreeItem.TreeModificationEvent event) {
//            processMasterTreeEvent((Event)event);
            if (event.getTarget() instanceof LabeledText) {
                status.setText(((LabeledText) event.getTarget()).getText());
            }
            if (event.getTarget() instanceof TreeView) {
                TreeView tree = (TreeView) event.getTarget();
                ObservableList<TreeItem> items = tree.getSelectionModel().getSelectedItems();
                TreeItem item = items.get(0);
                TreeItemValue value = (TreeItemValue) item.getValue();
                status.setText(value.getName());
            }
//            TreeItem item = (TreeItem) event.getSource(); // getTreeItem();
//            TreeItemValue value = (TreeItemValue) item.getValue();
//            if (value != null) {
//                status.setText(value.getName());
//            }
        }

    }

//    private void handleMasterTreeItemClicked(MouseEvent event) {
//        Node node = event.getPickResult().getIntersectedNode();
//        // Accept clicks only on node cells, and not on empty spaces of the TreeView
//        if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
//            String name = (String) ((TreeItem) masterTree.getSelectionModel().getSelectedItem()).getValue();
//            status.setText(name);
//        }
//    }
}

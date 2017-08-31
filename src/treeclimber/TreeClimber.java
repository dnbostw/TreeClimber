/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treeclimber;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utilities.ObjectUtilities;

/**
 *
 * @author dnbostw
 */
public class TreeClimber extends Application {

    String rootMethod = "edu.emory.commons.portal.server.EnterpriseDataServiceImpl:getProposals(java.lang.String,int,int,int,java.lang.String,boolean)"; // args[1];
    String line = "M:edu.emory.commons.portal.client.DataService:getProposals(java.lang.String,int,int,int,java.lang.String,boolean,com.google.gwt.user.client.rpc.AsyncCallback) (M)edu.emory.commons.portal.client.DataService:checkDead()";

    private void show(Object root, boolean flag) {
        if (root instanceof Parent) {
            for (Node node : ((Parent) root).getChildrenUnmodifiable()) {
                show(node, flag);
                node.setVisible(flag);
            }
        } else if (root instanceof Node) {
            ((Node) root).setVisible(flag);
        }
    }

    private static Stage mainStage;

    public static Stage getMainStage() {
        return mainStage;
    }

    private void setStyles(Scene scene, Parent root) {
//        URL url = getClass().getResource("../css/stylein.css");
        scene.getStylesheets().add("file:css/stylein.css"); // url.toExternalForm());
        
        SplitPane splitPane = new ObjectUtilities<SplitPane>().getObject(root, SplitPane.class, "splitPane");

        Pane topPane = new ObjectUtilities<Pane>().getObject(root, Pane.class, "headerPane");
//        Pane leftPane = new ObjectUtilities<Pane>().getObject(root, Pane.class, "masterPane");
//        Pane centerPane = new ObjectUtilities<Pane>().getObject(root, Pane.class, "detailPane");
        Pane bottomPane = new ObjectUtilities<Pane>().getObject(root, Pane.class, "footerPane");

        topPane.getStyleClass().add("pane");
        bottomPane.getStyleClass().add("pane");
        splitPane.getStyleClass().add("pane");
//        leftPane.getStyleClass().add("pane");
//        centerPane.getStyleClass().add("pane");
    }

    private void setResizeBindngs(Parent root) {
        BorderPane rootPane = new ObjectUtilities<BorderPane>().getObject(root, BorderPane.class, "rootNode");
        Pane topPane = new ObjectUtilities<Pane>().getObject(root, Pane.class, "headerPane");
        SplitPane splitPane = new ObjectUtilities<SplitPane>().getObject(root, SplitPane.class, "splitPane");
        Pane masterPane = new ObjectUtilities<Pane>().getObject(splitPane, Pane.class, "masterPane");
        ScrollPane masterScrollPane = new ObjectUtilities<ScrollPane>().getObject(masterPane, ScrollPane.class, "masterScrollPane");
        TreeView masterTree = new ObjectUtilities<TreeView>().getObject(masterScrollPane, TreeView.class, "masterTree");
//        Pane detailPane = new ObjectUtilities<Pane>().getObject(splitPane, Pane.class, "detailPane");
//        ComboBox nestingLimit = new ObjectUtilities<ComboBox>().getObject(detailPane, ComboBox.class, "nestingLimit");
//        nestingLimit.setItems((ObservableList<String>)new FXCollections.observableArrayList("5"));
//        splitPane.prefWidthProperty().bind(rootPane.widthProperty().subtract(20.0d));
        
//        masterPane.prefWidthProperty().bind(splitPane.widthProperty());
//        masterPane.prefHeightProperty().bind(splitPane.heightProperty());
        masterTree.minWidthProperty().bind(masterPane.widthProperty());
        masterTree.minHeightProperty().bind(masterPane.heightProperty());
        
//        leftPane.translateYProperty().bind(rootPane.layoutYProperty().add(topPane.heightProperty()));
//        leftPane.prefWidthProperty().bind(getMainStage().widthProperty().divide(2.0d));
//        leftPane.prefHeightProperty().bind(getMainStage().heightProperty().subtract(200.0d));
//        ScrollPane leftScrollPane = new ObjectUtilities<ScrollPane>().getObject(root, ScrollPane.class, "masterScrollPane");
//        leftScrollPane.setFitToHeight(true);
//        leftScrollPane.setFitToWidth(true);


//        if (masterTree.isResizable()) {
//            masterTree.prefWidthProperty().bind(leftPane.widthProperty().subtract(8.0d));
//        }
        
//        Pane centerPane = new ObjectUtilities<Pane>().getObject(root, Pane.class, "detailPane");
//        centerPane.translateXProperty().bind(getMainStage().widthProperty().divide(2.0d).negate());
//        centerPane.prefWidthProperty().bind(getMainStage().widthProperty().divide(2.0d));
//        centerPane.prefHeightProperty().bind(getMainStage().heightProperty().subtract(200.0d));
//        ScrollPane centerScrollPane = new ObjectUtilities<ScrollPane>().getObject(root, ScrollPane.class, "detailScrollPane");
//        centerScrollPane.setFitToHeight(true);
//        centerScrollPane.setFitToWidth(true);
        
        Pane bottomPane = new ObjectUtilities<Pane>().getObject(root, Pane.class, "footerPane");
        
//        TreeView detailTree = new ObjectUtilities<TreeView>().getObject(centerScrollPane, TreeView.class, "detailTree");

//        leftScrollPane.minWidthProperty().bind(leftPane.minWidthProperty().subtract(20.0d));
//        centerScrollPane.minWidthProperty().bind(centerPane.minWidthProperty().subtract(20.0d));
        
//        if (detailTree.isResizable()) {
//            detailTree.prefWidthProperty().bind(centerPane.widthProperty().subtract(8.0d));
//        }

//
//        Button leftButton = new ObjectUtilities<Button>().getObject(root, Button.class, "leftButton");
//        leftButton.layoutXProperty().bind(leftPane.layoutXProperty().add(10.0d));
//        leftButton.minWidthProperty().bind(leftPane.widthProperty().subtract(20.0d));
//        
//        Button centerButton = new ObjectUtilities<Button>().getObject(root, Button.class, "centerButton");
//        centerButton.layoutXProperty().bind(leftPane.layoutXProperty().add(10.0d));
//        centerButton.minWidthProperty().bind(leftPane.widthProperty().subtract(20.0d));
//        
//        Label headerLabel = new ObjectUtilities<Label>().getObject(root, Label.class, "headerLabel");
//        headerLabel.translateXProperty().bind(topPane.widthProperty().divide(2.0d).add(topPane.layoutXProperty()));
    }

//    private List<TreeItem> getTreeItems(String destination) {
//        LineParser parser = Core.getLineParser();
//        List<String> lines = new LinkedList<String>();
//        List<TreeItem> result = new LinkedList<>();
//        lines.add(line);
//        Set<String> info = (destination.equals("master") ? parser.getPackages(lines) : parser.getClasses(lines));
//        info.forEach((item) -> {
//            result.add(new TreeItem(item));
//        });
//        return result;
//
//    }
//
    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage;

        Pane root = FXMLLoader.load(getClass().getResource("TreeClimberDocument.fxml"));
//        setResizeBindngs(root);

        Scene scene = new Scene(root);
        Core.setMainScene(scene);
        
        scene.getRoot().requestFocus();
        setStyles(scene, root);

        stage.setScene(scene);
        stage.setTitle("Tree Climber");

//        FieldParser fp = new FieldParser("debugit");
//        String rawField1 = fp.parseRawField(line, 1);
//        String rawField2 = fp.parseRawField(line, 2);
//        String field1 = fp.parseField(line, 1);
//        String field2 = fp.parseField(line, 2);
//        String fullMethodLine1 = fp.parseFullMethodName(line, 1);
//        String fullMethod1 = fp.parseFullMethodName(rawField1, 1);
//        String fullMethod2 = fp.parseFullMethodName(rawField2, 2);
//        String packageName = fp.parsePackage(line);
//        String className = fp.parseClass(line);
        ComboBox context = new ObjectUtilities<ComboBox>().getObject(root, ComboBox.class, "context");
        ObservableList<String> contextItems = FXCollections.observableArrayList("commons","rapid");
        context.setItems(contextItems);
        context.setVisible(true);
//        Pane masterPane = new ObjectUtilities<Pane>().getObject(root, Pane.class, "masterPane");
//        Pane detailPane = new ObjectUtilities<Pane>().getObject(root, Pane.class, "detailPane");
//        TreeView masterTree = new ObjectUtilities<TreeView>().getObject(masterPane, TreeView.class, "masterTree");
//        TreeView detailTree = new ObjectUtilities<TreeView>().getObject(detailPane, TreeView.class, "detailTree");
//        TreeItem masterRoot = new TreeItem("Package List");
//        TreeItem detailRoot = new TreeItem("Class List");
//
//        masterRoot.getChildren().addAll(getTreeItems("master"));
//        detailRoot.getChildren().addAll(getTreeItems("detail"));
//
//        masterRoot.setExpanded(true);
//        detailRoot.setExpanded(true);
//
//        masterTree.setRoot(masterRoot);
//        detailTree.setRoot(detailRoot);
        scene.getRoot().requestFocus();

        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

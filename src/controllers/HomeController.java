/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;


import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.jetbrains.annotations.NotNull;
import utils.ConnectionUtil;
import utils.SQLViewer;

/**
Design idea oXCToo
 */
public class HomeController implements Initializable {
    @FXML
    Label lblStatus;
    @FXML
    TableView tblData;
    @FXML
    private Pane parentPanel;
    @FXML
    private ComboBox tableSelectorComboBox;

    Connection connection;
    String SQL = "";
    ResultSet resultSet;
    SQLViewer sqlViewer;

    public HomeController() {
        try {
            connection = new ConnectionUtil("Home_Finances").connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initComboBox();
        initSQLViewer();
    }
    private void initComboBox(){
        tableSelectorComboBox.getItems().add("products");
        tableSelectorComboBox.getItems().add("markets");
        tableSelectorComboBox.getItems().add("existing products");
        tableSelectorComboBox.getItems().add("purchases");
    }

    private void initSQLViewer(){
        try {
            sqlViewer = new SQLViewer(connection, tblData);
            SQL = "Select * From purchases";
            sqlViewer.show(getQueryResult(SQL));
        }
        catch (Exception exception){
            throw new RuntimeException(exception);
        }
    }

    public void tableSelectorComboBox_OnAction(ActionEvent actionEvent) {
        sqlViewer.clearTable();
        parentPanel.getChildren().clear();
        String selectedTable = tableSelectorComboBox.getValue().toString();
        if (selectedTable == "products"){
            SQL = "Select * From products Order By ID";
        }
        else if (selectedTable == "markets")
            SQL = "Select * From market";

        else if (selectedTable == "existing products"){
            loadProductController();
            SQL = "Select * From existing_products_view";
        }

        else if (selectedTable == "purchases")
            SQL = "Select * From purchases";

        ResultSet rs = getQueryResult(SQL);
        if (rs!= null)
            sqlViewer.show(rs);
    }
    private void loadProductController(){
        Node node = null;
        try {
            node = FXMLLoader.load(getClass().getResource("/fxml/product.fxml"));
        } catch (IOException e) {
            System.err.println();
        }
        initChildStage(node);
    }
    private void initChildStage(Node node){
            node.maxWidth(parentPanel.getMaxWidth());
            node.prefHeight(parentPanel.getPrefHeight());
            node.prefWidth(parentPanel.getPrefWidth());
            parentPanel.getChildren().add(node);
    }
    private ResultSet getQueryResult(String SQL){
        try {
            resultSet = connection.createStatement().executeQuery(SQL);
            return resultSet;
        } catch (SQLException e) {
            lblStatus.setText(e.getMessage());
            return null;
        }
    }

    public int filterData(@NotNull TextField filterInput){
        ObservableList<ObservableList<String>> data = sqlViewer.getData();
        FilteredList<ObservableList<String>> filteredData;
        TableView dataTable = sqlViewer.getDataTable();
        try {
            if (data != null)
                filteredData = new FilteredList(data, e -> true);
            else
                return -1;
        }
        catch (Exception exception){
            System.err.println(exception.getMessage());
            throw new RuntimeException(exception);
        }

        filterInput.setOnKeyReleased( keyEvent->{
            filterInput.textProperty().addListener((observableValue, newValue,oldValue)->{
                filteredData.setPredicate( (Predicate<? super ObservableList<String>>) e ->{
                    if (newValue == null|| newValue.isEmpty())
                        return true;
                    else if (e.contains(newValue))
                        return true;
                    else
                        return false;
                });
            });
        });
        SortedList<ObservableList> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(dataTable.comparatorProperty());
        dataTable.getItems().clear();
        dataTable.setItems(sortedData);
        return 0;
    }

    @FXML
    public void searchTxtField_onKeyReleased(KeyEvent keyEvent) {
//        filterData(searchTextField);
    }
//    private void initDBConnection(){
//        List<Window> open = Stage.getWindows().stream().filter(Window::isShowing).toList();
//        Stage stage = (Stage) open.get(0);
//        connection = (Connection) stage.getUserData();
//    }
}

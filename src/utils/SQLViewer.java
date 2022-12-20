package utils;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Predicate;

public class SQLViewer {
    private ObservableList<ObservableList<String>> data;
    private Connection connection;
    TableView dataTable;

    public SQLViewer(Connection connection, TableView dataTable){
        this.connection = connection;
        this.dataTable = dataTable;
    }


    public ObservableList<ObservableList<String>> getData() {
        return data;
    }

    public void setData(ObservableList<ObservableList<String>> data) {
        this.data = data;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setDataTable(TableView dataTable) {
        this.dataTable = dataTable;
    }

    public TableView getDataTable() {
        return dataTable;
    }

    private void filterData(String filter){

    }

    public void show(ResultSet resultSet){
        fetColumnList(resultSet);
        fetRowList(resultSet);
    }

    private void fetColumnList(ResultSet resultSet) {
        try {
            //SQL FOR SELECTING ALL OF CUSTOMER
            for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(resultSet.getMetaData().getColumnName(i + 1).toUpperCase());
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {

                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        if (param.getValue().get(j) != null){
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                        else {
                            SimpleStringProperty nullValue = new SimpleStringProperty("NULL");
                            return nullValue;
                        }
                    }
                });
                dataTable.getColumns().removeAll(col);
                dataTable.getColumns().addAll(col);
            }
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());

        }
    }

    //fetches rows and data from the list
    private void fetRowList(ResultSet resultSet) {
        data = FXCollections.observableArrayList();
        try {
            while (resultSet.next()) {
                //Iterate Row
                ObservableList row = FXCollections.observableArrayList();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(resultSet.getString(i));
                }
                data.add(row);

            }

            dataTable.setItems(data);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
    public void clearTable(){
        dataTable.getColumns().clear();
        dataTable.getItems().clear();
        data.clear();
    }
}

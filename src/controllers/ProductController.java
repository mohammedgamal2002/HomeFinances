package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import utils.ConnectionUtil;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ProductController{
    @FXML
    private ComboBox commandComboBox;
    @FXML
    private TextField txtProductName;

    @FXML
    private TextField txtMarketName;
    @FXML
    private TextField txtPrice;
    @FXML
    private Button btnSave;

    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    String SQL;

    public ProductController(){
        try {
            this.connection = new ConnectionUtil("Home_Finances").connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void HandleEvents(MouseEvent mouseEvent) {


    }

    public void btnExecuteSQL_onAction(ActionEvent actionEvent) {
        int productID = insertIntoProducts();
        int marketID = insertIntoMarket();
        insertIntoExistingProducts(Integer.toString(productID),Integer.toString(marketID));
        System.out.println("Insertion Successful");
    }

    private void insertIntoExistingProducts(String P_ID, String M_ID){
        String tableName = "existing_products";

        SQL = "Insert into " +
                tableName +
                " Values(?,?,?)";
        try {
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1,P_ID);
            preparedStatement.setString(2,M_ID);
            preparedStatement.setString(3,txtPrice.getText());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private int insertIntoProducts(){
        String tableName = "products";
        int lastID;
        int currentID;

        try{
            lastID = getIdCount(tableName);
            currentID = lastID + 1;
        }
        catch (Exception exception){

            System.err.println(exception);
            throw new RuntimeException("invalid ID");
        }
        SQL = "Insert into " +
                tableName +
                " Values(?,?)";
        try {
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1,Integer.toString(currentID));
            preparedStatement.setString(2,txtProductName.getText());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return currentID;
    }
    private  int  insertIntoMarket(){
        String tableName = "market";
        int lastID;
        int currentID;
        try{
            lastID = getIdCount(tableName);
            currentID = lastID + 1;
        }
        catch (Exception exception){

            System.err.println(exception);
            throw new RuntimeException("invalid ID");
        }
            SQL = "Insert into " +
                    tableName +"(ID,m_name)"+
                    " Values(?,?)";
            try {
                preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setString(1,Integer.toString(currentID));
                preparedStatement.setString(2,txtMarketName.getText());
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return currentID;
    }

    private int getIdCount(String tableName){
        int idCount = -1;
        String sql = "Select COUNT(*) from "+tableName;
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new RuntimeException("Couldn't get ID");
            } else {
                idCount = Integer.parseInt(resultSet.getString(1));
                return idCount;
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            throw new RuntimeException();
        }
    }

}


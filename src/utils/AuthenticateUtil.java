package utils;

import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.sql.*;

public class AuthenticateUtil {
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    String sql;
    final String authTableName = "home_user";
    final String usernameColName = "hu_name";
    final String passwordColName = "password";
    public AuthenticateUtil(Connection connection){
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    private void prepareAuthStatement(String username, String password){
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
        }
        catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
    public String logIn( String username, String password) {
        String status = "";
        if(username.isEmpty() || password.isEmpty()) {
            status = "Error : Empty credentials";
        } else {
                if (tryLogIn(username,password)) {
                    status = "Success";
                    return status;
                } else {
                    status = "Error";
                    return status;
                }
        }
        return status;
    }

    private boolean tryLogIn(String username, String password){
        try {
            sql = "SELECT * FROM " + authTableName +
                    " Where " +
                    usernameColName + " = ?" +
                    " AND " +
                    passwordColName+" = ?";

            prepareAuthStatement(username, password);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.next())
                return false;
            else
                return true;
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            return false;
        }
    }

    public String register(String username, String password) {
        String status = "";
        if(username.isEmpty() || password.isEmpty()) {
            status = "Error : Empty credentials";
        }
        else {
                if (tryRegister(username, password))
                    status = "Success";
                else
                    status = "Exception";
        }
        return status;
    }

    private boolean tryRegister(String username, String password){
        try {
            int id = getLastID(authTableName) + 1;
            sql = "Insert into home_user Values("+id+",?,?)";
            prepareAuthStatement(username, password);
            preparedStatement.executeUpdate();
            return true;
        }
        catch (SQLException ex) {
            System.err.println(ex.getMessage());
            return false;
        }
    }
    private int getLastID(String tableName){
        int idCount = -1;
        sql = "Select Max(ID) from "+ tableName;
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new RuntimeException();
            } else {
                idCount = Integer.parseInt(resultSet.getString(1));
                return idCount;
            }
        }
        catch (SQLException ex) {
            System.err.println(ex.getMessage());
            throw new RuntimeException();
        }
    }
}

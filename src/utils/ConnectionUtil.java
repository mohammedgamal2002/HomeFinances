package utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConnectionUtil {
    final String DB_URL;
    final String SERVER_NAME = "LAPTOP-IH3NAIAC";
    String dbName;
    Connection conn;

    public ConnectionUtil(String dbName) {
        this.dbName = dbName;
        DB_URL = "jdbc:sqlserver://" +SERVER_NAME+ ";" +
                "DatabaseName=" + dbName +";" +
                "encrypt=true;" +
                "trustServerCertificate=true;" +
                "integratedSecurity=true;";
        try {
            initDriverMS_SQL();
        } catch (Exception e) {
            System.err.println("ConnectionUtil : "+e.getMessage());
            throw new RuntimeException(e);
        }
    }//MS_SQL_DB.MS_SQL_DB.MyDatabase()

    private void initDriverMS_SQL() throws Exception{
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        }
        catch (ClassNotFoundException e) {
            System.err.println("error in Dependencies, ClassNotFoundException");
            throw new Exception();
        }
    }//initDriver

    public Connection connect() throws SQLException {
        try {
            conn = DriverManager.getConnection(DB_URL);
            System.err.printf("Connected \nServer : %s \nDatabase : %s \n",SERVER_NAME,dbName);
            return conn;
        }
        catch (SQLException e) {
            System.err.println("Cannot connect to ,"+SERVER_NAME);
            throw new SQLException(e);
        }
    }//connect

}

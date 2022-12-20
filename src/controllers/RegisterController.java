package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.AuthenticateUtil;
import utils.ConnectionUtil;
import utils.ErrorDescriptor;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    @FXML
    private Label lblErrors;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private Button btnSignIn;

    AuthenticateUtil authenticateUtil;
    String status;
    ErrorDescriptor errorDescriptor;

    public RegisterController() {
        Connection connection;
        try {
            connection = new ConnectionUtil("Home_Finances").connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        authenticateUtil = new AuthenticateUtil(connection);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initErrorLabel();
        errorDescriptor = new ErrorDescriptor(lblErrors);
    }

    private void initErrorLabel(){
        if (authenticateUtil == null) {
            lblErrors.setTextFill(Color.TOMATO);
            lblErrors.setText("Server Error : Check");
        } else {
            lblErrors.setTextFill(Color.GREEN);
            lblErrors.setText("Server is up : Good to go");
        }
    }

    public void btnSignIn_onAction(ActionEvent actionEvent) {
        loadLoginScreen(actionEvent);
    }
    public void btnSignUp_onAction(ActionEvent actionEvent) {
        if (validateInput()){
            errorDescriptor.status = authenticateUtil.register(txtUsername.getText(), txtPassword.getText());
            if (errorDescriptor.status == "Success"){
                loadLoginScreen(actionEvent);
            }
            else{
                errorDescriptor.showError();
            }
        }
    }

    private boolean validateInput(){
        if(txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty()) {
            errorDescriptor.status = "Error : Empty credentials";
            errorDescriptor.showError();
            return false;
        }
        else if(txtPassword.getText().length() < 8){
            lblErrors.setText("Password must be longer than 8");
            lblErrors.setTextFill(Color.TOMATO);
            return false;
        }
        else
            return  true;
    }

    private void loadLoginScreen(ActionEvent actionEvent){
        try {
            Node node = (Node) actionEvent.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/Login.fxml")));
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
}

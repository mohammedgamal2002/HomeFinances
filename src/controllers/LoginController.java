package controllers;

import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.fxml.*;
import javafx.stage.Stage;
import utils.AuthenticateUtil;
import utils.ConnectionUtil;
import utils.ErrorDescriptor;

public class LoginController implements Initializable {
    @FXML
    private Label lblErrors;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private Button btnLogIn;


    AuthenticateUtil authenticateUtil;
    String username;
    String password;
    String status;
    ErrorDescriptor errorDescriptor;

    public LoginController() {
        Connection  connection;
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

    public void btnLogInOnAction(javafx.event.ActionEvent actionEvent) {
        validateInput();
        if (actionEvent.getSource() == btnLogIn) {
            username = txtUsername.getText();
            password = txtPassword.getText();
            errorDescriptor.status = authenticateUtil.logIn(username, password);
            if (errorDescriptor.status.equals("Success")) {
                errorDescriptor.setErrorLabel(Color.GREEN, "Login Successful..Redirecting..");
                loadHomeScreen(actionEvent);
            }
            else{
                errorDescriptor.showError();
            }
        }
    }
    private void validateInput(){
        if(txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty()) {
            errorDescriptor.status = "Error : Empty credentials";
            errorDescriptor.showError();
        }
    }

    private void loadHomeScreen(javafx.event.ActionEvent actionEvent){
        try {
            Node node = (Node) actionEvent.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
            stage.setUserData(authenticateUtil.getConnection());
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/onBoard.fxml")));
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
    private void loadRegisterScreen(javafx.scene.input.MouseEvent  mouseEvent){
        try {
            Node node = (Node) mouseEvent.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/Register.fxml")));
            stage.setUserData(authenticateUtil.getConnection());
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void onBtnSignupClick(javafx.scene.input.MouseEvent mouseEvent) {
        loadRegisterScreen(mouseEvent);
    }
}

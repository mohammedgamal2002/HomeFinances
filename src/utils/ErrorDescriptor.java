package utils;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class ErrorDescriptor {
    private Label errorLabel;
    public String status;

    public void setStatus(String status) {
        this.status = status;
    }

    public ErrorDescriptor(Label errorLabel){
        this.errorLabel = errorLabel;
    }

    public void showError(){
        if (status.equals("Error : Empty credentials")){
            setErrorLabel(Color.TOMATO, "Empty credentials");
        }
        else if (status.equals("Error")){
            setErrorLabel(Color.TOMATO, "Enter Correct Email/Password");

        }
        else if (status.equals("")){
            setErrorLabel(Color.TOMATO, "Something went wrong , please try again");
        }
        else if (status.equals("Exception"))
            setErrorLabel(Color.TOMATO, "Exception");
    }

    public void setErrorLabel(Color color, String text) {
        errorLabel.setTextFill(color);
        errorLabel.setText(text);
//        System.out.println(text);
    }
}

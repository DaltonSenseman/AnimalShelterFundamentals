package io.github.animalshelter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Controller {
    @FXML
    private Button loginSubmitButton, logOutButton;

    @FXML
    private TextField loginUserField;

    @FXML
    private PasswordField loginPassField;

    @FXML
    private Label loginFailedLabel;

    /**
     * When the login scene submit button is clicked.
     * Checks username and password fields to see if db match.
     * @param event
     * @throws Exception
     */
    @FXML
    private void onSubmitButtonClicked(ActionEvent event) throws Exception {
        Stage stage = (Stage) loginSubmitButton.getScene().getWindow();
        Parent main = FXMLLoader.load(getClass().getResource("animalshelter.fxml"));

        // Check username and password field in order to login
        if (loginUserField.getText().equals("username") && loginPassField.getText().equals("password")) {
            stage.setScene(new Scene(main));
            stage.show();
        } else {
            loginFailedLabel.setText("Login failed!"); // If username or password do not match
        }
    }

    /**
     * When the main scene logout button is clicked.
     * Loads login scene onto the stage.
     * @param event
     * @throws Exception
     */
    @FXML
    private void logOutClicked(ActionEvent event) throws Exception {
        Stage stage = (Stage) logOutButton.getScene().getWindow();
        Parent login = FXMLLoader.load(getClass().getResource("animalShelterLogin.fxml"));

        stage.setScene(new Scene(login));
        stage.show();
    }

    /**
     * When the enter button is pressed.
     * Calls onSubmitButtonClicked module.
     * @param event
     * @throws Exception
     */
    @FXML
    private void onEnter(ActionEvent event) throws Exception {
        this.onSubmitButtonClicked(event);
    }
}

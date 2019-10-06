package io.github.animalshelter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
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

  Connection conn = null;
  Statement stmt = null;

  @FXML
  private Button loginSubmitButton, logOutButton, addAnimalBtn;

  @FXML
  private TextField loginUserField;

  @FXML
  private PasswordField loginPassField;

  @FXML
  private Label loginFailedLabel;
  @FXML private TextField animalName;
  @FXML private TextField species;

  public void initialize() {
    final String JDBC_DRIVER = "org.h2.Driver";
    final String DB_URL = "jdbc:h2:./res/data";
    final String USER = "";
    final String PASS = "";

    try {
      Class.forName(JDBC_DRIVER);
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      stmt = conn.createStatement();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * When the login scene submit button is clicked. Checks username and password fields to see if db
   * match.
   *
   * @param event
   * @throws Exception
   */
  @FXML
  private void onSubmitButtonClicked(ActionEvent event) throws Exception {
    Stage stage = (Stage) loginSubmitButton.getScene().getWindow();
    Parent main = FXMLLoader.load(getClass().getResource("animalshelter.fxml"));

    // Check username and password field in order to login
    if (loginUserField.getText().equals("username") && loginPassField.getText()
        .equals("password")) {
      stage.setScene(new Scene(main));
      stage.show();
    } else {
      loginFailedLabel.setText("Login failed!"); // If username or password do not match
    }
  }

  /**
   * When the main scene logout button is clicked. Loads login scene onto the stage.
   *
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
   * When the enter button is pressed. Calls onSubmitButtonClicked module.
   *
   * @param event
   * @throws Exception
   */
  @FXML
  private void onEnter(ActionEvent event) throws Exception {
    this.onSubmitButtonClicked(event);
  }

    /**
     * When the enter button is pressed. Calls onSubmitButtonClicked module.
     *
     * @param event
     * @throws Exception
     */
    @FXML
    private void addAnimal(ActionEvent event) {

      try {
        // Obtains the input from the text fields
        String newAnimalName = animalName.getText();
        String newSpecies = species.getText();
        String preparedStm = "INSERT INTO ANIMAL( NAME, SPECIES) VALUES ( ?, ? );";
        PreparedStatement preparedStatement = conn.prepareStatement(preparedStm);
        preparedStatement.setString(1, newAnimalName);
        preparedStatement.setString(2, newSpecies);
        preparedStatement.executeUpdate();

        stmt.close();
        conn.close();

      }
      catch (SQLException e){
        e.printStackTrace();

      }
    }
}

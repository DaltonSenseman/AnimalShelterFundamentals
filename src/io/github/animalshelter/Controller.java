package io.github.animalshelter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
  @FXML
  private TextField animalName;
  @FXML
  private TextField species;
  @FXML
  private TableView currentAnimals = new TableView();

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

    TableColumn<Animal, String> idNumber = new TableColumn<>("Collar ID");
    idNumber.setCellValueFactory(new PropertyValueFactory<>("Collar_ID"));
    TableColumn<Animal, String> currentName = new TableColumn<>("Name");
    currentName.setCellValueFactory(new PropertyValueFactory<>("name"));
    TableColumn<Animal, String> currentType = new TableColumn<>("Species");
    currentType.setCellValueFactory(new PropertyValueFactory<>("species"));

    currentAnimals.getColumns().add(idNumber);
    currentAnimals.getColumns().add(currentName);
    currentAnimals.getColumns().add(currentType);

    populateTable();

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

    try{
      // Obtains the input from the text fields
      String newAnimalName = animalName.getText();
      String newSpecies = species.getText();
      String preparedStm = "INSERT INTO ANIMAL( NAME, SPECIES) VALUES ( ?, ? );";
      PreparedStatement preparedStatement = conn.prepareStatement(preparedStm);
      preparedStatement.setString(1, newAnimalName);
      preparedStatement.setString(2, newSpecies);
      preparedStatement.executeUpdate();

      populateTable();

      stmt.close();
      conn.close();

    } catch (SQLException e) {
      e.printStackTrace();

    }
  }


  public void populateTable() {
    String sql = "SELECT * FROM ANIMAL;";
    ResultSet rs = null;
    try {
      rs = stmt.executeQuery(sql);

      ResultSetMetaData rsmd = rs.getMetaData();
      int numberOfColumns = rsmd.getColumnCount();

      ArrayList<Animal> arrOfAnimals = new ArrayList();
      // These loops are used to out put the table of data to the console
      while (rs.next()) {
        String id = rs.getString("COLLAR_ID");
        String name = rs.getString("Name");
        String animalSpecies = rs.getString("Species");
        Animal tableOfAnimals = new Animal(id, name, animalSpecies);
        arrOfAnimals.add(tableOfAnimals);
      }

      ObservableList pets = FXCollections.observableList(arrOfAnimals);
      currentAnimals.setItems(pets);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}

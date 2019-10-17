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
import javafx.scene.control.ComboBox;
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
  private Button loginSubmitButton, logOutButton, addAnimalBtn, animalIDSearchButton;

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
  // Search Function Declarations
  @FXML
  private ComboBox<String> animalEmployeeCmbBx = new ComboBox<>();
  @FXML
  private ComboBox<String> searchCatgryCmbBx = new ComboBox<>();
  @FXML
  private TextField searchField;

  public void initialize() {
    final String JDBC_DRIVER = "org.h2.Driver";
    final String DB_URL = "jdbc:h2:./res/data";
    final String USER = "";
    final String PASS = "";
    // Sets choices in search dropdown
    animalEmployeeCmbBx.getItems().addAll("Animal", "Employee");
    searchCatgryCmbBx.getItems()
        .addAll("Collar ID", "Animal Name", "Species", "Employee Num", "Emp First Name",
            "Emp Last Name", "Job Class");

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

      //stmt.close();
      //conn.close();

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

  @FXML
  private void search(ActionEvent event) {
    try {
      System.out.println("Search has been pressed");
      String preparedStm = "";
      String searchFieldValue;
      PreparedStatement preparedStatement;
      ResultSet resultSet;
      // If "Animal" chosen in first dropdown (combobox)
      if (animalEmployeeCmbBx.getValue().equals("Animal")) {
        // Depending on what they selected for "Search By" combobox, will change SELECT statement
        if (searchCatgryCmbBx.getValue().equals("Collar ID")) {
          // Select statement chooses animals that have the search term anywhere in their ID, Name,
          // or Species
          // Example: Search For: Animal, Search By: Collar ID, searchFieldValue = 1
          // returns all animals with "1" in their collard ID. This will later be changed so you can
          // choose to be specific or not
          preparedStm = "SELECT * FROM ANIMAL WHERE COLLAR_ID LIKE ?;";
        } else if (searchCatgryCmbBx.getValue().equals("Animal Name")) {
          preparedStm = "SELECT * FROM ANIMAL WHERE NAME LIKE ?";
        } else if (searchCatgryCmbBx.getValue().equals("Species")) {
          preparedStm = "SELECT * FROM ANIMAL WHERE SPECIES LIKE ?";
        }
        // If chooses "Animal" and an employee specific "Search By", it will send an error and not
        // execute the SQL, instead allowing user to reenter their search
        else {
          System.out.println("Cannot search for animals using employee search terms");
          return;
        }
      }
      // If "Employee" chosen in first combobox
      else {
        // look at if statement [if(animalEmployeeCmbBx...equals("Animal")] above for explanation
        // because the functions are basically the same, except this is for employees
        if (searchCatgryCmbBx.getValue().equals("Employee Num")) {
          preparedStm = "SELECT * FROM EMPLOYEE WHERE EMPLOYEE_NUM LIKE ?";
        } else if (searchCatgryCmbBx.getValue().equals("Emp First Name")) {
          preparedStm = "SELECT * FROM EMPLOYEE WHERE FIRST_NAME LIKE ?";
        } else if (searchCatgryCmbBx.getValue().equals("Emp Last Name")) {
          preparedStm = "SELECT * FROM EMPLOYEE WHERE LAST_NAME LIKE ?";
        } else if (searchCatgryCmbBx.getValue().equals("Job Class")) {
          preparedStm = "SELECT * FROM EMPLOYEE WHERE JOB_CLASS LIKE ?";
        } else {
          System.out.println("Cannot search for employees using animal search terms");
          return;
        }
      }
      searchFieldValue = searchField.getText();
      System.out.println(searchFieldValue);
      preparedStatement = conn.prepareStatement(preparedStm);
      preparedStatement.setString(1, "%" + searchFieldValue + "%");
      resultSet = preparedStatement.executeQuery();

      if (animalEmployeeCmbBx.getValue().equals("Animal")) {
        // Prints list of results of animal search
        while (resultSet.next()) {
          int collarID = resultSet.getInt("COLLAR_ID");
          String animalName = resultSet.getString("NAME");
          String species = resultSet.getString("SPECIES");

          System.out.println("----------");
          System.out.println("Collar ID: " + collarID);
          System.out.println("Animal Name: " + animalName);
          System.out.println("Species: " + species + "\n");
        }
      } else {
        // Prints list of results of employee search
        while (resultSet.next()) {
          int empNum = resultSet.getInt("EMPLOYEE_NUM");
          String empFirstName = resultSet.getString("FIRST_NAME");
          String empLastName = resultSet.getString("LAST_NAME");
          String jobClass = resultSet.getString("JOB_CLASS");

          System.out.println("----------");
          System.out.println("Employee Number: " + empNum);
          System.out.println("Employee First Name: " + empFirstName);
          System.out.println("Employee Last Name: " + empLastName);
          System.out.println("Job Class: " + jobClass + "\n");
        }
      }

//      stmt.close();
//      conn.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}

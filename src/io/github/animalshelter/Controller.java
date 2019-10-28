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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * This the the main controller class where all major functionality is stored.
 *
 * @author Dalton Senseman
 * @author Jeff Munoz
 * @author Jean Paul Mathew
 * @author Tomas Vergara
 * @author William Ramanand
 */
public class Controller {

  Connection conn = null;
  Statement stmt = null;

  @FXML
  private Button loginSubmitButton;

  @FXML
  private Button logOutButton;

  @FXML
  private Button addAnimalBtn;
  @FXML private Button deleteAnimal;

  @FXML
  private Button animalIDSearchButton;

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

  @FXML
  private TableView<AnimalEvent> eventsTable = new TableView();

  final ArrayList<Animal> arrOfAnimals = new ArrayList();
  ObservableList<Animal> pets;


  public void initialize() {
    pets = FXCollections.observableList(arrOfAnimals);
    // Sets choices in search dropdown
    animalEmployeeCmbBx.getItems().addAll("Animal", "Employee");
    searchCatgryCmbBx.getItems()
        .addAll("Collar ID", "Animal Name", "Species", "Employee Num", "Emp First Name",
            "Emp Last Name", "Job Class");


    TableColumn<Animal, Integer> idNumber = new TableColumn<>("Collar ID");
    idNumber.setCellValueFactory(new PropertyValueFactory<>("collarID"));
    TableColumn<Animal, String> currentName = new TableColumn<>("Name");
    currentName.setCellValueFactory(new PropertyValueFactory<>("name"));
    TableColumn<Animal, String> currentType = new TableColumn<>("Species");
    currentType.setCellValueFactory(new PropertyValueFactory<>("species"));

    currentAnimals.getColumns().add(idNumber);
    currentAnimals.getColumns().add(currentName);
    currentAnimals.getColumns().add(currentType);

    populateTable();

    TableColumn<AnimalEvent, String> eventType = new TableColumn<>("Type");
    eventType.setCellValueFactory(new PropertyValueFactory<>("eventType"));
    TableColumn<AnimalEvent, String> eventAnimalID = new TableColumn<>("Animal ID");
    eventAnimalID.setCellValueFactory(new PropertyValueFactory<>("animalID"));
    TableColumn<AnimalEvent, String> eventDate = new TableColumn<>("Date");
    eventDate.setCellValueFactory(new PropertyValueFactory<>("eventDate"));

    eventsTable.getColumns().addAll(eventType, eventAnimalID, eventDate);

    populateEventsTable();
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
    initializeDB();
    try {
      // Obtains the input from the text fields
      String newAnimalName = animalName.getText();
      String newSpecies = species.getText();
      String preparedStm = "INSERT INTO ANIMAL( NAME, SPECIES) VALUES ( ?, ? );";
      PreparedStatement preparedStatement = conn.prepareStatement(preparedStm);
      preparedStatement.setString(1, newAnimalName);
      preparedStatement.setString(2, newSpecies);
      preparedStatement.executeUpdate();

      populateTable();

    } catch (SQLException e) {
      e.printStackTrace();

    }
    closeDb();
  }
  public void deleteAnimal(){
    initializeDB();
    try {
      Animal animalToBeDeleted = (Animal) currentAnimals.getSelectionModel().getSelectedItem();
      int delete = animalToBeDeleted.getCollarID();
      String preparedStm = "DELETE FROM ANIMAL WHERE COLLAR_ID = ?;";
      PreparedStatement preparedStatement = null;
      preparedStatement = conn.prepareStatement(preparedStm);
      preparedStatement.setInt(1, delete);
      preparedStatement.executeUpdate();

      ObservableList<Animal> allAnimals = currentAnimals.getItems();
      ObservableList<Animal> selectedAnimals = currentAnimals.getSelectionModel().getSelectedItems();
      selectedAnimals.forEach(allAnimals::remove);

    } catch (SQLException e) {
      e.printStackTrace();
    }

    closeDb();
  }

  public void populateTable() {
    initializeDB();
    String sql = "SELECT * FROM ANIMAL;";
    ResultSet rs;
    try {
      arrOfAnimals.clear();
      pets.clear();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String name = rs.getString("Name");
        String animalSpecies = rs.getString("Species");
        int animalCollarId = rs.getInt("Collar_ID");
        Animal tableOfAnimals = new Animal(name, animalSpecies,animalCollarId);
        arrOfAnimals.add(tableOfAnimals);
      }
      pets = FXCollections.observableList(arrOfAnimals);
      currentAnimals.setItems(pets);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    closeDb();
  }

  @FXML
  private void search(ActionEvent event) {
    initializeDB();
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

    } catch (SQLException e) {
      e.printStackTrace();
    }
    closeDb();
  }

  public void populateEventsTable() {
    initializeDB();

    // Hard coded populate for eventsTable
    ArrayList<AnimalEvent> arrOfEvents = new ArrayList();
    arrOfEvents.add(new AnimalEvent("Cleaning", "0001", "09/12/2020"));
    arrOfEvents.add(new AnimalEvent("Vet Checkup", "0002", "12/21/2012"));


    ObservableList events = FXCollections.observableList(arrOfEvents);
    eventsTable.setItems(events);
    closeDb();
  }

  public void initializeDB() {
    // Connection to the database
    // JDBC driver name and database URL
    final String Jdbc_Driver = "org.h2.Driver";
    final String Db_Url = "jdbc:h2:./res/data";
    final String user = "";
    final String pass = "";

    try {
      Class.forName(Jdbc_Driver);
      // uses an empty password for now but it will be addressed at a later time
      conn = DriverManager.getConnection(Db_Url, user, pass);

      stmt = conn.createStatement();
    } catch (ClassNotFoundException e) {
      // e.printStackTrace();
      System.out.println("Unable to find class");
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Error in SQL please try again");
    }
  }
  public void closeDb(){
    try {
      stmt.close();
      conn.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}

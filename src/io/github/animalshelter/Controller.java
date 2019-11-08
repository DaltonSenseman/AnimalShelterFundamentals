package io.github.animalshelter;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.util.Callback;
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

  //********* LOGIN_SCREEN_FX_ID*****
  @FXML
  private Button loginSubmitButton;

  @FXML
  private TextField loginUserField;

  @FXML
  private PasswordField loginPassField;

  @FXML
  private Label loginFailedLabel;
  //*********************************


  //********* MISCELLANEOUS_FX_ID****
  @FXML
  private MenuBar menuBar;

  @FXML
  private Button openSearchBtn;

  @FXML
  private Button addAnimalBtn;
  @FXML
  private Button deleteAnimalBtn;

  @FXML
  private Button animalIDSearchButton;
  //*********************************


  //********* ADD_ANIMAL_FX_ID*******
  @FXML
  private TextField animalName;

  @FXML
  private TextField species;
  @FXML
  private ComboBox<String> animalGender = new ComboBox<>();

  @FXML
  private TextField breed;
  @FXML
  private TextField animalWeight;
  @FXML
  private ComboBox<Integer> kennelNumber = new ComboBox<>();

  @FXML
  private TextField animalAge;
  @FXML
  private ComboBox<String> animalSize = new ComboBox<>();

  @FXML
  private CheckBox neuteredCheckBox = new CheckBox();

  @FXML
  private TableView currentAnimals = new TableView();
  //*********************************

  //********* EMPLOYEE TAB **********
  @FXML
  private TableView<Employee> employeesTable = new TableView<>();

  private ObservableList<Employee> employees;

  @FXML
  private TextField employeeFirstName;

  @FXML
  private TextField employeeLastName;

  @FXML
  private TextField employeeAssignedTask;

  @FXML
  private ComboBox<String> jobTitle = new ComboBox<>();

  @FXML
  private TextField employeePhone;

  @FXML
  private TextField payRate;

  @FXML
  private DatePicker hireDate;

  @FXML
  private TextField empUsername;

  @FXML
  private TextField empPassword;

  @FXML
  private Button addEmployeeBtn;
  //**********************************


  // Search Function Declarations
  @FXML
  private ComboBox<String> animalEmployeeCmbBx = new ComboBox<>();

  @FXML
  private ComboBox<String> searchCatgryCmbBx = new ComboBox<>();

  @FXML
  private TextField searchField;

  @FXML
  private TableView searchResultTable = new TableView();

  @FXML
  private TableView<AnimalEvent> eventsTable = new TableView();

  @FXML
  private ChoiceBox<String> eventTypeChoice = new ChoiceBox<>();

  @FXML
  private DatePicker eventDatePick;

  @FXML
  private ChoiceBox<String> eventTimeChoice = new ChoiceBox<>();

  @FXML
  private TextField animalNameField;

  @FXML
  private Button addEventBtn;

  private ObservableList<AnimalEvent> animalEvents;

  final ArrayList<Animal> arrOfAnimals = new ArrayList();

  private ObservableList<Animal> pets;

  public void initialize() {
    pets = FXCollections.observableList(arrOfAnimals);
    // Sets choices in search dropdown
    animalEmployeeCmbBx.getItems().addAll("Animal", "Employee");
    searchCatgryCmbBx.getItems()
        .addAll("Collar ID", "Animal Name", "Species", "Employee Num", "Emp First Name",
            "Emp Last Name", "Job Class");
    animalGender.getItems().addAll("Male", "Female");
    animalGender.setEditable(false);
    animalSize.getItems().addAll("Small", "Medium","Large");
    kennelNumber.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    kennelNumber.setEditable(false);

    TableColumn<Animal, Integer> idNumber = new TableColumn<>("Collar ID");
    idNumber.setCellValueFactory(new PropertyValueFactory<>("collarID"));
    TableColumn<Animal, String> currentName = new TableColumn<>("Name");
    currentName.setCellValueFactory(new PropertyValueFactory<>("name"));
    TableColumn<Animal, String> gend = new TableColumn<>("Gender");
    gend.setCellValueFactory(new PropertyValueFactory<>("animalGender"));
    TableColumn<Animal, Integer> age = new TableColumn<>("Age");
    age.setCellValueFactory(new PropertyValueFactory<>("age"));
    TableColumn<Animal, Integer> currentKennel = new TableColumn<>("Kennel Location");
    currentKennel.setCellValueFactory(new PropertyValueFactory<>("kennelNumber"));
    TableColumn<Animal, String> currentType = new TableColumn<>("Species");
    currentType.setCellValueFactory(new PropertyValueFactory<>("species"));
    TableColumn<Animal, String> dateAddedToShelter = new TableColumn<>("Date joined");
    dateAddedToShelter.setCellValueFactory(new PropertyValueFactory<>("dateAdmitted"));

    currentAnimals.getColumns().add(idNumber);
    currentAnimals.getColumns().add(currentName);
    currentAnimals.getColumns().add(gend);
    currentAnimals.getColumns().add(age);
    currentAnimals.getColumns().add(currentKennel);
    currentAnimals.getColumns().add(currentType);
    currentAnimals.getColumns().add(dateAddedToShelter);

    populateTable();

    currentAnimals.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouseEvent) {
         if (mouseEvent.getClickCount() == 2) {
            openAnimalProfile();
          }
      }
    });

    // Setup Events Tab
    eventTypeChoice.getItems().addAll("Vet Checkup", "Cleaning");
    eventTimeChoice.getItems().addAll("8:00am", "8:30am", "9:00am", "9:30am", "10:00am", "10:30am", "11:00am", "11:30am", "12:00 noon", "12:30pm",
        "1:00pm", "1:30pm", "2:00pm", "2:30pm", "3:00pm", "3:30pm", "4:00pm", "4:30pm");
    setupEventsTable();
    populateEventsTable();

    // Setup Employees Tab
    jobTitle.getItems().addAll("Vet Tech", "Veterinarian", "Manager", "Janitor", "Accountant");
    setupEmployeesTable();
    populateEmployeesTable();
  }

  public void openAnimalProfile() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("animalProfile.fxml"));
      Parent root1 = (Parent) loader.load();

      ProfileController profileController = loader.getController();
      profileController.transferAnimal((Animal) currentAnimals.getSelectionModel().getSelectedItem());

      Stage stage = new Stage();
      stage.setScene(new Scene(root1));
      stage.show();

    } catch (IOException e) {
      System.out.println("Could not open animal profile!");
    }
  }

  /**
   * When the login scene submit button is clicked. Checks username and password fields to see if db
   * match.
   */
  @FXML
  private void onSubmitButtonClicked(ActionEvent event) throws Exception {
    Stage stage = (Stage) loginSubmitButton.getScene().getWindow();
    Parent main = FXMLLoader.load(getClass().getResource("animalshelterGUI.fxml"));

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
   */
  @FXML
  private void logOutClicked(ActionEvent event) throws Exception {
    Stage stage = (Stage) menuBar.getScene().getWindow();
    Parent login = FXMLLoader.load(getClass().getResource("animalShelterLogin.fxml"));

    stage.setScene(new Scene(login));
    stage.show();
  }

  /**
   * When the enter button is pressed. Calls onSubmitButtonClicked module.
   */
  @FXML
  private void onEnter(ActionEvent event) throws Exception {
    this.onSubmitButtonClicked(event);
  }

  /**
   * When the enter button is pressed. Calls onSubmitButtonClicked module.
   */
  @FXML
  private void addAnimal(ActionEvent event) {
    initializeDB();
    try {
      // Obtains the input from the text fields

      String newAnimalName = animalName.getText();
      String newAnimalAge = animalAge.getText();
      String newAnimalBreed = breed.getText();
      String newAnimalGender = animalGender.getValue();
      String newSize = animalSize.getValue();
      int newWeight = Integer.parseInt(animalWeight.getText());
      boolean isAnimalNeutered = neuteredCheckBox.isSelected();
      Date tempDate = new Date();
      String newSpecies = species.getText();
      int newKennelLocation = kennelNumber.getSelectionModel().getSelectedItem();

      String preparedStm = "INSERT INTO ANIMAL( NAME, SPECIES, BREED, AGE, DATE_ADMITTED, KENNEL_NUMBER, GENDER, NEUTERED, WEIGHT,SIZE) VALUES (?,?,?,?,?,?,?,?,?,?);";
      PreparedStatement preparedStatement = conn.prepareStatement(preparedStm);
      preparedStatement.setString(1, newAnimalName);
      preparedStatement.setString(2, newSpecies);
      preparedStatement.setString(3, newAnimalBreed);
      preparedStatement.setInt(4, Integer.parseInt(newAnimalAge));
      preparedStatement.setString(5, String.valueOf(tempDate));
      preparedStatement.setInt(6, newKennelLocation);
      preparedStatement.setString(7, newAnimalGender);
      preparedStatement.setBoolean(8, isAnimalNeutered);
      preparedStatement.setInt(9, newWeight);
      preparedStatement.setString(10, newSize);
      preparedStatement.executeUpdate();

      populateTable();

    } catch (SQLException e) {
      e.printStackTrace();

    } finally {
      Stage stage = (Stage) addAnimalBtn.getScene().getWindow();
      stage.close();
      closeDb();
    }

  }

  @FXML
  public void openAddAnimalDialog() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("animalAddPage.fxml"));
      Parent root1 = (Parent) fxmlLoader.load();
      Stage stage = new Stage();
      stage.setScene(new Scene(root1));
      stage.showAndWait();
      populateTable();
    } catch (IOException e) {
      System.out.println("Failed to open Animal Add Window!");
    }
  }

  public void deleteAnimal() {
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
      ObservableList<Animal> selectedAnimals = currentAnimals.getSelectionModel()
          .getSelectedItems();
      selectedAnimals.forEach(allAnimals::remove);

    } catch (SQLException e) {
      e.printStackTrace();
    }

    closeDb();
  }

  public void populateTable() {

    initializeDB();
    try {

      String sql = "SELECT * FROM ANIMAL;";
      arrOfAnimals.clear();
      pets.clear();
      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        int animalCollarId = rs.getInt("COLLAR_ID");
        String name = rs.getString("NAME");
        String animalSpecies = rs.getString("SPECIES");
        String tempBreed = rs.getString("BREED");
        int tempAge = rs.getInt("AGE");
        String tempDate = rs.getString("DATE_ADMITTED");
        int tempKennel = rs.getInt("KENNEL_NUMBER");
        String tempGender = rs.getString("GENDER");
        boolean tempNeutered = rs.getBoolean("NEUTERED");
        int tempWeight = rs.getInt("WEIGHT");
        String tempSize = rs.getString("SIZE");
        Animal tableOfAnimals = new Animal(name, animalSpecies, tempBreed, tempAge, tempKennel,
            tempGender, animalCollarId, tempDate, tempNeutered, tempWeight, tempSize);
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
      String preparedStm = "";
      String searchFieldValue;
      Boolean isAnimalTerm = false;
      PreparedStatement preparedStatement;
      ResultSet resultSet;
      if (searchCatgryCmbBx.getValue().equals("Collar ID")) {
        preparedStm = "SELECT * FROM ANIMAL WHERE COLLAR_ID LIKE ?;";
        isAnimalTerm = true;
      } else if (searchCatgryCmbBx.getValue().equals("Animal Name")) {
        preparedStm = "SELECT * FROM ANIMAL WHERE NAME LIKE ?";
        isAnimalTerm = true;
      } else if (searchCatgryCmbBx.getValue().equals("Species")) {
        preparedStm = "SELECT * FROM ANIMAL WHERE SPECIES LIKE ?";
        isAnimalTerm = true;
      } else if (searchCatgryCmbBx.getValue().equals("Employee Num")) {
        preparedStm = "SELECT * FROM EMPLOYEE WHERE EMPLOYEE_NUM LIKE ?";
        isAnimalTerm = false;
      } else if (searchCatgryCmbBx.getValue().equals("Emp First Name")) {
        preparedStm = "SELECT * FROM EMPLOYEE WHERE FIRST_NAME LIKE ?";
        isAnimalTerm = false;
      } else if (searchCatgryCmbBx.getValue().equals("Emp Last Name")) {
        preparedStm = "SELECT * FROM EMPLOYEE WHERE LAST_NAME LIKE ?";
        isAnimalTerm = false;
      } else if (searchCatgryCmbBx.getValue().equals("Job Class")) {
        preparedStm = "SELECT * FROM EMPLOYEE WHERE JOB_CLASS LIKE ?";
        isAnimalTerm = false;
      }
      searchFieldValue = searchField.getText();
      preparedStatement = conn.prepareStatement(preparedStm);
      preparedStatement.setString(1, "%" + searchFieldValue + "%");
      resultSet = preparedStatement.executeQuery();

      populateSearchResultsTable(resultSet);

    } catch (SQLException e) {
      e.printStackTrace();
    }
    closeDb();
  }

  @FXML
  private void populateSearchResultsTable(ResultSet resultSet) {
    ObservableList<ObservableList> obsListResults = FXCollections.observableArrayList();
    try {
      for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
        final int j = i;
        TableColumn col = new TableColumn(resultSet.getMetaData().getColumnName(i + 1));
        col.setCellValueFactory(
            new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
              public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty(param.getValue().get(j).toString());
              }
            });
        searchResultTable.getColumns().addAll(col);
      }

      while (resultSet.next()) {
        //Iterate Row
        ObservableList<String> row = FXCollections.observableArrayList();
        for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
          //Iterate Column
          row.add(resultSet.getString(i));
        }
        obsListResults.add(row);
      }

      searchResultTable.setItems(obsListResults);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void setupEventsTable() {
    TableColumn<AnimalEvent, String> eventType = new TableColumn<>("Type");
    eventType.setCellValueFactory(new PropertyValueFactory<>("eventType"));
    TableColumn<AnimalEvent, String> eventAnimalName = new TableColumn<>("Animal Name");
    eventAnimalName.setCellValueFactory(new PropertyValueFactory<>("animalName"));
    TableColumn<AnimalEvent, String> eventDate = new TableColumn<>("Date");
    eventDate.setCellValueFactory(new PropertyValueFactory<>("eventDate"));

    eventsTable.getColumns().addAll(eventType, eventAnimalName, eventDate);

    animalEvents = FXCollections.observableArrayList();
    eventsTable.setItems(animalEvents);
  }

  public void populateEventsTable() {
    initializeDB();

    animalEvents.clear();
    try {
      ResultSet resultSet;
      String query = "SELECT * FROM EVENTS";
      resultSet = stmt.executeQuery(query);

      int eventID;
      String eventType;
      String animalName;
      String eventDate;

      while (resultSet.next()) {
        eventID = resultSet.getInt("COLLAR_ID");
        eventType = resultSet.getString("EVENT_TYPE");
        animalName = resultSet.getString("ANIMAL_NAME");
        eventDate = resultSet.getString("EVENT_DATE");

        animalEvents.add(new AnimalEvent(eventID, eventType, animalName, eventDate));
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    closeDb();
  }


  public void addToEventsTable(ActionEvent actionEvent) {
    initializeDB();

    try {

      // Obtains the input from the text fields
      String eventType = eventTypeChoice.getValue();
      String animalName = animalNameField.getText();
      String eventDate = eventDatePick.getValue().toString();
      String eventTime = eventTimeChoice.getValue().toString();

      String preparedStm = "INSERT INTO EVENTS( EVENT_TYPE, ANIMAL_NAME, EVENT_DATE) VALUES (?,?,?);";
      PreparedStatement preparedStatement = conn.prepareStatement(preparedStm);

      preparedStatement.setString(1, eventType);
      preparedStatement.setString(2, animalName);
      preparedStatement.setString(3,  eventTime + " " + eventDate);

      preparedStatement.executeUpdate();

      preparedStatement.close();
      populateEventsTable();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    closeDb();
  }

  public void removeFromEventsTable(ActionEvent actionEvent) {
    initializeDB();
    try {

      AnimalEvent eventToBeDeleted = (AnimalEvent) eventsTable.getSelectionModel()
          .getSelectedItem();
      int idToDelete = eventToBeDeleted.getCollarID();
      String preparedStm = "DELETE FROM EVENTS WHERE COLLAR_ID = ?;";

      PreparedStatement preparedStatement = conn.prepareStatement(preparedStm);
      preparedStatement.setInt(1, idToDelete);
      preparedStatement.executeUpdate();

      ObservableList<AnimalEvent> allEvents = eventsTable.getItems();
      ObservableList<AnimalEvent> selectedEvents = eventsTable.getSelectionModel()
          .getSelectedItems();
      selectedEvents.forEach(allEvents::remove);

    } catch (SQLException e) {
      e.printStackTrace();
    }
    closeDb();
  }

  @FXML
  public void openSearchDialog() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("searchWindow.fxml"));
      Parent root1 = (Parent) fxmlLoader.load();
      Stage stage = new Stage();
      stage.setScene(new Scene(root1));
      stage.show();
    } catch (IOException e) {
      System.out.println("Failed to open Search Window!");
    }
  }

  public void setupEmployeesTable() {
    TableColumn<Employee, Integer> employeeNum = new TableColumn<>("Employee Number");
    employeeNum.setCellValueFactory(new PropertyValueFactory<>("employeeNum"));
    TableColumn<Employee, String> employeeFirstName = new TableColumn<>("First Name");
    employeeFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
    TableColumn<Employee, String> employeeLastName = new TableColumn<>("Last Name");
    employeeLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    TableColumn<Employee, String> employeeJobClass = new TableColumn<>("Job Class");
    employeeJobClass.setCellValueFactory(new PropertyValueFactory<>("jobClass"));
    TableColumn<Employee, String> employeePhone = new TableColumn<>("Phone Number");
    employeePhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
    TableColumn<Employee, Integer> employeePayRate = new TableColumn<>("Pay Rate");
    employeePayRate.setCellValueFactory(new PropertyValueFactory<>("payRate"));
    TableColumn<Employee, String> employeeHireDate = new TableColumn<>("Hire Date");
    employeeHireDate.setCellValueFactory(new PropertyValueFactory<>("hireDate"));
    TableColumn<Employee, String> employeeAssignedTask = new TableColumn<>("Assigned Task");
    employeeAssignedTask.setCellValueFactory(new PropertyValueFactory<>("assignedTask"));

    employeesTable.getColumns()
        .addAll(employeeNum, employeeFirstName, employeeLastName, employeeJobClass, employeePhone,
            employeePayRate, employeeHireDate, employeeAssignedTask);

    employees = FXCollections.observableArrayList();
    employeesTable.setItems(employees);
  }

  public void populateEmployeesTable() {
    initializeDB();

    employees.clear();
    try {
      ResultSet resultSet;
      String query = "SELECT * FROM EMPLOYEE";
      resultSet = stmt.executeQuery(query);

      int employeeNum;
      String firstName;
      String lastName;
      String jobClass;
      String phoneNumber;
      int payRate;
      String hireDate;
      String assignedTask;

      while (resultSet.next()) {
        employeeNum = resultSet.getInt("EMPLOYEE_NUM");
        firstName = resultSet.getString("FIRST_NAME");
        lastName = resultSet.getString("LAST_NAME");
        jobClass = resultSet.getString("JOB_CLASS");
        phoneNumber = resultSet.getString("PHONE_NUMBER");
        payRate = resultSet.getInt("PAY_RATE");
        hireDate = resultSet.getString("HIRE_DATE");
        assignedTask = resultSet.getString("ASSIGNED_TASK");

        employees.add(
            new Employee(employeeNum, firstName, lastName, jobClass, phoneNumber, payRate, hireDate,
                assignedTask));
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    closeDb();
  }

  @FXML
  public void openAddEmployeeDialog() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("employeeAddPage.fxml"));
      Parent root1 = (Parent) fxmlLoader.load();
      Stage stage = new Stage();
      stage.setScene(new Scene(root1));
      stage.showAndWait();
      populateEmployeesTable();
    } catch (IOException e) {
      System.out.println("Failed to open Employee Add Window!");
    }
  }

  @FXML
  public void addEmployee() {
    //gathers input from Add Employee page
    initializeDB();
    try {
      String addFirstName = employeeFirstName.getText();
      String addLastName = employeeLastName.getText();
      String addJobTitle = jobTitle.getValue();
      String addAssignedTask = employeeAssignedTask.getText();
      String addPhoneNumber = employeePhone.getText();
      int addPayRate = Integer.parseInt(payRate.getText());
      String addHireDate = hireDate.getValue().toString();
      String addUsername = empUsername.getText();
      String addPassword = empPassword.getText();

      String preparedStm = "INSERT INTO EMPLOYEE( FIRST_NAME, LAST_NAME, JOB_CLASS, ASSIGNED_TASK, PHONE_NUMBER, PAY_RATE, HIRE_DATE, USERNAME, PASSWORD) VALUES (?,?,?,?,?,?,?,?,?);";
      PreparedStatement preparedStatement = conn.prepareStatement(preparedStm);

      preparedStatement.setString(1, addFirstName);
      preparedStatement.setString(2, addLastName);
      preparedStatement.setString(3, addJobTitle);
      preparedStatement.setString(4, addAssignedTask);
      preparedStatement.setString(5, addPhoneNumber);
      preparedStatement.setInt(6, addPayRate);
      preparedStatement.setString(7, addHireDate);
      preparedStatement.setString(8, addUsername);
      preparedStatement.setString(9, addPassword);

      preparedStatement.executeUpdate();

      preparedStatement.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      Stage stage = (Stage) addEmployeeBtn.getScene().getWindow();
      stage.close();
      closeDb();
    }

  }

  public void removeFromEmployeesTable(ActionEvent actionEvent) {
    initializeDB();
    try {

      Employee employeeToBeDeleted = (Employee) employeesTable.getSelectionModel()
          .getSelectedItem();
      int idToDelete = employeeToBeDeleted.getEmployeeNum();
      String preparedStm = "DELETE FROM EMPLOYEE WHERE EMPLOYEE_NUM = ?;";

      PreparedStatement preparedStatement = conn.prepareStatement(preparedStm);
      preparedStatement.setInt(1, idToDelete);
      preparedStatement.executeUpdate();

      ObservableList<Employee> allEvents = employeesTable.getItems();
      ObservableList<Employee> selectedEvents = employeesTable.getSelectionModel()
          .getSelectedItems();
      selectedEvents.forEach(allEvents::remove);

    } catch (SQLException e) {
      e.printStackTrace();
    }
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
      e.printStackTrace();
      System.out.println("Unable to find class");
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Error in SQL please try again");
    }
  }

  public void closeDb() {
    try {
      stmt.close();
      conn.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void editAnimal(ActionEvent actionEvent) {
  }
}

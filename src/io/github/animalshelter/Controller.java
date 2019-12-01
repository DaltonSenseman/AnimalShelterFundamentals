package io.github.animalshelter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.SimpleStringProperty;
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
@SuppressWarnings("unchecked")
public class Controller {

  Connection conn = null;
  private Statement stmt = null;

  //<editor-fold desc="LOGIN_SCREEN_FX_ID">
  @FXML
  private Button loginSubmitButton;

  @FXML
  private TextField loginUserField;

  @FXML
  private PasswordField loginPassField;

  @FXML
  private Label loginFailedLabel;
  //</editor-fold>


  //<editor-fold desc="MISCELLANEOUS_FX_ID">
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
  //</editor-fold>


  //<editor-fold desc="ADD_ANIMAL_FX_ID">
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
  //</editor-fold>


  //<editor-fold desc="EMPLOYEE TAB">
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
  //</editor-fold>


  //<editor-fold desc="Search Function Declarations">
  @FXML
  private final ComboBox<String> animalEmployeeCmbBx = new ComboBox<>();

  @FXML
  private ComboBox<String> searchCatgryCmbBx = new ComboBox<>();

  @FXML
  private TextField searchField;

  @FXML
  private TableView searchResultTable = new TableView();

  @FXML
  private TableView<AnimalEvent> eventsTable = new TableView();

  @FXML
  private ComboBox<String> eventTypeChoice = new ComboBox<>();

  @FXML
  private DatePicker eventDatePick;

  @FXML
  private ComboBox<String> eventTimeChoice = new ComboBox<>();

  @FXML
  private TextField animalNameField;

  @FXML
  private TextField newTaskField;

  @FXML
  private Button addEventBtn;

  private ObservableList<AnimalEvent> animalEvents;

  final ArrayList<Animal> arrOfAnimals = new ArrayList();

  private ObservableList<Animal> pets;
  //</editor-fold>

  // Animal Profile Labels
  @FXML
  private Label animalProfileName;

  @FXML
  private Label animalProfileKennel;

  @FXML
  private Label animalProfileAge;

  @FXML
  private Label animalProfileDate;

  @FXML
  private Label animalProfileSpecies;

  @FXML
  private Label animalProfileBreed;

  @FXML
  private Label animalProfileSize;

  @FXML
  private Label animalProfileWeight;

  @FXML
  private Label animalProfileGender;

  @FXML
  private Label animalProfileNeutered;

  @FXML
  private Button closeAnimalProfileBtn;

  // Edit Animal
  @FXML
  private TextField editAnimalName;

  @FXML
  private TextField editAnimalKennel;

  @FXML
  private TextField editAnimalAge;

  @FXML
  private Label editAnimalDate;

  @FXML
  private TextField editAnimalSpecies;

  @FXML
  private TextField editAnimalBreed;

  @FXML
  private TextField editAnimalWeight;

  @FXML
  private TextField editAnimalGender;

  @FXML
  private CheckBox editAnimalNeutered;

  @FXML
  private ComboBox<String> editAnimalSize = new ComboBox();

  @FXML
  private Button saveAnimalProfile;


  private boolean hasBeenInitialized = false;

  private Animal selectedAnimal;

  public void initialize() {
    if (hasBeenInitialized) {
      return;
    }
    pets = FXCollections.observableList(arrOfAnimals);

    // Search Page Initializations
    // Didn't add Gender or Neutered since binary values and can be "searched" using TableView
    // Didn't add password either because that should be private
    animalEmployeeCmbBx.getItems().addAll("Animal", "Employee");
    // Sets choices for dropdown in Search page
    searchCatgryCmbBx.getItems()
        .addAll("Collar ID", "Animal Name", "Species", "Breed", "Animal Age",
            "Date Admitted", "Kennel Number", "Weight", "Size", "Employee Number",
            "Employee First Name",
            "Employee Last Name", "Job Class", "Assigned Task", "Phone Number", "Pay Rate",
            "Hire Date", "Username");
    // Sets default choice in search dropdown
    searchCatgryCmbBx.getSelectionModel().selectFirst();

    animalGender.getItems().addAll("Male", "Female");
    animalGender.setEditable(false);
    animalSize.getItems().addAll("Small", "Medium", "Large");
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

    currentAnimals.setOnMouseClicked(mouseEvent -> {
      selectedAnimal = (Animal) currentAnimals.getSelectionModel().getSelectedItem();
      if (mouseEvent.getClickCount() == 2) {
        openAnimalProfile((Animal) currentAnimals.getSelectionModel().getSelectedItem());
      }
    });

    // Setup Events Tab
    eventTypeChoice.getItems().addAll("Vet Checkup", "Cleaning");
    eventTimeChoice.getItems()
        .addAll("8:00am", "8:30am", "9:00am", "9:30am", "10:00am", "10:30am", "11:00am", "11:30am",
            "12:00 noon", "12:30pm",
            "1:00pm", "1:30pm", "2:00pm", "2:30pm", "3:00pm", "3:30pm", "4:00pm", "4:30pm");
    setupEventsTable();
    populateEventsTable();

    // Setup Employees Tab
    jobTitle.getItems().addAll("Vet Tech", "Veterinarian", "Manager", "Janitor", "Accountant");
    setupEmployeesTable();
    populateEmployeesTable();
    hasBeenInitialized = true;
  }

  private void openAnimalProfile(Animal selectedAnimal) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("animalProfile.fxml"));
      loader.setController(this);
      Parent root1 = loader.load();

      animalProfileName.setText(selectedAnimal.getName());
      animalProfileKennel.setText(String.valueOf(selectedAnimal.getKennelNumber()));
      animalProfileAge.setText(String.valueOf(selectedAnimal.getAge()));
      animalProfileDate.setText(selectedAnimal.getDateAdmitted());
      animalProfileSpecies.setText(selectedAnimal.getSpecies());
      animalProfileBreed.setText(selectedAnimal.getBreed());
      animalProfileSize.setText(selectedAnimal.getSize());
      animalProfileWeight.setText(String.valueOf(selectedAnimal.getWeight()));
      animalProfileGender.setText(selectedAnimal.getAnimalGender());
      animalProfileNeutered.setText(String.valueOf(selectedAnimal.isNeutered()));

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
      Parent root1 = fxmlLoader.load();
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

  //********* SEARCH PAGE FUNCTIONS ***************************************************************

  /**
   * Receives input from "searchCatgryCmbBx" to determine what column and table to search. Uses
   * "preparedStatement" to prevent SQL injection. Then inputs value received from "searchField"
   * into SQL statement and executed statement. Result of statement saved in "resultSet" which is
   * sent to "populateSearchResultsTable()" to print it in the TableView.
   */
  @FXML
  private void search(ActionEvent event) {
    initializeDB();

    try {
      String preparedStm = "";
      String searchFieldValue;
      PreparedStatement preparedStatement;
      ResultSet resultSet;

      // Uses selected choice from searchCatgryCmbBx dropdown to select correct SQL
      // "LIKE" selects data that contains the value being searched
      // Ex: Search: "a" = Cat, Mad, Glad, Shade
      // Case sensitive for the time being
      switch (searchCatgryCmbBx.getValue()) {
        case "Collar ID":
          preparedStm = "SELECT * FROM ANIMAL WHERE COLLAR_ID LIKE ?;";
          break;
        case "Animal Name":
          preparedStm = "SELECT * FROM ANIMAL WHERE UPPER(NAME) LIKE ?";
          break;
        case "Species":
          preparedStm = "SELECT * FROM ANIMAL WHERE UPPER(SPECIES) LIKE ?";
          break;
        case "Breed":
          preparedStm = "SELECT * FROM ANIMAL WHERE UPPER(BREED) LIKE ?";
          break;
        case "Animal Age":
          preparedStm = "SELECT * FROM ANIMAL WHERE AGE LIKE ?";
          break;
        case "Date Admitted":
          preparedStm = "SELECT * FROM ANIMAL WHERE UPPER(DATE_ADMITTED) LIKE ?";
          break;
        case "Kennel Number":
          preparedStm = "SELECT * FROM ANIMAL WHERE KENNEL_NUMBER LIKE ?";
          break;
        case "Weight":
          preparedStm = "SELECT * FROM ANIMAL WHERE WEIGHT LIKE ?";
          break;
        case "Size":
          preparedStm = "SELECT * FROM ANIMAL WHERE UPPER(SIZE) LIKE ?";
          break;
        case "Employee Number":
          preparedStm = "SELECT * FROM EMPLOYEE WHERE EMPLOYEE_NUM LIKE ?";
          break;
        case "Employee First Name":
          preparedStm = "SELECT * FROM EMPLOYEE WHERE UPPER(FIRST_NAME) LIKE ?";
          break;
        case "Employee Last Name":
          preparedStm = "SELECT * FROM EMPLOYEE WHERE UPPER(LAST_NAME) LIKE ?";
          break;
        case "Job Class":
          preparedStm = "SELECT * FROM EMPLOYEE WHERE UPPER(JOB_CLASS) LIKE ?";
          break;
        case "Assigned Task":
          preparedStm = "SELECT * FROM EMPLOYEE WHERE UPPER(ASSIGNED_TASK) LIKE ?";
          break;
        case "Phone Number":
          preparedStm = "SELECT * FROM EMPLOYEE WHERE PHONE_NUMBER LIKE ?";
          break;
        case "Pay Rate":
          preparedStm = "SELECT * FROM EMPLOYEE WHERE PAY_RATE LIKE ?";
          break;
        case "Username":
          preparedStm = "SELECT * FROM EMPLOYEE WHERE UPPER(USERNAME) LIKE ?";
          break;
        case "Hire Date":
          preparedStm = "SELECT * FROM EMPLOYEE WHERE HIRE_DATE LIKE ?";
          break;
      }

      searchFieldValue = searchField.getText(); // retrieves value from search text field
      preparedStatement = conn.prepareStatement(preparedStm);
      // Concatenates SQL statement chosen above and text to be searched
      preparedStatement.setString(1, "%" + searchFieldValue.toUpperCase() + "%");
      resultSet = preparedStatement.executeQuery(); // Executes SQL, saves results in resultSet

      populateSearchResultsTable(resultSet); // prints results to TableView

    } catch (SQLException e) {
      e.printStackTrace();
    }
    closeDb();
  }

  /**
   * Retrieves names of columns in database table and uses it to name TableView columns. Receives
   * values from resultSet and uses them to create a list matrix to populate TableView
   */
  @FXML
  private void populateSearchResultsTable(ResultSet resultSet) {
    searchResultTable.getItems().clear(); // Clears out data and columns before every search
    searchResultTable.getColumns().clear();
    // Stores data retrieved from search, held in resultSearch
    ObservableList<ObservableList> obsListResults = FXCollections.observableArrayList();

    try {
      // Counts the number of columns and retrieves their names
      for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
        final int j = i;
        // Sets database column name to TableView's column name
        TableColumn column = new TableColumn(resultSet.getMetaData().getColumnName(i + 1));
        column.setCellValueFactory(
            (Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(
                param.getValue().get(j).toString()));
        searchResultTable.getColumns().addAll(column); // adds created columns to TableView
      }

      while (resultSet.next()) { // while resultSet has values
        // Iterates through rows
        // Two lists needed, one for holding values per row,
        // other for holding list of lists of values per row (thus creating a sort of 2D array)
        ObservableList<String> row = FXCollections.observableArrayList();
        for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
          // sets values to list for that row
          row.add(resultSet.getString(i));
        }
        obsListResults.add(row);
      }
      // Populates TableView using list of lists of values per row
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
      preparedStatement.setString(3, eventTime + " " + eventDate);

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

      AnimalEvent eventToBeDeleted = eventsTable.getSelectionModel()
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
      Parent root1 = fxmlLoader.load();
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
      Parent root1 = fxmlLoader.load();
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

  // Sets employee's assigned task to complete
  public void completeTask(ActionEvent actionEvent) {
    initializeDB();
    try {
      Employee selectedEmployee = employeesTable.getSelectionModel()
          .getSelectedItem();
      // Doesn't let method continue if user hasn't selected employee
      if(selectedEmployee == null){
        return;
      }
      int selectedEmployeeID = selectedEmployee.getEmployeeNum();
      String preparedStm = "UPDATE EMPLOYEE SET ASSIGNED_TASK = 'Complete' WHERE EMPLOYEE_NUM = ?;";
      PreparedStatement preparedStatement = conn.prepareStatement(preparedStm);
      preparedStatement.setInt(1, selectedEmployeeID);
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    closeDb();
  }

  // Updates employee's assigned task to the new one
  public void assignTask(ActionEvent actionEvent) {
    initializeDB();
    try {
      Employee selectedEmployee = employeesTable.getSelectionModel()
          .getSelectedItem();
      // Doesn't let method continue if user hasn't selected employee
      if(selectedEmployee == null){
        return;
      }
      int selectedEmployeeID = selectedEmployee.getEmployeeNum();
      String newTask = newTaskField.getText();
      String preparedStm = "UPDATE EMPLOYEE SET ASSIGNED_TASK = ? WHERE EMPLOYEE_NUM = ?;";
      PreparedStatement preparedStatement = conn.prepareStatement(preparedStm);
      preparedStatement.setString(1, newTask);
      preparedStatement.setInt(2, selectedEmployeeID);
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    closeDb();
  }

  public void removeFromEmployeesTable(ActionEvent actionEvent) {
    initializeDB();
    try {

      Employee employeeToBeDeleted = employeesTable.getSelectionModel()
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
    Properties loginInfo = new Properties();
    try (InputStream input = new FileInputStream("./res/data.properties")) {
      loginInfo.load(input);

    } catch (FileNotFoundException e) {
      System.out.println("Properties File not found!");
    } catch (IOException ex) {
      System.out.println("IO exception occurred!");
    }

    // Connection to the database
    // JDBC driver name and database URL
    final String Jdbc_Driver = "org.h2.Driver";
    final String Db_Url = "jdbc:h2:./res/data";
    final String user = loginInfo.getProperty("db.username");
    final String pass = loginInfo.getProperty("db.password");

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

  @FXML
  public void editAnimalInfo() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("editAnimalProfile.fxml"));
      loader.setController(this);
      Parent root1 = loader.load();

      editAnimalName.setText(selectedAnimal.getName());
      editAnimalKennel.setText(String.valueOf(selectedAnimal.getKennelNumber()));
      editAnimalAge.setText(String.valueOf(selectedAnimal.getAge()));
      editAnimalDate.setText(selectedAnimal.getDateAdmitted());
      editAnimalSpecies.setText(selectedAnimal.getSpecies());
      editAnimalBreed.setText(selectedAnimal.getBreed());
      editAnimalSize.setValue(selectedAnimal.getSize());
      editAnimalSize.getItems().addAll("Small", "Medium", "Large");
      editAnimalWeight.setText(String.valueOf(selectedAnimal.getWeight()));
      editAnimalGender.setText(selectedAnimal.getAnimalGender());
      if (selectedAnimal.isNeutered()) {
        editAnimalNeutered.setSelected(true);
      }

      Stage stage = new Stage();
      stage.setScene(new Scene(root1));
      stage.showAndWait();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  public void saveAnimalEdit() {
    initializeDB();
    try {
      // Obtains the input from the text fields
      int animalId = selectedAnimal.getCollarID();
      String newAnimalName = editAnimalName.getText();
      String newAnimalAge = editAnimalAge.getText();
      String newAnimalBreed = editAnimalBreed.getText();
      String newAnimalGender = editAnimalGender.getText();
      String newSize = editAnimalSize.getValue();
      int newWeight = Integer.parseInt(editAnimalWeight.getText());
      boolean isAnimalNeutered = editAnimalNeutered.isSelected();
      String newSpecies = editAnimalSpecies.getText();
      int newKennelLocation = Integer.parseInt(editAnimalKennel.getText());

      String preparedStm = "UPDATE ANIMAL SET NAME=?, SPECIES=?, BREED=?, AGE=?, KENNEL_NUMBER=?, GENDER=?, NEUTERED=?, WEIGHT=?, SIZE=? WHERE COLLAR_ID=? ";
      PreparedStatement preparedStatement = conn.prepareStatement(preparedStm);
      preparedStatement.setString(1, newAnimalName);
      preparedStatement.setString(2, newSpecies);
      preparedStatement.setString(3, newAnimalBreed);
      preparedStatement.setInt(4, Integer.parseInt(newAnimalAge));
      preparedStatement.setInt(5, newKennelLocation);
      preparedStatement.setString(6, newAnimalGender);
      preparedStatement.setBoolean(7, isAnimalNeutered);
      preparedStatement.setInt(8, newWeight);
      preparedStatement.setString(9, newSize);
      preparedStatement.setInt(10, animalId);
      preparedStatement.executeUpdate();

      populateTable();

    } catch (SQLException e) {
      e.printStackTrace();

    } finally {
      Stage stage = (Stage) saveAnimalProfile.getScene().getWindow();
      stage.close();
      stage = (Stage) closeAnimalProfileBtn.getScene().getWindow();
      stage.close();
      closeDb();
    }
  }

  @FXML
  public void closeAnimalProfile() {
    Stage stage = (Stage) closeAnimalProfileBtn.getScene().getWindow();
    stage.close();
  }
}


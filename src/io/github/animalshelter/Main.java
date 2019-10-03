package io.github.animalshelter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    // establish connection to database.
    initializeDB();

    Parent root = FXMLLoader.load(getClass().getResource("animalshelterLogin.fxml"));
    primaryStage.setTitle("Animal Shelter");
    primaryStage.setScene(new Scene(root, 650, 450));
    primaryStage.show();
  }

  private void initializeDB() {
    final String JDBC_DRIVER = "org.h2.Driver";
    final String DB_URL = "jdbc:h2:./res/data";
    final String USER = "";
    final String PASS = "";
    Connection conn = null;
    Statement stmt = null;

    //This is a test SQL query to validate the DB is functioning this is to be removed once
    //SQL query's are mapped to a button / dialog in the GUI program.
    try {
      Class.forName(JDBC_DRIVER);
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      stmt = conn.createStatement();
      String test1 = "INSERT INTO ANIMAL(NAME, SPECIES) "
          + "VALUES ('Felix','TomCat')";
      stmt.executeUpdate(test1);
      stmt.close();
      conn.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}

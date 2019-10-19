package io.github.animalshelter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This the the main class of the the Animal Shelter application
 *
 * @author Dalton Senseman
 * @author Jeff Munoz
 * @author Jean Paul Mathew
 * @author Tomas Vergara
 * @author William Ramanand
 */
public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {

    Parent root = FXMLLoader.load(getClass().getResource("animalShelterLogin.fxml"));
    primaryStage.setTitle("Animal Shelter");
    primaryStage.setScene(new Scene(root, 426, 400));
    primaryStage.show();

  }


  public static void main(String[] args) {
    launch(args);
  }
}

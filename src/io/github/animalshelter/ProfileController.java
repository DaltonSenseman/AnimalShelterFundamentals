package io.github.animalshelter;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ProfileController {

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

  private Animal selectedAnimal;

  public ProfileController(Animal animal) {
    this.selectedAnimal = animal;
  }

  public void initialize() {
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
  }
  public void transferAnimal(Animal animal) {
    this.selectedAnimal = animal;
  }

  @FXML
  public void closeAnimalProfile() {
    Stage stage = (Stage) closeAnimalProfileBtn.getScene().getWindow();
    stage.close();
  }

}

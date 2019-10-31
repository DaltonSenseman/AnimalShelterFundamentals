package io.github.animalshelter;

import java.util.Date;

/**
 * The class allows the creation of animal objects
 *
 * @author Dalton Senseman
 * @author Jeff Munoz
 * @author Jean Paul Mathew
 * @author Tomas Vergara
 * @author William Ramanand
 */
public class Animal {

  private String name;
  private String species;
  private String breed;
  private int age;
  private int kennelNumber;
  private String animalGender;
  private int collarID;
  private String dateAdmitted;

  public Animal(String name, String species, int collarID) {
    this.name = name;
    this.species = species;
    this.collarID = collarID;
  }

  public Animal(String name, String species, String breed, int age, int kennelNumber,
      String animalGender, int collarID, String dateAdmitted) {
    this.name = name;
    this.species = species;
    this.breed = breed;
    this.age = age;
    this.kennelNumber = kennelNumber;
    this.animalGender = animalGender;
    this.collarID = collarID;
    this.dateAdmitted = dateAdmitted;
  }

  public String getBreed() {
    return breed;
  }

  public void setBreed(String breed) {
    this.breed = breed;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public int getKennelNumber() {
    return kennelNumber;
  }

  public void setKennelNumber(int kennelNumber) {
    this.kennelNumber = kennelNumber;
  }

  public String getAnimalGender() {
    return animalGender;
  }

  public void setAnimalGender(String animalGender) {
    this.animalGender = animalGender;
  }

  public int getCollarID() {
    return collarID;
  }

  public void setCollarID(int collarID) {
    this.collarID = collarID;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSpecies() {
    return species;
  }

  public void setSpecies(String species) {
    this.species = species;
  }

  public String getDateAdmitted() {
    return dateAdmitted;
  }

  public void setDateAdmitted(String dateAdmitted) {
    this.dateAdmitted = dateAdmitted;
  }
}

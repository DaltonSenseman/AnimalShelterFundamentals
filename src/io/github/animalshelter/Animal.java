package io.github.animalshelter;

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
  private int collarID;

  public Animal( String name, String species, int collarID) {
    this.name = name;
    this.species = species;
    this.collarID = collarID;
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


}

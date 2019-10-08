package io.github.animalshelter;

public class Animal {

  private String name;
  private String species;
  private String id;

  public Animal(String id, String name, String species) {
    this.id = id;
    this.name = name;
    this.species = species;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

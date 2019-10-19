package io.github.animalshelter;

/**
 * This class allows the creation of event objects that animals are involved in
 *
 * @author Dalton Senseman
 * @author Jeff Munoz
 * @author Jean Paul Mathew
 * @author Tomas Vergara
 * @author William Ramanand
 */
public class AnimalEvent {
  private String animalID;
  private String eventType;
  private String eventDate;

  public AnimalEvent(String eventType, String animalID, String eventDate) {
    this.eventType = eventType;
    this.animalID = animalID;
    this.eventDate = eventDate;
  }

  public String getAnimalID() {
    return animalID;
  }

  public String getEventType() {
    return eventType;
  }

  public String getEventDate() {
    return eventDate;
  }

  public void setAnimalID(String animalID) {
    this.animalID = animalID;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  public void setEventDate(String eventDate) {
    this.eventDate = eventDate;
  }
}

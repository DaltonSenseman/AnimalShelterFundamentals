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
@SuppressWarnings("unused")
public class AnimalEvent {
  private int collarID;
  private String animalName;
  private String eventType;
  private String eventDate;

  public AnimalEvent(int collarID, String eventType, String animalName, String eventDate) {
    this.collarID = collarID;
    this.eventType = eventType;
    this.animalName = animalName;
    this.eventDate = eventDate;
  }

  public int getCollarID() {
    return collarID;
  }

  @SuppressWarnings("unused")
  public void setEventID(int eventID) {
    this.collarID = collarID;
  }

  @SuppressWarnings("unused")
  public String getAnimalName() {
    return animalName;
  }

  public String getEventType() {
    return eventType;
  }

  public String getEventDate() {
    return eventDate;
  }

  public void setAnimalID(String animalName) {
    this.animalName = animalName;
  }

  @SuppressWarnings("unused")
  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  public void setEventDate(String eventDate) {
    this.eventDate = eventDate;
  }
}

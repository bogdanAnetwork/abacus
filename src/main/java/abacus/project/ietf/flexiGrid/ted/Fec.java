package abacus.project.ietf.flexiGrid.ted;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeration that defines the type of
 * Forward Error Correction
 */
public enum Fec {
  _15_SD("_15_SD"), _15_OV("_15_OV"), _25("_25");

  private final String jsonName;

  private Fec(){
      this.jsonName = this.name();
  }

  private Fec(String jsonName) {
    this.jsonName = jsonName;
  }

  @JsonCreator
  public static Fec fromJsonString(String jsonString) {
    for (Fec value : Fec.values())
      if (value.jsonName.equals(jsonString)) {
        return value;
      }
    return null;
  }

  @JsonValue
  public String toJsonString() {
    return this.jsonName;
  }

}
package abacus.project.ietf.flexiGrid.ted;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeration that defines the type of wave modulation
 */
public enum Modulation {
  QPSK, DP_QPSK_CP, DP_QPSK_I, DP_8QAM_CP, DP_8QAM_I, DP_16QAM_CP, DP_16QAM_I, DC_DP_16QAM;

  private final String jsonName;

  private Modulation(){
      this.jsonName = this.name();
  }

  private Modulation(String jsonName) {
    this.jsonName = jsonName;
  }

  @JsonCreator
  public static Modulation fromJsonString(String jsonString) {
    for (Modulation value : Modulation.values())
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
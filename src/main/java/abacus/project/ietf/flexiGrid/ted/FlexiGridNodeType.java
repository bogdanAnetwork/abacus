package abacus.project.ietf.flexiGrid.ted;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Determines the node type:
 * flexi-grid-node
 */
public enum FlexiGridNodeType {
  FLEXI_GRID_NODE("FLEXI_GRID_NODE"),
  TRANSPODENR("TRANSPONDER");

  private final String jsonName;

  private FlexiGridNodeType(){
      this.jsonName = this.name();
  }

  private FlexiGridNodeType(String jsonName) {
    this.jsonName = jsonName;
  }

  @JsonCreator
  public static FlexiGridNodeType fromJsonString(String jsonString) {
    for (FlexiGridNodeType value : FlexiGridNodeType.values())
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
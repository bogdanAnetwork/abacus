package abacus.project.ietf.flexiGrid.ted;

        import com.fasterxml.jackson.annotation.JsonCreator;
        import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeration that defines if an interface is numbered or
 * unnumbered
 */
public enum InterfaceType {
  CLIENT_INTERFACE("CLIENT_INTERFACE"), NETWORK_INTERFACE("NETWORK_INTERFACE");

  private final String jsonName;

  private InterfaceType(){
    this.jsonName = this.name();
  }

  private InterfaceType(String jsonName) {
    this.jsonName = jsonName;
  }

  @JsonCreator
  public static InterfaceType fromJsonString(String jsonString) {
    for (InterfaceType value : InterfaceType.values())
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
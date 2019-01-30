package abacus.project.ietf.flexiGrid.ted;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * List of connections between input and
 * output ports
 */
@JsonInclude(Include.NON_NULL)
public class ConnectionsListType implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Identifier of the input port
   */
  private final int inputPortId;

  /**
   * Identifier of the output port
   */
  private final int outputPortId;


  @JsonCreator
  public ConnectionsListType (
    @JsonProperty("input-port-id") int inputPortId,
    @JsonProperty("output-port-id") int outputPortId){
    this.inputPortId = inputPortId;
    this.outputPortId = outputPortId;
  }


  @JsonProperty("input-port-id")
  public int getInputPortId(){
    return this.inputPortId;
  }

  @JsonProperty("output-port-id")
  public int getOutputPortId(){
    return this.outputPortId;
  }


  @Override
  public int hashCode() {
    return Objects.hash(inputPortId, outputPortId);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConnectionsListType that = (ConnectionsListType) o;
    return Objects.equals(this.inputPortId, that.inputPortId) &&
       Objects.equals(this.outputPortId, that.outputPortId);
  }

}
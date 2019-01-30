package abacus.project.ietf.flexiGrid.mediaChannel;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import abacus.project.ietf.network.NodeId;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Source of the network media channel
 */
@JsonInclude(Include.NON_NULL)
public class LightPathSource implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Source node
   */
  private final NodeId sourceNode;

  /**
   * Source interface
   */
  private final String sourceInterface;


  @JsonCreator
  public LightPathSource (
    @JsonProperty("source-node") NodeId sourceNode,
    @JsonProperty("source-interface") String sourceInterface){
    this.sourceNode = sourceNode;
    this.sourceInterface = sourceInterface;
  }


  @JsonProperty("source-node")
  public NodeId getSourceNode(){
    return this.sourceNode;
  }

  @JsonProperty("source-interface")
  public String getSourceInterface(){
    return this.sourceInterface;
  }


  @Override
  public int hashCode() {
    return Objects.hash(sourceNode, sourceInterface);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LightPathSource that = (LightPathSource) o;
    return Objects.equals(this.sourceNode, that.sourceNode) &&
       Objects.equals(this.sourceInterface, that.sourceInterface);
  }

}
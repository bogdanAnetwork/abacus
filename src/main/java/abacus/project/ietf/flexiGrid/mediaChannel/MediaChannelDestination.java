package abacus.project.ietf.flexiGrid.mediaChannel;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import abacus.project.ietf.network.NodeId;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Destination of the media channel
 */
@JsonInclude(Include.NON_NULL)
public class MediaChannelDestination implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Destination node
   */
  private final NodeId destinationNode;

  /**
   * Destination port
   */
  private final String destinationInterface;


  @JsonCreator
  public MediaChannelDestination (
    @JsonProperty("destination-node") NodeId destinationNode,
    @JsonProperty("destination-interface") String destinationInterface){
    this.destinationNode = destinationNode;
    this.destinationInterface = destinationInterface;
  }


  @JsonProperty("destination-node")
  public NodeId getDestinationNode(){
    return this.destinationNode;
  }

  @JsonProperty("destination-interface")
  public String getDestinationInterface(){
    return this.destinationInterface;
  }


  @Override
  public int hashCode() {
    return Objects.hash(destinationNode, destinationInterface);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MediaChannelDestination that = (MediaChannelDestination) o;
    return Objects.equals(this.destinationNode, that.destinationNode) &&
       Objects.equals(this.destinationInterface, that.destinationInterface);
  }

}
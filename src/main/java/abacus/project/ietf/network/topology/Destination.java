package abacus.project.ietf.network.topology;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import abacus.project.ietf.network.NodeId;
import abacus.project.ietf.network.topology.TpId;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This container holds the logical destination of a
 * particular link.
 */
@JsonInclude(Include.NON_NULL)
public class Destination implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Destination node identifier, must be in the same
   * network.
   */
  private final NodeId destNode;

  /**
   * Termination point within destination node that
   * terminates the link.
   */
  private final TpId destTp;


  @JsonCreator
  public Destination (
    @JsonProperty("dest-node") NodeId destNode,
    @JsonProperty("dest-tp") TpId destTp){
    this.destNode = destNode;
    this.destTp = destTp;
  }


  @JsonProperty("dest-node")
  public NodeId getDestNode(){
    return this.destNode;
  }

  @JsonProperty("dest-tp")
  public TpId getDestTp(){
    return this.destTp;
  }


  @Override
  public int hashCode() {
    return Objects.hash(destNode, destTp);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Destination that = (Destination) o;
    return Objects.equals(this.destNode, that.destNode) &&
       Objects.equals(this.destTp, that.destTp);
  }

}
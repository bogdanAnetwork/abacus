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
 * This container holds the logical source of a particular
 * link.
 */
@JsonInclude(Include.NON_NULL)
public class Source implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Source node identifier, must be in same topology.
   */
  private final NodeId sourceNode;

  /**
   * Termination point within source node that terminates
   * the link.
   */
  private final TpId sourceTp;


  @JsonCreator
  public Source (
    @JsonProperty("source-node") NodeId sourceNode,
    @JsonProperty("source-tp") TpId sourceTp){
    this.sourceNode = sourceNode;
    this.sourceTp = sourceTp;
  }


  @JsonProperty("source-node")
  public NodeId getSourceNode(){
    return this.sourceNode;
  }

  @JsonProperty("source-tp")
  public TpId getSourceTp(){
    return this.sourceTp;
  }


  @Override
  public int hashCode() {
    return Objects.hash(sourceNode, sourceTp);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Source that = (Source) o;
    return Objects.equals(this.sourceNode, that.sourceNode) &&
       Objects.equals(this.sourceTp, that.sourceTp);
  }

}
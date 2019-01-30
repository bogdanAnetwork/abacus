package abacus.project.ietf.network.topology;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import abacus.project.ietf.network.NetworkId;
import abacus.project.ietf.network.NodeId;
import abacus.project.ietf.network.topology.TpId;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This list identifies any termination points that
 * the termination point is dependent on, or maps onto.
 * Those termination points will themselves be contained
 * in a supporting node.
 * This dependency information can be inferred from
 * the dependencies between links.  For this reason,
 * this item is not separately configurable.  Hence no
 * corresponding constraint needs to be articulated.
 * The corresponding information is simply provided by the
 * implementing system.
 */
@JsonInclude(Include.NON_NULL)
public class SupportingTerminationPointListType implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * This leaf identifies in which topology the
   * supporting termination point is present.
   */
  private final NetworkId networkRef;

  /**
   * This leaf identifies in which node the supporting
   * termination point is present.
   */
  private final NodeId nodeRef;

  /**
   * Reference to the underlay node, must be in a
   * different topology
   */
  private final TpId tpRef;


  @JsonCreator
  public SupportingTerminationPointListType (
    @JsonProperty("network-ref") NetworkId networkRef,
    @JsonProperty("node-ref") NodeId nodeRef,
    @JsonProperty("tp-ref") TpId tpRef){
    this.networkRef = networkRef;
    this.nodeRef = nodeRef;
    this.tpRef = tpRef;
  }


  @JsonProperty("network-ref")
  public NetworkId getNetworkRef(){
    return this.networkRef;
  }

  @JsonProperty("node-ref")
  public NodeId getNodeRef(){
    return this.nodeRef;
  }

  @JsonProperty("tp-ref")
  public TpId getTpRef(){
    return this.tpRef;
  }


  @Override
  public int hashCode() {
    return Objects.hash(networkRef, nodeRef, tpRef);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SupportingTerminationPointListType that = (SupportingTerminationPointListType) o;
    return Objects.equals(this.networkRef, that.networkRef) &&
       Objects.equals(this.nodeRef, that.nodeRef) &&
       Objects.equals(this.tpRef, that.tpRef);
  }

}
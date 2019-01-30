package abacus.project.ietf.network;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import abacus.project.ietf.network.NetworkId;
import abacus.project.ietf.network.NodeId;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents another node, in an underlay network, that
 * this node is supported by.  Used to represent layering
 * structure.
 */
@JsonInclude(Include.NON_NULL)
public class SupportingNodeListType implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * References the underlay network that the
   * underlay node is part of.
   */
  private final NetworkId networkRef;

  /**
   * References the underlay node itself.
   */
  private final NodeId nodeRef;


  @JsonCreator
  public SupportingNodeListType (
    @JsonProperty("network-ref") NetworkId networkRef,
    @JsonProperty("node-ref") NodeId nodeRef){
    this.networkRef = networkRef;
    this.nodeRef = nodeRef;
  }


  @JsonProperty("network-ref")
  public NetworkId getNetworkRef(){
    return this.networkRef;
  }

  @JsonProperty("node-ref")
  public NodeId getNodeRef(){
    return this.nodeRef;
  }


  @Override
  public int hashCode() {
    return Objects.hash(networkRef, nodeRef);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SupportingNodeListType that = (SupportingNodeListType) o;
    return Objects.equals(this.networkRef, that.networkRef) &&
       Objects.equals(this.nodeRef, that.nodeRef);
  }

}
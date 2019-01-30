package abacus.project.ietf.network;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.google.common.collect.ImmutableList;

import abacus.project.ietf.flexiGrid.ted.FlexiGridNodeType;
import abacus.project.ietf.flexiGrid.ted.NodeConfig;
import abacus.project.ietf.flexiGrid.ted.NodeState;
import abacus.project.ietf.network.NodeId;
import abacus.project.ietf.network.SupportingNodeListType;
import abacus.project.ietf.network.topology.TerminationPointListType;

import java.util.List;

/**
 * The inventory of nodes of this network.
 */
@JsonInclude(Include.NON_NULL)
public class NodeListType implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Identifies a node uniquely within the containing
   * network.
   */
  private final String nodeId;

  /**
   * Represents another node, in an underlay network, that
   * this node is supported by.  Used to represent layering
   * structure.
   */
  private final List<SupportingNodeListType> supportingNode;

  /**
   * A termination point can terminate a link.
   * Depending on the type of topology, a termination point
   * could, for example, refer to a port or an interface.
   */
  private final List<TerminationPointListType> terminationPoint;

  /**
   * Configuration of a flexi-grid node
   */
  private final NodeConfig nodeConfig;

  /**
   * State of a flexi-grid node
   */
  private final NodeState nodeState;

  /**
   * Type of flexi-grid node
   */
  private final FlexiGridNodeType nodeType;


  @JsonCreator
  public NodeListType (
    @JsonProperty("node-id") String nodeId,
    @JsonProperty("supporting-node") List<SupportingNodeListType> supportingNode,
    @JsonProperty("termination-point") List<TerminationPointListType> terminationPoint,
    @JsonProperty("node-config") NodeConfig nodeConfig,
    @JsonProperty("node-state") NodeState nodeState,
    @JsonProperty("node-type") FlexiGridNodeType nodeType){
    this.nodeId = nodeId;
    this.supportingNode = supportingNode != null ? ImmutableList.copyOf(supportingNode) : ImmutableList.<SupportingNodeListType>of();
    this.terminationPoint = terminationPoint != null ? ImmutableList.copyOf(terminationPoint) : ImmutableList.<TerminationPointListType>of();
    this.nodeConfig = nodeConfig;
    this.nodeState = nodeState;
    this.nodeType = nodeType;
  }


  @JsonProperty("node-id")
  public String getNodeId(){
    return this.nodeId;
  }

  @JsonProperty("supporting-node")
  public List<SupportingNodeListType> getSupportingNode(){
    return this.supportingNode;
  }

  @JsonProperty("termination-point")
  public List<TerminationPointListType> getTerminationPoint(){
    return this.terminationPoint;
  }

  @JsonProperty("node-config")
  public NodeConfig getNodeConfig(){
    return this.nodeConfig;
  }

  @JsonProperty("node-state")
  public NodeState getNodeState(){
    return this.nodeState;
  }

  @JsonProperty("node-type")
  public FlexiGridNodeType getNodeType(){
    return this.nodeType;
  }


  @Override
  public int hashCode() {
    return Objects.hash(nodeId, supportingNode, terminationPoint, nodeConfig, nodeState, nodeType);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NodeListType that = (NodeListType) o;
    return Objects.equals(this.nodeId, that.nodeId) &&
       Objects.equals(this.supportingNode, that.supportingNode) &&
       Objects.equals(this.terminationPoint, that.terminationPoint) &&
       Objects.equals(this.nodeConfig, that.nodeConfig) &&
       Objects.equals(this.nodeState, that.nodeState) &&
       Objects.equals(this.nodeType, that.nodeType);
  }

}
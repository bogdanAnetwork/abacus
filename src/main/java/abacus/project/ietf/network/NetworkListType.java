package abacus.project.ietf.network;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.google.common.collect.ImmutableList;

import abacus.project.ietf.network.topology.LinkListType;

import java.util.List;

/**
 * Describes a network.
 * A network typically contains an inventory of nodes,
 * topological information (augmented through
 * network-topology model), as well as layering
 * information.
 */
@JsonInclude(Include.NON_NULL)
public class NetworkListType implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Serves as an augmentation target.
   * The network type is indicated through corresponding
   * presence containers augmented into this container.
   */
  private final NetworkTypes networkTypes;

  /**
   * Identifies a network.
   */
  private final NetworkId networkId;

  /**
   * Indicates whether the information concerning this
   * particular network is populated by the server
   * (server-provided true, the general case for network
   * information discovered from the server),
   * or whether it is configured by a client
   * (server-provided true, possible e.g. for
   * service overlays managed through a controller).
   * Clients should not attempt to make modifications
   * to network instances with server-provided set to
   * true; when they do, they need to be aware that
   * any modifications they make are subject to be
   * reverted by the server.
   * For servers that support NACM (Netconf Access Control
   * Model), data node rules should ideally prevent
   * write access by other clients to the network instance
   * when server-provided is set to true.
   */
  private final boolean serverProvided;

  /**
   * An underlay network, used to represent layered network
   * topologies.
   */
  private final List<SupportingNetworkListType> supportingNetwork;

  /**
   * The inventory of nodes of this network.
   */
  private final List<NodeListType> node;

  /**
   * A network link connects a local (source) node and
   * a remote (destination) node via a set of
   * the respective node's termination points.
   * It is possible to have several links between the same
   * source and destination nodes.  Likewise, a link could
   * potentially be re-homed between termination points.
   * Therefore, in order to ensure that we would always know
   * to distinguish between links, every link is identified by
   * a dedicated link identifier.  Note that a link models a
   * point-to-point link, not a multipoint link.
   */
  private final List<LinkListType> link;


  @JsonCreator
  public NetworkListType (
    @JsonProperty("network-types") NetworkTypes networkTypes,
    @JsonProperty("network-id") NetworkId networkId,
    @JsonProperty("server-provided") boolean serverProvided,
    @JsonProperty("supporting-network") List<SupportingNetworkListType> supportingNetwork,
    @JsonProperty("node") List<NodeListType> node,
    @JsonProperty("link") List<LinkListType> link){
    this.networkTypes = networkTypes;
    this.networkId = networkId;
    this.serverProvided = serverProvided;
    this.supportingNetwork = supportingNetwork != null ? ImmutableList.copyOf(supportingNetwork) : ImmutableList.<SupportingNetworkListType>of();
    this.node = node != null ? ImmutableList.copyOf(node) : ImmutableList.<NodeListType>of();
    this.link = link != null ? ImmutableList.copyOf(link) : ImmutableList.<LinkListType>of();
  }


  @JsonProperty("network-types")
  public NetworkTypes getNetworkTypes(){
    return this.networkTypes;
  }

  @JsonProperty("network-id")
  public NetworkId getNetworkId(){
    return this.networkId;
  }

  @JsonProperty("server-provided")
  public boolean getServerProvided(){
    return this.serverProvided;
  }

  @JsonProperty("supporting-network")
  public List<SupportingNetworkListType> getSupportingNetwork(){
    return this.supportingNetwork;
  }

  @JsonProperty("node")
  public List<NodeListType> getNode(){
    return this.node;
  }

  @JsonProperty("link")
  public List<LinkListType> getLink(){
    return this.link;
  }


  @Override
  public int hashCode() {
    return Objects.hash(networkTypes, networkId, serverProvided, supportingNetwork, node, link);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NetworkListType that = (NetworkListType) o;
    return Objects.equals(this.networkTypes, that.networkTypes) &&
       Objects.equals(this.networkId, that.networkId) &&
       Objects.equals(this.serverProvided, that.serverProvided) &&
       Objects.equals(this.supportingNetwork, that.supportingNetwork) &&
       Objects.equals(this.node, that.node) &&
       Objects.equals(this.link, that.link);
  }

}
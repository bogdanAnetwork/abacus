package abacus.project.ietf.flexiGrid.mediaChannel;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import abacus.project.ietf.network.NodeId;
import abacus.project.ietf.network.topology.LinkId;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A link channel is one of the concatenated elements of
 * the media channel.
 */
@JsonInclude(Include.NON_NULL)
public class LinkChannelAttributes implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Is used to determine the Nominal Central Frequency.
   * The set of nominal central frequencies can be built
   * using the following expression:
   *   f = 193.1 THz + n x 0.00625 THz,
   * where 193.1 THz is ITU-T ''anchor frequency'' for
   * transmission over the C band, n is a positive or
   * negative integer including 0.
   */
  private final int n;

  /**
   * Is used to determine the slot width. A slot
   * width is constrained to be M x SWG (that is,
   * M x 12.5 GHz), where M is an integer greater than
   * or equal to 1.
   */
  private final int m;

  /**
   * Source node of the link channel
   */
  private final NodeId sourceNode;

  /**
   * Source port of the link channel
   */
  private final int sourcePort;

  /**
   * Destination node of the link channel
   */
  private final NodeId destinationNode;

  /**
   * Destination port of the link channel
   */
  private final int destinationPort;

  /**
   * Link of the link channel
   */
  private final LinkId link;

  /**
   * Determines whether the link is bidirectional or
   * not
   */
  private final boolean bidirectional;


  @JsonCreator
  public LinkChannelAttributes (
    @JsonProperty("N") int n,
    @JsonProperty("M") int m,
    @JsonProperty("source-node") NodeId sourceNode,
    @JsonProperty("source-port") int sourcePort,
    @JsonProperty("destination-node") NodeId destinationNode,
    @JsonProperty("destination-port") int destinationPort,
    @JsonProperty("link") LinkId link,
    @JsonProperty("bidirectional") boolean bidirectional){
    this.n = n;
    this.m = m;
    this.sourceNode = sourceNode;
    this.sourcePort = sourcePort;
    this.destinationNode = destinationNode;
    this.destinationPort = destinationPort;
    this.link = link;
    this.bidirectional = bidirectional;
  }


  @JsonProperty("N")
  public int getN(){
    return this.n;
  }

  @JsonProperty("M")
  public int getM(){
    return this.m;
  }

  @JsonProperty("source-node")
  public NodeId getSourceNode(){
    return this.sourceNode;
  }

  @JsonProperty("source-port")
  public int getSourcePort(){
    return this.sourcePort;
  }

  @JsonProperty("destination-node")
  public NodeId getDestinationNode(){
    return this.destinationNode;
  }

  @JsonProperty("destination-port")
  public int getDestinationPort(){
    return this.destinationPort;
  }

  @JsonProperty("link")
  public LinkId getLink(){
    return this.link;
  }

  @JsonProperty("bidirectional")
  public boolean getBidirectional(){
    return this.bidirectional;
  }


  @Override
  public int hashCode() {
    return Objects.hash(n, m, sourceNode, sourcePort, destinationNode, destinationPort, link, bidirectional);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LinkChannelAttributes that = (LinkChannelAttributes) o;
    return Objects.equals(this.n, that.n) &&
       Objects.equals(this.m, that.m) &&
       Objects.equals(this.sourceNode, that.sourceNode) &&
       Objects.equals(this.sourcePort, that.sourcePort) &&
       Objects.equals(this.destinationNode, that.destinationNode) &&
       Objects.equals(this.destinationPort, that.destinationPort) &&
       Objects.equals(this.link, that.link) &&
       Objects.equals(this.bidirectional, that.bidirectional);
  }

}
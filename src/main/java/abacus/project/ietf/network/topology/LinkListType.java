package abacus.project.ietf.network.topology;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.google.common.collect.ImmutableList;

import abacus.project.ietf.flexiGrid.ted.LinkConfig;
import abacus.project.ietf.flexiGrid.ted.LinkState;
import abacus.project.ietf.network.topology.Destination;
import abacus.project.ietf.network.topology.LinkId;
import abacus.project.ietf.network.topology.Source;
import abacus.project.ietf.network.topology.SupportingLinkListType;

import java.util.List;

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
@JsonInclude(Include.NON_NULL)
public class LinkListType implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * This container holds the logical source of a particular
   * link.
   */
  private final Source source;

  /**
   * This container holds the logical destination of a
   * particular link.
   */
  private final Destination destination;

  /**
   * The identifier of a link in the topology.
   * A link is specific to a topology to which it belongs.
   */
  private final LinkId linkId;

  /**
   * Identifies the link, or links, that this link
   * is dependent on.
   */
  private final List<SupportingLinkListType> supportingLink;

  /**
   * Configuration of a flexi-grid link
   */
  private final LinkConfig linkConfig;

  /**
   * State of a flexi-grid link
   */
  private final LinkState linkState;


  @JsonCreator
  public LinkListType (
    @JsonProperty("source") Source source,
    @JsonProperty("destination") Destination destination,
    @JsonProperty("link-id") LinkId linkId,
    @JsonProperty("supporting-link") List<SupportingLinkListType> supportingLink,
    @JsonProperty("link-config") LinkConfig linkConfig,
    @JsonProperty("link-state") LinkState linkState){
    this.source = source;
    this.destination = destination;
    this.linkId = linkId;
    this.supportingLink = supportingLink != null ? ImmutableList.copyOf(supportingLink) : ImmutableList.<SupportingLinkListType>of();
    this.linkConfig = linkConfig;
    this.linkState = linkState;
  }


  @JsonProperty("source")
  public Source getSource(){
    return this.source;
  }

  @JsonProperty("destination")
  public Destination getDestination(){
    return this.destination;
  }

  @JsonProperty("link-id")
  public LinkId getLinkId(){
    return this.linkId;
  }

  @JsonProperty("supporting-link")
  public List<SupportingLinkListType> getSupportingLink(){
    return this.supportingLink;
  }

  @JsonProperty("link-config")
  public LinkConfig getLinkConfig(){
    return this.linkConfig;
  }

  @JsonProperty("link-state")
  public LinkState getLinkState(){
    return this.linkState;
  }


  @Override
  public int hashCode() {
    return Objects.hash(source, destination, linkId, supportingLink, linkConfig, linkState);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LinkListType that = (LinkListType) o;
    return Objects.equals(this.source, that.source) &&
       Objects.equals(this.destination, that.destination) &&
       Objects.equals(this.linkId, that.linkId) &&
       Objects.equals(this.supportingLink, that.supportingLink) &&
       Objects.equals(this.linkConfig, that.linkConfig) &&
       Objects.equals(this.linkState, that.linkState);
  }

}
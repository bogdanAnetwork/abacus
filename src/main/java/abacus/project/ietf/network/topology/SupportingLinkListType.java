package abacus.project.ietf.network.topology;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import abacus.project.ietf.network.NetworkId;
import abacus.project.ietf.network.topology.LinkId;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Identifies the link, or links, that this link
 * is dependent on.
 */
@JsonInclude(Include.NON_NULL)
public class SupportingLinkListType implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * This leaf identifies in which underlay topology
   * the supporting link is present.
   */
  private final NetworkId networkRef;

  /**
   * This leaf identifies a link which is a part
   * of this link's underlay. Reference loops in which
   * a link identifies itself as its underlay, either
   * directly or transitively, are not allowed.
   */
  private final LinkId linkRef;


  @JsonCreator
  public SupportingLinkListType (
    @JsonProperty("network-ref") NetworkId networkRef,
    @JsonProperty("link-ref") LinkId linkRef){
    this.networkRef = networkRef;
    this.linkRef = linkRef;
  }


  @JsonProperty("network-ref")
  public NetworkId getNetworkRef(){
    return this.networkRef;
  }

  @JsonProperty("link-ref")
  public LinkId getLinkRef(){
    return this.linkRef;
  }


  @Override
  public int hashCode() {
    return Objects.hash(networkRef, linkRef);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SupportingLinkListType that = (SupportingLinkListType) o;
    return Objects.equals(this.networkRef, that.networkRef) &&
       Objects.equals(this.linkRef, that.linkRef);
  }

}
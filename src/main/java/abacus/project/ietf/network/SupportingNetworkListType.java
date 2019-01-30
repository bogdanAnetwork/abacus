package abacus.project.ietf.network;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import abacus.project.ietf.network.NetworkId;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * An underlay network, used to represent layered network
 * topologies.
 */
@JsonInclude(Include.NON_NULL)
public class SupportingNetworkListType implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * References the underlay network.
   */
  private final NetworkId networkRef;


  @JsonCreator
  public SupportingNetworkListType (
    @JsonProperty("network-ref") NetworkId networkRef){
    this.networkRef = networkRef;
  }


  @JsonProperty("network-ref")
  public NetworkId getNetworkRef(){
    return this.networkRef;
  }


  @Override
  public int hashCode() {
    return Objects.hash(networkRef);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SupportingNetworkListType that = (SupportingNetworkListType) o;
    return Objects.equals(this.networkRef, that.networkRef);
  }

}
package abacus.project.ietf.network;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.google.common.collect.ImmutableList;

import abacus.project.ietf.network.NetworkListType;

import java.util.List;

/**
 * Serves as top-level container for a list of networks.
 */
@JsonInclude(Include.NON_NULL)
public class Networks implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Describes a network.
   * A network typically contains an inventory of nodes,
   * topological information (augmented through
   * network-topology model), as well as layering
   * information.
   */
  private final List<NetworkListType> network;
  //empty constructor as a cheap fix for XML to JSON conversion and deserialization
  public Networks(String x){this.network = null;}

  @JsonCreator
  public Networks (
    @JsonProperty("network") List<NetworkListType> network){
    this.network = network != null ? ImmutableList.copyOf(network) : ImmutableList.<NetworkListType>of();
  }


  @JsonProperty("network")
  public List<NetworkListType> getNetwork(){
    return this.network;
  }


  @Override
  public int hashCode() {
    return Objects.hash(network);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Networks that = (Networks) o;
    return Objects.equals(this.network, that.network);
  }

}
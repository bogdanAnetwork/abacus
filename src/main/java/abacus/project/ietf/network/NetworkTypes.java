package abacus.project.ietf.network;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Serves as an augmentation target.
 * The network type is indicated through corresponding
 * presence containers augmented into this container.
 */
@JsonInclude(Include.NON_NULL)
public class NetworkTypes implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * flexi-grid optical network
   */
  private final String flexiGridNetwork;


  @JsonCreator
  public NetworkTypes (
    @JsonProperty("flexi-grid-network") String flexiGridNetwork){
    this.flexiGridNetwork = flexiGridNetwork;
  }


  @JsonProperty("flexi-grid-network")
  public String getFlexiGridNetwork(){
    return this.flexiGridNetwork;
  }


  @Override
  public int hashCode() {
    return Objects.hash(flexiGridNetwork);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NetworkTypes that = (NetworkTypes) o;
    return Objects.equals(this.flexiGridNetwork, that.flexiGridNetwork);
  }

}
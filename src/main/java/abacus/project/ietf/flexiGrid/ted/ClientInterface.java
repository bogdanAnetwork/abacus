package abacus.project.ietf.flexiGrid.ted;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Container that defines a client
 * interface
 */
@JsonInclude(Include.NON_NULL)
public class ClientInterface implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Number as label for the interface
   */
  private final int label;


  @JsonCreator
  public ClientInterface (
    @JsonProperty("label") int label){
    this.label = label;
  }


  @JsonProperty("label")
  public int getLabel(){
    return this.label;
  }


  @Override
  public int hashCode() {
    return Objects.hash(label);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClientInterface that = (ClientInterface) o;
    return Objects.equals(this.label, that.label);
  }

}
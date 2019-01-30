package abacus.project.ietf.flexiGrid.ted;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * flexi-grid optical network
 */
@JsonInclude(Include.NON_NULL)
public class FlexiGridNetwork implements Serializable {

  private static final long serialVersionUID = 1L;


  @JsonCreator
  public FlexiGridNetwork (
    ){
  }



  @Override
  public int hashCode() {
    return Objects.hash();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    return true;
  }

}
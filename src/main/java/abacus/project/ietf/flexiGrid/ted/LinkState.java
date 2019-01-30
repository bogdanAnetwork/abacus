package abacus.project.ietf.flexiGrid.ted;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.google.common.collect.ImmutableList;
import java.util.List;

/**
 * State of a flexi-grid link
 */
@JsonInclude(Include.NON_NULL)
public class LinkState implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Array of bits that determines whether a spectral
   * slot is available or not.
   */
  private final List<Boolean> availableLabelFlexiGrid;


  @JsonCreator
  public LinkState (
    @JsonProperty("available-label-flexi-grid") List<Boolean> availableLabelFlexiGrid){
    this.availableLabelFlexiGrid = availableLabelFlexiGrid != null ? ImmutableList.copyOf(availableLabelFlexiGrid) : ImmutableList.of();
  }


  @JsonProperty("available-label-flexi-grid")
  public List<Boolean> getAvailableLabelFlexiGrid(){
    return this.availableLabelFlexiGrid;
  }


  @Override
  public int hashCode() {
    return Objects.hash(availableLabelFlexiGrid);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LinkState that = (LinkState) o;
    return Objects.equals(this.availableLabelFlexiGrid, that.availableLabelFlexiGrid);
  }

}
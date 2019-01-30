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
 * Configuration of a flexi-grid link
 */
@JsonInclude(Include.NON_NULL)
public class LinkConfig implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Array of bits that determines whether a spectral
   * slot is available or not.
   */
  private final List<Boolean> availableLabelFlexiGrid;
  /**
   * maximum number of channels available
   */
  private final int n_max;
  /**
   * default central frequency
   */
  private final double base_frequency;

  /**
   *
   * spacing between allowed nominal central frequencies
   * and it is set to 6.25 Ghz
   */
  private final double nominal_central_frequency_granularity;

  /**
   * minimum space between slot widths
   */
  private final double slot_width_granularity;


  @JsonCreator
  public LinkConfig (
    @JsonProperty("available-label-flexi-grid") List<Boolean> availableLabelFlexiGrid,
    @JsonProperty("N-max") int n_max,
    @JsonProperty("base-frequency") double base_frequency,
    @JsonProperty("nominal-central-frequency-granularity") double nominal_central_frequency_granularity,
    @JsonProperty("slot-width-granularity") double slot_width_granularity) {
    this.availableLabelFlexiGrid = availableLabelFlexiGrid != null ? ImmutableList.copyOf(availableLabelFlexiGrid) : ImmutableList.of();
    this.n_max = n_max;
    this.base_frequency = base_frequency;
    this.nominal_central_frequency_granularity = nominal_central_frequency_granularity;
    this.slot_width_granularity = slot_width_granularity;
  }


  @JsonProperty("available-label-flexi-grid")
  public List<Boolean> getAvailableLabelFlexiGrid(){
    return this.availableLabelFlexiGrid;
  }

  @JsonProperty("N-max")
  public int getNmax(){
    return this.n_max;
  }

  @JsonProperty("base-frequency")
  public double getBaseFrequency(){
    return this.base_frequency;
  }

  @JsonProperty("nominal-central-frequency-granularity")
  public double getNominal_central_frequency_granularity(){
    return this.nominal_central_frequency_granularity;
  }

  @JsonProperty("slot-width-granularity")
  public double getSlot_width_granularity(){
    return this.slot_width_granularity;
  }

  @Override
  public int hashCode() {
    return Objects.hash(availableLabelFlexiGrid, n_max, base_frequency, nominal_central_frequency_granularity, slot_width_granularity);
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LinkConfig that = (LinkConfig) o;
    return Objects.equals(this.availableLabelFlexiGrid, that.availableLabelFlexiGrid)&&
           Objects.equals(this.n_max, that.n_max)&&
           Objects.equals(this.base_frequency, that.base_frequency)&&
            Objects.equals(this.nominal_central_frequency_granularity, that.nominal_central_frequency_granularity)&&
            Objects.equals(this.slot_width_granularity, that.slot_width_granularity);
  }

}
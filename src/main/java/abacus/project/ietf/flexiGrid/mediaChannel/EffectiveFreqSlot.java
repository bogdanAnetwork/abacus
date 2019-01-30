package abacus.project.ietf.flexiGrid.mediaChannel;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The effective frequency slot is an attribute of
 * a media channel and, being a frequency slot, it is
 * described by its nominal central frequency and slot
 * width
 */
@JsonInclude(Include.NON_NULL)
public class EffectiveFreqSlot implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Is used to determine the Nominal Central
   * Frequency. The set of nominal central frequencies
   * can be built using the following expression:
   *   f = 193.1 THz + n x 0.00625 THz,
   * where 193.1 THz is ITU-T ''anchor frequency'' for
   * transmission over the C band, n is a positive or
   * negative integer including 0.
   */
  private final int n;

  /**
   * Is used to determine the slot width. A slot width
   * is constrained to be M x SWG (that is, M x 12.5 GHz),
   * where M is an integer greater than or equal to 1.
   */
  private final int m;


  @JsonCreator
  public EffectiveFreqSlot (
    @JsonProperty("N") int n,
    @JsonProperty("M") int m){
    this.n = n;
    this.m = m;
  }


  @JsonProperty("N")
  public int getN(){
    return this.n;
  }

  @JsonProperty("M")
  public int getM(){
    return this.m;
  }


  @Override
  public int hashCode() {
    return Objects.hash(n, m);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EffectiveFreqSlot that = (EffectiveFreqSlot) o;
    return Objects.equals(this.n, that.n) &&
       Objects.equals(this.m, that.m);
  }

}
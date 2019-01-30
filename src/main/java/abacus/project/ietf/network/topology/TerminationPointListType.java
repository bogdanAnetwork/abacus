package abacus.project.ietf.network.topology;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.google.common.collect.ImmutableList;

import abacus.project.ietf.network.topology.SupportingTerminationPointListType;
import abacus.project.ietf.network.topology.TpId;

import java.util.List;

/**
 * A termination point can terminate a link.
 * Depending on the type of topology, a termination point
 * could, for example, refer to a port or an interface.
 */
@JsonInclude(Include.NON_NULL)
public class TerminationPointListType implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Termination point identifier.
   */
  private final TpId tpId;

  /**
   * This list identifies any termination points that
   * the termination point is dependent on, or maps onto.
   * Those termination points will themselves be contained
   * in a supporting node.
   * This dependency information can be inferred from
   * the dependencies between links.  For this reason,
   * this item is not separately configurable.  Hence no
   * corresponding constraint needs to be articulated.
   * The corresponding information is simply provided by the
   * implementing system.
   */
  private final List<SupportingTerminationPointListType> supportingTerminationPoint;
  //dummy constructor as a solution to XML to JSON

  @JsonCreator
  public TerminationPointListType (
    @JsonProperty("tp-id") TpId tpId,
    @JsonProperty("supporting-termination-point") List<SupportingTerminationPointListType> supportingTerminationPoint){
    this.tpId = tpId;
    this.supportingTerminationPoint = supportingTerminationPoint != null ? ImmutableList.copyOf(supportingTerminationPoint) : ImmutableList.<SupportingTerminationPointListType>of();
  }


  @JsonProperty("tp-id")
  public TpId getTpId(){
    return this.tpId;
  }

  @JsonProperty("supporting-termination-point")
  public List<SupportingTerminationPointListType> getSupportingTerminationPoint(){
    return this.supportingTerminationPoint;
  }


  @Override
  public int hashCode() {
    return Objects.hash(tpId, supportingTerminationPoint);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TerminationPointListType that = (TerminationPointListType) o;
    return Objects.equals(this.tpId, that.tpId) &&
       Objects.equals(this.supportingTerminationPoint, that.supportingTerminationPoint);
  }

}
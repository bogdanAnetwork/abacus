package abacus.project.ietf.flexiGrid.ted;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.google.common.collect.ImmutableList;

import abacus.project.ietf.flexiGrid.ted.ConnectionsListType;
import abacus.project.ietf.flexiGrid.ted.InterfacesListType;

import java.util.List;

/**
 * Configuration of a flexi-grid node
 */
@JsonInclude(Include.NON_NULL)
public class NodeConfig implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * List of interfaces contained in the node
   */
  private final List<InterfacesListType> interfaces;

  /**
   * List of connections between input and
   * output ports
   */
  private final List<ConnectionsListType> connections;


  @JsonCreator
  public NodeConfig (
    @JsonProperty("interfaces") List<InterfacesListType> interfaces,
    @JsonProperty("connections") List<ConnectionsListType> connections){
    this.interfaces = interfaces != null ? ImmutableList.copyOf(interfaces) : ImmutableList.<InterfacesListType>of();
    this.connections = connections != null ? ImmutableList.copyOf(connections) : ImmutableList.<ConnectionsListType>of();
  }


  @JsonProperty("interfaces")
  public List<InterfacesListType> getInterfaces(){
    return this.interfaces;
  }

  @JsonProperty("connections")
  public List<ConnectionsListType> getConnections(){
    return this.connections;
  }


  @Override
  public int hashCode() {
    return Objects.hash(interfaces, connections);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NodeConfig that = (NodeConfig) o;
    return Objects.equals(this.interfaces, that.interfaces) &&
       Objects.equals(this.connections, that.connections);
  }

}
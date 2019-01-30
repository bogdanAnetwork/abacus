package abacus.project.ietf.flexiGrid.ted;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.google.common.collect.ImmutableList;

import abacus.project.ietf.flexiGrid.ted.ClientInterface;
import abacus.project.ietf.flexiGrid.ted.Fec;
import abacus.project.ietf.flexiGrid.ted.InterfaceType;
import abacus.project.ietf.flexiGrid.ted.Modulation;
import abacus.project.ietf.flexiGrid.ted.NetworkInterface;

import java.util.List;

/**
 * List of interfaces contained in the node
 */
@JsonInclude(Include.NON_NULL)
public class InterfacesListType implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Interface name
   */
  private final String name;

  /**
   * Number of the port used by the interface
   */
  private final int portNumber;

  /**
   * Determines if the port is an input port
   */
  private final boolean inputPort;

  /**
   * Determines if the port is an output port
   */
  private final boolean outputPort;

  /**
   * Description of the interface
   */
  private final String description;

  /**
   * Determines the type of the interface
   */
  private final InterfaceType type;

  /**
   * Container that defines a client
   * interface
   */
  private final ClientInterface clientInterface;

  /**
   * Container that defines a network interface
   */
  private final NetworkInterface networkInterface;

  /**
   * List determining all the available modulations
   */
  private final List<Modulation> availableModulation;

  /**
   * Modulation type of the wave
   */
  private final Modulation modulationType;

  /**
   * List determining all the available FEC
   */
  private final List<Fec> availableFEC;

  /**
   * Determines whether the FEC is enabled or not
   */
  private final boolean fECEnabled;

  /**
   * FEC type of the network lane
   */
  private final Fec fECType;


  @JsonCreator
  public InterfacesListType (
    @JsonProperty("name") String name,
    @JsonProperty("port-number") int portNumber,
    @JsonProperty("input-port") boolean inputPort,
    @JsonProperty("output-port") boolean outputPort,
    @JsonProperty("description") String description,
    @JsonProperty("type") InterfaceType type,
    @JsonProperty("client-interface") ClientInterface clientInterface,
    @JsonProperty("network-interface") NetworkInterface networkInterface,
    @JsonProperty("available-modulation") List<Modulation> availableModulation,
    @JsonProperty("modulation-type") Modulation modulationType,
    @JsonProperty("available-FEC") List<Fec> availableFEC,
    @JsonProperty("FEC-enabled") boolean fECEnabled,
    @JsonProperty("FEC-type") Fec fECType){
    this.name = name;
    this.portNumber = portNumber;
    this.inputPort = inputPort;
    this.outputPort = outputPort;
    this.description = description;
    this.type = type;
    this.clientInterface = clientInterface;
    this.networkInterface = networkInterface;
    this.availableModulation = availableModulation != null ? ImmutableList.copyOf(availableModulation) : ImmutableList.of();
    this.modulationType = modulationType;
    this.availableFEC = availableFEC != null ? ImmutableList.copyOf(availableFEC) : ImmutableList.of();
    this.fECEnabled = fECEnabled;
    this.fECType = fECType;
  }


  @JsonProperty("name")
  public String getName(){
    return this.name;
  }

  @JsonProperty("port-number")
  public int getPortNumber(){
    return this.portNumber;
  }

  @JsonProperty("input-port")
  public boolean getInputPort(){
    return this.inputPort;
  }

  @JsonProperty("output-port")
  public boolean getOutputPort(){
    return this.outputPort;
  }

  @JsonProperty("description")
  public String getDescription(){
    return this.description;
  }

  @JsonProperty("type")
  public InterfaceType getType(){
    return this.type;
  }

  @JsonProperty("client-interface")
  public ClientInterface getClientInterface(){
    return this.clientInterface;
  }

  @JsonProperty("network-interface")
  public NetworkInterface getNetworkInterface(){
    return this.networkInterface;
  }

  @JsonProperty("available-modulation")
  public List<Modulation> getAvailableModulation(){
    return this.availableModulation;
  }

  @JsonProperty("modulation-type")
  public Modulation getModulationType(){
    return this.modulationType;
  }

  @JsonProperty("available-FEC")
  public List<Fec> getAvailableFEC(){
    return this.availableFEC;
  }

  @JsonProperty("FEC-enabled")
  public boolean getFECEnabled(){
    return this.fECEnabled;
  }

  @JsonProperty("FEC-type")
  public Fec getFECType(){
    return this.fECType;
  }


  @Override
  public int hashCode() {
    return Objects.hash(name, portNumber, inputPort, outputPort, description, type, clientInterface, networkInterface, availableModulation, modulationType, availableFEC, fECEnabled, fECType);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InterfacesListType that = (InterfacesListType) o;
    return Objects.equals(this.name, that.name) &&
       Objects.equals(this.portNumber, that.portNumber) &&
       Objects.equals(this.inputPort, that.inputPort) &&
       Objects.equals(this.outputPort, that.outputPort) &&
       Objects.equals(this.description, that.description) &&
       Objects.equals(this.type, that.type) &&
       Objects.equals(this.clientInterface, that.clientInterface) &&
       Objects.equals(this.networkInterface, that.networkInterface) &&
       Objects.equals(this.availableModulation, that.availableModulation) &&
       Objects.equals(this.modulationType, that.modulationType) &&
       Objects.equals(this.availableFEC, that.availableFEC) &&
       Objects.equals(this.fECEnabled, that.fECEnabled) &&
       Objects.equals(this.fECType, that.fECType);
  }

}
package abacus.project.ietf.network;

import java.io.Serializable;

import abacus.project.ietf.inet.types.Uri;

/**
 * Identifier for a network.  The precise structure of the
 * network-id will be up to an implementation.
 * The identifier SHOULD be chosen such that the same network
 * will always be identified through the same identifier,
 * even if the model is instantiated in separate datastores.
 * An implementation MAY choose to capture semantics in the
 * identifier, for example to indicate the type of network.
 */
public class NetworkId extends Uri implements Serializable {

  private static final long serialVersionUID = 1L;

  public NetworkId (String uri){
    super(uri);
  }

  public NetworkId (){
    super("");
  }

}
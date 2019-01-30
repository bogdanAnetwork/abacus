package abacus.project.ietf.network.topology;

import java.io.Serializable;

import abacus.project.ietf.inet.types.Uri;

/**
 * An identifier for termination points (TPs) on a node.
 * The precise structure of the tp-id
 * will be up to the implementation.
 * The identifier SHOULD be chosen such that the same termination
 * point in a real network topology will always be identified
 * through the same identifier, even if the model is instantiated
 * in separate datastores. An implementation MAY choose to
 * capture semantics in the identifier, for example to indicate
 * the type of termination point and/or the type of node
 * that contains the termination point.
 */
public class TpId extends Uri implements Serializable {

  private static final long serialVersionUID = 1L;

  public TpId (String uri){
    super(uri);
  }
  //dummy constructor
  public TpId (){
    super("");
  }
}
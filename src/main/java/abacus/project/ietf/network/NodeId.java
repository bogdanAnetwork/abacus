package abacus.project.ietf.network;

import java.io.Serializable;

import abacus.project.ietf.inet.types.Uri;

/**
 * Identifier for a node.  The precise structure of the node-id
 * will be up to the implementation.  Some implementations MAY
 * for example, pick a uri that includes the network-id as
 * part of the path. The identifier SHOULD be chosen such that
 * the same node in a real network topology will always be
 * identified through the same identifier, even if the model is
 * instantiated in separate datastores. An implementation MAY
 * choose to capture semantics in the identifier, for example to
 * indicate the type of node.
 */
public class NodeId extends Uri implements Serializable {

  private static final long serialVersionUID = 1L;

  public NodeId (String uri){
    super(uri);
  }
  public NodeId (){
    super("");
  }

}
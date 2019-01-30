package abacus.project.ietf.network.topology;

import java.io.Serializable;

import abacus.project.ietf.inet.types.Uri;

/**
 * An identifier for a link in a topology.
 * The precise structure of the link-id
 * will be up to the implementation.
 * The identifier SHOULD be chosen such that the same link in a
 * real network topology will always be identified through the
 * same identifier, even if the model is instantiated in
 *     separate datastores. An implementation MAY choose to capture
 * semantics in the identifier, for example to indicate the type
 * of link and/or the type of topology that the link is a part
 * of.
 */
public class LinkId extends Uri implements Serializable {

  private static final long serialVersionUID = 1L;

  public LinkId (String uri){
    super(uri);
  }

}
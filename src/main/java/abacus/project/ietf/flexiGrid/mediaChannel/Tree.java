package abacus.project.ietf.flexiGrid.mediaChannel;

import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;

public class Tree extends AllDirectives {

  public Route getTreeRoute() {
  return get(()->  route( 
  pathPrefix("media-channels",  () -> route(

  pathPrefix("media-channel",  () -> route(

  pathPrefix("channel-id",  () -> complete(StatusCodes.OK)),
  pathPrefix("media-channel-source",  () -> route(

  pathPrefix("source-node",  () -> complete(StatusCodes.OK)),
  pathPrefix("source-interface",  () -> complete(StatusCodes.OK)),  complete(StatusCodes.OK))),
  pathPrefix("media-channel-destination",  () -> route(

  pathPrefix("destination-node",  () -> complete(StatusCodes.OK)),
  pathPrefix("destination-interface",  () -> complete(StatusCodes.OK)),  complete(StatusCodes.OK))),
  pathPrefix("effective-freq-slot",  () -> route(

  pathPrefix("N",  () -> complete(StatusCodes.OK)),
  pathPrefix("M",  () -> complete(StatusCodes.OK)),  complete(StatusCodes.OK))),
  pathPrefix("link-channel",  () -> route(

  pathPrefix("link-id",  () -> complete(StatusCodes.OK)),
  pathPrefix("N",  () -> complete(StatusCodes.OK)),
  pathPrefix("M",  () -> complete(StatusCodes.OK)),
  pathPrefix("source-node",  () -> complete(StatusCodes.OK)),
  pathPrefix("source-port",  () -> complete(StatusCodes.OK)),
  pathPrefix("destination-node",  () -> complete(StatusCodes.OK)),
  pathPrefix("destination-port",  () -> complete(StatusCodes.OK)),
  pathPrefix("link",  () -> complete(StatusCodes.OK)),
  pathPrefix("bidirectional",  () -> complete(StatusCodes.OK)),  complete(StatusCodes.OK))),  complete(StatusCodes.OK))),  complete(StatusCodes.OK))),
  pathPrefix("light-path-channels",  () -> route(

  pathPrefix("light-path-channel",  () -> route(

  pathPrefix("lp-channel-id",  () -> complete(StatusCodes.OK)),
  pathPrefix("light-path-source",  () -> route(

  pathPrefix("source-node",  () -> complete(StatusCodes.OK)),
  pathPrefix("source-interface",  () -> complete(StatusCodes.OK)),  complete(StatusCodes.OK))),
  pathPrefix("light-path-destination",  () -> route(

  pathPrefix("destination-node",  () -> complete(StatusCodes.OK)),
  pathPrefix("destination-interface",  () -> complete(StatusCodes.OK)),  complete(StatusCodes.OK))),
  pathPrefix("effective-freq-slot",  () -> route(

  pathPrefix("N",  () -> complete(StatusCodes.OK)),
  pathPrefix("M",  () -> complete(StatusCodes.OK)),  complete(StatusCodes.OK))),
  pathPrefix("link-channel",  () -> route(

  pathPrefix("link-id",  () -> complete(StatusCodes.OK)),
  pathPrefix("N",  () -> complete(StatusCodes.OK)),
  pathPrefix("M",  () -> complete(StatusCodes.OK)),
  pathPrefix("source-node",  () -> complete(StatusCodes.OK)),
  pathPrefix("source-port",  () -> complete(StatusCodes.OK)),
  pathPrefix("destination-node",  () -> complete(StatusCodes.OK)),
  pathPrefix("destination-port",  () -> complete(StatusCodes.OK)),
  pathPrefix("link",  () -> complete(StatusCodes.OK)),
  pathPrefix("bidirectional",  () -> complete(StatusCodes.OK)),  complete(StatusCodes.OK))),  complete(StatusCodes.OK))),  complete(StatusCodes.OK))))
  );
  }

}
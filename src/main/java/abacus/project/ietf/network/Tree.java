package abacus.project.ietf.network;

import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;

public class Tree extends AllDirectives {

  public Route getTreeRoute() {
  return get(()-> 
  pathPrefix("networks",  () -> route(

  pathPrefix("network",  () -> route(

  pathPrefix("network-types",  () -> route(

  pathPrefix("flexi-grid-network",  () -> complete(StatusCodes.OK)),  complete(StatusCodes.OK))),
  pathPrefix("network-id",  () -> complete(StatusCodes.OK)),
  pathPrefix("server-provided",  () -> complete(StatusCodes.OK)),
  pathPrefix("supporting-network",  () -> route(

  pathPrefix("network-ref",  () -> complete(StatusCodes.OK)),  complete(StatusCodes.OK))),
  pathPrefix("node",  () -> route(

  pathPrefix("node-id",  () -> complete(StatusCodes.OK)),
  pathPrefix("supporting-node",  () -> route(

  pathPrefix("network-ref",  () -> complete(StatusCodes.OK)),
  pathPrefix("node-ref",  () -> complete(StatusCodes.OK)),  complete(StatusCodes.OK))),
  pathPrefix("termination-point",  () -> route(

  pathPrefix("tp-id",  () -> complete(StatusCodes.OK)),
  pathPrefix("supporting-termination-point",  () -> route(

  pathPrefix("network-ref",  () -> complete(StatusCodes.OK)),
  pathPrefix("node-ref",  () -> complete(StatusCodes.OK)),
  pathPrefix("tp-ref",  () -> complete(StatusCodes.OK)),  complete(StatusCodes.OK))),  complete(StatusCodes.OK))),
  pathPrefix("node-config",  () -> route(

  pathPrefix("interfaces",  () -> route(

  pathPrefix("name",  () -> complete(StatusCodes.OK)),
  pathPrefix("port-number",  () -> complete(StatusCodes.OK)),
  pathPrefix("input-port",  () -> complete(StatusCodes.OK)),
  pathPrefix("output-port",  () -> complete(StatusCodes.OK)),
  pathPrefix("description",  () -> complete(StatusCodes.OK)),
  pathPrefix("type",  () -> complete(StatusCodes.OK)),
  pathPrefix("client-interface",  () -> route(

  pathPrefix("label",  () -> complete(StatusCodes.OK)),  complete(StatusCodes.OK))),
  pathPrefix("network-interface",  () -> route(

  pathPrefix("label",  () -> complete(StatusCodes.OK)),  complete(StatusCodes.OK))),
  pathPrefix("available-modulation",  () -> complete(StatusCodes.OK)),
  pathPrefix("modulation-type",  () -> complete(StatusCodes.OK)),
  pathPrefix("available-FEC",  () -> complete(StatusCodes.OK)),
  pathPrefix("FEC-enabled",  () -> complete(StatusCodes.OK)),
  pathPrefix("FEC-type",  () -> complete(StatusCodes.OK)),  complete(StatusCodes.OK))),
  pathPrefix("connections",  () -> route(

  pathPrefix("input-port-id",  () -> complete(StatusCodes.OK)),
  pathPrefix("output-port-id",  () -> complete(StatusCodes.OK)),  complete(StatusCodes.OK))),  complete(StatusCodes.OK))),
  pathPrefix("node-state",  () -> route(

  pathPrefix("interfaces",  () -> route(

  pathPrefix("name",  () -> complete(StatusCodes.OK)),
  pathPrefix("port-number",  () -> complete(StatusCodes.OK)),
  pathPrefix("input-port",  () -> complete(StatusCodes.OK)),
  pathPrefix("output-port",  () -> complete(StatusCodes.OK)),
  pathPrefix("description",  () -> complete(StatusCodes.OK)),
  pathPrefix("type",  () -> complete(StatusCodes.OK)),
  pathPrefix("client-interface",  () -> route(

  pathPrefix("label",  () -> complete(StatusCodes.OK)),  complete(StatusCodes.OK))),
  pathPrefix("network-interface",  () -> route(

  pathPrefix("label",  () -> complete(StatusCodes.OK)),  complete(StatusCodes.OK))),  complete(StatusCodes.OK))),
  pathPrefix("connections",  () -> route(

  pathPrefix("input-port-id",  () -> complete(StatusCodes.OK)),
  pathPrefix("output-port-id",  () -> complete(StatusCodes.OK)),  complete(StatusCodes.OK))),  complete(StatusCodes.OK))),
  pathPrefix("node-type",  () -> complete(StatusCodes.OK)),  complete(StatusCodes.OK))),
  pathPrefix("link",  () -> route(

  pathPrefix("source",  () -> route(

  pathPrefix("source-node",  () -> complete(StatusCodes.OK)),
  pathPrefix("source-tp",  () -> complete(StatusCodes.OK)),  complete(StatusCodes.OK))),
  pathPrefix("destination",  () -> route(

  pathPrefix("dest-node",  () -> complete(StatusCodes.OK)),
  pathPrefix("dest-tp",  () -> complete(StatusCodes.OK)),  complete(StatusCodes.OK))),
  pathPrefix("link-id",  () -> complete(StatusCodes.OK)),
  pathPrefix("supporting-link",  () -> route(

  pathPrefix("network-ref",  () -> complete(StatusCodes.OK)),
  pathPrefix("link-ref",  () -> complete(StatusCodes.OK)),  complete(StatusCodes.OK))),
  pathPrefix("link-config",  () -> route(

  pathPrefix("available-label-flexi-grid",  () -> complete(StatusCodes.OK)),
  pathPrefix("N-max",  () -> complete(StatusCodes.OK)),
  pathPrefix("base-frequency",  () -> complete(StatusCodes.OK)),
  pathPrefix("nominal-central-frequency-granularity",  () -> complete(StatusCodes.OK)),
  pathPrefix("slot-width-granularity",  () -> complete(StatusCodes.OK)),  complete(StatusCodes.OK))),
  pathPrefix("link-state",  () -> route(

  pathPrefix("available-label-flexi-grid",  () -> complete(StatusCodes.OK)),
  pathPrefix("N-max",  () -> complete(StatusCodes.OK)),
  pathPrefix("base-frequency",  () -> complete(StatusCodes.OK)),
  pathPrefix("nominal-central-frequency-granularity",  () -> complete(StatusCodes.OK)),
  pathPrefix("slot-width-granularity",  () -> complete(StatusCodes.OK)),  complete(StatusCodes.OK))),  complete(StatusCodes.OK))),  complete(StatusCodes.OK))),  complete(StatusCodes.OK)))
  );
  }

}
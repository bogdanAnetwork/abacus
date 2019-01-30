package docs.io.japi;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.LoggingBus;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.ConfigFactory;

import abacus.project.modules.NetController;



public class App {
	  // logger for the startup procedure
	  private static final Log log = LogFactory.getLog(App.class);
	  
	  // jackson object mapper for mapping json to java objects
	  public static final ObjectMapper mapper = new ObjectMapper();
	  // asynchronous log bus that is used for non actor classes
	  private static LoggingBus logBus;
	  
	  public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException, TimeoutException, InterruptedException{
		    log.info("Starting the flexi-grid controller app");
		        ActorSystem system = ActorSystem.create("flexi-grid-controller", ConfigFactory.load("application"));
		        logBus = system.eventStream();
		        final ActorRef netDriver =
                        system.actorOf(NetController.props("/ietfNetworkConfig.json"), "netDriver");
		    

	  }
}


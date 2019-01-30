package docs.io.japi;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.event.LoggingBus;
import scala.concurrent.duration.Duration;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.ConfigFactory;

import abacus.project.actorMsgs.MsgType;
import abacus.project.actorMsgs.NetMsg;
import abacus.project.actorMsgs.NetMsg.target;
import abacus.project.ietf.extensions.MediaChannelWrapper;
import abacus.project.modules.NetController;
/**
 * 
 * @author BMA
 *
 */
public class MC_Config_test {
        //nr of nodes in the SIM to test
       static int x = 32;
       static int rounds =150;
      // logger for the startup procedure
      private static final Log log = LogFactory.getLog(App.class);
      
      // jackson object mapper for mapping json to java objects
      public static final ObjectMapper mapper = new ObjectMapper();
      // asynchronous log bus that is used for non actor classes
      private static LoggingBus logBus;
      
      public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException, TimeoutException, InterruptedException{
            log.info("Starting the flexi-grid app");
            

            ActorSystem system = ActorSystem.create("flexi-grid", ConfigFactory.load("application"));
            logBus = system.eventStream();
            final ActorRef netDriver =
                    system.actorOf(NetController.props("/ietf_NodesConfig.json"), "netDriver");
            Thread.sleep(6000);
        
             /**
             * TESTING THE MEDIA CHANNEL START  
             */
            for (int i=0; i<rounds;i++){
                final Inbox inbox = Inbox.create(system);
                List<MediaChannelWrapper> mcList = new ArrayList<MediaChannelWrapper>();
                JsonNode mcs = mapper.readValue(App.class.getResource("/media-channel64.json"), JsonNode.class);
                for (JsonNode mc : mcs.get("media-channels").get("media-channel")){
                  inbox.send(netDriver, new NetMsg(mc.toString(), target.MEDIA_CHANNEL, MsgType.WR));
                  NetMsg recMC = (NetMsg) inbox.receive(Duration.create(200, TimeUnit.SECONDS));
                }
            }

      }
}


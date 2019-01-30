package abacus.project.modules;

import java.net.InetSocketAddress;
import java.util.List;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.Props;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import abacus.project.acc400Dev.intern.Parameters;
import abacus.project.acc400Dev.intern.Registers;
import abacus.project.acc400Dev.intern.Utils;
import abacus.project.actorMsgs.EbSubCmd;
import abacus.project.actorMsgs.MsgType;
import abacus.project.actorMsgs.TCPMsg;
import akka.io.Tcp;
import akka.io.Tcp.CommandFailed;
import akka.io.Tcp.Connected;
import akka.io.Tcp.ConnectionClosed;
import akka.io.Tcp.Received;
import akka.io.TcpMessage;
import akka.japi.pf.ReceiveBuilder;
import akka.util.ByteString;
import scala.PartialFunction;
import scala.concurrent.duration.FiniteDuration;
import scala.runtime.BoxedUnit;


import java.util.LinkedList;
import java.util.Deque;


/**
 * This custom actor class represents a single 400G flexible optical transponder
 * 
 * @author BMA
 *
 */
public class Acc400TCPInterface extends AbstractActor {
	// external message
	public final static String GET_PORTS = "Get ports.";
	// internal message
	private static final String RETRY_CONNECTION = "retry";
	private Cancellable retryScheduler;
	private int rdy_cnt=0;
	private final boolean ok=true;

	//command types
	private static final String MDIO_A = "0006";
	private static final String MDIO_R = "0009";
	private static final String MDIO_W = "0007";
	// message sections
	//SECTION 1 - Syncro
	private static final ByteString PREFIX = Utils.HextoBS("76303032");
	//SECTION 2 - 
	private static final ByteString MSA_MDIO = Utils.HextoBS("0005");
	private static final ByteString BD_CNT = Utils.HextoBS("0200");
	//SECTION 3 - commands
	private static final ByteString MDIO_ADDR = Utils.HextoBS(MDIO_A);
	private static final ByteString MDIO_READ = Utils.HextoBS(MDIO_R);
	private static final ByteString MDIO_WRITE = Utils.HextoBS(MDIO_W);

	//SECTION 4 - not assigned 
	private static final ByteString NA = Utils
			.HextoBS("000000000000000000000000000000");
	//SECTION 5 - length
	private static final ByteString LEN_ST = Utils
			.HextoBS("04");	
	private static final ByteString LEN_ZR= Utils
			.HextoBS("00");	
	//SECTION 6 - RegEmun
	//SECTION 7 - data
	private static final ByteString PADDING = Utils.HextoBS("0000");
	//write-ready data and mask
	private static final ByteString rdy = Utils.HextoBS("8000");
	private static final ByteString rdy_msk = Utils.HextoBS("8000");
	//write not ready data
	private static final ByteString nrdy = Utils.HextoBS("0000");
	// Ready-for-write register address
	private static final ByteString WR_RDY_REG = Registers.WR_RDY.getRegAddr();

	// logger
	private final Logger log = LoggerFactory.getLogger(Acc400TCPInterface.class);
	// TCP socket address; default 34111 port (hard-coded)
	private final InetSocketAddress remote;
	// reference to the TCP actor
	private ActorRef tcp = Tcp.get(getContext().system()).manager();
	// the connection to the managing actor of the transceiver module
	private ActorRef DeviceManager;
	// internal states
	private PartialFunction<Object, BoxedUnit> idle;
	private PartialFunction<Object, BoxedUnit> reading;
	private PartialFunction<Object, BoxedUnit> writing;
	private PartialFunction<Object, BoxedUnit> boardControl;
	// message queue implementation; Queuing the incoming messages from the
	// manager before making any write requests
	private Deque<TCPMsg> msgQueue = new LinkedList<TCPMsg>();

	/********************
	 * PROPERTIES START *
	 ********************/

	/**
	 * Create the properties through this method for better type safety.
	 * 
	 * @param remote
	 *            the TCP listening port on the transceiver
	 * @param DeviceManager
	 *            reference to the transceiver manager actor
	 * @return the properties
	 */

	public static Props props(InetSocketAddress remote, ActorRef DeviceManager) {
		return Props.create(Acc400TCPInterface.class, () -> new Acc400TCPInterface(
				remote, DeviceManager));
	}

	/******************
	 * PROPERTIES END *
	 ******************/

	/**********************
	 * CONSTRUCTORS START *
	 **********************/

	/**
	 * Constructor for TcpInterface
	 * 
	 * @param remote
	 *            the TCP listening port on the transceiver
	 * @param DeviceManager
	 *            reference to the transceiver manager actor
	 */
	public Acc400TCPInterface(InetSocketAddress remote, ActorRef DeviceManager) {
		this.remote = remote;
		this.DeviceManager = DeviceManager;
		// trigger connection setup through a scheduler
		startScheduler();
		// ask the tcp actor to start the connection to the transceiver
		tcp.tell(TcpMessage.connect(remote), self());
		// RECEIVE START
		receive(ReceiveBuilder
				.match(CommandFailed.class, msg -> {
					log.debug("{} Connection Failed: {}", self().path().name(), msg.toString());
				})
				.match(Connected.class,
						msg -> {
							retryScheduler.cancel();
							log.debug(
									"Connection established successfully: {}",
									msg.toString());
							// send connected message to the Device manager
							DeviceManager.tell(msg, self());
							tcp = sender();
							tcp.tell(TcpMessage.register(self()), self());
							log.trace("Switching from connected to idle {}", self().path().name());
							getContext().become(idle, false);
						}).build());
		// RECEIVE END
		// IDLE STATE START
		idle = ReceiveBuilder
				.match(CommandFailed.class, msg -> {
					log.debug("Command failed: {}", msg.toString());
				})
				.match(TCPMsg.class, msg -> {
					// Queue the message received from the manager
						msgQueue.addLast(msg);
					// check the type of message for the next one in the queue	
						//TODO:make function for following steps, migh need to reuse
						TCPMsg qmsg = msgQueue.getFirst();
						switch(qmsg.getMType())
						{
						case R:
							sendTcp(MSA_MDIO, MDIO_ADDR, LEN_ST, qmsg.getRegisterAdr(), PADDING);
							log.trace("Switching from idle state to reading {}", self().path().name());
							getContext().become(reading);
							break;
						case WR:
							sendTcp(MSA_MDIO, MDIO_ADDR, LEN_ST, WR_RDY_REG, PADDING);
							log.trace("Switching from idle state to writing {}", self().path().name());
							getContext().become(writing);
							break;
						case B_CMD:
							if (qmsg.getData() != ByteString.empty())
								sendTcp(BD_CNT, qmsg.getSubCmdAdr(), LEN_ST, ByteString.empty(), qmsg.getData());
							else 
								sendTcp(BD_CNT, qmsg.getSubCmdAdr(), LEN_ZR, ByteString.empty(), qmsg.getData()); 
							log.trace("Switching from idle state to boardcontrol {}", self().path().name());
							getContext().become(boardControl);
							break;
					  }
					})
				//match on the received message from self to pop the next message from the queue
				.match(String.class, msg ->{
					if (msg.equals("next message")){
						TCPMsg qmsg = msgQueue.getFirst();
						//same  conditions apply as 
						switch(qmsg.getMType()){
						case R:
							sendTcp(MSA_MDIO, MDIO_ADDR, LEN_ST, qmsg.getRegisterAdr(), PADDING);
							log.trace("Switching from idle state to reading {}", self().path().name());
							getContext().become(reading);
							break;
						case WR:
							sendTcp(MSA_MDIO, MDIO_ADDR,LEN_ST, WR_RDY_REG, PADDING);
							log.trace("Switching from idle state to writing {}", self().path().name());
							getContext().become(writing);
							break;
						case B_CMD:
							if (qmsg.getData() != ByteString.empty())
								sendTcp(BD_CNT, qmsg.getSubCmdAdr(), LEN_ST, ByteString.empty(), qmsg.getData());
							else 
								sendTcp(BD_CNT, qmsg.getSubCmdAdr(), LEN_ZR, ByteString.empty(), qmsg.getData());
							log.trace("Switching from idle state to boardcontrol {}", self().path().name());
							getContext().become(boardControl);
							break;
						
					  }
					}
					//TODO: you can implement other types of self returned messages
						
				})
				.match(ConnectionClosed.class, msg -> {
					log.debug("Connection closed: {}", msg.toString());
					getContext().unbecome();
				}).build();
		// IDLE STATE END
		//WRITING STATE START
		reading = ReceiveBuilder
				//received message from device manager
				.match(TCPMsg.class, msg -> {
					msgQueue.addLast(msg);
				})
				//received message from device
				.match(Received.class, msg -> {
					log.trace("{}: {}", self().path().name(), Utils.BStoHex(msg.data()));
					// extract the MDIO, register and data information
					ByteString mdio = msg.data().slice(6, 8);
					//ByteString reg = msg.data().slice(24, 26);
					ByteString dat = msg.data().slice(26, 28);
					if(mdio.equals(MDIO_ADDR)){ 
						sendTcp(MSA_MDIO, MDIO_READ, LEN_ST, msgQueue.getFirst().getRegisterAdr(), PADDING);
					}
					else if(mdio.equals(MDIO_READ)){
						//TODO: can simplify the DevDriver actor message parsing if we send STATE message reply
						//in separate message 
						sendMng(MsgType.R, msgQueue.pollFirst().getParameter(), dat, ok);
						if (!msgQueue.isEmpty())
							//TODO: check self message and correct string
							self().tell("next message", self());
						log.trace("Switching from reading state to idle {}", self().path().name());
						getContext().become(idle);
					}
				})
				.build();
		boardControl = ReceiveBuilder
				//received message from device manager
				.match(TCPMsg.class, msg -> {
					msgQueue.addLast(msg);
				})
				//received message from device
				.match(Received.class, msg -> {
					log.trace("{}: {}", self().path().name(),  Utils.BStoHex(msg.data()));
					// extract the MDIO, register and data information
					ByteString subcmd = msg.data().slice(6, 8);
					ByteString dat = msg.data().slice(24, 32);
					sendMng(MsgType.B_CMD, subcmd, dat, ok);
					msgQueue.pollFirst();
					if (!msgQueue.isEmpty())
						//TODO: check self message and correct string
						self().tell("next message", self());
					log.trace("Switching from boardcontrol to idle {}", self().path().name());
					getContext().become(idle);
				})
				.build();
		//READING STATE END
		//WRITING STATE START
		writing = ReceiveBuilder
				//received message from device manager
				.match(TCPMsg.class, msg -> {
					msgQueue.addLast(msg);
				})
				//received message from device
				.match(Received.class, msg -> {
					log.trace("{}: {}", self().path().name(),  Utils.BStoHex(msg.data()));
					// extract the MDIO, register and data information
					ByteString mdio = msg.data().slice(6, 8);
					ByteString reg = msg.data().slice(24, 26);
					ByteString dat = msg.data().slice(26, 28);
					TCPMsg qmsg=msgQueue.getFirst();
					//check the MDIO section received (address, read or write phase)
					switch (Utils.BStoHex(mdio)){
					//address phase reply
					case MDIO_A:
						if (reg.equals(WR_RDY_REG))
							sendTcp(MSA_MDIO, MDIO_READ,LEN_ST, WR_RDY_REG, PADDING);
						else
							sendTcp(MSA_MDIO, MDIO_WRITE, LEN_ST, qmsg.getRegisterAdr(), qmsg.getData());
						break;
					//read phase reply
					case MDIO_R:
						//check the write-ready bit by masking the data with the ready-mask
						if (Utils.maskData(dat, rdy_msk).equals(rdy)) {
							rdy_cnt=0;
							sendTcp(MSA_MDIO, MDIO_ADDR, LEN_ST, qmsg.getRegisterAdr(), PADDING);
						} else if (Utils.maskData(dat, rdy_msk).equals(nrdy)) {
							/*
							 * check and increment counter for write-ready bit request
							 * When the counter is reached, remove the message from queue and send it back to manager with !ok
							 */
							if (rdy_cnt<9){
								rdy_cnt++;
								sendTcp(MSA_MDIO, MDIO_ADDR, LEN_ST, WR_RDY_REG, PADDING);
							} else{
								//delete the message from the queue and send the reply to the device manager
								
								sendMng(MsgType.WR, msgQueue.pollFirst().getParameter(), qmsg.getData(), !ok);
							}
						} else {
							//the write register was not readable; try to read again
							log.debug("{} the write register was not readable, try again to read: {}...{}", self().path().name(), qmsg.getParameter(), Utils.BStoHex(msg.data()));
							sendTcp(MSA_MDIO, MDIO_ADDR, LEN_ST, WR_RDY_REG, PADDING);
						}
						break;
					// write phase reply
					case MDIO_W:
						sendMng(MsgType.WR, msgQueue.pollFirst().getParameter(), dat, ok);
						if (!msgQueue.isEmpty())
							//TODO: check self message and correct string
							self().tell("next message", self());
						log.trace("switching from writing state to idle {}", self().path().name());
						getContext().become(idle);
						break;
					}
				}).build();
		//WRITING STATE END
	}

	// TODO: move the GetStats procedure to the TCP manager actor above
	private void getStateInformation(List<ByteString> values) {
		for (int i = 0; i < values.size(); i++) {
			tcp.tell(TcpMessage.write((ByteString) values.get(i)), self());
		}

	}

	/********************
	 * CONSTRUCTORS END *
	 ********************/
	/**
	 * Starts the scheduler that triggers connection setups.
	 */
	private void startScheduler() {
		// TODO: get retry_timeout_from configuration file
		if (retryScheduler == null || retryScheduler.isCancelled())
			retryScheduler = getContext()
					.system()
					.scheduler()
					.schedule(FiniteDuration.apply(5, "seconds"),
							FiniteDuration.apply(3, "seconds"), tcp,
							TcpMessage.connect(remote), getContext().dispatcher(), self());
	}

	// send register configuration message to TCP actor
	private void sendTcp(ByteString msa_mdio, ByteString mtype,  ByteString len, ByteString reg, ByteString data) {
		if (reg.isEmpty()){

			tcp.tell(TcpMessage.write((ByteString) PREFIX.concat(msa_mdio)
					.concat(mtype).concat(NA).concat(len).concat(PADDING).concat(data)),
					self());
		}
		else { 
			//read/write command
			tcp.tell(TcpMessage.write((ByteString) PREFIX.concat(msa_mdio)
					.concat(mtype).concat(NA).concat(len).concat(reg).concat(data)),
					self());
		}
	}

	// send board subcommand back to device manager
	private void sendMng(MsgType mtype, ByteString subcmd, ByteString data, boolean ok) {
		//iterate over subcommand enumeration to find the match
		for (EbSubCmd iter : EbSubCmd.values()) {
			if (iter.subAddr().equals(subcmd)) {
				//construct the message and send it to the device manager actor
				TCPMsg rep = new TCPMsg(mtype, iter, data, ok);
				DeviceManager.tell(rep, self());
				//write the reply to console to test the real board
				break;
			}
			// TODO: else raise exception, subcommand not supported
		}
	}
	// send read/write reply back to device manager
	private void sendMng(MsgType mtype, Parameters param, ByteString data, boolean ok) {
		//construct the message and send it to the device manager actor
			TCPMsg rep = new TCPMsg(mtype, param, data, ok);	
			DeviceManager.tell(rep, self());
			//write the reply to console to test the real board
	}
}

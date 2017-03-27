package de.htwsaar.wirth.server;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Main entry point to the server application. Takes the following arguments
 * as its input and tries to start a new server instance.
 * 
 * 	- the group name of this server instance
 * 	- the port on which this instance is running on
 * 
 * To connect this instance with a parent instance, it is necessary to also
 * specify these two parameters:
 * 
 * 	- the group name of the parent server
 * 	- the host address of the parent server
 * 
 * @author janibal, makohn
 *
 */
public class Main {
	
	private static final Logger logger = LogManager.getLogger(Main.class);
	
	private static final String USAGE_HEADER = "_____________________________________________________________\n"
			+ "to start as child-server:\t > server --group <arg> --port <arg> --parent <arg> --pgroup <arg> \n"
			+ "to start as a root-server:\t > server  server --group <arg> --port <arg> \n\n";
	
	private static final String USAGE_FOOTER = "_____________________________________________________________\n";

	private static final String ALREADY_IN_USE 			= "The specified port is already in use.";
	private static final String PARENT_NOT_AVAILABLE 	= "The parent is not responding.";
	private static final String PARENT_PORT_MISSING 	= "Please specify the port of the parent server";
	
	private static final Option GROUP = Option.builder("g")
			   .longOpt("group")
			   .desc("the groupname of this server instance")
			   .hasArg()
			   .required()
			   .build();
	
	private static final Option PORT = Option.builder("p")
			   .longOpt("port")
			   .desc("the local port to access this server")
			   .hasArg()
			   .required()
			   .build();

	private static final Option PARENT = Option.builder("ph")
			   .longOpt("parent")
			   .desc("the parent server's ip address")
			   .hasArg()
			   .build();
	
	private static final Option PARENT_GROUP = Option.builder("pg")
			   .longOpt("pgroup")
			   .desc("the groupname of the parent server")
			   .hasArg()
			   .build();


	/**
	 * Main method. Uses the Apache Commons CLI Framework to parse the passed
	 * arguments as UNIX style command line arguments. Thus the order of the 
	 * passed arguments is irrelevant as long as the semantics are correct.
	 * @see <a href="https://commons.apache.org/proper/commons-cli/">Apache Commons CLI Framework</a>
	 * @param args - the passed arguments in command line style, i.e. -p 4711
	 */
	public static void main(String[] args) {
		
		boolean root 		= false;
		String group 		= null;
		String parent 		= null;
		String parentGroup 	= null;
		String ipadr        = null;
		int port 			= 0;
		
		Options options = new Options();
		options.addOption(GROUP);
		options.addOption(PORT);
		options.addOption(PARENT);
		options.addOption(PARENT_GROUP);
		
		HelpFormatter formatter = new HelpFormatter();
		
		CommandLineParser parser = new DefaultParser();
		
		// parse the command line input
	    try {
	    	CommandLine cli = parser.parse(options, args);
	    	root        = isRoot(cli);
			group  		= getOptionValue(cli, GROUP);
			port   		= Integer.parseInt(getOptionValue(cli, PORT));
			parent 		= root? null : getOptionValue(cli, PARENT);
			parentGroup	= root? null : getOptionValue(cli, PARENT_GROUP);
			
		} catch (ParseException e1) {
			formatter.printHelp("server", USAGE_HEADER, options, USAGE_FOOTER);
			System.exit(1);
		}
	    
	    // for purely logging/debugging reasons
	    try {
			ipadr = Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e1) {
			logger.info("Host is unknown.");
		}

	    // start server
		if (root) {
			try {
				new Server(group, port);
				logger.info("Server is alive, running on Port " + port + " and kicking");
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (AlreadyBoundException e) {
				logger.error(ALREADY_IN_USE);
			}
		} else {
			try {
				new Server(group, port, parent, parentGroup);
				logger.info("Server is connected to "+ parent + " in group "+ parentGroup);

			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (AlreadyBoundException e) {
				logger.error(ALREADY_IN_USE);
			} catch (NotBoundException e) {
				logger.error(PARENT_NOT_AVAILABLE);
			}
		}
		logger.info("Server is running on host: " + (ipadr != null ? ipadr : "Unknown"));
	}
	
	/**
	 * Checks whether the passed arguments imply a server start as a root server or if
	 * they connect this instance to a parent server. The latter of which takes two
	 * arguments: the parent's group and the parent's ip address.
	 * @param cli - the set of passed arguments
	 * @return true - if this instance should run as a root instance
	 * @throws ParseException - thrown if only one argument is passed
	 */
	private static boolean isRoot(CommandLine cli)  throws ParseException {
		if(cli.hasOption(PARENT.getOpt()) || cli.hasOption(PARENT.getLongOpt())) {
			if(cli.hasOption(PARENT_GROUP.getOpt()) || cli.hasOption(PARENT_GROUP.getLongOpt())) {
				return false;
			}
			else throw new ParseException(PARENT_PORT_MISSING);
		}
		return true;
	}
	
	/**
	 * Helper method to parse the value of a passed option. Checks both, the short
	 * and the long name of the given option. 
	 * @param cli - the set of passed arguments
	 * @param opt - the expected option
	 * @return the value of the passed and expected option, if it is contained
	 * 		   within the set of passed arguments.
	 * @throws ParseException - if the expected option is not contained in cli
	 */
	private static String getOptionValue(CommandLine cli, Option opt) throws ParseException {
		if(cli.hasOption(opt.getOpt())) {
			return cli.getOptionValue(opt.getOpt());
		} 
		else if (cli.hasOption(opt.getLongOpt())) {
			return cli.getOptionValue(opt.getLongOpt());
		}
		return null;
	}
}

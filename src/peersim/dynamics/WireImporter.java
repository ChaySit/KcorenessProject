 package peersim.dynamics;

import peersim.config.Configuration;
import peersim.graph.Graph;
import peersim.graph.GraphFactory;
import peersim.graph.GraphIO;

/**
 * This class allows for importing a graphML file that
 * will be used for building the network topology
 * */
public class WireImporter extends WireGraph  {

	
	public static final String GRAPHML_READING_PATH = "D:\\Workspace\\peerSim\\graphs\\graph3.graphml";
	public static final String NETWORK_SIZE = "network.size" ;
	private static int size;
	
	public WireImporter(String prefix) { 
		super(prefix);
		size = Configuration.getInt(NETWORK_SIZE);
	}
	
	/** Calls {@link GraphFactory#wireGrid}.*/
	public void wire(Graph g) {
		//The wiring logic 
		GraphIO.graphMLReader(g,GRAPHML_READING_PATH,size);
	}


	}

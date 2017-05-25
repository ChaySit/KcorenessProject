package peersim.dynamics;

import peersim.config.Configuration;
import peersim.graph.Graph;
import peersim.graph.GraphFactory;
import peersim.graph.GraphIO;

public class WireImporter extends WireGraph  {

	public WireImporter(String prefix) { super(prefix); }

	// ===================== public methods ==============================
	// ===================================================================

	public static final String GRAPHML_READING_PATH = "D:\\Workspace\\peerSim\\graphs\\graph3.graphml";
	public static final String NETWORK_SIZE = "network.size" ;
	
	private static int size;

	/** Calls {@link GraphFactory#wireGrid}.*/
	public void wire(Graph g) {
		//The wiring logic 
		
		size = Configuration.getInt(NETWORK_SIZE);
		GraphIO.graphMLReader(g,GRAPHML_READING_PATH,size);
	}


	}

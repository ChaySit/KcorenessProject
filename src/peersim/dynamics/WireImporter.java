package peersim.dynamics;

import peersim.graph.Graph;
import peersim.graph.GraphFactory;
import peersim.graph.GraphIO;

public class WireImporter extends WireGraph  {

	public WireImporter(String prefix) { super(prefix); }

	// ===================== public methods ==============================
	// ===================================================================


	/** Calls {@link GraphFactory#wireGrid}.*/
	public void wire(Graph g) {
		//The wiring logic 
		GraphIO.graphMLReader(g);
	}


	}

package peersim.dynamics;

import peersim.graph.*;

/**
 * Takes a {@link peersim.core.Linkable} protocol and adds connection
 * which for a grid
 * topology. No connections are removed, they are only added. So it can be used
 * in combination with other initializers.
 * @see GraphFactory#wireGrid
 */
public class WireGrid extends WireGraph {

// ===================== initialization ==============================
// ===================================================================

/**
 * Standard constructor that reads the configuration parameters.
 * Invoked by the simulation engine.
 * @param prefix the configuration prefix for this class
 */
public WireGrid(String prefix) { super(prefix); }

// ===================== public methods ==============================
// ===================================================================


/** Calls {@link GraphFactory#wireGrid}.*/
public void wire(Graph g) {
	
	GraphFactory.wireGrid(g);
}


}


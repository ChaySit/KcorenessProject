package projet.Kcoreness;

import peersim.config.*;
import peersim.core.*;
import java.util.Vector;

/* A protocol that stores links */
public class NeighborsProtocol implements Protocol, Linkable {
	
	/** Neighbors */ 
	protected Vector<Node> neighbors;
	/** Parameter for initial capacity of the vector */
	public final static String PAR_INIT_CAPACITY="capacity";
	/** Default initial capacity of the vector */
	private final static int defaultInitialCapacity=10;
	/** Initial capacity */
	private static int initialCapacity;

	/** Constructor */
	public NeighborsProtocol(String prefix) {
		// get the parameter value from the configuration file 
		initialCapacity = Configuration.getInt(prefix+"."+PAR_INIT_CAPACITY,defaultInitialCapacity);
	    neighbors = new Vector<Node>(initialCapacity);
	}

	//TODO for optimization ?!
	@Override
	public void onKill() {
		// TODO Auto-generated method stub	
	}

	/** Returns the degree of a node */
	@Override
	public int degree() {
		return neighbors.size();
	}

	/** Return the neighbor with the given index */
	@Override
	public Node getNeighbor(int i) {
		return neighbors.elementAt(i);
	}

	/** Add a neighbor to the current set of neighbors */
	@Override
	public boolean addNeighbor(Node neighbour) {
		neighbors.add(neighbour);
		return true;
	}

	/** Return true if the given node is a member of the neighbor set */
	@Override
	public boolean contains(Node neighbor) {
		return neighbors.contains(neighbor);
	}

	//TODO for optimization ?!
	@Override
	public void pack() {
		// TODO Auto-generated method stub	
	}
	
	/** Clonable */
	public Object clone(){
		NeighborsProtocol np = null;
		try {
			np = (NeighborsProtocol) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		np.neighbors = new Vector<Node>();
		return np;
	}

}

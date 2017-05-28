package projet.Kcoreness;

import peersim.cdsim.CDProtocol;
import peersim.config.Configuration;
import peersim.core.Linkable;
import peersim.core.Node;

import java.util.HashMap;

import javax.sound.sampled.ReverbType;


/**
 * This class implements the k-Coreness decomposition algorithm .
 * Objects of this class holds the local k-Coreness attributes along 
 * with their own estimations for their neighbors .
 * The instance method nextCycle() carries the code that will be executed 
 * by each node . 
 * */


public class KcorenessFunction implements CDProtocol {


	
	/* Fields that defines the attributes of the K-CorenessFunction protocol
	 *  
	 * */
	private boolean changed; // boolean flag set to true if coreness has been changed 
	private HashMap<Integer,Integer> estimation; // <id of the neighbor, the estimation of its coreness>
	private int coreness; // the local estimate of the coreness of the node 

	
	public KcorenessFunction() {
		estimation = new HashMap<>();

	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public HashMap getEstimation() {
		return estimation;
	}

	public void setEstimation(HashMap estimation) {
		this.estimation = estimation;
	}

	public int getCoreness() {
		return coreness;
	}

	public void setCoreness(int coreness) {
		this.coreness = coreness;
	}

	/**This method adds a node 
	 * @param : node to be added as a neighboor with a default value of positive infinity
	 * */
	
	public void newEntry(Node peer){
		estimation.put((int) peer.getID(),Integer.MAX_VALUE);
	}
  
	/**
	 * This method is used to create nodes based on an node prototype 
	 * */
	@Override
	public Object clone() {
		KcorenessFunction corenessProtocol = null;
		try {
			corenessProtocol = (KcorenessFunction) super.clone();
		}catch (CloneNotSupportedException e){}

		corenessProtocol.estimation = new HashMap<>();
		return corenessProtocol;
	}

	/* Linkable protocol */
	private static final String LINKABLE_PROT = "linkable";
	/* Linkable protocol identifier */
	private static int linkpid;

	public KcorenessFunction(String prefix) {
		linkpid = Configuration.getPid(prefix + "." + LINKABLE_PROT);
	}

	/**
	 * The nextCycle() method will be invoked in a cycle-based manner 
	 * for each node  
	 * @param node : the local node 
	 * @param protocolID : the protocol being executed
	 * **/
	
	@Override
	public void nextCycle(Node node, int protocolID) {

		
		
		KcorenessFunction currentNode = (KcorenessFunction) node.getProtocol(protocolID);
		Linkable link = (Linkable) node.getProtocol(linkpid);
		int neighborCoreness;
		KcorenessFunction neighborNode;
		int neighborID ;

		
		if (link.degree() > 0) {
			
			for (int i=0; i<link.degree(); i++) {
				
				neighborNode = (KcorenessFunction) link.getNeighbor((Integer)i).getProtocol(protocolID);
				neighborID=(int) link.getNeighbor(i).getID();
				
				
		
				if(!Dynamics.removedNodesID.contains(neighborID)){
					// get the coreness value of the neighboor
					neighborCoreness = neighborNode.getCoreness();

					// get the estimation of the coreness value of the neighboor
					int ncore = (Integer)currentNode.getEstimation().get(neighborID);
					
					/*	
					if the estimation of the neighbor's coreness is greater that what is currently estimated, 
					 the estimation gets updated, and the computation of the updated local coreness
					 is started.
					*/
					if (neighborCoreness < ncore){
						currentNode.getEstimation().put(neighborID, neighborCoreness);

					         // invoke the computation of the updated local coreness based on the coreness 
				        	int t = ComputeIndex(currentNode.getEstimation(),currentNode, currentNode.getCoreness(),node);
						/*
						* We compare the value computed by computeIndex with the local coreness
						* If the returned value is less than the current value , the coreness value
						* gets updated.
						*/
						if (t < currentNode.getCoreness()) {
							currentNode.setCoreness(t);
							currentNode.setChanged(true);
						}
					}
				}

			}  
		}

	}

	/**This method computes the new temporary local coreness of a the current node
	 * based on the local estimation of that of its neighbors.
	 * @param estimation      : neighbors' coreness value estimation
	 * @param currentNode     : current node 
	 * @param node 		  : the node for which the current coreness value is less than its local estimation
	 * @param k		  : current local coreness of the current node 
	 * @return the largest value i such that there are at least i neighbors with a coreness estimation greater
	 * or equal to i
	 */
	public int ComputeIndex(HashMap<Integer,Integer> estimation, KcorenessFunction currentNode, int k, Node node){

		Linkable link = (Linkable) node.getProtocol(linkpid);
		int[] counts = new int[currentNode.getCoreness() + 1];
		int i,j;

		// initialization of  the counts array 
		for (i = 1; i < currentNode.getCoreness(); i++) {
			counts[i] = 0;
		}

		/* compute how many nodes have an estimated coreness greater or equal than 
		*  a value j and store this number in array counts j-th element.
		*  Note that j is garanteed to be less or equal to the  local coreness value k
		*   
		 */
		for (i = 0; i < estimation.size(); i++) {
			int neighborID=(int) link.getNeighbor(i).getID();
			if(!Dynamics.removedNodesID.contains(neighborID)){

				j = (k < (int) estimation.get(neighborID)) ? k : (int) estimation.get(neighborID);
				counts[j] = counts[j] + 1;
			}

		}
		
		for (i = k; i >= 2; i--) {
			counts[i - 1] = counts[i - 1] + counts[i];
		}
		
		i=k;

		//Searching the largest value i such that count[i] is greater or equal to i
		while (i > 1 && counts[i] < i) {
			i--;
		}

		return i;

	}


}

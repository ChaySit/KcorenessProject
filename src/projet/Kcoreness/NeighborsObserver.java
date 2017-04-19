package projet.Kcoreness;

import peersim.config.*;
import peersim.core.*;

public class NeighborsObserver implements Control{
	
	/** Parameter for linkable protocol identifier */
	private static final String PAR_PROT = "protocol";
	
	/** Protocol identifier : obtained from config property {@link #PAR_PROT} */
	private static int pid;
	
	public NeighborsObserver(String prefix){
		//get identifier of linkable protocol
		pid = Configuration.getPid(prefix+"."+PAR_PROT);
		
	}
	
	
	/** return true if the simulation has to be stopped */
	@Override
	public boolean execute() {
		Node node;
		NeighborsProtocol neighbors;
		//Print the neighbors of each node
		for (int i=0; i<Network.size(); i++){
			node = Network.get(i);
			neighbors = (NeighborsProtocol) node.getProtocol(pid);
			System.out.println(node.getID());
			for (int n=0; n<neighbors.degree(); n++){
				System.out.println(neighbors.getNeighbor(n).getID());
			}
			System.out.println();
		}
		return false;
	}
}

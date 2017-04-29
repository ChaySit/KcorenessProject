package projet.Kcoreness;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Linkable;
import peersim.core.Network;
import peersim.core.Node;

/**
 *  A controller that gets the Kcore of each node 
 **/
public class KcorenessObserver implements Control{
	
	/* Parameter for Linkable protocol identifier */
	private static final String PAR_PROT = "protocol";
	
	/* Protocol identifier : obtained from config property {@link #PAR_PROT} */
	private static int pid;
	
	/* Constructor */
    public KcorenessObserver(String prefix){
    	// get identifier of linkable protocol 
        pid = Configuration.getPid(prefix + "."+PAR_PROT);
    }
      
    /* return true if the simulation has to be stopped */
	@Override
	public boolean execute() {
		Node peer = null;
		KcorenessFunction currentNode;
		
		for (int i=0; i< Network.size(); i++){
			peer = Network.get(i);
			currentNode = (KcorenessFunction) peer.getProtocol(pid);
			
			System.out.println("Peer "+peer.getID());
			System.out.println("the estimation of its neighbors's coreness " + currentNode.getEstimation());	
		}
		return false;
	}
}

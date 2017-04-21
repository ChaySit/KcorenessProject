package projet.Kcoreness;

import peersim.config.Configuration;
import peersim.core.Control;
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
        
	@Override
	public boolean execute() {
		Node peer;
		CorenessProtocol KCprotocol;
		//Prints the Kcore of each node 
		for (int i=0; i< Network.size(); i++){
			peer = Network.get(i);
			KCprotocol = (CorenessProtocol) peer.getProtocol(pid);
			System.out.println("Peer "+peer.getID()+" Kcore = "+KCprotocol.getCoreness());
		}
		return false;
	}
}

package projet.Kcoreness;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Linkable;
import peersim.core.Network;
import peersim.core.Node;
/**
 * This Control class is used to initialize a node 
 * */

public class KcorenessInitializer implements Control {
    

   
	
	/**These values are used to parse the configuration file
	 * PAR_PROT represents the protocol of K-Coreness Function
	 * LINKABLE_PROT represents the Linkable protocol
	 * */
    private static final String PAR_PROT = "protocol";
    private static final String LINKABLE_PROT = "linkable";

    //Fields
    /*These fields will hold the identifiers of 
     * both the K-Coreness and linkable protocols
     * */
    private static int pid;
    private static int linkpid;

    //Constructor
    public KcorenessInitializer(String prefix){
        pid = Configuration.getPid(prefix + "."+PAR_PROT);
        linkpid = Configuration.getPid(prefix + "." + LINKABLE_PROT);
    }
    /**
     * This method performs the actual initialization of the local coreness
     * and the linking between neighboors
     * */
    @Override
    public boolean execute() {
        for (int i=0; i< Network.size(); i++){
        	KcorenessFunction protocol = (KcorenessFunction) Network.get(i).getProtocol(pid);
            Linkable linkable = (Linkable) Network.get(i).getProtocol(linkpid);
            protocol.setChanged(false);
            protocol.setCoreness(linkable.degree());
            for (int j=0; j<linkable.degree(); j++){
            
                protocol.newEntry(linkable.getNeighbor(j));
            }
        }
        return false;
    }
}

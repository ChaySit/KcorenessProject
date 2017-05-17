 package projet.Kcoreness;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Linkable;
import peersim.core.Network;
import peersim.dynamics.DynamicNetwork;
import projet.Kcoreness.*;
import peersim.cdsim.CDProtocol;


public class DynamicRemoval implements  Control {
	
    /**
     * Specifies the number of nodes to remove  **/

    private static final String PAR_REMOVE  = "remove";

    /**
     * Nodes are added until the size specified by this parameter is reached
     */

    private static final String PAR_MAX = "maxsize";

    /**
     *	The protocol to operate on.*/

    private static final String PAR_PROT="protocol";

    private static final String LINKABLE_PROT = "linkable";

    /**
     * Nodes are removed until the size specified by this parameter is reached.*/

    private static final String PAR_MIN = "minsize";

    //Fields

    private static int pid;
    private static int linkpid;

	
	public DynamicRemoval(String prefix){
		  pid = Configuration.getPid(prefix + "."+PAR_PROT);
		  linkpid = Configuration.getPid(prefix + "." + LINKABLE_PROT);
	}


	/*
	 * Removes a node given a current index 
	 * */
	public void removebyIndex(int i){

		Network.swap(i, Network.size()-1);
		Network.remove();

	}

	/*
	 * Removes a node given a given node ID 
	 * @Returns 0 if removed 1 otherwise
	 * */

	public void removebyID(int ID){
	
		int	 i = 0;
		int	Id = Integer.MIN_VALUE;
		KcorenessFunction neighboor = null;
		Linkable link =  null ; 

		while(i<Network.size() && Id!=ID){
			Id = (int)Network.get(i).getID();
			i++;
         }

		if(Id==ID){
			
			link = (Linkable) Network.get(Id).getProtocol(linkpid);
			for(int j=0 ;j<link.degree();j++){
				neighboor = (KcorenessFunction)link.getNeighbor(j).getProtocol(pid);
				neighboor.getEstimation().remove(Id);
			}
			Network.remove(Id);
			


	       }
	}

	/*
	 * Removes a given layer based on a particular coreness value
	 * @Returns number of removed nodes
	 * */

	public int removeCorenessLayer(int corenessValue){

		KcorenessFunction protocol = null ;
		int nbRemoved = 0;
		Linkable link =  null ; 
		for(int i=0;i<Network.size();i++){
			
			link = (Linkable) Network.get(i).getProtocol(linkpid);
			protocol = (KcorenessFunction)Network.get(i);
			if(protocol.getCoreness()==corenessValue){
				removebyID((int)Network.get(i).getID());
				nbRemoved++;
			}
		}

		return nbRemoved;

	}
	
	public  boolean execute(){
		System.out.println("ib execute ----------------");
		removebyID(4);
		return false;
	}






}

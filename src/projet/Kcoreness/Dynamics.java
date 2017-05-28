package projet.Kcoreness;

import java.util.ArrayList;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Linkable;
import peersim.core.Network;
import peersim.core.Node;

/**
 * This class can change the size of our network during the simulation by adding or removing
 * nodes.
 */



public class Dynamics implements Control{

	/**
	 * Specifies the number of nodes to add  **/

	private static final String PAR_ADD = "add";

	/**
	 *	The protocol to operate on */

	private static final String PAR_PROT="protocol";

	/**
	 *  Parameter for Linkable protocol identifier */
	private static final String LINKABLE_PROT = "linkable";

	/**
	 * Specifies whether to add or remove a node or a layer
	 * possible values : remove - addNodes */
	private static final String DYNAMICS_FLAG  = "flag";

	/**
	 * List of removed nodes */
	public static ArrayList<Integer> removedNodesID = new ArrayList<Integer>();

	/**
	 * Id of node to remove */
	private static final String ID_TO_REMOVE = "idremoved";

	//Fields
	private static int pid;
	private static int linkpid;
	
	private static String dynamicFlag;
	private static int add;
	private static int idToRemove; 
	
	// To initialize a node before inserting it into the topology 
	private static NewNodeInitializer init;

	/* Constructor */
	public Dynamics(String prefix){

		pid = Configuration.getPid(prefix + "."+PAR_PROT);
		linkpid = Configuration.getPid(prefix + "." + LINKABLE_PROT);
		dynamicFlag = Configuration.getString(prefix + "." + DYNAMICS_FLAG);
		
		if(dynamicFlag.equals("addNodes")){
			add = Configuration.getInt(prefix + "." + PAR_ADD);
		}
		else {
			idToRemove = Configuration.getInt(prefix + "." + ID_TO_REMOVE);
		}
		
		init = new NewNodeInitializer("newnode");
	}

	
	/**
	 * initializes nodes using NewNodeInitialize.initialize(node) and link a defined number of random neighbors from the network to each node
	 * @param n numbers of nodes to add
	 */
	public void add(int n) {
		for (int i=0; i<n; i++) {
            
			//Create a node by cloning the network prototype
			Node node = (Node) Network.prototype.clone();
			Network.add(node);
			// Initializing a node
			init.initialize(node);
			Linkable linkable = (Linkable) node.getProtocol(linkpid);
			KcorenessFunction newNode = (KcorenessFunction) node.getProtocol(pid);

			for (int j=0; j<init.degree; j++) {
				/* Since Network.get(j) returns the node with the index j, which changes in each cycle
				 * neighbors added to each node won't be the same */
				linkable.addNeighbor(Network.get(j));
				//filling the hashmap of the node with its neighbors and the estimation of their kcoreness
				newNode.newEntry(linkable.getNeighbor(j));
			}

		}
	}


	/**
	 * Removes a node given the node ID
	 * @Returns 0 if removed 1 otherwise
	 * */
	public void removebyID(int ID){
		int	 i = 0;
		int	Id = Integer.MIN_VALUE;
		KcorenessFunction neighboor = null ;
		Linkable link = null ;

		while(i<Network.size() && Id!=ID){
			if((int)Network.get(i).getID()==ID)
				Id=ID;
			i++;
		}

		if (Id == Integer.MIN_VALUE)
			return;
		else {

			for (int j=0; j<Network.size(); j++ ){

				if (Network.get(j).getID()==Id){

					link = (Linkable) Network.get(j).getProtocol(linkpid);

					for (int k=0; k<link.degree(); k++)
					{
						neighboor = (KcorenessFunction)link.getNeighbor(k).getProtocol(pid);
						neighboor.getEstimation().remove(Id);
					}
					removedNodesID.add(Id);
					Network.remove(Id);
				}
			} 
		}
	}

	@Override
	/**
	 * Add or remove by id.
	 */
	public boolean execute() {
		if (dynamicFlag.equals("addNodes")){
			System.out.println("Adding node");
			add(add);
		}
		else if(dynamicFlag.equals("remove")){
			removebyID(idToRemove);
		}else{
			return false;
		}	
		return false;
	}
}

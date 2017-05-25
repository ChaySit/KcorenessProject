package projet.Kcoreness;

import java.util.ArrayList;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Linkable;
import peersim.core.Network;
import peersim.core.Node;



public class Dynamics implements Control{

    /**
    * Specifies the number of nodes to add  **/

    private static final String PAR_ADD = "add";


    /**
     * Nodes are added until the size specified by this parameter is reached
     */

    private static final String PAR_MAX = "maxsize";

    /**
     *	The protocol to operate on.*/

    private static final String PAR_PROT="protocol";

    /**
     *      */
    private static final String LINKABLE_PROT = "linkable";

    /**
     * Nodes are removed until the size specified by this parameter is reached.*/

    private static final String PAR_MIN = "minsize";


    /**
     * Specifies whether to add or remove a node or a layer
     * possible values : remove - add */
    private static final String DYNAMICS_FLAG  = "dynamics.flag";
    
    /**
     * Specifies which methode to use to remove nodes; possible values : id - layer
     * Default value id */
    private static final String REMOVE_BY  = "remove.by";
    
    /**
     * Specifies the ID of the node to remove OR the coreness of the layer to remove (layer = nodes with the same Kcoreness)
     * Default value : 0 */
    private static final String PAR_REMOVE  = "remove.parameter";
    
    /**
     * List of removed nodes */
    public static ArrayList<Integer> removedNodesID = new ArrayList<Integer>();
    


    //Fields

    private static int pid;
    private static int linkpid;
    private final int add;
    private static String dynamicFlag = "add";
    private static String removeBy = "id";
    private static int removedPar = -1 ;
    private final NewNodeInitializer init;
    



    public Dynamics(String prefix){

        pid = Configuration.getPid(prefix + "."+PAR_PROT);
        linkpid = Configuration.getPid(prefix + "." + LINKABLE_PROT);
        add = Configuration.getInt(prefix + "." + PAR_ADD);
        
        
        if (Configuration.contains(DYNAMICS_FLAG)){
        	dynamicFlag = Configuration.getString(DYNAMICS_FLAG);
        	System.out.println("init "+dynamicFlag);
        }
        if (Configuration.contains(REMOVE_BY) && Configuration.contains(PAR_REMOVE)){
        	removeBy = Configuration.getString(REMOVE_BY);
        	removedPar = Configuration.getInt(PAR_REMOVE);
        }
        
 
        init = new NewNodeInitializer("newnode");



    }

    /**
     * initializes a node using NewNodeInitialize.initialize(node) and
      link random neighbors from the network
     * @param n numbers of nodes to add
     */


    public void add(int n) {

        for (int i=0; i<n; i++) {

            Node node = (Node) Network.prototype.clone();
            Network.add(node);
            init.initialize(node);
            Linkable linkable = (Linkable) node.getProtocol(linkpid);
            KcorenessFunction newNode = (KcorenessFunction) node.getProtocol(pid);

            for (int j=0; j<init.degree; j++) {
                linkable.addNeighbor(Network.get(j));
                newNode.newEntry(linkable.getNeighbor(j));
            }

        }
    }


    /**
	 * Removes a node given a given node ID
	 * @Returns 0 if removed 1 otherwise
	 * */

    public void removebyID(int ID){

            int	 i = 0;
            int	Id = Integer.MIN_VALUE;
            KcorenessFunction neighboor ;
            Linkable link ;

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

	/**
	 * Removes a given layer based on a particular coreness value
	 * @Returns number of removed nodes
	 * NOT STABLE
	 * */
    public int removeCorenessLayer(int corenessValue){

            Node node = (Node) Network.prototype.clone();
            KcorenessFunction protocol ;
            int nbRemoved = 0;

            System.out.println(Network.size());
            for(int i=0;i<Network.size();i++){
                protocol = (KcorenessFunction) Network.get(i).getProtocol(pid);
                if(protocol.getCoreness()==corenessValue){
                    removebyID((int)Network.get(i).getID());
                    removedNodesID.add((int)Network.get(i).getID());
                    
                    nbRemoved++;
                }
            }
            return nbRemoved;

       

    }


    @Override
    /**
     * Add or remove, if remove : by id or by layer.
     */
    public boolean execute() {
    	
    	if (dynamicFlag.equals("add")){
    		System.out.println("Adding node");
    		add(add);
    	
    	}else if(dynamicFlag.equals("remove")){
    		
    		if(removeBy.equals("id")){
    			System.out.println("Removing node with id: "+ removedPar);
    			removebyID(removedPar);
    		
    		}else if (removeBy.equals("layer")){
    			System.out.println("Removing layer with coreness: " +removedPar);
    			removeCorenessLayer(removedPar);
    		}else{
    			return false;
    		}
    	
    	}else{
    		return false;
    	}
    	
    	return false;

    }



}

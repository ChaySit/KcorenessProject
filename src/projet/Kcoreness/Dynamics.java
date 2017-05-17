package projet.Kcoreness;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Linkable;
import peersim.core.Network;
import peersim.core.Node;


/**
 * Created by root on 5/12/17.
 */
public class Dynamics implements Control {

    /**
    * Specifies the number of nodes to add  **/

    private static final String PAR_ADD = "add";

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
    private final int add;
   /* private final double remove;
    private final int minsize;
    private final int maxsize;*/

    public Dynamics(String prefix){

        pid = Configuration.getPid(prefix + "."+PAR_PROT);
        linkpid = Configuration.getPid(prefix + "." + LINKABLE_PROT);
        add = Configuration.getInt(prefix + "." + PAR_ADD);
        //remove = Configuration.getDouble(prefix + "." + PAR_REMOVE);
       // minsize = Configuration.getInt(prefix + "." + PAR_MIN, Integer.MAX_VALUE);
        //maxsize = Configuration.getInt(prefix + "." + PAR_MIN, 0);

    }


    public void add(int n) {

        for (int i=0; i<n; i++) {

            Node node = (Node) Network.prototype.clone();
            Network.add(node);
            KcorenessFunction newNode = (KcorenessFunction) node.getProtocol(pid);
            Linkable linkable = (Linkable) node.getProtocol(linkpid);

            newNode.setChanged(false);
            newNode.setCoreness(linkable.degree());
            for (int j = 0; j < linkable.degree(); j++) {
                newNode.newEntry(linkable.getNeighbor(j));
            }
           // ( (Linkable) Network.get(Network.size()-1)).addNeighbor(node);
            linkable.addNeighbor(node);


        }

    }


    /* * Removes a node given a given node ID
		 *
		 * */

    public void removebyID(int ID){
        int	 i = Network.size()-1;
        int	Id = Integer.MIN_VALUE;

        while(i>=0 && Id!=ID){
            Id = (int)Network.get(i).getID();

        }

        if(Id!=Integer.MIN_VALUE){
            Network.remove(i);

        }


    }


    @Override
    public boolean execute() {

        if (add == 0)
            return false;
        add(add);





        return false;

    }
}

package projet.Kcoreness;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Linkable;
import peersim.core.Network;
import peersim.core.Node;



/**
 * Created by root on 5/12/17.
 */
public class Dynamics implements Control{

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
    private final NewNodeInitializer init;
   /* private final double remove;
    private final int minsize;
    private final int maxsize;*/

    public Dynamics(String prefix){

        pid = Configuration.getPid(prefix + "."+PAR_PROT);
        linkpid = Configuration.getPid(prefix + "." + LINKABLE_PROT);
        add = Configuration.getInt(prefix + "." + PAR_ADD);
        init = new NewNodeInitializer("newnode");
        //remove = Configuration.getDouble(prefix + "." + PAR_REMOVE);
       // minsize = Configuration.getInt(prefix + "." + PAR_MIN, Integer.MAX_VALUE);
        //maxsize = Configuration.getInt(prefix + "." + PAR_MIN, 0);

    }



    public void add(int n) {

        for (int i=0; i<n; i++) {

            Node node = (Node) Network.prototype.clone();
            Network.add(node);
            init.initialize(node);

            Linkable linkable = (Linkable) node.getProtocol(linkpid);

            System.out.println(Network.size());

            KcorenessFunction newNode = (KcorenessFunction) node.getProtocol(pid);

            for (int j=0; j<init.degree; j++)
            {
                linkable.addNeighbor(Network.get(j));
                newNode.newEntry(linkable.getNeighbor(j));

            }


           // ( (Linkable) Network.get(Network.size()-1)).addNeighbor(node);
            //linkable.addNeighbor(node);


        }

    }


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

        Node node = (Node) Network.prototype.clone();
        KcorenessFunction protocol ;
        int nbRemoved = 0;

        for(int i=0;i<Network.size();i++){

            protocol = (KcorenessFunction) Network.get(i).getProtocol(pid);
            if(protocol.getCoreness()==corenessValue){
                removebyID((int)Network.get(i).getID());
                nbRemoved++;
            }
        }

        return nbRemoved;

    }


    @Override
    public boolean execute() {

       /* if (add == 0)
            return false;
        add(add);*/
        removeCorenessLayer(4);

        return false;

    }
}

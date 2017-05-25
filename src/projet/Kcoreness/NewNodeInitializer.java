package projet.Kcoreness;

import peersim.config.Configuration;
import peersim.core.Linkable;
import peersim.core.Node;
import peersim.dynamics.NodeInitializer;

/**
 * this class initializes the nodes before adding them to the topology during a cycle
 * by defining its degree
 * and the protocols that the node will hold
 */

public class NewNodeInitializer implements NodeInitializer {



    private static final String PAR_DEGREE = "degree";
    private static final String PAR_PROT= "protocol";
    private static final String PAR_LINKABLE ="linkable";


    // the kcoreness protocol
    private static int pid;
    // the linkable protocol
    private static int linkpid;
    // number of neighbors
    public int degree;

    /* Parameters identifiers : obtained from config property */

    public NewNodeInitializer(String prefix){
        pid = Configuration.getPid(prefix + "."+PAR_PROT);
        linkpid = Configuration.getPid(prefix + "." + PAR_LINKABLE);
        degree = Configuration.getInt(prefix +"." + PAR_DEGREE);
    }



    @Override
    public void initialize(Node node) {

        KcorenessFunction newNode = (KcorenessFunction) node.getProtocol(pid);
        Linkable linkable = (Linkable) node.getProtocol(linkpid);

        newNode.setChanged(false);
        //initializing the kcoreness of a node with its degree
        newNode.setCoreness(degree);
        /*filling the hashmap of the node with its neighbors and the estimation of their kcoreness*/
        for (int j = 0; j < linkable.degree(); j++) {

            newNode.newEntry(linkable.getNeighbor(j));
        }

    }
}

package projet.Kcoreness;

import peersim.config.Configuration;
import peersim.core.Control;

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
    private final double add;
    private final double remove;
    private final int minsize;
    private final int maxsize;

    public Dynamics(String prefix){

        pid = Configuration.getPid(prefix + "."+PAR_PROT);
        linkpid = Configuration.getPid(prefix + "." + LINKABLE_PROT);
        add = Configuration.getDouble(prefix + "." + PAR_ADD);
        remove = Configuration.getDouble(prefix + "." + PAR_REMOVE);
        minsize = Configuration.getInt(prefix + "." + PAR_MIN, Integer.MAX_VALUE);
        maxsize = Configuration.getInt(prefix + "." + PAR_MIN, 0);

    }



    @Override
    public boolean execute() {
        return false;
    }
}

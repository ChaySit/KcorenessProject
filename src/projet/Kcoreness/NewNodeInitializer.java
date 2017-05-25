package projet.Kcoreness;

import peersim.config.Configuration;
import peersim.core.Linkable;
import peersim.core.Node;
import peersim.dynamics.NodeInitializer;


public class NewNodeInitializer implements NodeInitializer {

    private static final String PAR_DEGREE = "degree";
    private static final String PAR_PROT= "protocol";
    private static final String PAR_LINKABLE ="linkable";


    private static int pid;
    private static int linkpid;
    public int degree;

    public NewNodeInitializer(String prefix){
        pid = Configuration.getPid(prefix + "."+PAR_PROT);
        linkpid = Configuration.getPid(prefix + "." + PAR_LINKABLE);
        degree = Configuration.getInt(prefix +"." + PAR_DEGREE);
    }



    @Override
    public void initialize(Node n) {

        KcorenessFunction newNode = (KcorenessFunction) n.getProtocol(pid);
        Linkable linkable = (Linkable) n.getProtocol(linkpid);

        newNode.setChanged(false);
        newNode.setCoreness(degree);
        for (int j = 0; j < linkable.degree(); j++) {
            newNode.newEntry(linkable.getNeighbor(j));
        }

    }
}

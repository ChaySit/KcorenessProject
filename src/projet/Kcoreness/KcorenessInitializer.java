package projet.Kcoreness;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Linkable;
import peersim.core.Network;
import peersim.core.Node;


public class KcorenessInitializer implements Control {
  
   //Parameters
    private static final String PAR_PROT = "protocol";
    private static final String LINKABLE_PROT = "linkable";

    //Fields
    private static int pid;
    private static int linkpid;

    //Constructor
    public KcorenessInitializer(String prefix){
        pid = Configuration.getPid(prefix + "."+PAR_PROT);
        linkpid = Configuration.getPid(prefix + "." + LINKABLE_PROT);
    }

    @Override
    public boolean execute() {
        for (int i=0; i< Network.size(); i++){
            CorenessProtocol protocol = (CorenessProtocol) Network.get(i).getProtocol(pid);
            Linkable linkable = (Linkable) Network.get(i).getProtocol(linkpid);
            protocol.setChanged(false);
            protocol.setCoreness(linkable.degree());
            for (int j=0; j<linkable.degree(); j++){
                Node peer = linkable.getNeighbor(j);
                protocol.newEntry(peer);
            }
        }
        return false;
    }

}

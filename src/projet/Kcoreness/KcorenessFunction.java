package projet.Kcoreness;

import peersim.cdsim.CDProtocol;
import peersim.config.Configuration;
import peersim.core.Linkable;
import peersim.core.Node;

import java.util.HashMap;


public class KcorenessFunction  implements CDProtocol {


    //Fields
    private boolean changed;
    private HashMap<Integer,Integer> estimation; // <id of the neighbor, the estimation of its coreness>
    private int coreness;

    public KcorenessFunction() {
        estimation = new HashMap<>();
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public HashMap getEstimation() {
        return estimation;
    }

    public void setEstimation(HashMap estimation) {
        this.estimation = estimation;
    }

    public int getCoreness() {
        return coreness;
    }

    public void setCoreness(int coreness) {
        this.coreness = coreness;
    }


    public void newEntry(Node peer){
        estimation.put((int) peer.getID(),Integer.MAX_VALUE);
    }

    @Override
    public Object clone() {
    	KcorenessFunction corenessProtocol = null;
        try {
            corenessProtocol = (KcorenessFunction) super.clone();
        }catch (CloneNotSupportedException e){
        }
        corenessProtocol.estimation = new HashMap<>();
        return corenessProtocol;

    }

    private static final String LINKABLE_PROT = "linkable";

    private static int linkpid;

    public KcorenessFunction(String prefix) {
        linkpid = Configuration.getPid(prefix + "." + LINKABLE_PROT);
    }

    @Override
    public void nextCycle(Node node, int protocolID) {
    	KcorenessFunction currentNode = (KcorenessFunction) node.getProtocol(protocolID);
        Linkable link = (Linkable) node.getProtocol(linkpid);
    System.out.println("entered");
        int[] counts = new int[currentNode.getCoreness() + 1];
        int neighborCoreness;
        KcorenessFunction neighborNode;

        if (link.degree() > 0) {

            for (int i = 0; i < link.degree(); i++) {
                neighborNode = (KcorenessFunction) link.getNeighbor(i).getProtocol(protocolID);
                neighborCoreness = neighborNode.getCoreness();

                      if(currentNode.getEstimation()== null)
                            System.out.println("null");

                if (neighborCoreness < (int) currentNode.getEstimation().get(i));
                    currentNode.getEstimation().put(i, neighborCoreness);

            }

            //recalculating the estimation of local coreness
            for (int j = 1; j <= currentNode.getCoreness(); j++)
                counts[j] = 0;

            int k ;

            for (int i = 0; i < link.degree(); i++) {

                k = (currentNode.getCoreness() < (int) currentNode.getEstimation().get(i)) ? currentNode.getCoreness() : (int) currentNode.getEstimation().get(i);
                counts[k] = counts[k] + 1;
            }

            for (int i = currentNode.getCoreness(); i >= 2; i--)
                counts[i - 1] = counts[i - 1] + counts[i];

            int i = currentNode.getCoreness();
            while (i > 1 && counts[i] < i)
                i--;


            if (i < currentNode.getCoreness()) {

                currentNode.setCoreness(i);
                currentNode.setChanged(true);

            }

        }

    }

}



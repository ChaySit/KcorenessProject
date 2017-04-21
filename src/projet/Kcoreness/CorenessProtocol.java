package projet.Kcoreness;


import peersim.core.Node;
import peersim.core.Protocol;

import java.util.HashMap;


public class CorenessProtocol implements Protocol {

    //Fields
    private boolean changed;
    private HashMap<Integer,Integer> estimation; // <id of the neighbor, the estimation of its coreness>
    private int coreness;

    public CorenessProtocol() {
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
        CorenessProtocol corenessProtocol = null;
        try {
            corenessProtocol = (CorenessProtocol) super.clone();
        }catch (CloneNotSupportedException e){
        }
        corenessProtocol.estimation = new HashMap<>();
        return corenessProtocol;

    }
}

package projet.Kcoreness;

import peersim.cdsim.CDProtocol;
import peersim.config.Configuration;
import peersim.core.Linkable;
import peersim.core.Node;

import java.util.HashMap;


public class KcorenessFunction implements CDProtocol {


//Fields
private boolean changed; // boolean flag set to true if coreness has been changed 
private HashMap<Integer,Integer> estimation; // <id of the neighbor, the estimation of its coreness>
private int coreness; // the local estimate of the coreness of the node 

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

   // add a new neighbor 
   public void newEntry(Node peer){
      estimation.put((int) peer.getID(),Integer.MAX_VALUE);
   }

   @Override
   public Object clone() {
       KcorenessFunction corenessProtocol = null;
       try {
            corenessProtocol = (KcorenessFunction) super.clone();
       }catch (CloneNotSupportedException e){}

       corenessProtocol.estimation = new HashMap<>();
       return corenessProtocol;
   }

   /* Linkable protocol */
   private static final String LINKABLE_PROT = "linkable";
   /* Linkable protocol identifier */
   private static int linkpid;

   public KcorenessFunction(String prefix) {
       linkpid = Configuration.getPid(prefix + "." + LINKABLE_PROT);
   }

   @Override
   public void nextCycle(Node node, int protocolID) {

       KcorenessFunction currentNode = (KcorenessFunction) node.getProtocol(protocolID);
       Linkable link = (Linkable) node.getProtocol(linkpid);
       int neighborCoreness;
       KcorenessFunction neighborNode;
       int neighborID ;

       // Initialization send (currentNode,currentNode.getCoreness) to neighbors(currentNode)

          if (link.degree() > 0) {
        	  
              for (int i=0; i<link.degree(); i++) {
            	  
                   neighborNode = (KcorenessFunction) link.getNeighbor((Integer)i).getProtocol(protocolID);
                   neighborID=(int) link.getNeighbor(i).getID();
                   neighborCoreness = neighborNode.getCoreness();

                   int ncore = (Integer)currentNode.getEstimation().get(neighborID);

                   if (neighborCoreness < ncore){
                       currentNode.getEstimation().put(neighborID, neighborCoreness);

                       //recalculating the estimation of local coreness
                       int t = ComputeIndex(currentNode.getEstimation(),currentNode, currentNode.getCoreness(),node);

                       if (t < currentNode.getCoreness()) {
                           currentNode.setCoreness(t);
                           currentNode.setChanged(true);
                       }
                   }
             }
      
            
    }

  }

    /** ComputeIndex compute the new temporary estimation of coreness
     * @param estimation
     * @param currentNode
     * @param k
     * @return the largest value i such that there are at least i entries; >= than i in neighbors estimation
     */

  public int ComputeIndex(HashMap<Integer,Integer> estimation, KcorenessFunction currentNode,int k , Node node){

      Linkable link = (Linkable) node.getProtocol(linkpid);
      int[] counts = new int[currentNode.getCoreness() + 1];
      int i,j;

      for (i = 1; i < currentNode.getCoreness(); i++) {
          counts[i] = 0;
      }

      //compute how many nodes have estimated coreness >=i and store this value in array counts

      for (i = 0; i < estimation.size(); i++) {

          int neighborID=(int) link.getNeighbor(i).getID();

          j = (k < (int) estimation.get(neighborID)) ? k : (int) estimation.get(neighborID);
          counts[j] = counts[j] + 1;
      }

      for (i = k; i >= 2; i--) {
          counts[i - 1] = counts[i - 1] + counts[i];
      }

      i=k;

      //Searching the largest value i such that count[i]>=i

      while (i > 1 && counts[i] < i) {
          i--;
      }

      return i;

  }


}
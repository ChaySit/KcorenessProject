package projet.Kcoreness;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Linkable;
import peersim.core.Network;
import peersim.core.Node;

import java.util.Iterator;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

/**
 *  A controller that gets the Kcore of each node and its neighbors
 **/
public class KcorenessObserver implements Control{
	
	/* Parameter for Linkable protocol identifier */
	private static final String PAR_PROT = "protocol";
	
	/* Protocol identifier : obtained from config property {@link #PAR_PROT} */
	private static int pid;
	
	 // TODO : Animation of the graph
	 /*public void explore(SingleNode source) {
             source.setAttribute("ui.class", "marked");
             sleep();  
     }
     protected void sleep() {
         try { Thread.sleep(3000); } catch (Exception e) {}
     }*/

	/* StyleSheet (CSS for GraphStream) */ 
    protected String styleSheet =
            "node {" +
            "	fill-color: black;" +
            "}" +
            "node.marked {" +
            "	fill-color: red;" +
            "}";
	
	/* Constructor */
    public KcorenessObserver(String prefix){
    	// get identifier of linkable protocol 
        pid = Configuration.getPid(prefix + "."+PAR_PROT);
    }
      
    /* return true if the simulation has to be stopped */
	@Override
	public boolean execute() {
				
		/*** Graph à visualiser avec graphstream ***/ 
		SingleGraph graph = new SingleGraph("Kcoreness graph");
        // Nodes //
		for(int i=0;i<16;i++){
			graph.addNode("n"+i);
		}
		// Edges //	
        // Lignes 
     	graph.addEdge("l10", "n0", "n1");
     	graph.addEdge("l11", "n1", "n2");
     	graph.addEdge("l12", "n2", "n3");
     	// =============================		
     	graph.addEdge("l20", "n4", "n5");
     	graph.addEdge("l21", "n5", "n6");
     	graph.addEdge("l22", "n6", "n7");
        // =============================		
     	graph.addEdge("l30", "n8", "n9");
     	graph.addEdge("l31", "n9", "n10");
     	graph.addEdge("l32", "n10", "n11");
        // =============================		
     	graph.addEdge("l40", "n12", "n13");
     	graph.addEdge("l41", "n13", "n14");
     	graph.addEdge("l42", "n14", "n15");			
     	// Colonnes 
     	graph.addEdge("c10", "n0", "n4");
     	graph.addEdge("c11", "n4", "n8");
     	graph.addEdge("c12", "n8", "n12");
        // =============================		
     	graph.addEdge("c20", "n1", "n5");
     	graph.addEdge("c21", "n5", "n9");
     	graph.addEdge("c22", "n9", "n13");
        // =============================		
     	graph.addEdge("c30", "n2", "n6");
     	graph.addEdge("c31", "n6", "n10");
     	graph.addEdge("c32", "n10", "n14");
        // =============================		
     	graph.addEdge("c40", "n3", "n7");
     	graph.addEdge("c41", "n7", "n11");
     	graph.addEdge("c42", "n11", "n15");
     	// Display and adding style 
     	graph.addAttribute("ui.stylesheet", styleSheet);
		graph.display();
		
		Node peer = null;
		KcorenessFunction currentNode;
		
		for (int i=0; i< Network.size(); i++){
			peer = Network.get(i);
			currentNode = (KcorenessFunction) peer.getProtocol(pid);
			// Diplay console 
			System.out.println("Peer "+peer.getID()+ " has Kcoreness = "+ currentNode.getCoreness());
			System.out.println("the estimation of its neighbors's coreness " + currentNode.getEstimation());
			
	         // Stockage du kcore de chaque node sur les nodes du graphStream
	         SingleNode n = graph.getNode("n"+i);
	         n.setAttribute("kcore",currentNode.getCoreness());
	         n.setAttribute("ID",peer.getID());
	         n.setAttribute("NeighborsCoreness",currentNode.getEstimation());
	         
	         // Graphic display without coreness of neighbors
	         n.addAttribute("ui.label","Peer"+n.getAttribute("ID")+" Kcore="+n.getAttribute("kcore"));
	         
	         // Graphic display with coreness of neighbors 
	         //n.addAttribute("ui.label","Peer"+n.getAttribute("ID")+" Kcore="+n.getAttribute("kcore")+"NeighborsCoreness "+n.getAttribute("NeighborsCoreness"));    
		}
				
		// TODO : Animation of the graph
		/*for(int i=0;i<16;i++){
			explore(graph.getNode("n"+i));
		}*/
			
		return false;
	}
}

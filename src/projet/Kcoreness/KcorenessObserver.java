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
		for(int i=0;i<Network.size();i++){
			graph.addNode("n"+i);
		}
		// Edges //
		for (int m=0; m<Math.sqrt(Network.size()); m++){
		if (m != Math.sqrt(Network.size())-1){
			for(int i=(int) (m*Network.size()/Math.sqrt(Network.size())); i<((m+1)*Network.size()/Math.sqrt(Network.size())); ++i) {
			    if (i != (m+1)*Network.size()/Math.sqrt(Network.size())-1){
			    	graph.addEdge("l"+i+(i+1),"n"+i,"n"+(i+1));
			    }
			    graph.addEdge("c"+i+(i+1),"n"+i,"n"+(int) (i+Math.sqrt(Network.size())));
		    }
		}
		else {
			for(int i=(int) ((Math.sqrt(Network.size())-1)*Network.size()/Math.sqrt(Network.size())); i<Network.size()-1; ++i) {
				graph.addEdge("l"+i+(i+1),"n"+i,"n"+(i+1));
		    }
		 }
	    }
     	// Display and add style //
     	graph.addAttribute("ui.stylesheet", styleSheet);
		graph.display();
		/* *************************************************** */
		
		Node peer = null;
		KcorenessFunction currentNode;
		
		for (int i=0; i< Network.size(); i++){
			peer = Network.get(i);
			currentNode = (KcorenessFunction) peer.getProtocol(pid);
			// Diplay console 
			System.out.println("Peer "+peer.getID()+ " has Kcoreness = "+ currentNode.getCoreness());
			System.out.println("the estimation of its neighbors's coreness " + currentNode.getEstimation());
			
	         // Storing the kcore of each node on the nodes of the graphStream
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
		/*for(int i=0;i<Network.size();i++){
			explore(graph.getNode("n"+i));
		}*/
			
		return false;
	}
}

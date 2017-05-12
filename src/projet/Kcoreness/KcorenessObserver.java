package projet.Kcoreness;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Linkable;
import peersim.core.Network;
import peersim.core.Node;
import peersim.graph.Graph;
import peersim.graph.Parser;

import java.util.Iterator;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

/**
 *  A controller that gets the Kcore of each node and its neighbors
 **/
public class KcorenessObserver implements Control{

	/* Parameters for Linkable protocol identifier */
	private static final String PAR_PROT = "protocol";
	private static final String LINKABLE_PROT = "linkable";

	/* Protocol identifiers : obtained from config property {@link #PAR_PROT} */
	private static int pid;
	private static int linkpid;

	/* Constructor */
	public KcorenessObserver(String prefix){
		// get identifiers of linkable protocol 
		pid = Configuration.getPid(prefix + "."+PAR_PROT);
		linkpid = Configuration.getPid(prefix + "." + LINKABLE_PROT);
	}


	/** 
	 * return true if the simulation has to be stopped 
	 * **/
	@Override
	public boolean execute() {

		// Construction of graphStream graph 	
		SingleGraph graph = new SingleGraph("Kcoreness graph");

		// Nodes
		for(int i=0; i<Network.size(); i++){
			graph.addNode("n"+i);
		}


		for (int i=0; i< Network.size(); i++){

			Node peer = Network.get(i); //Network.get(index)
			KcorenessFunction currentNode = (KcorenessFunction) peer.getProtocol(pid);
			Linkable link = (Linkable) peer.getProtocol(linkpid);
			int currentNodeID = (int) peer.getID();

			/// Edges
			if (link.degree() > 0){
				for(int j=0; j<link.degree(); j++){
					int neighborID = (int) link.getNeighbor(j).getID();
					graph.addEdge("e"+currentNodeID+neighborID,"n"+currentNodeID,"n"+neighborID,true);		
				}	
			}//*/

			// Diplay console 
			System.out.println("Peer "+peer.getID()+ " has Kcoreness = "+ currentNode.getCoreness());
			System.out.println("the estimation of its neighbors coreness " + currentNode.getEstimation());

			// Storing the kcore of each node on the node of the graphStream graph
			SingleNode n = graph.getNode("n"+currentNodeID);
			n.setAttribute("kcore",currentNode.getCoreness());
			n.setAttribute("ID",peer.getID());
			n.setAttribute("NeighborsCoreness",currentNode.getEstimation());

			// Graphic display without coreness neighbors
			n.addAttribute("ui.label","Peer"+n.getAttribute("ID")+" Kcore="+n.getAttribute("kcore"));

			// Graphic display with coreness neighbors 
			//n.addAttribute("ui.label","Peer"+n.getAttribute("ID")+" Kcore="+n.getAttribute("kcore")+"NeighborsCoreness "+n.getAttribute("NeighborsCoreness"));    
		}

		graph.display();	
		return false;
	}
}

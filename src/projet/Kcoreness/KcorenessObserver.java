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
 *  A controller that displays the topology of the network including the kcoreness of each node during a cycle
 **/
public class KcorenessObserver implements Control{

	/* Parameter for kcoreness protocol identifier */
	private static final String PAR_PROT = "protocol";
    /* Parameter for Linkable protocol identifier */
	private static final String LINKABLE_PROT = "linkable";

	/* Protocol identifiers : obtained from config property {@link #PAR_PROT} */
	private static int pid;
	private static int linkpid;

	/* Constructor */
	public KcorenessObserver(String prefix){
		// get identifiers of the protocols
		pid = Configuration.getPid(prefix + "."+PAR_PROT);
		linkpid = Configuration.getPid(prefix + "." + LINKABLE_PROT);
	}

	/* StyleSheet (CSS for GraphStream) */
    protected String styleSheet =
            "node.kcore2 {" +
            "	fill-color: green;" +
            "}" +
            "node.kcore3 {" +
            "	fill-color: red;" +
            "}"+
            "node.kcore4 {" +
            "	fill-color: blue;" +
            "}";


	/**
	 * return true if the simulation has to be stopped
	 * **/
	@Override
	public boolean execute() {

		// Construction of graphStream graph
		SingleGraph graph = new SingleGraph("Kcoreness graph");

		// Adding nodes to the graph
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
                   /* creates an edge with an id= eNode-Neighbor between the node and its neighbor     */
					graph.addEdge("e"+currentNodeID+"-"+neighborID,"n"+currentNodeID,"n"+neighborID,true);		
				}	
			}//*/

			/*// Edges for remove 
			Set<Integer> set = currentNode.getEstimation().keySet();
			Object[] array = (Object[]) set.toArray();
			for(int j=0; j<array.length; j++){
				int neighborID = (int) array[j];
				graph.addEdge("e"+currentNodeID+"-"+neighborID,"n"+currentNodeID,"n"+neighborID,true);	
			}//*/

			// Diplays a node with its kcoreness and the estimation of its neighbors kcoreness
			System.out.println("Peer "+peer.getID()+ " has Kcoreness = "+ currentNode.getCoreness());
			System.out.println("the estimation of its neighbors coreness " + currentNode.getEstimation());

			/*Storing the kcoreness of each node on the graphStream graph */
			SingleNode node = graph.getNode("n"+currentNodeID);

			if(node!=null){

				node.setAttribute("kcore",currentNode.getCoreness());
				node.setAttribute("ID",peer.getID());
				node.setAttribute("NeighborsCoreness",currentNode.getEstimation());

				/*Graphic display of a node with a color that identifies its kcoreness using the CSS stylesheet declared above*/
				if (currentNode.getCoreness() == 2) {
					node.addAttribute("ui.label","Peer"+node.getAttribute("ID")+" Kcore="+node.getAttribute("kcore"));
					node.setAttribute("ui.class", "kcore2"); // make the node appear as important.
				}
				if(currentNode.getCoreness() == 3) {
					node.addAttribute("ui.label","Peer"+node.getAttribute("ID")+" Kcore="+node.getAttribute("kcore"));
					node.setAttribute("ui.class", "kcore3");
				}
				if(currentNode.getCoreness() == 4) {
					node.addAttribute("ui.label","Peer"+node.getAttribute("ID")+" Kcore="+node.getAttribute("kcore"));
					node.setAttribute("ui.class", "kcore4");
				}

				//In this case the node will use the default display style
				else {
					node.addAttribute("ui.label","Peer"+node.getAttribute("ID")+" Kcore="+node.getAttribute("kcore"));
				}


			}else{
				System.out.println("Current node may be removed");
			}


			}


		//adding the css stylesheet to the graph
		graph.addAttribute("ui.stylesheet", styleSheet);

		graph.display();
		return false;
	}
}

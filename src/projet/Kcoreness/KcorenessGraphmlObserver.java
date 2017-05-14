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
public class KcorenessGraphmlObserver implements Control{
	
	public static final String GRAPHML_WRITING_PATH = "D:\\Workspace\\peerSim\\graphs\\";
	
	/* Parameters for Linkable protocol identifier */
	private static final String PAR_PROT = "protocol";
	private static final String LINKABLE_PROT = "linkable";
	

	/* Protocol identifiers : obtained from config property {@link #PAR_PROT} */
	private static int pid;
	private static int linkpid;
	
	private static Parser parser;
	private static int cycle = 0;

	/* Constructor */
	public KcorenessGraphmlObserver(String prefix){
		// get identifiers of linkable protocol 
		pid = Configuration.getPid(prefix + "."+PAR_PROT);
		linkpid = Configuration.getPid(prefix + "." + LINKABLE_PROT);
	
		parser = new Parser();
		parser.setPath(GRAPHML_WRITING_PATH);
		
	}


	/** 
	 * return true if the simulation has to be stopped 
	 * **/
	@Override
	public boolean execute() {
		
		//Creating new file
		parser.createFile(cycle);

		//nodes
		for(int i=0 ; i<Network.size(); i++){  
			Node peer = Network.get(i); //Network.get(index)
			KcorenessFunction currentNode = (KcorenessFunction) peer.getProtocol(pid);
			Linkable link = (Linkable) peer.getProtocol(linkpid);
			int currentNodeID = (int) peer.getID();

			//Node
			parser.createNode((int)peer.getID(), currentNode.getCoreness());
		
			
		}
		for(int i=0 ; i<Network.size(); i++){ 
			
			Node peer = Network.get(i);
			KcorenessFunction currentNode = (KcorenessFunction) peer.getProtocol(pid);
			Linkable link = (Linkable) peer.getProtocol(linkpid);
			int currentNodeID = (int) peer.getID();
			
			//edges
			if (link.degree() > 0){
				for(int j=0; j<link.degree(); j++){
					int neighborID = (int) link.getNeighbor(j).getID();
					parser.createEdge(currentNodeID, neighborID);	
				}
			}
		}
		parser.saveFile(cycle);
		cycle++;
		return false;
	}

}

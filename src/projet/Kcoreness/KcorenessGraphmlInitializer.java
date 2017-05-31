package projet.Kcoreness;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Linkable;
import peersim.core.Network;

/**
 * This Control class is used to initialize a node using Kcoreness loaded from a graphml file
 * */
public class KcorenessGraphmlInitializer implements Control {
    

	
	/**These values are used to parse the configuration file
	 * PAR_PROT represents the protocol of K-Coreness Function
	 * LINKABLE_PROT represents the Linkable protocol
	 * */
    //Parameters
    private static final String PAR_PROT = "protocol";
    private static final String LINKABLE_PROT = "linkable";
	public static final String GRAPHML_READING_PATH = "D:\\Workspace\\peerSim\\graphs\\graph1.graphml";
	public static final String NETWORK_SIZE = "network.size" ;

    //Fields
    /**These fields will hold the identifiers of 
     * both the K-Coreness and linkable protocols
     * */
    private static int pid;
    private static int linkpid;
    private static int size;

    //Constructor
    public KcorenessGraphmlInitializer(String prefix){
        pid = Configuration.getPid(prefix + "."+PAR_PROT);
        linkpid = Configuration.getPid(prefix + "." + LINKABLE_PROT);
        size = Configuration.getInt(NETWORK_SIZE);
    }

    /**
     * 
     * This method performs the actual initialization of the local coreness
     * and the linking between neighboors
     * It loads coreness from Graphml file
     * */
    @Override
    public boolean execute() {
    	
    	try {
      		
    		//Reading graphml file
  		    File fXmlFile = new File(GRAPHML_READING_PATH);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            
            //Verifying the size
            NodeList nodeList = doc.getElementsByTagName("node");             
            if(nodeList.getLength()>size){
            	throw new Exception("Graphml nodes size and network.size do not match. Please set network.size to "+ (nodeList.getLength()+1)+" or more.");
            }
            
            
            for (int i=0; i< Network.size(); i++){
            	
            	KcorenessFunction protocol = (KcorenessFunction) Network.get(i).getProtocol(pid);
                Linkable linkable = (Linkable) Network.get(i).getProtocol(linkpid);
                protocol.setChanged(false);

                int nodeID = (int)Network.get(i).getID();
                
                //Loading Kcoreness from node with the right id in the given graphml file .              
                for(int it=0; it <nodeList.getLength();it++){
                	
                    Element eNode = (Element) nodeList.item(it);
                    int graphmlNodeId = Integer.parseInt(eNode.getAttribute("id"));
                   	if (nodeID==graphmlNodeId){       		
                   		Element data  = (Element) nodeList.item(it).getFirstChild();
                        int coreness = Integer.parseInt(data.getTextContent());
                        protocol.setCoreness(coreness);
                   	}
                }

                for (int j=0; j<linkable.degree(); j++){
                	protocol.newEntry(linkable.getNeighbor(j));
                }
            }
            

    	} catch (Exception e) {
	   		e.printStackTrace();
           
   		}

        return false;
    }
}


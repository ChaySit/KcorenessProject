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
import peersim.core.Node;


public class KcorenessGraphmlInitializer implements Control {
    

    //Parameters
	public static final String GRAPHML_READING_PATH = "D:\\Workspace\\peerSim\\graphs\\graph3.graphml";
	public static final String NETWORK_SIZE = "network.size" ;
    private static final String PAR_PROT = "protocol";
    private static final String LINKABLE_PROT = "linkable";

    //Fields
    private static int pid;
    private static int linkpid;
    private static int size;

    //Constructor
    public KcorenessGraphmlInitializer(String prefix){
        pid = Configuration.getPid(prefix + "."+PAR_PROT);
        linkpid = Configuration.getPid(prefix + "." + LINKABLE_PROT);
        size = Configuration.getInt(NETWORK_SIZE);
    }

    @Override
    public boolean execute() {
    	
    	try {
      		
    		//Reading graphml file
  		    File fXmlFile = new File(GRAPHML_READING_PATH);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            
            
            NodeList nodeList = doc.getElementsByTagName("node");             
            if(nodeList.getLength()>size){
            	throw new Exception("Graphml nodes size and network.size do not match. Please set network.size to "+ (nodeList.getLength()+1)+" or more.");
            }
            

            for (int i=0; i< Network.size(); i++){
            	
            	KcorenessFunction protocol = (KcorenessFunction) Network.get(i).getProtocol(pid);
                Linkable linkable = (Linkable) Network.get(i).getProtocol(linkpid);
                protocol.setChanged(false);
                

                //Loading Kcoreness from graphml file to nodes.
                
                /*
                 * TODO fix issue : 
                 * Load id from graphml.
                 * Give right coreness to the right node.
                 */
   
                Element data = (Element) nodeList.item(i).getFirstChild();
                protocol.setCoreness(Integer.parseInt(data.getTextContent()));
                
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


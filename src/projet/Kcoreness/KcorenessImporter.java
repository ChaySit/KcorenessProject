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

/**
 * load graph from graphML file
 */
public class KcorenessImporter implements Control {
    

    //Parameters
    private static final String PAR_PROT = "protocol";
    private static final String LINKABLE_PROT = "linkable";
    public static final String GRAPHML_READING_PATH = "D:\\Workspace\\peerSim\\graphs\\graph0.graphml";


    //Fields
    private static int pid;
    private static int linkpid;

    //Constructor
    public KcorenessImporter(String prefix){
        pid = Configuration.getPid(prefix + "."+PAR_PROT);
        linkpid = Configuration.getPid(prefix + "." + LINKABLE_PROT);
    }

    @Override
    public boolean execute() {
    	
    	try {    
		    File fXmlFile = new File(GRAPHML_READING_PATH);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            
            doc.getDocumentElement().normalize();   
            NodeList edgeList = doc.getElementsByTagName("edge");  
            
           for (int i = 0; i < edgeList.getLength(); i++) {
        	   org.w3c.dom.Node edge = edgeList.item(i);
               if (edge.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {  
				    Element eElement = (Element) edge;
                   int s = Integer.parseInt(eElement.getAttribute("source"));
                   int t = Integer.parseInt(eElement.getAttribute("target"));
                   
                   /*
                    * TODO create nodes/edges
                   KcorenessFunction protocol = (KcorenessFunction) Network.get(i).getProtocol(pid);
                   Linkable linkable = (Linkable) Network.get(i).getProtocol(linkpid);
                   protocol.setChanged(false);
                   protocol.setCoreness(linkable.degree());
                   for (int j=0; j<linkable.degree(); j++){
                       Node peer = linkable.getNeighbor(j);
                       protocol.newEntry(peer);
                     
                   }*/
               }
           }

       } catch (Exception e) {
               e.printStackTrace();
       }	
                
        return false;
    }
}

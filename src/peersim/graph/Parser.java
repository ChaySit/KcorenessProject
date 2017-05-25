package peersim.graph;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Parser {

			
	private DocumentBuilderFactory docFactory;
	private DocumentBuilder docBuilder;
	private Document doc;
	private Element rootElement;
	private Element graph;
	
	private String path;
	
	
	public void setPath(String path){
		this.path = path;
	}
	
	public void createFile(int cycle){

		  try {
			  
			this.docFactory = DocumentBuilderFactory.newInstance();
			this.docBuilder = docFactory.newDocumentBuilder();

			// root elements
			this.doc = docBuilder.newDocument();
			this.rootElement = doc.createElement("graphml");
			doc.appendChild(rootElement);
			
			// set attribute xmlns to root element
			Attr xmlns = doc.createAttribute("xmlns");
			xmlns.setValue("http://graphml.graphdrawing.org/xmlns");
			rootElement.setAttributeNode(xmlns);

			// set attribute xsi to root element
			Attr xsi = doc.createAttribute("xmlns:xsi");
			xsi.setValue("http://www.w3.org/2001/XMLSchema-instance");
			rootElement.setAttributeNode(xsi);
			
			// set attribute schemaLocation to root element
			Attr schemaLocation = doc.createAttribute("xsi:schemaLocation");
			schemaLocation.setValue("http://graphml.graphdrawing.org/xmlnshttp://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd");
			rootElement.setAttributeNode(schemaLocation);
			
			
			
			// key elements
			Element keyCore = doc.createElement("key");
			rootElement.appendChild(keyCore);
			
			// set attribute ID to key element
			Attr keyID = doc.createAttribute("id");
			keyID.setValue("k");
			keyCore.setAttributeNode(keyID);
			
			// set attribute for to key element
			Attr keyFor = doc.createAttribute("for");
			keyFor.setValue("node");
			keyCore.setAttributeNode(keyFor);

			// set attribute attName to key element
			Attr keyName = doc.createAttribute("attr.name");
			keyName.setValue("Kcore");
			keyCore.setAttributeNode(keyName);
			
			// set attribute for to key element
			Attr keyType = doc.createAttribute("attr.type");
			keyType.setValue("int");
			keyCore.setAttributeNode(keyType);
					
					
			// graph elements
			this.graph = doc.createElement("graph");
			rootElement.appendChild(graph);

			// set attribute ID to graph element
			Attr attrID = doc.createAttribute("id");
			attrID.setValue("G");
			graph.setAttributeNode(attrID);
			
			// set attribute edgedefault to graph element
			Attr edgedefault = doc.createAttribute("edgedefault");
			edgedefault.setValue("directed");
			graph.setAttributeNode(edgedefault);

			// set attribute cycle to graph element
			Attr myCycle = doc.createAttribute("cycle");
			myCycle.setValue(""+cycle);
			graph.setAttributeNode(myCycle);

		  } catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		  }
	}
	
	public void createNode(int i){
		Element node = doc.createElement("node");
		
		Attr nodeID = doc.createAttribute("id");
		nodeID.setValue(""+i);
		node.setAttributeNode(nodeID);
		
		graph.appendChild(node);	
	}
	
	
	public void createNode(int i,int core){
		Element node = doc.createElement("node");
				
		Attr nodeID = doc.createAttribute("id");
		nodeID.setValue(""+i);
		node.setAttributeNode(nodeID);
		
		Element data = doc.createElement("data");
		
		Attr key = doc.createAttribute("key");
		key.setValue("k");
		data.setAttributeNode(key);
		
		data.setTextContent(""+core);
		node.appendChild(data);

		
	
		graph.appendChild(node);	
	}
	
	
	public void createEdge(int i, int j){
		
    	Element edge = doc.createElement("edge");
    	
		Attr edgeID = doc.createAttribute("id");
		edgeID.setValue(i+"_"+j);
		edge.setAttributeNode(edgeID);
		
		Attr source = doc.createAttribute("source");
		source.setValue(""+i);
		edge.setAttributeNode(source);
						
		Attr target = doc.createAttribute("target");
		target.setValue(""+j);
		edge.setAttributeNode(target);
		
		graph.appendChild(edge);
		
	}
	
	
	
	public void saveFile(int cycle){
		
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(path+"graph"+cycle+".graphml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("File saved!");
			
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

}

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
	
	public Parser(){

		  try {
			  
			this.docFactory = DocumentBuilderFactory.newInstance();
			this.docBuilder = docFactory.newDocumentBuilder();

			// root elements
			this.doc = docBuilder.newDocument();
			this.rootElement = doc.createElement("grpahml");
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
			schemaLocation.setValue("http://graphml.graphdrawing.org/xmlns");
			rootElement.setAttributeNode(schemaLocation);
			
			// graph elements
			this.graph = doc.createElement("graph");
			rootElement.appendChild(graph);

			// set attribute ID to graph element
			Attr attrID = doc.createAttribute("id");
			attrID.setValue("G");
			graph.setAttributeNode(attrID);
			
			// set attribute edgedefault to graph element
			Attr edgedefault = doc.createAttribute("edgedefault");
			edgedefault.setValue("undirected");
			graph.setAttributeNode(edgedefault);


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

	
	public void saveFile(){
				
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("C:\\Eclipse\\ProjetApplication\\src\\projet\\Kcoreness\\graphML.grpahml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("File saved!");
			
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
	
}

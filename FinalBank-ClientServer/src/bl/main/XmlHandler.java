package bl.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlHandler {
	
	//function that received string of element and path of the xml file and returns the number of elements in the file
	public static int getNumberOfElements(String element, String xmlPath) {
		
		File xmlFile = new File(System.getProperty("user.dir") + File.separator + "res" + File.separator + xmlPath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			
			e.printStackTrace();
		}
		Document doc = null;
		try {
			doc = dBuilder.parse(xmlFile);
		} catch (SAXException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		NodeList nList = doc.getElementsByTagName(element);
		
		return nList.getLength();
	}
	
	//function that returns a node by index
	public static Node getElement(String element, String xmlPath, int index) {
		
		File xmlFile = new File(System.getProperty("user.dir") + File.separator + "res" + File.separator + xmlPath); 
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			
			e.printStackTrace();
		}
		Document doc = null;
		try {
			doc = dBuilder.parse(xmlFile);
		} catch (SAXException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		doc.getDocumentElement().normalize();

		NodeList nlist = doc.getElementsByTagName(element);	
		
		Node nNode = nlist.item(index);

		return nNode;
		
	}
	
	//function that reads a text file and returns a BufferedReader type
	public static BufferedReader readTxtfile(String nameOfFile)	{
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(nameOfFile + ".txt"));
				
			return reader;

		} catch (IOException e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	
}

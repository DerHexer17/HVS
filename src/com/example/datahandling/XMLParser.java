package com.example.datahandling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.example.hvs.R;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

public class XMLParser {

	private Document xmlLigen;
	private Context context;
	String l = "XML";
	
	public XMLParser(Context context){
		this.context = context;
	}

	public String LoadLigenFile() {
	    // The InputStream opens the resourceId and sends it to the buffer
	    InputStream is = context.getResources().openRawResource(R.raw.ligen_xml);
	    BufferedReader br = new BufferedReader(new InputStreamReader(is));
	    String readLine = null;
	    String result = null;

	    try {
	        // While the BufferedReader readLine is not null 
	        while ((readLine = br.readLine()) != null) {
	        	result += readLine;
	    }

	    // Close the InputStream and BufferedReader
	    is.close();
	    br.close();

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    result = "<?xml"+result.split("<?xml")[1];
	    //result = result.replaceAll("[^\\x20-\\x7e]", "");
	    return result;
	    
	}
	
	public void setXMLLigenFromFile(){
		Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
 
            DocumentBuilder db = dbf.newDocumentBuilder();
            
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(LoadLigenFile()));

            doc = db.parse(is); 
 
            } catch (ParserConfigurationException e) {
                Log.e("Error: ", e.getMessage());
                
            } catch (SAXException e) {
                Log.e("Error: ", e.getMessage());
                
            } catch (IOException e) {
                Log.e("Error: ", e.getMessage());
                
            }catch(Exception e){
            	Log.d(l, e.getMessage());
            }
                // return DOM
        this.xmlLigen = doc;
	}
	
	public List<Liga> createLigen(List<Liga> ligen){

		setXMLLigenFromFile();
		
		Log.d(l, "Doc Nodes: "+xmlLigen.getElementsByTagName("liga").getLength());
		NodeList nodes = xmlLigen.getElementsByTagName("liga");
		for(int i = 0; i < nodes.getLength(); i++){
			NodeList childNodes = nodes.item(i).getChildNodes();
			Liga l = new Liga();
			for(int k = 0; k < childNodes.getLength(); k++){
				switch(childNodes.item(k).getNodeName()){
				case "name":
					l.setName(childNodes.item(k).getTextContent());
					break;
				case "nummer":
					l.setLigaNr(Integer.parseInt(childNodes.item(k).getTextContent()));
					break;
				case "link":
					l.setLink(childNodes.item(k).getTextContent());
					break;
				case "ebene":
					l.setEbene(childNodes.item(k).getTextContent());
					break;
				case "geschlecht":
					l.setGeschlecht(childNodes.item(k).getTextContent());
					break;
				case "saison":
					l.setSaison(childNodes.item(k).getTextContent());
					break;
				case "pokal":
					l.setPokal(Integer.parseInt(childNodes.item(k).getTextContent()));
					break;
				case "jugend":
					Log.d("XML", childNodes.item(k).getTextContent());
					l.setJugend(childNodes.item(k).getTextContent());
					break;
				default: 	
				}
				
			}
			
			l.setInitial("Nein");
			ligen.add(l);
		}
		
		return ligen;
	}
}

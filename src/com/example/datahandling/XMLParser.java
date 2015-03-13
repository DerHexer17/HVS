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
		Log.d(l, "XML Parser Objekt erfolgreich instanziiert");
	}

	public String LoadText() {
	    // The InputStream opens the resourceId and sends it to the buffer
	    InputStream is = context.getResources().openRawResource(R.raw.ligen_xml);
	    Log.d(l, "Input Stream");
	    BufferedReader br = new BufferedReader(new InputStreamReader(is));
	    String readLine = null;
	    String result = null;
	    
	    Log.d(l, "Methode LoadText konnte normal gestartet werden");
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
	    Log.d(l, result);
	    result = result.replaceAll("[^\\x20-\\x7e]", "");
	    return result;
	    
	}
	
	public void setXMLLigenFromFile(){
		Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
 
            DocumentBuilder db = dbf.newDocumentBuilder();
            Log.d(l, "XML from file methode gestartet");
            InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(LoadText()));

                doc = db.parse(is); 
                Log.d(l, "Das Doc konnte geparset werden");
 
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
        Log.d(l, "doc: "+doc.toString());
        this.xmlLigen = doc;
	}
	
	public List<Liga> createLigen(List<Liga> ligen){

		setXMLLigenFromFile();
		Log.d(l, "Doc Nodes: "+xmlLigen.getElementsByTagName("liga").getLength());
		
		return ligen;
	}
}

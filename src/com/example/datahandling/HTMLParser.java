package com.example.datahandling;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import android.util.Log;

public class HTMLParser {

	String TAG = "HTML";
	private ArrayList<Spiel> alleSpiele;
	private ArrayList<String> trimHTMLList;
	private Map<Integer, String> hallenMap;

	DatabaseHelper dbh;

	/* Main Method for Parsing the HTML from "Liste aller Spiele" */
	public ArrayList<Spiel> initialHTMLParsing(String source, int ligaNr) {
		// Split the Source into the Rows of the Table of all Games
		String[] trimHTML = source.split("</tr>");
		trimHTML = trimHTML[1].split("</TR>");
		trimHTMLList = new ArrayList<String>();

		// Need to delete just the last position of the array
		for (int i = 0; i < trimHTML.length - 1; i++) {
			trimHTMLList.add(trimHTML[i]);
		}

		alleSpiele = new ArrayList<Spiel>();
		hallenMap = new HashMap<Integer, String>();

		// Call splitTableRow for every Game-Object
		for (String s : trimHTMLList) {
			Spiel spiel = splitTableRow(s);
			spiel.setLigaNr(ligaNr);
			alleSpiele.add(spiel);
		}

		Log.d(TAG, "Size von alleSpiele: " + alleSpiele.size());

		return alleSpiele;
	}
	
	public Spiel splitTableRow(String spielSource) {
		String[] tds = spielSource.split("</TD");
		Spiel spiel = new Spiel();

		String numberStr = tds[0];
		String dateStr = tds[1];
		String timeStr = tds[2];
		String teamHomeStr = tds[3];
		String teamGuestStr = tds[4];
		String goalOrRefStr = tds[5];
		String fieldStr;
		
		spiel.setDate(new java.sql.Date(parseDate(dateStr, timeStr).getTime()));
		
		spiel.setSpielNr(parseNumber(numberStr));	

		spiel.setTeamHeim(parseTeam(teamHomeStr));
		spiel.setTeamGast(parseTeam(teamGuestStr));

		if(!goalOrRefStr.contains(":")){
			parseReferee(goalOrRefStr);
			fieldStr = tds[6];
			spiel.setHalle(parseField(fieldStr));
		}else{
			int[] goals = parseGoals(goalOrRefStr);
			if(goals[0] == -1){
				fieldStr = tds[7];
				spiel.setHalle(parseField(fieldStr));
			}else{
				String pointsStr = tds[6];
				int[] points = parsePoints(pointsStr);
				spiel.setToreHeim(goals[0]);
				spiel.setToreGast(goals[1]);
				spiel.setPunkteHeim(points[0]);
				spiel.setPunkteGast(points[1]);
			}
		}

		return spiel;
	}
	
	private int parseNumber(String numberStr){
		String[] temp = numberStr.split("</FONT");
		temp = temp[0].split(">");
		return Integer.parseInt(temp[temp.length - 1]);
	}
	
	private Date parseDate(String dateStr, String timeStr){
		Date date;
		String[] temp = dateStr.split("</FONT");
		temp = temp[0].split(">");
		String tempDate = temp[temp.length - 1];
		temp = tempDate.split(" ");
		String partDate = temp[1];
		
		temp = timeStr.split("</FONT");
		temp = temp[0].split(">");
		String partTime = temp[temp.length - 1];
		
		String newDate = partDate + " " + partTime;
		
		try{
			SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMANY);
			date = formatter.parse(newDate);
			return date;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	private String parseTeam(String teamStr){
		String[] temp = teamStr.split("</FONT");
		temp = temp[0].split("</a>");
		temp = temp[0].split(">");
		return temp[temp.length - 1];
	}
	
	private int[] parseGoals(String goalStr){
		int[] goals = new int[2];
		String[] temp = goalStr.split("</FONT");
		temp = temp[0].split(">");
		String tempGoals = temp[temp.length - 1];
		if(tempGoals.equals(":")){
			goals[0] = -1;
			return goals;
		}else{
			temp = tempGoals.split(":");
			goals[0] = Integer.parseInt(temp[0].trim());
			goals[1] = Integer.parseInt(temp[1].trim());
			return goals;
		}
	}
	
	private String parseReferee(String refStr){
		String[] temp = refStr.split("</FONT");
		temp = temp[0].split(">");
		return temp[temp.length - 1];
	}
	
	private int parseField(String fieldStr){

		String[] temp = fieldStr.split("</FONT>");
		temp = temp[0].split("<a href=");
		temp = temp[1].split(">");
		
		int tempHallenNr = Integer.parseInt(temp[1].split("<")[0]);
		
		String tempHallenLink = temp[0].split("\\.\\.")[1].split(" ")[0];
		hallenMap.put(tempHallenNr, "http://hvs-handball.de"+tempHallenLink);

		return tempHallenNr;
	}
	
	private int[] parsePoints(String pointsStr){
		int[] points = new int[2];
		String[] temp = pointsStr.split("</FONT");
		temp = temp[0].split(">");
		String tempPoints = temp[temp.length - 1];
		temp = tempPoints.split(":");
		points[0] = Integer.parseInt(temp[0].trim());
		points[1] = Integer.parseInt(temp[1].trim());
		return points;
	}

	
	//Diese Methode wird separat aus dem AsyncHttpTask aufgerufen um erstmal eine Liste der Hallen zu erzeugen, die neu geparst werden müssen
	public Map<Integer, String> getUnsavedHallenLinkListe(List<Halle> alleHallen){
		
		for(Halle h : alleHallen){
			Log.d("Benni", "removed halle: "+h.getHallenNr());
			hallenMap.remove(h.getHallenNr());
		}
		
		for(Entry<Integer, String> e : hallenMap.entrySet()){
			Log.d("Hallenmap", e.getKey()+", "+e.getValue());
		}
		return hallenMap;
	}
	
	public Halle hallenHTMLParsing(String s, int hallenNr){
		Halle h = new Halle();
		h.setHallenNr(hallenNr);
		h.setName(s.split("text-g1\">")[1].split("<")[0]);
		
		String tempAdresse = s.split("t12b\">")[1].split("<")[0];
		String[] tempHausnummer = tempAdresse.split(",")[0].split(" ");
		
		/*
		 * Teilweise sind die Straßen ungenau auf der Website eingetragen
		 * Entweder ohne Leerzeichen zwischen Straße und Nummer
		 * oder ganz ohne Nummer
		 * Das müssen wir hier abfangen
		 */
		try{
			h.setHausnummer(Integer.parseInt(tempHausnummer[tempHausnummer.length-1]));
			Log.d("Benni", "Hausnummer "+h.getHausnummer());
			Integer hs = Integer.valueOf(h.getHausnummer());
			h.setStrasse(tempAdresse.split(",")[0].split(hs.toString())[0].trim());
		}catch(NumberFormatException ex){
			Log.d("Hallen", "Bei der Hausnummer von Halle "+h.getName()+" gab es Probleme");
			h.setStrasse(tempAdresse.split(",")[0].trim());
		}		
		
		h.setPlz(tempAdresse.split(",")[1].split(" ")[1]);
		String[] tempPlz = tempAdresse.split(",")[1].split(h.getPlz());
		
		h.setOrt(tempPlz[tempPlz.length-1].trim());
				
		return h;
	}

}

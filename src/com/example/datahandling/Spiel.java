package com.example.datahandling;
import java.util.Date;

public class Spiel {

	private int spielNr;
	private Date date;
	private String teamHeim;
	private String teamGast;
	private int toreHeim;
	private int toreGast;
	private int punkteHeim;
	private int punkteGast;
	private String schiedsrichter;
	private int halle;
	private int ligaNr;
	private int spieltagsNr;
	private int spieltagsID;

	public Spiel() {

	}

	public int getSpielNr() {
		return spielNr;
	}

	public void setSpielNr(int spielNr) {
		this.spielNr = spielNr;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTeamHeim() {
		return teamHeim;
	}

	public void setTeamHeim(String teamHeim) {
		this.teamHeim = teamHeim;
	}

	public String getTeamGast() {
		return teamGast;
	}

	public void setTeamGast(String teamGast) {
		this.teamGast = teamGast;
	}

	public int getToreHeim() {
		return toreHeim;
	}

	public void setToreHeim(int toreHeim) {
		this.toreHeim = toreHeim;
	}

	public int getToreGast() {
		return toreGast;
	}

	public void setToreGast(int toreGast) {
		this.toreGast = toreGast;
	}

	public int getPunkteHeim() {
		return punkteHeim;
	}

	public void setPunkteHeim(int punkteHeim) {
		this.punkteHeim = punkteHeim;
	}

	public int getPunkteGast() {
		return punkteGast;
	}

	public void setPunkteGast(int punkteGast) {
		this.punkteGast = punkteGast;
	}

	public String getSchiedsrichter() {
		return schiedsrichter;
	}

	public void setSchiedsrichter(String schiedsrichter) {
		this.schiedsrichter = schiedsrichter;
	}

	public int getHalle() {
		return halle;
	}

	public void setHalle(int halle) {
		this.halle = halle;
	}

	public int getLigaNr() {
		return ligaNr;
	}

	public void setLigaNr(int ligaNr) {
		this.ligaNr = ligaNr;
	}

	public int getSpieltagsNr() {
		return spieltagsNr;
	}

	public void setSpieltagsNr(int spieltagsNr) {
		this.spieltagsNr = spieltagsNr;
	}

	public int getSpieltagsID() {
		return spieltagsID;
	}

	public void setSpieltagsID(int spieltagsID) {
		this.spieltagsID = spieltagsID;
	}

}

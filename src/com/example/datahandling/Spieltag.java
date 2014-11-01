package com.example.datahandling;

public class Spieltag {

	private int spieltags_ID;
	private int ligaNr;
	private int spieltags_Nr;
	private String spieltags_Name;
	private String datumBeginn;
	private String datumEnde;
	private String saison;

	public int getSpieltags_ID() {
		return spieltags_ID;
	}

	public void setSpieltags_ID(int spieltags_ID) {
		this.spieltags_ID = spieltags_ID;
	}

	public int getLigaNr() {
		return ligaNr;
	}

	public void setLigaNr(int ligaNr) {
		this.ligaNr = ligaNr;
	}

	public int getSpieltags_Nr() {
		return spieltags_Nr;
	}

	public void setSpieltags_Nr(int spieltags_Nr) {
		this.spieltags_Nr = spieltags_Nr;
	}

	public String getSpieltags_Name() {
		return spieltags_Name;
	}

	public void setSpieltags_Name(String spieltags_Name) {
		this.spieltags_Name = spieltags_Name;
	}

	public String getDatumBeginn() {
		return datumBeginn;
	}

	public void setDatumBeginn(String datumBeginn) {
		this.datumBeginn = datumBeginn;
	}

	public String getDatumEnde() {
		return datumEnde;
	}

	public void setDatumEnde(String datumEnde) {
		this.datumEnde = datumEnde;
	}

	public String getSaison() {
		return saison;
	}

	public void setSaison(String saison) {
		this.saison = saison;
	}

}

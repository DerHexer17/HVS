package com.example.datahandling;

public class Liga {

	private int ligaNr;
	private String name;
	private String ebene;
	private String geschlecht;
	private String jugend;
	private String saison;
	private String link;
	private int pokal;
	private String initial;
	private boolean favorit;

	public int getLigaNr() {
		return ligaNr;
	}

	public void setLigaNr(int ligaNr) {
		this.ligaNr = ligaNr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEbene() {
		return ebene;
	}

	public void setEbene(String ebene) {
		this.ebene = ebene;
	}

	public String getGeschlecht() {
		return geschlecht;
	}

	public void setGeschlecht(String geschlecht) {
		this.geschlecht = geschlecht;
	}

	public String getJugend() {
		return jugend;
	}

	public void setJugend(String jugend) {
		this.jugend = jugend;
	}

	public String getSaison() {
		return saison;
	}

	public void setSaison(String saison) {
		this.saison = saison;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getPokal() {
		return pokal;
	}

	public void setPokal(int pokal) {
		this.pokal = pokal;
	}

	public String getInitial() {
		return initial;
	}

	public void setInitial(String initial) {
		this.initial = initial;
	}

	public boolean isFavorit() {
		return favorit;
	}

	public void setFavorit(boolean favorit) {
		this.favorit = favorit;
	}

}

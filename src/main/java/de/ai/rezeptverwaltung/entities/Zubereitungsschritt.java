package de.ai.rezeptverwaltung.entities;

import java.util.LinkedList;

public class Zubereitungsschritt {

	private int schrittId;
	private String beschreibung;
	
	private int schrittNummer;
	
	private LinkedList<Werkzeug> werkzeuge;
	private LinkedList<Bild> bilder;
	
	public int getSchrittId() {
		return schrittId;
	}
	public void setSchrittId(int schrittId) {
		this.schrittId = schrittId;
	}
	public String getBeschreibung() {
		return beschreibung;
	}
	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}
	public LinkedList<Werkzeug> getWerkzeuge() {
		return werkzeuge;
	}
	public void setWerkzeuge(LinkedList<Werkzeug> werkzeuge) {
		this.werkzeuge = werkzeuge;
	}
	public LinkedList<Bild> getBilder() {
		return bilder;
	}
	public void setBilder(LinkedList<Bild> bilder) {
		this.bilder = bilder;
	}
	public int getSchrittNummer() {
		return schrittNummer;
	}
	public void setSchrittNummer(int schrittNummer) {
		this.schrittNummer = schrittNummer;
	}
	
}

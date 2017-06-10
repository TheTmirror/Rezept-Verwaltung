package de.ai.rezeptverwaltung.entities;

public class Bewertung {

	private int bewertungId;
	private int rezeptId;
	private int punkte;
	
	public int getBewertungId() {
		return bewertungId;
	}
	public void setBewertungId(int bewertungId) {
		this.bewertungId = bewertungId;
	}
	public int getRezeptId() {
		return rezeptId;
	}
	public void setRezeptId(int rezeptId) {
		this.rezeptId = rezeptId;
	}
	public int getPunkte() {
		return punkte;
	}
	public void setPunkte(int punkte) {
		this.punkte = punkte;
	}
	
}
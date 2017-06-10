package de.ai.rezeptverwaltung.entities;

public class Speisekarte_Rezept {

	private int speisekarteId;
	private int rezeptId;
	private int preis;
	
	public int getSpeisekarteId() {
		return speisekarteId;
	}
	public void setSpeisekarteId(int speisekarteId) {
		this.speisekarteId = speisekarteId;
	}
	public int getRezeptId() {
		return rezeptId;
	}
	public void setRezeptId(int rezeptId) {
		this.rezeptId = rezeptId;
	}
	public int getPreis() {
		return preis;
	}
	public void setPreis(int preis) {
		this.preis = preis;
	}
	
}
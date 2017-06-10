package de.ai.rezeptverwaltung.entities;

public class Rezept {

	private int rezeptId;
	private int bildId;
	private int kategorieId;
	private int gesamtbewertung;
	private String bezeichnung;
	
	public int getRezeptId() {
		return rezeptId;
	}
	public void setRezeptId(int rezeptId) {
		this.rezeptId = rezeptId;
	}
	public int getBildId() {
		return bildId;
	}
	public void setBildId(int bildId) {
		this.bildId = bildId;
	}
	public int getKategorieId() {
		return kategorieId;
	}
	public void setKategorieId(int kategorieId) {
		this.kategorieId = kategorieId;
	}
	public int getGesamtbewertung() {
		return gesamtbewertung;
	}
	public void setGesamtbewertung(int gesamtbwertung) {
		this.gesamtbewertung = gesamtbwertung;
	}
	public String getBezeichnung() {
		return bezeichnung;
	}
	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}
	
}

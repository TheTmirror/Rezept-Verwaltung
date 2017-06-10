package de.ai.rezeptverwaltung.entities;

import java.util.LinkedList;

public class Rezept {

	private int rezeptId;
	private int bildId;
	private int kategorieId;
	private double gesamtbewertung;
	private String bezeichnung;
	
	//NICHT ERD KONFORM!!!! (UND ST AUCH NICHT)
	private Kategorie kategorie;
	private LinkedList<Schlagwort> schlagworte;
	private LinkedList<Zubereitungsschritt> zubereitungsschritte;
	private Bild bild;
	private LinkedList<Zutat_Rezept> zutat_rezept;
	private LinkedList<Kommentar> kommentare;
	private LinkedList<Bewertung> bewertungen;
	private Freigabe freigabe;
	
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
	public double getGesamtbewertung() {
		return gesamtbewertung;
	}
	public void setGesamtbewertung(double gesamtbwertung) {
		this.gesamtbewertung = gesamtbwertung;
	}
	public String getBezeichnung() {
		return bezeichnung;
	}
	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}
	public Kategorie getKategorie() {
		return kategorie;
	}
	public void setKategorie(Kategorie kategorie) {
		this.kategorie = kategorie;
	}
	public LinkedList<Schlagwort> getSchlagworte() {
		return schlagworte;
	}
	public void setSchlagworte(LinkedList<Schlagwort> schlagworte) {
		this.schlagworte = schlagworte;
	}
	public LinkedList<Zubereitungsschritt> getZubereitungsschritte() {
		return zubereitungsschritte;
	}
	public void setZubereitungsschritte(LinkedList<Zubereitungsschritt> zubereitungsschritte) {
		this.zubereitungsschritte = zubereitungsschritte;
	}
	public Bild getBild() {
		return bild;
	}
	public void setBild(Bild bild) {
		this.bild = bild;
	}
	public LinkedList<Zutat_Rezept> getZutat_rezept() {
		return zutat_rezept;
	}
	public void setZutat_rezept(LinkedList<Zutat_Rezept> zutat_rezept) {
		this.zutat_rezept = zutat_rezept;
	}
	public LinkedList<Kommentar> getKommentare() {
		return kommentare;
	}
	public void setKommentare(LinkedList<Kommentar> kommentare) {
		this.kommentare = kommentare;
	}
	public LinkedList<Bewertung> getBewertungen() {
		return bewertungen;
	}
	public void setBewertungen(LinkedList<Bewertung> bewertungen) {
		this.bewertungen = bewertungen;
	}
	public Freigabe getFreigabe() {
		return freigabe;
	}
	public void setFreigabe(Freigabe freigabe) {
		this.freigabe = freigabe;
	}
	
}

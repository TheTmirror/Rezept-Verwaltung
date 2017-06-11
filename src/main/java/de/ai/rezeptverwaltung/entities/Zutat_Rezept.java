package de.ai.rezeptverwaltung.entities;

public class Zutat_Rezept {

	private int rezeptId;
	private int zutatId;
	private int einheitId;
	private int anzahl;
	
	private Zutat zutat;
	private Mengeneinheiten mengeneinheit;
	
	public int getRezeptId() {
		return rezeptId;
	}
	public void setRezeptId(int rezeptId) {
		this.rezeptId = rezeptId;
	}
	public int getZutatId() {
		return zutatId;
	}
	public void setZutatId(int zutatId) {
		this.zutatId = zutatId;
	}
	public int getEinheitId() {
		return einheitId;
	}
	public void setEinheitId(int einheitId) {
		this.einheitId = einheitId;
	}
	public int getAnzahl() {
		return anzahl;
	}
	public void setAnzahl(int anzahl) {
		this.anzahl = anzahl;
	}
	public Zutat getZutat() {
		return zutat;
	}
	public void setZutat(Zutat zutat) {
		this.zutat = zutat;
	}
	public Mengeneinheiten getMengeneinheit() {
		return mengeneinheit;
	}
	public void setMengeneinheit(Mengeneinheiten mengeneinheit) {
		this.mengeneinheit = mengeneinheit;
	}
	
	
	
}
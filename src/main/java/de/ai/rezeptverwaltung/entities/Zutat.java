package de.ai.rezeptverwaltung.entities;

public class Zutat {

	private int zutatId;
	private String bezeichnung;
	
	public int getZutatId() {
		return zutatId;
	}
	public void setZutatId(int zutatId) {
		this.zutatId = zutatId;
	}
	public String getBezeichnung() {
		return bezeichnung;
	}
	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}
	
	@Override
	public String toString() {
		return bezeichnung;
	}
	
}
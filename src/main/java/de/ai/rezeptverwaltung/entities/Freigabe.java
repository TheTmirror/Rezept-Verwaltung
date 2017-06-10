package de.ai.rezeptverwaltung.entities;

public class Freigabe {

	private int freigabeId;
	private int rezeptId;
	private String begründung;
	private int status;
	
	public int getFreigabeId() {
		return freigabeId;
	}
	public void setFreigabeId(int freigabeId) {
		this.freigabeId = freigabeId;
	}
	public int getRezeptId() {
		return rezeptId;
	}
	public void setRezeptId(int rezeptId) {
		this.rezeptId = rezeptId;
	}
	public String getBegründung() {
		return begründung;
	}
	public void setBegründung(String begründung) {
		this.begründung = begründung;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
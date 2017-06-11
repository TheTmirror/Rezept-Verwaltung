package de.ai.rezeptverwaltung.entities;

import java.util.Date;

public class Kommentar {

	private int kommentarId;
	private int rezeptId;
	private String kommentartext;
	private String autor;
	private Date erstellungsdatum;
	
	public int getKommentarId() {
		return kommentarId;
	}
	public void setKommentarId(int kommentarId) {
		this.kommentarId = kommentarId;
	}
	public int getRezeptId() {
		return rezeptId;
	}
	public void setRezeptId(int rezeptId) {
		this.rezeptId = rezeptId;
	}
	public String getKommentartext() {
		return kommentartext;
	}
	public void setKommentartext(String kommentartext) {
		this.kommentartext = kommentartext;
	}
	public String getAutor() {
		return autor;
	}
	public void setAutor(String autor) {
		this.autor = autor;
	}
	public Date getErstellungsdatum() {
		return erstellungsdatum;
	}
	public void setErstellungsdatum(Date erstellungsdatum) {
		this.erstellungsdatum = erstellungsdatum;
	}
	
}
package de.ai.rezeptverwaltung.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import de.ai.rezeptverwaltung.entities.Rezept;

public class RezeptService {

	Connection connection;
	
	public RezeptService(Connection connection) {
		
		this.connection = connection;
		
	}
	
	public LinkedList<Rezept> getAll() {
		
		Statement s = null;
		ResultSet r;
		
		LinkedList<Rezept> ergebnisse = new LinkedList<Rezept>();
		
		try {
			s = connection.createStatement();
			r = s.executeQuery("SELECT * FROM rezept");
			
			Rezept rezept;
			
			while(r.next()) {
				
				rezept = new Rezept();
				if(r.getString("BILD_ID") != null)
					rezept.setBildId(Integer.parseInt(r.getString("BILD_ID")));
				rezept.setBezeichnung(r.getString("BEZEICHNUNG"));
				rezept.setGesamtbewertung(Integer.parseInt(r.getString("GESAMTBEWERTUNG")));
				rezept.setKategorieId(Integer.parseInt(r.getString("KATEGORIE_ID")));
				rezept.setRezeptId(Integer.parseInt(r.getString("REZEPT_ID")));
				
				ergebnisse.add(rezept);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(s != null)
				s.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return ergebnisse;
	}
	
}
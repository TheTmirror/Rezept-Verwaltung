package de.ai.rezeptverwaltung.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import de.ai.rezeptverwaltung.entities.Bild;
import de.ai.rezeptverwaltung.entities.Mengeneinheiten;
import de.ai.rezeptverwaltung.entities.Zutat;
import de.ai.rezeptverwaltung.entities.Zutat_Rezept;

public class Zutat_RezeptService {

Connection connection;
	
	public Zutat_RezeptService(Connection connection) {
		
		this.connection = connection;
		
	}
	
	public LinkedList<Zutat_Rezept> getAllById(int id) {
		
		LinkedList<Zutat_Rezept> ergebnisse = new LinkedList<Zutat_Rezept>();
		
		PreparedStatement searchQuery = null;
		ResultSet r = null;
		
		try {
			
			//START
			
			String queryString = "SELECT zutat.zutat_id, zutat.bezeichnung AS zutat, anzahl, mengeneinheiten.einheit_ID, mengeneinheiten.bezeichnung AS einheit " +
								"FROM Zutat_Rezept " +
								"JOIN Zutat ON(Zutat.zutat_id=Zutat_rezept.rezept_id) " +
								"JOIN Mengeneinheiten ON(Mengeneinheiten.einheit_id=Zutat_rezept.einheit_id) " +
								"WHERE Rezept_id=?";
			
			searchQuery = connection.prepareStatement(queryString);
			
			searchQuery.setInt(1, id);
			
			//END
					
			r = searchQuery.executeQuery();
			
			Zutat_Rezept zR;
			
			while(r.next()) {
				
				zR = new Zutat_Rezept();
				Zutat z = new Zutat();
				z.setZutatId(Integer.parseInt(r.getString("ZUTAT.ZUTAT_ID")));
				z.setBezeichnung(r.getString("zutat"));
				zR.setZutat(z);
				Mengeneinheiten m = new Mengeneinheiten();
				m.setEinheitId(Integer.parseInt(r.getString("mengeneinheiten.einheit_ID")));
				m.setBezeichnung(r.getString("einheit"));
				zR.setMengeneinheit(m);
				zR.setAnzahl(Integer.parseInt(r.getString("anzahl")));
				zR.setEinheitId(m.getEinheitId());
				zR.setRezeptId(id);
				zR.setZutatId(z.getZutatId());
				ergebnisse.add(zR);
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				searchQuery.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return ergebnisse;
		
	}
	
}
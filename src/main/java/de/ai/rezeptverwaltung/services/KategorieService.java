package de.ai.rezeptverwaltung.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.ai.rezeptverwaltung.entities.Kategorie;
import de.ai.rezeptverwaltung.entities.Zutat;

public class KategorieService {

	Connection connection;
	
	public KategorieService(Connection connection) {
		
		this.connection = connection;
		
	}
	
	public Kategorie getByBezeichnung(String b) {
		
		PreparedStatement s = null;
		Kategorie ergebniss = null;
		
		String query = "SELECT * FROM kategorie WHERE bezeichnung = ?";
		
		try {
			s = connection.prepareStatement(query);
			s.setString(1, b);
			ResultSet r = s.executeQuery();
			
			r.next();
			ergebniss = new Kategorie();
			ergebniss.setKategorieId(Integer.parseInt(r.getString("kategorie_id")));
			ergebniss.setBezeichnung(r.getString("bezeichnung"));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				s.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return ergebniss;
		
	}
	
	public void addOrUpdate(Kategorie k) {
		
		PreparedStatement s = null;
		
		String query = "SELECT kategorie_id FROM kategorie WHERE bezeichnung = ?";
		
		try {
			s = connection.prepareStatement(query);
			s.setString(1, k.getBezeichnung());
			ResultSet r = s.executeQuery();
			
			int value = -1;
			while(r.next())
				value = Integer.parseInt(r.getString("kategorie_id"));
			
			if(value != -1) {
				k.setKategorieId(value);
			}
			else {
				query = "INSERT INTO kategorie VALUES (kategorie_sequenz.nextval, ?)";
				s.close();
				s = connection.prepareStatement(query);
				s.setString(1, k.getBezeichnung());
				s.executeUpdate();
				connection.commit();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				s.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}

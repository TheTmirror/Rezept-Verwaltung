package de.ai.rezeptverwaltung.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.ai.rezeptverwaltung.entities.Mengeneinheiten;
import de.ai.rezeptverwaltung.entities.Zutat;

public class ZutatService {

	Connection connection;
	
	public ZutatService (Connection connection) {
		
		this.connection = connection;
		
	}
	
	public Zutat getByBezeichnung(String b) {
		
		PreparedStatement s = null;
		Zutat ergebniss = null;
		
		String query = "SELECT * FROM zutat WHERE bezeichnung = ?";
		
		try {
			s = connection.prepareStatement(query);
			s.setString(1, b);
			ResultSet r = s.executeQuery();
			
			r.next();
			ergebniss = new Zutat();
			ergebniss.setZutatId(Integer.parseInt(r.getString("zutat_id")));
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
	
	public void addOrUpdate(Zutat z) {
		
		PreparedStatement s = null;
		
		String query = "SELECT zutat_id FROM zutat WHERE bezeichnung = ?";
		
		try {
			s = connection.prepareStatement(query);
			s.setString(1, z.getBezeichnung());
			ResultSet r = s.executeQuery();
			
			int value = -1;
			while(r.next())
				value = Integer.parseInt(r.getString("zutat_id"));
			
			if(value != -1) {
				z.setZutatId(value);
			}
			else {
				query = "INSERT INTO zutat VALUES (zutat_sequenz.nextval, ?)";
				s.close();
				s = connection.prepareStatement(query);
				s.setString(1, z.getBezeichnung());
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
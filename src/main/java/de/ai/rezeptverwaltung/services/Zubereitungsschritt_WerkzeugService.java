package de.ai.rezeptverwaltung.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.ai.rezeptverwaltung.entities.Rezept;
import de.ai.rezeptverwaltung.entities.Werkzeug;
import de.ai.rezeptverwaltung.entities.Zubereitungsschritt;
import de.ai.rezeptverwaltung.entities.Zutat;

public class Zubereitungsschritt_WerkzeugService {

	Connection connection;
	
	public Zubereitungsschritt_WerkzeugService (Connection connection) {
		
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
	
	public void addOrUpdate(Zubereitungsschritt z, Werkzeug w) {
		
		PreparedStatement s = null;
		
		String query = "SELECT werkzeug_id, zubereitungsschritt_id FROM zubereitungsschritt_werkzeug WHERE werkzeug_id = ? AND zubereitungsschritt_id = ?";
		
		try {
			s = connection.prepareStatement(query);
			s.setInt(1, w.getWerkzeugId());
			s.setInt(2, z.getSchrittId());
			ResultSet r = s.executeQuery();
			
			int value1 = -1;
			int value2 = -1;
			while(r.next()) {
				value1 = Integer.parseInt(r.getString("werkzeug_id"));
				value2 = Integer.parseInt(r.getString("zubereitungsschritt_id"));
			}
			
			if(value1 != -1 && value2 != -1) {
//				z.setZutatId(value);
			}
			else {
				query = "INSERT INTO zubereitungsschritt_werkzeug VALUES (?, ?)";
				s.close();
				s = connection.prepareStatement(query);
				s.setInt(2, w.getWerkzeugId());
				s.setInt(1, z.getSchrittId());
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
package de.ai.rezeptverwaltung.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import de.ai.rezeptverwaltung.entities.Werkzeug;
import de.ai.rezeptverwaltung.entities.Zutat;

public class WerkzeugService {

	Connection connection;
	
	public WerkzeugService(Connection connection) {
		
		this.connection = connection;
		
	}
	
	public Werkzeug getByBezeichnung(String b) {
		
		PreparedStatement s = null;
		Werkzeug ergebniss = null;
		
		String query = "SELECT * FROM werkzeug WHERE bezeichnung = ?";
		
		try {
			s = connection.prepareStatement(query);
			s.setString(1, b);
			ResultSet r = s.executeQuery();
			
			r.next();
			ergebniss = new Werkzeug();
			ergebniss.setWerkzeugId(Integer.parseInt(r.getString("werkzeug_id")));
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
	
	public void addOrUpdate(Werkzeug w) {
		
		PreparedStatement s = null;
		
		String query = "SELECT werkzeug_id FROM werkzeug WHERE bezeichnung = ?";
		
		try {
			s = connection.prepareStatement(query);
			s.setString(1, w.getBezeichnung());
			ResultSet r = s.executeQuery();
			
			int value = -1;
			while(r.next())
				value = Integer.parseInt(r.getString("werkzeug_id"));
			
			if(value != -1) {
				w.setWerkzeugId(value);
			}
			else {
				query = "INSERT INTO werkzeug VALUES (werkzeug_sequenz.nextval, ?)";
				s.close();
				s = connection.prepareStatement(query);
				s.setString(1, w.getBezeichnung());
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
	
	public LinkedList<Werkzeug> getAllById(int id) {
		
		LinkedList<Werkzeug> ergebnisse = new LinkedList<Werkzeug>();
		
		PreparedStatement searchQuery = null;
		ResultSet r = null;
		
		try {
			
			//START
			
			String queryString = "SELECT werkzeug.Werkzeug_ID, Bezeichnung " +
								"FROM Werkzeug " +
								"JOIN Zubereitungsschritt_Werkzeug ON(Zubereitungsschritt_Werkzeug.werkzeug_id = werkzeug.werkzeug_id) " +
								"WHERE Zubereitungsschritt_Werkzeug.zubereitungsschritt_id = ?";
			
			searchQuery = connection.prepareStatement(queryString);
			
			searchQuery.setInt(1, id);
			
			//END
					
			r = searchQuery.executeQuery();
			
			Werkzeug werkzeug;
			
			while(r.next()) {
				
				werkzeug = new Werkzeug();
				
				werkzeug.setWerkzeugId(Integer.parseInt(r.getString("WERKZEUG_ID")));
				werkzeug.setBezeichnung(r.getString("BEZEICHNUNG"));
				
				ergebnisse.add(werkzeug);
				
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

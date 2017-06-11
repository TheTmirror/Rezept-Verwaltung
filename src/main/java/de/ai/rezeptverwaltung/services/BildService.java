package de.ai.rezeptverwaltung.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import de.ai.rezeptverwaltung.entities.Bild;
import de.ai.rezeptverwaltung.entities.Freigabe;
import de.ai.rezeptverwaltung.entities.Kategorie;
import de.ai.rezeptverwaltung.entities.Rezept;

public class BildService {

	Connection connection;
	
	public BildService(Connection connection) {
		
		this.connection = connection;
		
	}
	
	public Bild getByPfad(String p) {
		
		PreparedStatement s = null;
		Bild ergebniss = null;
		
		String query = "SELECT * FROM bild WHERE pfad = ?";
		
		try {
			s = connection.prepareStatement(query);
			s.setString(1, p);
			ResultSet r = s.executeQuery();
			
			r.next();
			ergebniss = new Bild();
			ergebniss.setBildId(Integer.parseInt(r.getString("bild_id")));
			ergebniss.setPfad(r.getString("pfad"));
			
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
	
	public void addOrUpdate(Bild b) {
		
		PreparedStatement s = null;
		
		String query = "SELECT bild_id FROM bild WHERE pfad = ?";
		
		try {
			s = connection.prepareStatement(query);
			s.setString(1, b.getPfad());
			ResultSet r = s.executeQuery();
			
			int value = -1;
			while(r.next())
				value = Integer.parseInt(r.getString("bild_id"));
			
			if(value != -1) {
				b.setBildId(value);
			}
			else {
				query = "INSERT INTO bild VALUES (bild_sequenz.nextval, ?)";
				s.close();
				s = connection.prepareStatement(query);
				s.setString(1, b.getPfad());
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
	
	public LinkedList<Bild> getAllBySchrittId(int id) {
		
		LinkedList<Bild> ergebnisse = new LinkedList<Bild>();
		
		PreparedStatement searchQuery = null;
		ResultSet r = null;
		
		try {
			
			//START
			
			String queryString = "SELECT bild.BILD_ID, Pfad " +
								"FROM bild " +
								"JOIN Zubereitungsschritt_Bild ON(Zubereitungsschritt_Bild.bild_id = bild.bild_id) " +
								"WHERE Zubereitungsschritt_Bild.schritt_id = ?";
			
			searchQuery = connection.prepareStatement(queryString);
			
			searchQuery.setInt(1, id);
			
			//END
					
			r = searchQuery.executeQuery();
			
			Bild bild;
			
			while(r.next()) {
				
				bild = new Bild();
				bild.setBildId(Integer.parseInt(r.getString("BILD_ID")));
				bild.setPfad(r.getString("PFAD"));
				ergebnisse.add(bild);
				
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
	
	public LinkedList<Bild> getAllByBildId(int id) {
		
		LinkedList<Bild> ergebnisse = new LinkedList<Bild>();
		
		PreparedStatement searchQuery = null;
		ResultSet r = null;
		
		try {
			
			//START
			
			String queryString = "SELECT BILD_ID, Pfad " +
								"FROM bild " +
								"WHERE bild_id = ?";
			
			searchQuery = connection.prepareStatement(queryString);
			
			searchQuery.setInt(1, id);
			
			//END
					
			r = searchQuery.executeQuery();
			
			Bild bild;
			
			while(r.next()) {
				
				bild = new Bild();
				bild.setBildId(Integer.parseInt(r.getString("BILD_ID")));
				bild.setPfad(r.getString("PFAD"));
				ergebnisse.add(bild);
				
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
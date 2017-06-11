package de.ai.rezeptverwaltung.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import de.ai.rezeptverwaltung.entities.Schlagwort;
import de.ai.rezeptverwaltung.entities.Zubereitungsschritt;
import de.ai.rezeptverwaltung.entities.Zubereitungsschritt;

public class ZubereitungsschrittService {

	Connection connection;
	
	public ZubereitungsschrittService(Connection connection) {
		
		this.connection = connection;
		
	}
	
	public Zubereitungsschritt getByBezeichnung(String b) {
		
		PreparedStatement s = null;
		Zubereitungsschritt ergebniss = null;
		
		String query = "SELECT * FROM zubereitungsschritt WHERE beschreibung = ?";
		
		try {
			s = connection.prepareStatement(query);
			s.setString(1, b);
			ResultSet r = s.executeQuery();
			
			r.next();
			ergebniss = new Zubereitungsschritt();
			ergebniss.setSchrittId(Integer.parseInt(r.getString("schritt_id")));
			ergebniss.setBeschreibung(r.getString("beschreibung"));
			
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
	
	public void addOrUpdate(Zubereitungsschritt z) {
		
		PreparedStatement s = null;
		
		String query = "SELECT schritt_id FROM zubereitungsschritt WHERE beschreibung = ?";
		
		try {
			s = connection.prepareStatement(query);
			s.setString(1, z.getBeschreibung());
			ResultSet r = s.executeQuery();
			
			int value = -1;
			while(r.next())
				value = Integer.parseInt(r.getString("schritt_id"));
			
			if(value != -1) {
				z.setSchrittId(value);
			}
			else {
				query = "INSERT INTO zubereitungsschritt VALUES (zubereitungsschritt_sequenz.nextval, ?)";
				s.close();
				s = connection.prepareStatement(query);
				s.setString(1, z.getBeschreibung());
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
	
	public LinkedList<Zubereitungsschritt> getAllById(int id) {
		
		LinkedList<Zubereitungsschritt> ergebnisse = new LinkedList<Zubereitungsschritt>();
		
		PreparedStatement searchQuery = null;
		ResultSet r = null;
		
		try {
			
			//START
			
			String queryString = "SELECT Zubereitungsschritt_Rezept.Schritt_id, Beschreibung " +
								"FROM Zubereitungsschritt_Rezept " +
								"JOIN Zubereitungsschritt ON(Zubereitungsschritt_Rezept.schritt_id = Zubereitungsschritt.schritt_id) " +
								"WHERE Zubereitungsschritt_Rezept.rezept_id = ? " +
								"ORDER BY schrittnummer";
			
			searchQuery = connection.prepareStatement(queryString);
			
			searchQuery.setInt(1, id);
			
			//END
					
			r = searchQuery.executeQuery();
			
			Zubereitungsschritt schritt;
			WerkzeugService ws = new WerkzeugService(connection);
			BildService bs = new BildService(connection);
			
			while(r.next()) {
				
				schritt = new Zubereitungsschritt();
				schritt.setSchrittId(Integer.parseInt(r.getString("SCHRITT_ID")));
				schritt.setBeschreibung(r.getString("BESCHREIBUNG"));
				schritt.setWerkzeuge(ws.getAllById(schritt.getSchrittId()));
				schritt.setBilder(bs.getAllBySchrittId(schritt.getSchrittId()));
				
				ergebnisse.add(schritt);
				
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

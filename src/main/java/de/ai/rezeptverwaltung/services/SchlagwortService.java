package de.ai.rezeptverwaltung.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import de.ai.rezeptverwaltung.entities.Schlagwort;
import de.ai.rezeptverwaltung.entities.Zutat;

public class SchlagwortService {

	Connection connection;
	
	public SchlagwortService(Connection connection) {
		
		this.connection = connection;
		
	}
	
	public Schlagwort getByBezeichnung(String b) {
		
		PreparedStatement s = null;
		Schlagwort ergebniss = null;
		
		String query = "SELECT * FROM schlagwort WHERE bezeichnung = ?";
		
		try {
			s = connection.prepareStatement(query);
			s.setString(1, b);
			ResultSet r = s.executeQuery();
			
			r.next();
			ergebniss = new Schlagwort();
			ergebniss.setSchlagwortId(Integer.parseInt(r.getString("schlagwort_id")));
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
	
	public void addOrUpdate(LinkedList<Schlagwort> schlagworte) {
		
		PreparedStatement s = null;
		
		for(Schlagwort schlagwort : schlagworte) {
		
			String query = "SELECT schlagwort_id FROM schlagwort WHERE bezeichnung = ?";
			
			try {
				s = connection.prepareStatement(query);
				s.setString(1, schlagwort.getBezeichnung());
				ResultSet r = s.executeQuery();
				
				int value = -1;
				while(r.next())
					value = Integer.parseInt(r.getString("schlagwort_id"));
				
				if(value != -1) {
					schlagwort.setSchlagwortId(value);
				}
				else {
					query = "INSERT INTO schlagwort VALUES (schlagwort_sequenz.nextval, ?)";
					s.close();
					s = connection.prepareStatement(query);
					s.setString(1, schlagwort.getBezeichnung());
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
	
	public LinkedList<Schlagwort> getAllByRezeptId(int id) {
		
		LinkedList<Schlagwort> ergebnisse = new LinkedList<Schlagwort>();
		
		PreparedStatement searchQuery = null;
		ResultSet r = null;
		
		try {
			
			//START
			
			String queryString = "SELECT schlagwort.schlagwort_id, bezeichnung "+
								"FROM schlagwort " +
								"JOIN schlagwort_rezept ON(schlagwort_rezept.schlagwort_id=schlagwort.schlagwort_id) " +
								"WHERE rezept_id=?";
			
			searchQuery = connection.prepareStatement(queryString);
			
			searchQuery.setInt(1, id);
			
			//END
					
			r = searchQuery.executeQuery();
			
			Schlagwort schlagwort;
			
			while(r.next()) {
				
				schlagwort = new Schlagwort();
				schlagwort.setSchlagwortId(Integer.parseInt(r.getString("SCHLAGWORT_ID")));
				schlagwort.setBezeichnung(r.getString("BEZEICHNUNG"));
				ergebnisse.add(schlagwort);
				
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
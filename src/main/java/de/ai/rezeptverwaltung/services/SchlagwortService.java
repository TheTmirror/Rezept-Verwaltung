package de.ai.rezeptverwaltung.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import de.ai.rezeptverwaltung.entities.Schlagwort;

public class SchlagwortService {

	Connection connection;
	
	public SchlagwortService(Connection connection) {
		
		this.connection = connection;
		
	}
	
	public LinkedList<Schlagwort> getAllById(int id) {
		
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
package de.ai.rezeptverwaltung.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import de.ai.rezeptverwaltung.entities.Werkzeug;

public class WerkzeugService {

	Connection connection;
	
	public WerkzeugService(Connection connection) {
		
		this.connection = connection;
		
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

package de.ai.rezeptverwaltung.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.ai.rezeptverwaltung.entities.Bild;
import de.ai.rezeptverwaltung.entities.Rezept;
import de.ai.rezeptverwaltung.entities.Werkzeug;
import de.ai.rezeptverwaltung.entities.Zubereitungsschritt;
import de.ai.rezeptverwaltung.entities.Zutat;

public class Zubereitungsschritt_BildService {

	Connection connection;
	
	public Zubereitungsschritt_BildService (Connection connection) {
		
		this.connection = connection;
		
	}
	
	public void addOrUpdate(Zubereitungsschritt z, Bild b) {
		
		PreparedStatement s = null;
		
		String query = "SELECT bild_id, schritt_id FROM zubereitungsschritt_bild WHERE bild_id = ? AND schritt_id = ?";
		
		try {
			s = connection.prepareStatement(query);
			s.setInt(1, b.getBildId());
			s.setInt(2, z.getSchrittId());
			ResultSet r = s.executeQuery();
			
			int value1 = -1;
			int value2 = -1;
			while(r.next()) {
				value1 = Integer.parseInt(r.getString("bild_id"));
				value2 = Integer.parseInt(r.getString("schritt_id"));
			}
			
			if(value1 != -1 && value2 != -1) {
//				z.setZutatId(value);
			}
			else {
				query = "INSERT INTO zubereitungsschritt_bild VALUES (?, ?)";
				s.close();
				s = connection.prepareStatement(query);
				s.setInt(2, b.getBildId());
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
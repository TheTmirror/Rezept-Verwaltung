package de.ai.rezeptverwaltung.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.ai.rezeptverwaltung.entities.Bild;
import de.ai.rezeptverwaltung.entities.Rezept;
import de.ai.rezeptverwaltung.entities.Zubereitungsschritt;

public class Zubereitungsschritt_RezeptService {

	Connection connection;
	
	public Zubereitungsschritt_RezeptService(Connection connection) {
		
		this.connection = connection;
		
	}
	
	public void addOrUpdate(Rezept rezept, Zubereitungsschritt z, int nummer) {
		
		PreparedStatement s = null;
		
		String query = "SELECT schritt_id, rezept_id FROM zubereitungsschritt_rezept WHERE rezept_id = ? AND schritt_id = ?";
		
		try {
			s = connection.prepareStatement(query);
			s.setInt(1, rezept.getRezeptId());
			s.setInt(2, z.getSchrittId());
			ResultSet r = s.executeQuery();
			
			int value1 = -1;
			int value2 = -1;
			while(r.next()) {
				value1 = Integer.parseInt(r.getString("rezept_id"));
				value2 = Integer.parseInt(r.getString("schritt_id"));
			}
			
			if(value1 != -1 && value2 != -1) {
//				b.setBildId(value);
			}
			else {
				query = "INSERT INTO zubereitungsschritt_rezept(rezept_id, schritt_id, schrittnummer) VALUES (?, ?, ?)";
				s.close();
				s = connection.prepareStatement(query);
				s.setInt(1, rezept.getRezeptId());
				s.setInt(2, z.getSchrittId());
				s.setInt(3, nummer);
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

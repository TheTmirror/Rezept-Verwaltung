package de.ai.rezeptverwaltung.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import de.ai.rezeptverwaltung.entities.Rezept;
import de.ai.rezeptverwaltung.entities.Schlagwort;

public class Schlagwort_Rezept_Service {

	Connection connection;
	
	public Schlagwort_Rezept_Service(Connection connection) {
		
		this.connection = connection;
		
	}
	
	public void addOrUpdate(Rezept rezept, Schlagwort schlagwort) {
		
		PreparedStatement s = null;
		
		String query = "SELECT * FROM schlagwort_rezept WHERE rezept_id = ? AND schlagwort_id = ?";
		
			try {
				s = connection.prepareStatement(query);
				s.setInt(1, rezept.getRezeptId());
				s.setInt(2, schlagwort.getSchlagwortId());
				ResultSet r = s.executeQuery();
				
				int value = -1;
				while(r.next())
					value = Integer.parseInt(r.getString("schlagwort_id"));
				
				if(value != -1) {
//					z.setZutatId(Integer.parseInt(r.getString("zutat_id")));
				}
				else {
					query = "INSERT INTO schlagwort_rezept VALUES (?, ?)";
					s.close();
					s = connection.prepareStatement(query);
					s.setInt(1, rezept.getRezeptId());
					s.setInt(2, schlagwort.getSchlagwortId());
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
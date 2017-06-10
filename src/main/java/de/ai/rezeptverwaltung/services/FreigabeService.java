package de.ai.rezeptverwaltung.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import de.ai.rezeptverwaltung.entities.Rezept;

public class FreigabeService {

	Connection connection;
	
	public FreigabeService(Connection connection) {
		
		this.connection = connection;
		
	}
	
	public void updateFreigabe(Rezept rezept) {
		
		PreparedStatement s = null;
		
		
		try {
			String queryString = "UPDATE freigabe SET status = ?, begruendung = ? WHERE rezept_id = ?";
			s = connection.prepareStatement(queryString);
				s.setInt(1, rezept.getFreigabe().getStatus());
				s.setString(2, rezept.getFreigabe().getBegründung());
				s.setInt(3, rezept.getRezeptId());
			
			s.executeUpdate();
			connection.commit();
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
	
	public void createFreigabe(Rezept rezept) {
		
		PreparedStatement s = null;
		
		try {
			
			String queryString = "INSERT INTO freigabe(freigabe_id, rezept_id, begruendung, status) VALUES(freigabe_sequenz.nextVal,?,?,?)";
			s = connection.prepareStatement(queryString);
			s.setInt(1, rezept.getRezeptId());
			s.setString(2, rezept.getFreigabe().getBegründung());
			s.setInt(3, rezept.getFreigabe().getStatus());
			
			s.executeUpdate();
			connection.commit();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(s != null)
					s.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
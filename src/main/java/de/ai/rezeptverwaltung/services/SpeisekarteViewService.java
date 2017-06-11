package de.ai.rezeptverwaltung.services;

import java.awt.image.Raster;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.vaadin.ui.Notification;

import de.ai.rezeptverwaltung.entities.Rezept;

public class SpeisekarteViewService {

	Connection connection;
	
	public SpeisekarteViewService(Connection connection) {
		
		this.connection = connection;
		
	}
	
	public boolean isAufSpeisekarte(Rezept r) {
		
		PreparedStatement statement = null;
		
		String query = "SELECT rezept_id FROM speisekarten_ansicht WHERE rezept_id = ?";
		
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, r.getRezeptId());
			
			while(statement.executeQuery().next())
				return true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return false;
		
	}
	
	public void add(int speisekartenId, Rezept rezept, int preis) {
		
		PreparedStatement statement = null;
		
		String queryString = "INSERT INTO speisekarten_ansicht (speisekarte_id, rezept_id, preis) VALUES (?, ?, ?)";
		
		try {
			statement = connection.prepareStatement(queryString);
			statement.setInt(1, speisekartenId);
			statement.setInt(2, rezept.getRezeptId());
			statement.setInt(3, preis);
			statement.executeUpdate();
			
			connection.commit();
			Notification.show("Erfolgreich hinzugefügt!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(statement != null)
					statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void remove(int id) {
		
		PreparedStatement statement = null;
		
		String queryString = "DELETE FROM speisekarten_ansicht WHERE rezept_id = ?";
		
		try {
			statement = connection.prepareStatement(queryString);
			statement.setInt(1, id);
			statement.executeUpdate();
			
			connection.commit();
			Notification.show("Erfolgreich entfernt!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(statement != null)
					statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
}
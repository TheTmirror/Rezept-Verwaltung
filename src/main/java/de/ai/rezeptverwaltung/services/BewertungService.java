package de.ai.rezeptverwaltung.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.vaadin.ui.Notification;

import de.ai.rezeptverwaltung.entities.Bewertung;

public class BewertungService {

	Connection connection;
	
	public BewertungService(Connection connection) {
		
		this.connection = connection;
		
	}
	
	public void save(Bewertung bewertung) {
		
		PreparedStatement s = null;
		
		String queryString = "INSERT INTO bewertung(bewertungs_id, rezept_id, punkte) VALUES (bewertung_sequenz.nextval, ?, ?)";
		
		try {
			s = connection.prepareStatement(queryString);
			s.setInt(1, bewertung.getRezeptId());
			s.setInt(2, bewertung.getPunkte());
			
			s.executeUpdate();
			
			connection.commit();
			Notification.show("Bewertung wurde hinterlassen");
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
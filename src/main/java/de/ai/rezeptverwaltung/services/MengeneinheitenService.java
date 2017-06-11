package de.ai.rezeptverwaltung.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.ai.rezeptverwaltung.entities.Mengeneinheiten;

public class MengeneinheitenService {

	Connection connection;
	
	public MengeneinheitenService(Connection connection) {
		this.connection = connection;
	}
	
	public Mengeneinheiten getByBezeichnung(String b) {
		
		PreparedStatement s = null;
		Mengeneinheiten einheit = null;
		
		String query = "SELECT * FROM mengeneinheiten WHERE bezeichnung = ?";
		
		try {
			s = connection.prepareStatement(query);
			s.setString(1, b);
			ResultSet r = s.executeQuery();
			
			r.next();
			einheit = new Mengeneinheiten();
			einheit.setEinheitId(Integer.parseInt(r.getString("einheit_id")));
			einheit.setBezeichnung(r.getString("bezeichnung"));
			
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
		
		return einheit;
		
	}
	
	public void addOrUpdate(Mengeneinheiten m) {
		
		PreparedStatement s = null;
		
		String query = "SELECT einheit_id FROM mengeneinheiten WHERE bezeichnung = ?";
		
		try {
			s = connection.prepareStatement(query);
			s.setString(1, m.getBezeichnung());
			ResultSet r = s.executeQuery();
			
			int value = -1;
			while(r.next())
				value = Integer.parseInt(r.getString("einheit_id"));
			
			if(value != -1) {
				m.setEinheitId(value);
			}
			else {
				query = "INSERT INTO mengeneinheiten VALUES (mengeneinheiten_sequenz.nextval, ?)";
				s.close();
				s = connection.prepareStatement(query);
				s.setString(1, m.getBezeichnung());
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
package de.ai.rezeptverwaltung.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import org.atmosphere.interceptor.SSEAtmosphereInterceptor;

import de.ai.rezeptverwaltung.entities.Kommentar;

public class KommentarService {

	Connection connection;
	
	public KommentarService (Connection connection) {
		
		this.connection = connection;
		
	}
	
	public void save(Kommentar kommentar) {
		
		PreparedStatement s = null;
		
		String queryString = "INSERT INTO KOMMENTAR(kommentar_id, rezept_id, kommentartext, autor) VALUES(kommentar_sequenz.nextval, ?, ?, ?)";
		
		try {
			s = connection.prepareStatement(queryString);
			s.setInt(1, kommentar.getRezeptId());
			s.setString(2, kommentar.getKommentartext());
			s.setString(3, kommentar.getAutor());
			
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
	
	public LinkedList<Kommentar> getAllById(int id) {
		
		LinkedList<Kommentar> ergebnisse = new LinkedList<Kommentar>();
		
		PreparedStatement s = null;
		ResultSet r;
		
		String queryString = "SELECT * "
				+ "FROM kommentar "
				+ "WHERE rezept_id = ?";
		
		try {
			s = connection.prepareStatement(queryString);
			s.setInt(1, id);
			r = s.executeQuery();
			
			Kommentar k;
			
			while(r.next()) {
				
				k = new Kommentar();
				k.setRezeptId(id);
				k.setKommentarId(Integer.parseInt(r.getString("KOMMENTAR_ID")));
				k.setKommentartext(r.getString("kommentartext"));
				k.setAutor(r.getString("Autor"));
				DateFormat format = new SimpleDateFormat("YYYY-MM-DD", Locale.ENGLISH);
				try {
					k.setErstellungsdatum(format.parse(r.getString("erstellungsdatum")));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				ergebnisse.add(k);
				
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
		
		return ergebnisse;
		
	}
	
}
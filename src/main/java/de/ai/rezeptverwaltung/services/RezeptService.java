package de.ai.rezeptverwaltung.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import com.vaadin.ui.Notification;

import de.ai.rezeptverwaltung.entities.Freigabe;
import de.ai.rezeptverwaltung.entities.Kategorie;
import de.ai.rezeptverwaltung.entities.Rezept;
import de.ai.rezeptverwaltung.entities.RezeptSpeisekarte;

public class RezeptService {

	Connection connection;
	
	public RezeptService(Connection connection) {
		
		this.connection = connection;
		
	}
	
	public Rezept getByBezeichnung(String b) {
		
		PreparedStatement s = null;
		Rezept ergebniss = null;
		
		String query = "SELECT * FROM rezept WHERE bezeichnung = ?";
		
		try {
			s = connection.prepareStatement(query);
			s.setString(1, b);
			ResultSet r = s.executeQuery();
			
			r.next();
			ergebniss = new Rezept();
			ergebniss.setRezeptId(Integer.parseInt(r.getString("rezept_id")));
			ergebniss.setBezeichnung(r.getString("bezeichnung"));
			ergebniss.setBildId(Integer.parseInt(r.getString("bild_id")));
			ergebniss.setKategorieId(Integer.parseInt(r.getString("kategorie_id")));
			if(r.getString("gesamtbewertung") != null)
				ergebniss.setGesamtbewertung(Double.parseDouble(r.getString("gesamtbewertung")));
			else
				ergebniss.setGesamtbewertung(0.0);
			
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
		
		return ergebniss;
		
	}
	
	/*
	 * Achtung: Bei einem Update passiert nichts, bisher können nur neue
	 * Rezepte hinzugefügt werden
	 */
	public void addOrUpdate(Rezept rezept) {
		
		PreparedStatement s = null;
		
		String query = "SELECT rezept_id FROM rezept WHERE bezeichnung = ?";
		
		try {
			s = connection.prepareStatement(query);
			s.setString(1, rezept.getBezeichnung());
			ResultSet r = s.executeQuery();
			
			int value = -1;
			while(r.next())
				value = Integer.parseInt(r.getString("rezept_id"));
			
			if(value != -1) {
				Notification.show("REZEPT WURDE NICHT HINZUGEFÜGT DA DER NAME BEREITS VERGEBEN IST (implement update)");
				rezept.setRezeptId(value);
			}
			else {
				query = "INSERT INTO rezept(rezept_id, bezeichnung, bild_id, kategorie_id) VALUES (rezept_sequenz.nextval, ?, ?, ?)";
				s.close();
				s = connection.prepareStatement(query);
				s.setString(1, rezept.getBezeichnung());
				s.setInt(2, rezept.getBild().getBildId());
				s.setInt(3, rezept.getKategorie().getKategorieId());
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
	
	public LinkedList<RezeptSpeisekarte> getSpeisekarteView() {
		
		LinkedList<RezeptSpeisekarte> ergebnisse = new LinkedList<RezeptSpeisekarte>();
		
		PreparedStatement searchQuery = null;
		
		String queryString = "SELECT rezept_id, rezept_name, preis FROM speisekarten_ansicht";
		
		try {
			searchQuery = connection.prepareStatement(queryString);
			ResultSet r = searchQuery.executeQuery();
			
			RezeptSpeisekarte rs;
			
			while(r.next()){
				
				rs = new RezeptSpeisekarte();
				rs.setRezeptId(Integer.parseInt(r.getString("rezept_id")));
				rs.setBezeichnung(r.getString("rezept_name"));
				rs.setPreis(Integer.parseInt(r.getString("preis")));
				
				ergebnisse.add(rs);
				
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
	
	public LinkedList<Rezept> getFreigegebene() {
		
		LinkedList<Rezept> ergebnisse = new LinkedList<Rezept>();
		
		PreparedStatement searchQuery = null;
		PreparedStatement searchSubQuery = null;
		ResultSet r = null;
		ResultSet rSub = null;
		
		String queryString = "SELECT rezept.rezept_id FROM rezept JOIN freigabe ON(freigabe.rezept_id = rezept.rezept_id) WHERE status = 1";
		
		try {
			searchQuery = connection.prepareStatement(queryString);
			r = searchQuery.executeQuery();
			
			queryString = "SELECT rezept.REZEPT_ID, rezept.BILD_ID, rezept.BEZEICHNUNG AS rezeptBezeichnung, GESAMTBEWERTUNG, BEGRUENDUNG, FREIGABE_ID, STATUS, rezept.KATEGORIE_ID, kategorie.bezeichnung AS kategorieName "+
						  "FROM rezept "+
						  "LEFT OUTER JOIN freigabe ON(rezept.rezept_id = freigabe.rezept_id) "+
						  "JOIN kategorie ON(kategorie.kategorie_id=rezept.kategorie_id) "+
						  "WHERE  rezept.REZEPT_ID = ANY(";
			LinkedList<Integer> values = new LinkedList<Integer>();
			while(r.next()) {
				values.add(Integer.parseInt(r.getString("REZEPT_ID")));
				queryString += "?,";
			}
			if(values.isEmpty())
				return ergebnisse;
			queryString = queryString.substring(0, queryString.length()-1);
			queryString += ")";
			
			searchSubQuery = connection.prepareStatement(queryString);
			for(int i=0; i<values.size(); i++)
			{
				searchSubQuery.setInt(i+1, values.get(i));
			}
			rSub = searchSubQuery.executeQuery();
			
			Rezept rezept;
			
			while(rSub.next()) {
				
				rezept = new Rezept();
				if(rSub.getString("BILD_ID") != null)
					rezept.setBildId(Integer.parseInt(rSub.getString("BILD_ID")));
				rezept.setBezeichnung(rSub.getString("REZEPTBEZEICHNUNG"));
				rezept.setGesamtbewertung(Double.parseDouble(rSub.getString("GESAMTBEWERTUNG")));
				rezept.setKategorieId(Integer.parseInt(rSub.getString("KATEGORIE_ID")));
				Kategorie k = new Kategorie();
				k.setKategorieId(Integer.parseInt(rSub.getString("KATEGORIE_ID")));
				k.setBezeichnung(rSub.getString("KATEGORIENAME"));
				rezept.setKategorie(k);
				if(rSub.getString("FREIGABE_ID") != null) {
					Freigabe f = new Freigabe();
					f.setFreigabeId(Integer.parseInt(rSub.getString("FREIGABE_ID")));
					f.setRezeptId(Integer.parseInt(rSub.getString("REZEPT_ID")));
					f.setStatus(Integer.parseInt(rSub.getString("STATUS")));
					f.setBegründung(rSub.getString("BEGRUENDUNG"));
					rezept.setFreigabe(f);
				}
				rezept.setRezeptId(Integer.parseInt(rSub.getString("REZEPT_ID")));
				
				ergebnisse.add(rezept);
				
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
	
	public LinkedList<Rezept> getSpeisekarte() {
		
		LinkedList<Rezept> ergebnisse = new LinkedList<Rezept>();
		
		PreparedStatement searchQuery = null;
		PreparedStatement searchSubQuery = null;
		ResultSet r = null;
		ResultSet rSub = null;
		
		String queryString = "SELECT rezept_id FROM speisekarten_ansicht";
		
		try {
			searchQuery = connection.prepareStatement(queryString);
			r = searchQuery.executeQuery();
			
			queryString = "SELECT rezept.REZEPT_ID, rezept.BILD_ID, rezept.BEZEICHNUNG AS rezeptBezeichnung, GESAMTBEWERTUNG, BEGRUENDUNG, FREIGABE_ID, STATUS, rezept.KATEGORIE_ID, kategorie.bezeichnung AS kategorieName "+
						  "FROM rezept "+
						  "LEFT OUTER JOIN freigabe ON(rezept.rezept_id = freigabe.rezept_id) "+
						  "JOIN kategorie ON(kategorie.kategorie_id=rezept.kategorie_id) "+
						  "WHERE  rezept.REZEPT_ID = ANY(";
			LinkedList<Integer> values = new LinkedList<Integer>();
			while(r.next()) {
				values.add(Integer.parseInt(r.getString("REZEPT_ID")));
				queryString += "?,";
			}
			if(values.isEmpty())
				return ergebnisse;
			queryString = queryString.substring(0, queryString.length()-1);
			queryString += ")";
			
			searchSubQuery = connection.prepareStatement(queryString);
			for(int i=0; i<values.size(); i++)
			{
				searchSubQuery.setInt(i+1, values.get(i));
			}
			rSub = searchSubQuery.executeQuery();
			
			Rezept rezept;
			
			while(rSub.next()) {
				
				rezept = new Rezept();
				if(rSub.getString("BILD_ID") != null)
					rezept.setBildId(Integer.parseInt(rSub.getString("BILD_ID")));
				rezept.setBezeichnung(rSub.getString("REZEPTBEZEICHNUNG"));
				rezept.setGesamtbewertung(Double.parseDouble(rSub.getString("GESAMTBEWERTUNG")));
				rezept.setKategorieId(Integer.parseInt(rSub.getString("KATEGORIE_ID")));
				Kategorie k = new Kategorie();
				k.setKategorieId(Integer.parseInt(rSub.getString("KATEGORIE_ID")));
				k.setBezeichnung(rSub.getString("KATEGORIENAME"));
				rezept.setKategorie(k);
				if(rSub.getString("FREIGABE_ID") != null) {
					Freigabe f = new Freigabe();
					f.setFreigabeId(Integer.parseInt(rSub.getString("FREIGABE_ID")));
					f.setRezeptId(Integer.parseInt(rSub.getString("REZEPT_ID")));
					f.setStatus(Integer.parseInt(rSub.getString("STATUS")));
					f.setBegründung(rSub.getString("BEGRUENDUNG"));
					rezept.setFreigabe(f);
				}
				rezept.setRezeptId(Integer.parseInt(rSub.getString("REZEPT_ID")));
				
				ergebnisse.add(rezept);
				
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
	
	public LinkedList<Rezept> searchFor(String name, String kategorie, LinkedList<String> schlagworte, LinkedList<String> zutaten) {
		
		LinkedList<Rezept> ergebnisse = new LinkedList<Rezept>();
		
		// TODO searchFOR basteln
		
		PreparedStatement searchQuery = null;
		ResultSet r = null;
		
		try {
			
			//START
			
			String queryString = 
					"SELECT rezept.REZEPT_ID, rezept.BILD_ID, rezept.BEZEICHNUNG AS rezeptBezeichnung, GESAMTBEWERTUNG, BEGRUENDUNG, FREIGABE_ID, STATUS, rezept.KATEGORIE_ID, kategorie.bezeichnung AS kategorieName "+
					"FROM rezept "+
					"LEFT OUTER JOIN freigabe ON(rezept.rezept_id = freigabe.rezept_id) "+
					"JOIN kategorie ON(kategorie.kategorie_id=rezept.kategorie_id) "+
					"WHERE  rezept.bezeichnung LIKE(?) "+
					"AND kategorie.bezeichnung LIKE(?) ";
					if(!schlagworte.isEmpty())
					{
						queryString += "AND rezept.rezept_id = ("+
						"SELECT schlagwort_rezept.rezept_id "+
						"FROM rezept "+
						"JOIN schlagwort_rezept ON(schlagwort_rezept.rezept_id = rezept.rezept_id) "+
						"JOIN schlagwort ON(schlagwort_rezept.schlagwort_id = schlagwort.schlagwort_id) "+
						"WHERE schlagwort.bezeichnung = ANY(";
						for(int i=0; i<schlagworte.size(); i++)
						{
								queryString += "?";
								if(i+1<schlagworte.size()) queryString += ",";
						}
						queryString += 
						") " +
						"GROUP BY schlagwort_rezept.rezept_id "+
						"HAVING COUNT(schlagwort_rezept.rezept_id) = ? ) ";
					}
					if(!zutaten.isEmpty())
					{
						queryString += "AND rezept.rezept_id = ( "+
						"SELECT rezept.rezept_id "+
						"FROM rezept "+
						"JOIN zutat_rezept ON(zutat_rezept.rezept_id = rezept.rezept_id) "+
						"JOIN zutat ON(zutat_rezept.zutat_id = zutat.zutat_id) "+
						"WHERE zutat.bezeichnung = ANY(";
						for(int i=0; i<zutaten.size(); i++)
						{
								queryString += "?";
								if(i+1<zutaten.size()) queryString += ",";
						}
						queryString +=
						") "+
						"GROUP BY rezept.rezept_id "+
						"HAVING COUNT(rezept.rezept_id) = ?)";
					}
					searchQuery = connection.prepareStatement(queryString);
					searchQuery.setString(1, name.equals("")?"%":name);
					searchQuery.setString(2, kategorie.equals("")?"%":kategorie);
					int offset = 3;
					if(!schlagworte.isEmpty())
					{
						for(int i=0; i<schlagworte.size(); i++)
						{			
							searchQuery.setString(offset, schlagworte.get(i));
							offset++;
						}
						searchQuery.setInt(offset, schlagworte.size());
						offset++;
					}
					
					if(!zutaten.isEmpty())
					{
						for(int i=0; i<zutaten.size(); i++)
						{			
							searchQuery.setString(offset, zutaten.get(i));
							offset++;
						}
						searchQuery.setInt(offset, zutaten.size());
					}
					//offset++; -> nicht nötig
			
			//END
					
			r = searchQuery.executeQuery();
			
			Rezept rezept;
			
			while(r.next()) {
				
				rezept = new Rezept();
				if(r.getString("BILD_ID") != null)
					rezept.setBildId(Integer.parseInt(r.getString("BILD_ID")));
				rezept.setBezeichnung(r.getString("REZEPTBEZEICHNUNG"));
				if(r.getString("GESAMTBEWERTUNG") != null)
					rezept.setGesamtbewertung(Double.parseDouble(r.getString("GESAMTBEWERTUNG")));
				else
					rezept.setGesamtbewertung(0.0);
				rezept.setKategorieId(Integer.parseInt(r.getString("KATEGORIE_ID")));
				Kategorie k = new Kategorie();
				k.setKategorieId(Integer.parseInt(r.getString("KATEGORIE_ID")));
				k.setBezeichnung(r.getString("KATEGORIENAME"));
				rezept.setKategorie(k);
				if(r.getString("FREIGABE_ID") != null) {
					Freigabe f = new Freigabe();
					f.setFreigabeId(Integer.parseInt(r.getString("FREIGABE_ID")));
					f.setRezeptId(Integer.parseInt(r.getString("REZEPT_ID")));
					f.setStatus(Integer.parseInt(r.getString("STATUS")));
					f.setBegründung(r.getString("BEGRUENDUNG"));
					rezept.setFreigabe(f);
				}
				rezept.setRezeptId(Integer.parseInt(r.getString("REZEPT_ID")));
				
				ergebnisse.add(rezept);
				
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
	
	public LinkedList<Rezept> getAll() {
		
		Statement s = null;
		ResultSet r;
		
		LinkedList<Rezept> ergebnisse = new LinkedList<Rezept>();
		
		try {
			s = connection.createStatement();
			r = s.executeQuery("SELECT * FROM rezept");
			
			Rezept rezept;
			
			while(r.next()) {
				
				rezept = new Rezept();
				if(r.getString("BILD_ID") != null)
					rezept.setBildId(Integer.parseInt(r.getString("BILD_ID")));
				rezept.setBezeichnung(r.getString("BEZEICHNUNG"));
				rezept.setGesamtbewertung(Double.parseDouble(r.getString("GESAMTBEWERTUNG")));
				rezept.setKategorieId(Integer.parseInt(r.getString("KATEGORIE_ID")));
				rezept.setRezeptId(Integer.parseInt(r.getString("REZEPT_ID")));
				
				ergebnisse.add(rezept);
				
			}
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
		
		return ergebnisse;
	}
	
}
package de.ai.rezeptverwaltung.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import de.ai.rezeptverwaltung.entities.Freigabe;
import de.ai.rezeptverwaltung.entities.Kategorie;
import de.ai.rezeptverwaltung.entities.Rezept;

public class RezeptService {

	Connection connection;
	
	public RezeptService(Connection connection) {
		
		this.connection = connection;
		
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
				rezept.setGesamtbewertung(Double.parseDouble(r.getString("GESAMTBEWERTUNG")));
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
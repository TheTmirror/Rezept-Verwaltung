package de.ai.rezeptverwaltung.panelview;

import java.sql.Connection;
import java.util.LinkedList;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.ai.rezeptverwaltung.entities.Bild;
import de.ai.rezeptverwaltung.entities.Kategorie;
import de.ai.rezeptverwaltung.entities.Mengeneinheiten;
import de.ai.rezeptverwaltung.entities.Rezept;
import de.ai.rezeptverwaltung.entities.Schlagwort;
import de.ai.rezeptverwaltung.entities.Zubereitungsschritt;
import de.ai.rezeptverwaltung.entities.Zutat;
import de.ai.rezeptverwaltung.entities.Zutat_Rezept;
import de.ai.rezeptverwaltung.services.BildService;
import de.ai.rezeptverwaltung.services.KategorieService;
import de.ai.rezeptverwaltung.services.MengeneinheitenService;
import de.ai.rezeptverwaltung.services.RezeptService;
import de.ai.rezeptverwaltung.services.SchlagwortService;
import de.ai.rezeptverwaltung.services.Schlagwort_Rezept_Service;
import de.ai.rezeptverwaltung.services.ZutatService;
import de.ai.rezeptverwaltung.services.Zutat_RezeptService;

public class RezeptHinzufuegen extends VerticalLayout {
	
	Connection connection;
	
	//Services
	RezeptService rs;
	
	//Hilfskomponenten
	LinkedList<LinkedList<TextField>> zutatenFelder;
	
	public RezeptHinzufuegen(Connection connection){
		this.connection = connection;
		
		initServices();
		init();
	}
	
	private void initServices() {
		
		rs = new RezeptService(connection);
		
	}

	private void init(){
		
		//oberlfäche
		Label sample = new Label("Rezept hinzufuegen");
		VerticalLayout layout = new VerticalLayout();
		HorizontalLayout layout1 = new HorizontalLayout();
		VerticalLayout layout2 = new VerticalLayout();
		VerticalLayout layout3 = new VerticalLayout();
		VerticalLayout layout4 = new VerticalLayout();
		
		
		
		//textfelder für name, username, kategorie und schlagwort erstellen
		TextField name = new TextField("Name des Rezepts: ");
		name.setMaxLength(20);
		//Wird nicht gebraucht
		TextField username = new TextField("Erstellt von: (ACHTUNG NICHT IMPLEMENTIERT IM ERD!");
		username.setMaxLength(20);
		TextField tfKategorie = new TextField("Kategorie zuweisen: ");
		tfKategorie.setMaxLength(20);
		TextArea taSchlagwort = new TextArea("Schlagworte ergänzen: ");
//		taSchlagwort.setMaxLength(20);
		
		//button für hinzufügen, zubereitung, zutat erstellen
		Button hinzufuegen = new Button("Rezept hinzufügen");
		hinzufuegen.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Rezept rezept = new Rezept();
				Kategorie kategorie = new Kategorie();
				Bild bild = new Bild();
				LinkedList<Schlagwort> schlagworte = new LinkedList<Schlagwort>();
				LinkedList<Zubereitungsschritt> zubereitungsschritte = new LinkedList<Zubereitungsschritt>();
				
				rezept.setBezeichnung(name.getValue());
				rezept.setKategorie(kategorie);
				rezept.setBild(bild);
				rezept.setSchlagworte(schlagworte);
				rezept.setZubereitungsschritte(zubereitungsschritte);
				
				//KategorieID per Squence
				kategorie.setBezeichnung(tfKategorie.getValue());
				KategorieService ks = new KategorieService(connection);
				ks.addOrUpdate(kategorie);
				kategorie = ks.getByBezeichnung(kategorie.getBezeichnung());
				
				//Bild
				BildService bs = new BildService(connection);
				bild.setPfad("//standardPfad/bild.png");
				bs.addOrUpdate(bild);
				bild = bs.getByPfad(bild.getPfad());
				
				//Rezept speichern -> Kennt zu dem Zeitpunkt alleseine FKs
				//Welche auch schon gespeichert sind
				rezept.setKategorie(kategorie);
				rezept.setBild(bild);
				rezept.setSchlagworte(schlagworte);
				rezept.setZubereitungsschritte(zubereitungsschritte);
				rs.addOrUpdate(rezept);
				rezept = rs.getByBezeichnung(rezept.getBezeichnung());
				rezept.setKategorie(kategorie);
				rezept.setBild(bild);
				rezept.setSchlagworte(schlagworte);
				rezept.setZubereitungsschritte(zubereitungsschritte);
				
				//Schlagworte filtern
				LinkedList<String> worte = new LinkedList<String>();
				
				String input = taSchlagwort.getValue();
				
				while(!input.equals("")) {
					int i = input.indexOf(" "); // 4
					int j = input.indexOf("\n");
					if(j < i && j != -1)
						i = j;
					else if(i == -1 && j != -1)
						i = j;
					else if(i == -1 && j == -1) {
						worte.add(input);
						break;
					}
					
					String word = input.substring(0, i); // from 0 to 3
					String rest = input.substring(i+1); // after the space to the rest of the line
					
					worte.add(word);
					input = rest;
				}
				
				for(String wort : worte) {
					Schlagwort s = new Schlagwort();
					s.setBezeichnung(wort);
					schlagworte.add(s);
				}
				
				//Schlagworte mit ID's versehen & speichern
				SchlagwortService ss = new SchlagwortService(connection);
				ss.addOrUpdate(schlagworte);
				LinkedList<Schlagwort> newSchlagworte = new LinkedList<Schlagwort>();
				for(Schlagwort swort : schlagworte)
					newSchlagworte.add(ss.getByBezeichnung(swort.getBezeichnung()));
				schlagworte = newSchlagworte;
				
				//Rezept - Schlagwort Tabelle füllen
				Schlagwort_Rezept_Service svs = new Schlagwort_Rezept_Service(connection);
				for(Schlagwort swort2 : schlagworte)
					svs.addOrUpdate(rezept, swort2);
				
//				Zutatenliste
				LinkedList<Zutat_Rezept> zutatRezeptListe = new LinkedList<Zutat_Rezept>();
				
				Zutat_Rezept zutatRezept;
				Mengeneinheiten einheit;
				Zutat zutat;
				
//				for(LinkedList<TextField> liste : zutatenFelder) {
//					//Mengeneinheit mit SequenceID
//					MengeneinheitenService ms = new MengeneinheitenService(connection);
//					einheit = new Mengeneinheiten();
//					einheit.setBezeichnung(liste.get(2).getValue());
//					ms.addOrUpdate(einheit);
//					einheit = ms.getByBezeichnung(einheit.getBezeichnung());
//				}
//				
//				for(LinkedList<TextField> liste : zutatenFelder) {
//					//Zutat mit ZutatId
//					ZutatService zs = new ZutatService(connection);
//					zutat = new Zutat();
//					zutat.setBezeichnung(liste.get(0).getValue());
//					zs.addOrUpdate(zutat);
//					zutat = zs.getByBezeichnung(zutat.getBezeichnung());
//				}
//				
//				for(LinkedList<TextField> liste : zutatenFelder) {
//					//Zutatrezept mit ID
//					
//					zutatRezept = new Zutat_Rezept();
//					zutatRezept.setAnzahl(Integer.parseInt(liste.get(1).getValue()));
//					zutatRezept.setMengeneinheit(einheit);
//					zutatRezept.setEinheitId(zutatRezept.getMengeneinheit().getEinheitId());
//					zutatRezept.setZutat(zutat);
//					zutatRezept.setZutatId(zutatRezept.getZutat().getZutatId());
//					zutatRezept.setRezeptId(rezept.getRezeptId());
//					
//				}
				
				//Zutatenliste neu
				int i = 0;
				for(TextField bezeichnung : zutatenFelder.get(0)) {
					
					//Mengeneinheit mit SequenceID
					MengeneinheitenService ms = new MengeneinheitenService(connection);
					einheit = new Mengeneinheiten();
					einheit.setBezeichnung(zutatenFelder.get(2).get(i).getValue());
					ms.addOrUpdate(einheit);
					einheit = ms.getByBezeichnung(einheit.getBezeichnung());
					
					//Zutat mit ZutatId
					ZutatService zs = new ZutatService(connection);
					zutat = new Zutat();
					zutat.setBezeichnung(zutatenFelder.get(0).get(i).getValue());
					zs.addOrUpdate(zutat);
					zutat = zs.getByBezeichnung(zutat.getBezeichnung());
					
					//Zutatrezept mit ID
					
					zutatRezept = new Zutat_Rezept();
					zutatRezept.setAnzahl(Integer.parseInt(zutatenFelder.get(1).get(i).getValue()));
					zutatRezept.setMengeneinheit(einheit);
					zutatRezept.setEinheitId(zutatRezept.getMengeneinheit().getEinheitId());
					zutatRezept.setZutat(zutat);
					zutatRezept.setZutatId(zutatRezept.getZutat().getZutatId());
					zutatRezept.setRezeptId(rezept.getRezeptId());
					
					zutatRezeptListe.add(zutatRezept);
					
					i++;
					
				}
				
				//Rezept - Zutat Tabelle speichern
				Zutat_RezeptService zrs = new Zutat_RezeptService(connection);
				for(Zutat_Rezept rs : zutatRezeptListe)
					zrs.addOrUpdate(rs);
				
			}
			
		});
		Button zubereitung = new Button("Zubereitungsschritt erstellen");
		Button zutat = new Button("Zutat hinzufügen ");

		addComponent(sample);
		addComponent(layout);
		layout.addComponent(layout1);
		layout1.addComponent(layout2);
		layout1.addComponent(layout3);
		layout1.addComponent(layout4);
		
		//text felder visualisieren
		layout2.addComponentAsFirst(new VerticalLayout(name, username, tfKategorie, taSchlagwort));
		
		//buttons visualisieren
		layout.addComponentAsFirst(new HorizontalLayout(zutat, zubereitung, hinzufuegen));
		hinzufuegen.addClickListener(e -> Notification.show("Rezept hinzugefügt",
				Type.TRAY_NOTIFICATION));
		
		//bei jedem click wird ein feld für zutat und/oder zubereitung erstellt
		zutatenFelder = new LinkedList<LinkedList<TextField>>();
		LinkedList<TextField> zFelder = new LinkedList<TextField>();
		LinkedList<TextField> mFelder = new LinkedList<TextField>();
		LinkedList<TextField> eFelder = new LinkedList<TextField>();
		zutatenFelder.addLast(zFelder);
		zutatenFelder.addLast(mFelder);
		zutatenFelder.addLast(eFelder);
		
		zutat.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				HorizontalLayout hl = new HorizontalLayout();
				layout3.addComponent(hl);
				
				TextField zF = new TextField("Zutat: ");
				TextField mF = new TextField("Menge: ");
				TextField eF = new TextField("Einheit: ");
				
				zutatenFelder.get(0).add(zF);
				zutatenFelder.get(1).add(mF);
				zutatenFelder.get(2).add(eF);
				
				hl.addComponent(zF);
				hl.addComponent(mF);
				hl.addComponent(eF);
			}
		});
		
//		zutat.addClickListener(t -> layout3.addComponents(
//				new HorizontalLayout(new TextField("Zutat:"), new TextField("Menge:"))));
		
		zubereitung.addClickListener(a -> layout4.addComponent(new TextArea("Zubereitungsschritt:"))); 
			


	}
	

}

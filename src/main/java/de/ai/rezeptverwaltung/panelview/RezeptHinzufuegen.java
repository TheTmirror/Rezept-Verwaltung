package de.ai.rezeptverwaltung.panelview;

import java.sql.Connection;
import java.util.LinkedList;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;

import de.ai.rezeptverwaltung.entities.Bild;
import de.ai.rezeptverwaltung.entities.Kategorie;
import de.ai.rezeptverwaltung.entities.Mengeneinheiten;
import de.ai.rezeptverwaltung.entities.Rezept;
import de.ai.rezeptverwaltung.entities.Schlagwort;
import de.ai.rezeptverwaltung.entities.Werkzeug;
import de.ai.rezeptverwaltung.entities.Zubereitungsschritt;
import de.ai.rezeptverwaltung.entities.Zutat;
import de.ai.rezeptverwaltung.entities.Zutat_Rezept;
import de.ai.rezeptverwaltung.helper.ThreeTypeArray;
import de.ai.rezeptverwaltung.services.BildService;
import de.ai.rezeptverwaltung.services.KategorieService;
import de.ai.rezeptverwaltung.services.MengeneinheitenService;
import de.ai.rezeptverwaltung.services.RezeptService;
import de.ai.rezeptverwaltung.services.SchlagwortService;
import de.ai.rezeptverwaltung.services.Schlagwort_Rezept_Service;
import de.ai.rezeptverwaltung.services.WerkzeugService;
import de.ai.rezeptverwaltung.services.ZubereitungsschrittService;
import de.ai.rezeptverwaltung.services.Zubereitungsschritt_BildService;
import de.ai.rezeptverwaltung.services.Zubereitungsschritt_RezeptService;
import de.ai.rezeptverwaltung.services.Zubereitungsschritt_WerkzeugService;
import de.ai.rezeptverwaltung.services.ZutatService;
import de.ai.rezeptverwaltung.services.Zutat_RezeptService;

public class RezeptHinzufuegen extends VerticalLayout{

	Connection connection;
	
	//Services
	RezeptService rs;
	
	//Hilfskomponenten
	LinkedList<LinkedList<TextField>> zutatenFelder;
	LinkedList<TextArea> zuFelder;
	LinkedList<TextField> wFelder;
	ThreeTypeArray tta;
	Panel p;
	
	public RezeptHinzufuegen(Connection connection){
		this.connection = connection;
		
		initServices();
		init();
	}
	
	private void initServices() {
		
		rs = new RezeptService(connection);
		
	}
	
	@SuppressWarnings("serial")
	private void init() {
		
		Label header = new Label("Rezept hinzufügen Prototyp");
		
		VerticalLayout vl1 = new VerticalLayout();
		vl1.setSizeUndefined();
		vl1.setMargin(false);
		HorizontalLayout hl1 = new HorizontalLayout();
		HorizontalLayout hl2 = new HorizontalLayout();
		hl2.setMargin(false);
		VerticalLayout vl2 = new VerticalLayout();
		VerticalLayout vl3 = new VerticalLayout();
		vl3.setMargin(new MarginInfo(true, false, true, false));
		Accordion acc = new Accordion();
		
		vl1.addComponent(hl1);
		vl1.addComponent(hl2);
		hl2.addComponent(vl2);
		vl2.setWidth(33, Unit.PERCENTAGE);
		hl2.addComponent(vl3);
		vl3.setWidth(33, Unit.PERCENTAGE);
		hl2.addComponent(acc);
		
		//textfelder für name, username, kategorie und schlagwort erstellen
		TextField name = new TextField("Name des Rezepts:(*) ");
		name.setMaxLength(20);
		TextField username = new TextField("Erstellt von: ");
		username.setMaxLength(20);
		username.setWidth(100, Unit.PERCENTAGE);
		TextField tfKategorie = new TextField("Kategorie zuweisen:(*) ");
		tfKategorie.setMaxLength(20);
		TextArea taSchlagwort = new TextArea("Schlagworte ergänzen: ");
		Label pflicht = new Label("(*) Pflichtfelder");
		
		vl2.addComponent(name);
		vl2.addComponent(username);
		vl2.addComponent(tfKategorie);
		vl2.addComponent(taSchlagwort);
		vl2.addComponent(pflicht);
		
		Button zutat = new Button("Zutat hinzufügen");
		zutat.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				HorizontalLayout hl = new HorizontalLayout();
				vl3.addComponent(hl);
				
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
		Button zubereitung = new Button("Zubereitungsschritt erstellen");
		Button werkzeug = new Button("Werkzeug hinzufügen");
		werkzeug.setEnabled(false);
		Button bild = new Button("Bild zu akutellem Zubereitungsschritt hinzufügen");
		bild.setEnabled(false);
		Button hinzufuegen = new Button("Rezept hinzufügen");
		hinzufuegen.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(name.isEmpty() || tfKategorie.isEmpty()) {
					Notification.show("Rezept nicht hinzugefügt!", Type.TRAY_NOTIFICATION);
					return;
				}
				
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
				
				while(worte.contains(""))
					worte.remove("");
				
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
				
				//Zutatenliste
				LinkedList<Zutat_Rezept> zutatRezeptListe = new LinkedList<Zutat_Rezept>();
				
				Zutat_Rezept zutatRezept;
				Mengeneinheiten einheit;
				Zutat zutat;
				
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
				
				//Zubereitungsschritt speichern
				//Zubereitungsschritt - Rezept speichern
				LinkedList<Zubereitungsschritt> schritte = new LinkedList<Zubereitungsschritt>();
				
				Zubereitungsschritt schritt;
				
				int schrittCounter = 1;
				for(TextArea ta : tta.getZuFelder()) {
					if(ta != null) {
						schritt = new Zubereitungsschritt();
						schritt.setBeschreibung(ta.getValue());
						schritt.setSchrittNummer(schrittCounter);
						schritte.addLast(schritt);
						schrittCounter++;
					}
				}
				
				ZubereitungsschrittService zs = new ZubereitungsschrittService(connection);
				Zubereitungsschritt_RezeptService zurs = new Zubereitungsschritt_RezeptService(connection);
				for(Zubereitungsschritt schritt1 : schritte) {
					zs.addOrUpdate(schritt1);
					int nummer = schritt1.getSchrittNummer();
					schritt1 = zs.getByBezeichnung(schritt1.getBeschreibung());
					zurs.addOrUpdate(rezept, schritt1, nummer);
				}
				
				//Werkzeug speichern
				//Zubereitungsschritt - Werkzeug speichern
				Werkzeug werkzeug;
				WerkzeugService ws = new WerkzeugService(connection);
				
				int zIndex = 0;
				int wIndex = 0;
				for(TextField tf : tta.getwFelder()) {
					if(tta.getZuFelder().get(wIndex) != null)
						zIndex = wIndex;
					if(tf != null) {
						werkzeug = new Werkzeug();
						werkzeug.setBezeichnung(tf.getValue());
						ws.addOrUpdate(werkzeug);
						werkzeug = ws.getByBezeichnung(werkzeug.getBezeichnung());
						//Lässt sucg dieser extra Zugriff vllt vermeiden?
						Zubereitungsschritt z = zs.getByBezeichnung(tta.getZuFelder().get(zIndex).getValue());
						Zubereitungsschritt_WerkzeugService zws = new Zubereitungsschritt_WerkzeugService(connection);
						zws.addOrUpdate(z, werkzeug);
					}
					wIndex++;
				}
				
				//Zubereitungsschritt - Bild füllen
				zIndex = 0;
				int bIndex = 0;
				for(TextField tf : tta.getbFelder()) {
					if(tta.getZuFelder().get(bIndex) != null)
						zIndex = bIndex;
					if(tf != null) {
						bild = new Bild();
						bild.setPfad(tf.getValue());
						bs.addOrUpdate(bild);
						bild = bs.getByPfad(bild.getPfad());
						//Lässt sucg dieser extra Zugriff vllt vermeiden?
						Zubereitungsschritt z = zs.getByBezeichnung(tta.getZuFelder().get(zIndex).getValue());
						Zubereitungsschritt_BildService zbs = new Zubereitungsschritt_BildService(connection);
						zbs.addOrUpdate(z, bild);
					}
					bIndex++;
				}
				
				
				
				
//				Notification.show("Rezept vollständig hinzugefügt!", Type.TRAY_NOTIFICATION);
				
			}
			
		});
		
		hl1.addComponent(zutat);
		hl1.addComponent(zubereitung);
		hl1.addComponent(werkzeug);
		hl1.addComponent(bild);
		hl1.addComponent(hinzufuegen);
		
		//bei jedem click wird ein feld für zutat und/oder zubereitung erstellt
		zutatenFelder = new LinkedList<LinkedList<TextField>>();
		LinkedList<TextField> zFelder = new LinkedList<TextField>();
		LinkedList<TextField> mFelder = new LinkedList<TextField>();
		LinkedList<TextField> eFelder = new LinkedList<TextField>();
		zutatenFelder.addLast(zFelder);
		zutatenFelder.addLast(mFelder);
		zutatenFelder.addLast(eFelder);
		
		tta = new ThreeTypeArray();
		
		zubereitung.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				werkzeug.setEnabled(true);
				bild.setEnabled(true);
				TextArea taZubereitungsschritt = new TextArea("Zubereitungsschritt: ");
				p = new Panel();
				HorizontalLayout content = new HorizontalLayout();
				content.setMargin(true);
				p.setContent(content);
				VerticalLayout vlText = new VerticalLayout();
				vlText.setMargin(false);
				VerticalLayout vlBild = new VerticalLayout();
				vlBild.setMargin(false);
				VerticalLayout vlWerkzeug = new VerticalLayout();
				vlWerkzeug.setMargin(false);
				vlText.addComponent(taZubereitungsschritt);
				content.addComponent(vlText);
				content.addComponent(vlWerkzeug);
				content.addComponent(vlBild);
				acc.addTab(p, "Zubereitungsschritt");
				acc.setSelectedTab(p);
				tta.addZuFeld(taZubereitungsschritt);
			}
			
		});
		
		werkzeug.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				TextField tfWerkzeug = new TextField("Werkzeug: ");
				VerticalLayout vlWerkzeug = (VerticalLayout) ((HorizontalLayout) p.getContent()).getComponent(1);
				vlWerkzeug.addComponent(tfWerkzeug);
				tta.addWFeld(tfWerkzeug);
			}
		});
		
		bild.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				TextField tfBild = new TextField("Bild(Pfad): ");
				VerticalLayout vlBild = (VerticalLayout) ((HorizontalLayout) p.getContent()).getComponent(2);
				vlBild.addComponent(tfBild);
				tta.addBFeld(tfBild);
			}
		});
		
		
		
		addComponent(header);
		addComponent(vl1);
		
	}
	
}

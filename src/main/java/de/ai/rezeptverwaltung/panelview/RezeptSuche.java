package de.ai.rezeptverwaltung.panelview;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.ItemClick;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.grid.ItemClickListener;
import com.vaadin.ui.components.grid.SingleSelectionModel;
import com.vaadin.ui.renderers.ButtonRenderer;

import de.ai.rezeptverwaltung.entities.Bewertung;
import de.ai.rezeptverwaltung.entities.Bild;
import de.ai.rezeptverwaltung.entities.Freigabe;
import de.ai.rezeptverwaltung.entities.Kommentar;
import de.ai.rezeptverwaltung.entities.Rezept;
import de.ai.rezeptverwaltung.entities.Schlagwort;
import de.ai.rezeptverwaltung.entities.Werkzeug;
import de.ai.rezeptverwaltung.entities.Zubereitungsschritt;
import de.ai.rezeptverwaltung.entities.Zutat_Rezept;
import de.ai.rezeptverwaltung.services.BewertungService;
import de.ai.rezeptverwaltung.services.BildService;
import de.ai.rezeptverwaltung.services.FreigabeService;
import de.ai.rezeptverwaltung.services.KommentarService;
import de.ai.rezeptverwaltung.services.RezeptService;
import de.ai.rezeptverwaltung.services.SchlagwortService;
import de.ai.rezeptverwaltung.services.SpeisekarteViewService;
import de.ai.rezeptverwaltung.services.WerkzeugService;
import de.ai.rezeptverwaltung.services.ZubereitungsschrittService;
import de.ai.rezeptverwaltung.services.Zutat_RezeptService;

public class RezeptSuche extends HorizontalLayout{
	
	Connection connection;
	RezeptService rs;
	LinkedList<Rezept> ergebnisse = new LinkedList<Rezept>();
	
	public RezeptSuche(Connection connection) {
		
		this.connection = connection;
		
		rs = new RezeptService(connection);
		
		init();
		
	}
	
	@SuppressWarnings({ "unchecked", "serial" })
	private void init() {
		
		Label header = new Label("Rezept suchen Prototyp");
		
		FormLayout fl = new FormLayout();
//		fl.setWidth(50.0f, Unit.PERCENTAGE);
		
		VerticalLayout l = new VerticalLayout();
		l.addComponent(new Label("Ergebnisse"));
		
		TextField tfName = new TextField("Name: ");
		
		TextField tfKategorie = new TextField("Kategorie: ");
		
		TextArea taSchlagworte = new TextArea("Schlagworte: ");
		
		TextArea taZutaten = new TextArea("Zutaten: ");
		
		Button suchen = new Button("Suchen!");
		
		Grid<Rezept> ergebnisListe = new Grid<Rezept>();
		ergebnisListe.setSelectionMode(SelectionMode.SINGLE);
		ergebnisListe.setItems(ergebnisse);
		ergebnisListe.addColumn(Rezept::getBezeichnung).setCaption("Bezeichnung");
		ergebnisListe.addItemClickListener(new ItemClickListener() {

			@Override
			public void itemClick(ItemClick event) {
				Rezept selected = (Rezept) event.getItem();
				l.removeAllComponents();
				l.addComponent(initDetailansicht(selected));
			}
			
		});
		
		suchen.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				
//				if(tfName.isEmpty() && tfKategorie.isEmpty() && taSchlagworte.isEmpty() && taZutaten.isEmpty()) {
//					System.out.println("Bitte mindestens 1 eingeben");
//					return;
//				}
				
				String name = tfName.getValue();
				String kategorie = tfKategorie.getValue();
				LinkedList<String> schlagworte = new LinkedList<String>();
				
				String input = taSchlagworte.getValue();
				
				while(!input.equals("")) {
					int i = input.indexOf(" "); // 4
					int j = input.indexOf("\n");
					if(j < i && j != -1)
						i = j;
					else if(i == -1 && j != -1)
						i = j;
					else if(i == -1 && j == -1) {
						schlagworte.add(input);
						break;
					}
					
					String word = input.substring(0, i); // from 0 to 3
					String rest = input.substring(i+1); // after the space to the rest of the line
					
					schlagworte.add(word);
					input = rest;
				}
				
				while(schlagworte.contains(""))
					schlagworte.remove("");
				
				LinkedList<String> zutaten = new LinkedList<String>();
				
				input = taZutaten.getValue();
				
				while(!input.equals("")) {
					int i = input.indexOf(" "); // 4
					int j = input.indexOf("\n");
					if(j < i && j != -1)
						i = j;
					else if(i == -1 && j != -1)
						i = j;
					else if(i == -1 && j == -1) {
						zutaten.add(input);
						break;
					}
					
					String word = input.substring(0, i); // from 0 to 3
					String rest = input.substring(i+1); // after the space to the rest of the line
					
					zutaten.add(word);
					input = rest;
				}
				
				while(zutaten.contains(""))
					zutaten.remove("");
				
				ergebnisse = rs.searchFor(name, kategorie, schlagworte, zutaten);
//				ergebnisse = rs.getAll();
				ergebnisListe.setItems(ergebnisse);
				
				l.removeAllComponents();
				l.addComponent(ergebnisListe);
				
			}
			
		});
		
		//ACHTUNG: Alle Rezepte auf Speisekarte -> Freigabe Button Disablen
		Button speisekarteSuche = new Button("Alle Rezepte auf Speisekarte");
		speisekarteSuche.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				ergebnisse = rs.getSpeisekarte();
				ergebnisListe.setItems(ergebnisse);
				
				l.removeAllComponents();
				l.addComponent(ergebnisListe);
			}
			
		});
		
		//Alle freigegebenen Rezepte
		Button freigegebeneSuche = new Button("Alle freigegebenen Rezepte");
		freigegebeneSuche.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				ergebnisse = rs.getFreigegebene();
				ergebnisListe.setItems(ergebnisse);
				
				l.removeAllComponents();
				l.addComponent(ergebnisListe);
			}
			
		});
		
		fl.addComponent(tfName);
		fl.addComponent(tfKategorie);
		fl.addComponent(taSchlagworte);
		fl.addComponent(taZutaten);
		fl.addComponent(suchen);
		fl.addComponent(speisekarteSuche);
		fl.addComponent(freigegebeneSuche);
		
		l.addComponent(ergebnisListe);
		
		addComponent(header);
		addComponent(fl);
		addComponent(l);
		
	}
	
	private Panel initDetailansicht(Rezept rezept) {
		
		SchlagwortService ss = new SchlagwortService(connection);
		BildService bs = new BildService(connection);
		ZubereitungsschrittService zs = new ZubereitungsschrittService(connection);
		
		Panel details = new Panel();
		
		HorizontalLayout outerLayout = new HorizontalLayout();
		
		FormLayout layout = new FormLayout();
		layout.addComponent(new Label(rezept.getBezeichnung()));
		LinkedList<Bild> rezeptBild = bs.getAllByBildId(rezept.getBildId());
		if(!rezeptBild.isEmpty()) {
			rezept.setBild(bs.getAllByBildId(rezept.getBildId()).getFirst());
			layout.addComponent(new Label("Hier das Bild: " + rezept.getBild().getPfad()));
		}
		layout.addComponent(new Label("Kategorie: " + rezept.getKategorie().getBezeichnung()));
		rezept.setSchlagworte(ss.getAllByRezeptId(rezept.getRezeptId()));
		String temp = "";
		for(Schlagwort s : rezept.getSchlagworte())
			temp += s.getBezeichnung() + " ";
		layout.addComponent(new Label("Schlagworte: " + temp));
		Label bewertung = new Label("Bewertung: " + rezept.getGesamtbewertung());
		layout.addComponent(bewertung);
		if(rezept.getFreigabe() != null)
			if(rezept.getFreigabe().getStatus() == 0)
				layout.addComponent(new Label("Ablehngrund: " + rezept.getFreigabe().getBegründung()));
		
		Button freigeben = new Button("Freigeben");
		Button entziehen = new Button("Freigabe entziehen");
		
		if(rezept.getRezeptId() == 0) {
			
			Rezept r2 = rs.getByBezeichnung(rezept.getBezeichnung());
			rezept.setRezeptId(r2.getRezeptId());
			
		}
		
		SpeisekarteViewService svs = new SpeisekarteViewService(connection);
		if(svs.isAufSpeisekarte(rezept))
			entziehen.setEnabled(false);
		
		freigeben.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if(rezept.getFreigabe() == null) {
					
					FreigabeService fs = new FreigabeService(connection);
					
					Freigabe f = new Freigabe();
					f.setBegründung(null);
					f.setRezeptId(rezept.getRezeptId());
					f.setStatus(1);
					
					rezept.setFreigabe(f);
					
					fs.createFreigabe(rezept);
				}
				else {
					FreigabeService fs = new FreigabeService(connection);
					
					Freigabe f = rezept.getFreigabe();
					f.setBegründung(null);
					f.setStatus(1);
					
					fs.updateFreigabe(rezept);
				}
				
				freigeben.setEnabled(false);
				entziehen.setEnabled(true);
			}
			
		});
		
		entziehen.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				//Keine Freigabe existiert
				if(rezept.getFreigabe() == null) {
					Window w = new Window("Begründung");
					UI.getCurrent().addWindow(w);
					VerticalLayout vl = new VerticalLayout();
					Label l = new Label("Bitte eine Begründung angeben!");
					TextArea ta = new TextArea();
					Button ok = new Button("OK");
					ok.addClickListener(new ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
							FreigabeService fs = new FreigabeService(connection);
							
							Freigabe f = new Freigabe();
							f.setRezeptId(rezept.getRezeptId());
							f.setBegründung(ta.getValue());
							f.setStatus(0);
							
							rezept.setFreigabe(f);
							
							fs.createFreigabe(rezept);
							
							w.close();
							
							entziehen.setEnabled(false);
							freigeben.setEnabled(true);
						}
						
					});
					vl.addComponent(l);
					vl.addComponent(ta);
					vl.addComponent(ok);
					w.setContent(vl);
					w.center();
				}
				else {
					
					Window w = new Window("Begründung");
					UI.getCurrent().addWindow(w);
					VerticalLayout vl = new VerticalLayout();
					Label l = new Label("Bitte eine Begründung angeben!");
					TextArea ta = new TextArea();
					Button ok = new Button("OK");
					ok.addClickListener(new ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
							FreigabeService fs = new FreigabeService(connection);
							
							Freigabe f = rezept.getFreigabe();
							f.setBegründung(ta.getValue());
							f.setStatus(0);
							
							fs.updateFreigabe(rezept);
							
							w.close();
							
							entziehen.setEnabled(false);
							freigeben.setEnabled(true);
						}
						
					});
					vl.addComponent(l);
					vl.addComponent(ta);
					vl.addComponent(ok);
					w.setContent(vl);
					w.center();
					
				}
			}
			
		});
		
		if(rezept.getFreigabe() != null)
			if(rezept.getFreigabe().getStatus() == 0)
				entziehen.setEnabled(false);
			else if(rezept.getFreigabe().getStatus() == 1)
				freigeben.setEnabled(false);
		
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.addComponent(freigeben);
		buttons.addComponent(entziehen);
		layout.addComponent(buttons);
		
		//Kommentare
		Panel kommentare = new Panel();
		
		Grid<Kommentar> kommentar = new Grid<Kommentar>();
		
//		VerticalLayout kommentarLayout = new VerticalLayout();
//		kommentarLayout.setHeight(150.0f, Unit.PIXELS);
		
		KommentarService ks = new KommentarService(connection);
		
		rezept.setKommentare(ks.getAllById(rezept.getRezeptId()));
		
//		// TODO Kommentarpanel schön machen!
//		for(Kommentar k : rezept.getKommentare()) {
//			kommentarLayout.addComponent(new Label(k.getKommentartext()));
//		}
		
		kommentar.setItems(rezept.getKommentare());
		kommentar.addColumn(Kommentar::getKommentartext);
		kommentar.setHeightByRows(5);
		kommentare.setContent(kommentar);
		
		layout.addComponent(kommentare);
		
		//Neue Kommentare
		Button neuesKommentar = new Button("Kommentar hinzufügen!");
		neuesKommentar.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				Window w = new Window("Neues Kommentar");
				UI.getCurrent().addWindow(w);
				w.center();
				VerticalLayout vl = new VerticalLayout();
				w.setContent(vl);
				
				Label l = new Label("Bitte Kommentar eingeben!");
				TextArea ta = new TextArea();
				Label la = new Label("Bitte Autor eingeben!");
				TextField ta2 = new TextField();
				Button ok = new Button("Kommentar speichern");
				
				ok.addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						KommentarService ks = new KommentarService(connection);
						
						Kommentar k = new Kommentar();
						k.setRezeptId(rezept.getRezeptId());
						k.setKommentartext(ta.getValue());
						if(!ta2.isEmpty())
							k.setAutor(ta2.getValue());
						
						ks.save(k);
						
						rezept.setKommentare(ks.getAllById(rezept.getRezeptId()));
//						//Damit man mit dem selben Objekt weiter arbeiten kann
//						rezept.getKommentare().add(k);
						kommentar.setItems(rezept.getKommentare());
						
						w.close();
					}
					
				});
				
				vl.addComponent(l);
				vl.addComponent(ta);
				vl.addComponent(la);
				vl.addComponent(ta2);
				vl.addComponent(ok);
				
				ok.setEnabled(false);
				
				ta.addValueChangeListener(new ValueChangeListener<String>() {
					
					@Override
					public void valueChange(ValueChangeEvent<String> event) {
						String testString = event.getValue();
						testString = testString.replaceAll("\\s","");
						if(testString.equals(""))
							ok.setEnabled(false);
						else
							ok.setEnabled(true);
					}
					
				});
			}
			
		});
		
		layout.addComponent(neuesKommentar);
		
		//Bewertung
		Slider s = new Slider("Bewertung");
		s.setMin(0.0);
		s.setMax(5.0);
		s.setValue(3.0);
		
		Button sliderOk = new Button("Bewertung hinterlasen!");
		
		sliderOk.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				Bewertung b = new Bewertung();
				b.setRezeptId(rezept.getRezeptId());
				b.setPunkte(s.getValue().intValue());
				
				BewertungService bs = new BewertungService(connection);
				bs.save(b);
				
				rezept.setGesamtbewertung(rs.getById(rezept.getRezeptId()).getGesamtbewertung());
				bewertung.setValue("Bewertung: " + rezept.getGesamtbewertung());
			}
			
		});
		
		layout.addComponent(s);
		layout.addComponent(sliderOk);
		
		FormLayout af = new FormLayout();
		af.setMargin(true);
		
		//Zutaten
		Grid<Zutat_Rezept> zutaten = new Grid<Zutat_Rezept>();
		Zutat_RezeptService zrs = new Zutat_RezeptService(connection);
		rezept.setZutat_rezept(zrs.getAllById(rezept.getRezeptId()));
		zutaten.setItems(rezept.getZutat_rezept());
		zutaten.addColumn(Zutat_Rezept::getZutat);
		zutaten.addColumn(Zutat_Rezept::getAnzahl);
		zutaten.addColumn(Zutat_Rezept::getMengeneinheit);
		zutaten.setHeightByRows(5);
		
		//Zubereitungsschritte
		Accordion schritte = new Accordion();
		af.addComponent(schritte);
		
		af.addComponent(zutaten);
		
		rezept.setZubereitungsschritte(zs.getAllById(rezept.getRezeptId()));
		
		if(rezept.getZubereitungsschritte().isEmpty())
			schritte.addTab(new VerticalLayout(new Label("Es sind keine Schritte vorhanden")));
		
		//New
		int i = 1;
		GridLayout schritt = null;
		
		for(Zubereitungsschritt z : rezept.getZubereitungsschritte()) {
			schritt = new GridLayout(1, 2);
			schritt.setSizeFull();
			VerticalLayout vlText = new VerticalLayout();
			VerticalLayout vlBild = new VerticalLayout();
			VerticalLayout vlWerkzeug = new VerticalLayout();
			vlText.setSizeFull();
			vlBild.setSizeFull();
			vlWerkzeug.setSizeFull();
			
			vlText.addComponent(new Label(z.getBeschreibung()));
			
			for(Bild b : z.getBilder())
				vlBild.addComponent(new Label(b.getPfad()));
			
			WerkzeugService ws = new WerkzeugService(connection);
			z.setWerkzeuge(ws.getAllById(z.getSchrittId()));
			Accordion werkzeuge = new Accordion();
			int j = 1;
			for(Werkzeug w : z.getWerkzeuge()) {
				werkzeuge.addTab(new VerticalLayout(new Label(w.getBezeichnung())), "Werkzeug " + j);
				j++;
			}
			vlWerkzeug.addComponent(werkzeuge);
			
			schritt.addComponent(vlText, 0, 0);
			GridLayout underSchritt = new GridLayout(2, 1);
			if(!z.getBilder().isEmpty())
				underSchritt.addComponent(vlBild, 0, 0);
			if(!z.getWerkzeuge().isEmpty())
				underSchritt.addComponent(vlWerkzeug, 1, 0);
			schritt.addComponent(underSchritt, 0, 1);
			
			schritte.addTab(schritt, "Schritt " + i);
			i++;
		}
		
		
		//Old
//		int i = 1;
//		VerticalLayout schritt = null;
//		Accordion werkzeuge = null;
//		WerkzeugService ws = new WerkzeugService(connection);
//		for(Zubereitungsschritt z : rezept.getZubereitungsschritte()) {
//			z.setBilder(bs.getAllBySchrittId(z.getSchrittId()));
//			schritt = new VerticalLayout();
//			Label l = new Label(z.getBeschreibung());
//			schritt.addComponent(l);
//			for(Bild b : z.getBilder())
//				schritt.addComponent(new Label(b.getPfad()));
//			
//			werkzeuge = new Accordion();
//			z.setWerkzeuge(ws.getAllById(z.getSchrittId()));
//			int j = 1;
//			for(Werkzeug w : z.getWerkzeuge()) {
//				werkzeuge.addTab(new Label(w.getBezeichnung()), "Werkzeug " + j);
//				j++;
//			}
//			if(!z.getWerkzeuge().isEmpty())
//				schritt.addComponent(werkzeuge);
//			
//			schritte.addTab(schritt, "Schritt " + i);
//			i++;
//		}
		
		outerLayout.addComponent(layout);
		outerLayout.addComponent(af);
		
		details.setContent(outerLayout);
		
		return details;
		
	}

}

package de.ai.rezeptverwaltung.panelview;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.grid.ItemClickListener;
import com.vaadin.ui.components.grid.SingleSelectionModel;
import com.vaadin.ui.renderers.ButtonRenderer;

import de.ai.rezeptverwaltung.entities.Bild;
import de.ai.rezeptverwaltung.entities.Freigabe;
import de.ai.rezeptverwaltung.entities.Rezept;
import de.ai.rezeptverwaltung.entities.Schlagwort;
import de.ai.rezeptverwaltung.entities.Zubereitungsschritt;
import de.ai.rezeptverwaltung.services.BildService;
import de.ai.rezeptverwaltung.services.FreigabeService;
import de.ai.rezeptverwaltung.services.RezeptService;
import de.ai.rezeptverwaltung.services.SchlagwortService;
import de.ai.rezeptverwaltung.services.ZubereitungsschrittService;

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
				
				ergebnisse = rs.searchFor(name, kategorie, schlagworte, zutaten);
//				ergebnisse = rs.getAll();
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
		rezept.setSchlagworte(ss.getAllById(rezept.getRezeptId()));
		String temp = "";
		for(Schlagwort s : rezept.getSchlagworte())
			temp += s.getBezeichnung() + " ";
		layout.addComponent(new Label("Schlagworte: " + temp));
		layout.addComponent(new Label("Bewertung: " + rezept.getGesamtbewertung()));
		if(rezept.getFreigabe() != null)
			if(rezept.getFreigabe().getStatus() == 0)
				layout.addComponent(new Label("Ablehngrund: " + rezept.getFreigabe().getBegründung()));
		
		Button freigeben = new Button("Freigeben");
		Button entziehen = new Button("Freigabe entziehen");
		
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
		
		//Zubereitungsschritte
		FormLayout af = new FormLayout();
		af.setMargin(true);
		Accordion schritte = new Accordion();
		af.addComponent(schritte);
		
		rezept.setZubereitungsschritte(zs.getAllById(rezept.getRezeptId()));
		
		if(rezept.getZubereitungsschritte().isEmpty())
			schritte.addTab(new VerticalLayout(new Label("Es sind keine Schritte vorhanden")));
		
		int i = 1;
		for(Zubereitungsschritt z : rezept.getZubereitungsschritte()) {
			schritte.addTab(new VerticalLayout(new Label(z.getBeschreibung())), "Schritt " + i);
			i++;
		}
		
		outerLayout.addComponent(layout);
		outerLayout.addComponent(af);
		
		details.setContent(outerLayout);
		
		return details;
		
	}

}

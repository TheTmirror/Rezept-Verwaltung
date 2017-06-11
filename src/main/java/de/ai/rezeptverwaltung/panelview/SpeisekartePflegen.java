package de.ai.rezeptverwaltung.panelview;

import java.sql.Connection;
import java.util.Iterator;

import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.ai.rezeptverwaltung.entities.Rezept;
import de.ai.rezeptverwaltung.entities.RezeptSpeisekarte;
import de.ai.rezeptverwaltung.services.RezeptService;
import de.ai.rezeptverwaltung.services.SpeisekarteViewService;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid.SelectionMode;

public class SpeisekartePflegen extends VerticalLayout{

	Connection connection;
	
	//Service
	RezeptService rs;
	SpeisekarteViewService svs;
	
	public SpeisekartePflegen (Connection connection) {
		
		this.connection = connection;
		
		initServices();
		initView();
		
	}
	
	private void initServices() {
		
		rs = new RezeptService(connection);
		svs = new SpeisekarteViewService(connection);
		
	}
	
	private void initView() {
		addComponent(new Label("Speisekarte pflegen Prototyp"));
		
		//Drag and Drop Grids
		HorizontalLayout grids = new HorizontalLayout();
		Grid<RezeptSpeisekarte> left = new Grid<RezeptSpeisekarte>();
		Grid<Rezept> right = new Grid<Rezept>();
		
		////Vertical Button Layout
		VerticalLayout gridsButtonLayout = new VerticalLayout();
		gridsButtonLayout.setMargin(new MarginInfo(true, false, true, false));
		Button add = new Button("Rezept hinzufügen");
		Button remove = new Button("Rezept entfernen");
		
		add.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Rezept selectedItem1 = null;
				
				for(Rezept s : right.getSelectedItems())
					selectedItem1 = s;
				
				//gecheatet wegen final -.-
				final Rezept selectedItem = selectedItem1;
				
				//Preis Window
				Window w = new Window("Preis");
				UI.getCurrent().addWindow(w);
				w.center();
				FormLayout content = new FormLayout();
				TextField tfPreis = new TextField();
				Button ok = new Button("Rezept Speichern!");
				content.addComponent(tfPreis);
				content.addComponent(ok);
				w.setContent(content);
				
				ok.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						svs.add(1, selectedItem, Integer.parseInt(tfPreis.getValue()));
						w.close();
					}
				});
				
			}
			
		});
		
		remove.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				RezeptSpeisekarte selectedItem = null;
				
				for(RezeptSpeisekarte s : left.getSelectedItems())
					selectedItem = s;
				
				svs.remove(selectedItem.getRezeptId());
			}
			
		});
		
		gridsButtonLayout.addComponent(add);
		gridsButtonLayout.addComponent(remove);
		
		left.setSelectionMode(SelectionMode.SINGLE);
		left.setItems(rs.getSpeisekarteView());
		left.addColumn(RezeptSpeisekarte::getBezeichnung).setCaption("Name");
		left.addColumn(RezeptSpeisekarte::getPreis).setCaption("Preis");
		left.addSelectionListener(new SelectionListener<RezeptSpeisekarte>() {
			
			@Override
			public void selectionChange(SelectionEvent<RezeptSpeisekarte> event) {
				if(event.getAllSelectedItems().size() != 0) {
					right.deselectAll();
					add.setEnabled(false);
					remove.setEnabled(true);
				}
			}
			
		});
		
		right.setSelectionMode(SelectionMode.SINGLE);
		right.setItems(rs.getFreigegebene());
		right.addColumn(Rezept::getBezeichnung).setCaption("Name");
		right.addSelectionListener(new SelectionListener<Rezept>() {
			
			@Override
			public void selectionChange(SelectionEvent<Rezept> event) {
				if(event.getAllSelectedItems().size() != 0) {
					left.deselectAll();
					add.setEnabled(true);
					remove.setEnabled(false);
				}
			}
			
		});
		
		grids.addComponent(left);
		grids.addComponent(gridsButtonLayout);
		grids.addComponent(right);
		
		addComponent(grids);
	}
	
}

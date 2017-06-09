package de.ai.rezeptverwaltung.panelview;

import java.util.LinkedList;
import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class RezeptSuche extends HorizontalLayout{
	
	public RezeptSuche() {
		
		init();
		
	}
	
	private void init() {
		
		Label header = new Label("Rezept suchen Prototyp");
		
		FormLayout fl = new FormLayout();
//		fl.setWidth(50.0f, Unit.PERCENTAGE);
		
		TextField tfName = new TextField("Name: ");
		
		Button suchen = new Button("Suchen!");
		
		fl.addComponent(tfName);
		fl.addComponent(suchen);
		
		VerticalLayout l = new VerticalLayout();
		l.addComponent(new Label("Ergebnisse"));
//		l.setWidth(50.0f, Unit.PERCENTAGE);
		
		LinkedList<String> ergebnisse = new LinkedList<String>();
		ergebnisse.add("A");
		ergebnisse.add("B");
		ergebnisse.add("C");
		ergebnisse.add("D");
		ergebnisse.add("E");
		ergebnisse.add("F");
		ergebnisse.add("G");
		ergebnisse.add("H");
		ergebnisse.add("I");
		ergebnisse.add("J");
		ergebnisse.add("K");
		ergebnisse.add("L");
		ergebnisse.add("M");
		ergebnisse.add("N");
		ergebnisse.add("O");
		ergebnisse.add("P");
		
		ListSelect<String> ergebnisListe = new ListSelect<String>();
		ergebnisListe.setData(ergebnisse);
		ergebnisListe.setRows(15);
		ergebnisListe.setWidth(100.0f, Unit.PERCENTAGE);
		
		l.addComponent(ergebnisListe);
		
		addComponent(header);
		addComponent(fl);
		addComponent(l);
		
	}

}

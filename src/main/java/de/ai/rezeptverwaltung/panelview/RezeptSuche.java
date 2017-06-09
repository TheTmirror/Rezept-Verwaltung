package de.ai.rezeptverwaltung.panelview;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class RezeptSuche extends VerticalLayout{
	
	public RezeptSuche() {
		
		init();
		
	}
	
	private void init() {
		
		addComponent(new Label("Rezept Suche Vorschau"));
		
	}

}

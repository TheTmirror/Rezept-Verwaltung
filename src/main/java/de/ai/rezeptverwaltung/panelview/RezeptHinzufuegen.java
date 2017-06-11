package de.ai.rezeptverwaltung.panelview;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class RezeptHinzufuegen extends VerticalLayout {
	
	public RezeptHinzufuegen(){
		init();
	}

	public void init(){
		
		//oberlfäche
		Label sample = new Label("Rezept hinzufuegen");
		VerticalLayout layout1 = new VerticalLayout();
		
		//textfelder für name, username, kategorie und schlagwort erstellen
		TextField name = new TextField("Name des Rezepts: ");
		name.setMaxLength(20);
		TextField username = new TextField("Erstellt von: ");
		username.setMaxLength(20);
		TextField kategorie = new TextField("Kategorie zuweisen: ");
		kategorie.setMaxLength(20);
		TextField schlagwort = new TextField("Schlagworte ergänzen: ");
		schlagwort.setMaxLength(20);
		
		//button für hinzufügen, zubereitung, zutat erstellen
		Button hinzufuegen = new Button("Rezept hinzufügen");
		Button zubereitung = new Button("Zubereitungsschritt erstellen");
		Button zutat = new Button("Zutat hinzufügen ");

		addComponent(sample);
		addComponent(layout1);
		
		//text felder visualisieren
		layout1.addComponentAsFirst(new VerticalLayout(name, username, kategorie, schlagwort));
		
		//buttons visualisieren
		layout1.addComponentAsFirst(new HorizontalLayout(zutat, zubereitung, hinzufuegen));
//		layout1.addComponentAsFirst(zubereitung);
//		layout1.addComponentAsFirst(hinzufuegen);
		hinzufuegen.addClickListener(e -> Notification.show("Rezept hinzugefügt",
				Type.TRAY_NOTIFICATION));
		
		//bei jedem click wird ein feld für zutat und/oder zubereitung erstellt
		zutat.addClickListener(t -> layout1.addComponents(
				new HorizontalLayout(new TextField("Zutat:"), new TextField("Menge:"))));
		
		zubereitung.addClickListener(a -> layout1.addComponent(new TextArea("Zubereitungsschritt:"))); 
			


	}
	

}

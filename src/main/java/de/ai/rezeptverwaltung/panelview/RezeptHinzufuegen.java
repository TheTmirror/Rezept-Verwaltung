package de.ai.rezeptverwaltung.panelview;

import java.sql.Connection;

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
	
	Connection connection;
	
	public RezeptHinzufuegen(Connection connection){
		this.connection = connection;
		
		init();
	}

	public void init(){
		
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
		addComponent(layout);
		layout.addComponent(layout1);
		layout1.addComponent(layout2);
		layout1.addComponent(layout3);
		layout1.addComponent(layout4);
		
		//text felder visualisieren
		layout2.addComponentAsFirst(new VerticalLayout(name, username, kategorie, schlagwort));
		
		//buttons visualisieren
		layout.addComponentAsFirst(new HorizontalLayout(zutat, zubereitung, hinzufuegen));
		hinzufuegen.addClickListener(e -> Notification.show("Rezept hinzugefügt",
				Type.TRAY_NOTIFICATION));
		
		//bei jedem click wird ein feld für zutat und/oder zubereitung erstellt
		zutat.addClickListener(t -> layout3.addComponents(
				new HorizontalLayout(new TextField("Zutat:"), new TextField("Menge:"))));
		
		zubereitung.addClickListener(a -> layout4.addComponent(new TextArea("Zubereitungsschritt:"))); 
			


	}
	

}

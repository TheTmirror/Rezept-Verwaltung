package de.ai.rezeptverwaltung.panelview;


import java.util.ArrayList;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class SpeisekartePflegen extends HorizontalLayout{
		
	public SpeisekartePflegen(){
		init();
	}
		
	public void init(){
		//testvariable
		Rezepte rezept = new Rezepte("rezept");
		ArrayList<Rezepte> leftItems = new ArrayList<Rezepte>();
		ArrayList<Rezepte> rightItems = new ArrayList<Rezepte>();
			
		//tabelle erzeugen
		Grid<Rezepte> left = new Grid<Rezepte>();
		Grid<Rezepte> right = new Grid<Rezepte>();
			
		left.setCaption("Freigegebene Rezepte");
		right.setCaption("Speisekarte");
			
		left.addColumn(Rezepte :: getName).setCaption("Name");
			
		right.addColumn(Rezepte :: getName).setCaption("Name");
			
		leftItems.add(rezept);
		rightItems.add(rezept);
			
			//testvariable
		left.setItems(leftItems);
		right.setItems(rightItems);
			
		addComponent(left);
		addComponent(right);
			
//		GridDragSource<Rezepte> dragSourceLeft = addDragSourceExtension(left, leftItems);
			
	}
		
//		private GridDragSource addDragSourceExtension(Grid<Rezepte> source, ArrayList<Rezepte> items){
//			
//		}

	}


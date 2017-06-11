package de.ai.rezeptverwaltung.panelview;

//import java.util.ArrayList;

public class Rezepte {


	private String name;
	
	public Rezepte(String name){
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
//	public void testRezepte(){
//		ArrayList<Rezepte> rezepte = new ArrayList<Rezepte>();
//		Rezepte rezept = new Rezepte("rezept");
//		rezepte.add(rezept);
//	}
}

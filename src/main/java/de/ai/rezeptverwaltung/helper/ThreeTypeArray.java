package de.ai.rezeptverwaltung.helper;

import java.util.LinkedList;

import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class ThreeTypeArray {

	LinkedList<TextArea> zuFelder;
	LinkedList<TextField> wFelder;
	LinkedList<TextField> bFelder;
	
	public ThreeTypeArray() {
		
		zuFelder = new LinkedList<TextArea>();
		wFelder = new LinkedList<TextField>();
		bFelder = new LinkedList<TextField>();
		
	}
	
	public void addZuFeld(TextArea ta) {
	
		zuFelder.addLast(ta);
		
	}
	
	public void addBFeld(TextField tf) {
		
		if(bFelder.size() < zuFelder.size()) {
			while(bFelder.size() < wFelder.size() && bFelder.size() < zuFelder.size()-1)
				bFelder.addLast(null);
			bFelder.addLast(tf);
		}
		else {
			while(bFelder.size() >= zuFelder.size())
				zuFelder.addLast(null);
			bFelder.addLast(tf);
		}
		
	}
	
	public void addWFeld(TextField tf) {
		
		if(wFelder.size() < zuFelder.size()) {
			while(wFelder.size() < bFelder.size() && wFelder.size() < zuFelder.size()-1)
				wFelder.addLast(null);
			wFelder.addLast(tf);
		}
		else {
			while(wFelder.size() >= zuFelder.size())
				zuFelder.addLast(null);
			wFelder.addLast(tf);
		}
		
	}

	public LinkedList<TextArea> getZuFelder() {
		return zuFelder;
	}

	public void setZuFelder(LinkedList<TextArea> zuFelder) {
		this.zuFelder = zuFelder;
	}

	public LinkedList<TextField> getwFelder() {
		return wFelder;
	}

	public void setwFelder(LinkedList<TextField> wFelder) {
		this.wFelder = wFelder;
	}

	public LinkedList<TextField> getbFelder() {
		return bFelder;
	}

	public void setbFelder(LinkedList<TextField> bFelder) {
		this.bFelder = bFelder;
	}
	
}
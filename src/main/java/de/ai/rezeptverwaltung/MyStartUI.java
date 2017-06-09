package de.ai.rezeptverwaltung;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import de.ai.rezeptverwaltung.panelview.RezeptSuche;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyStartUI extends UI {
	
	//test
	
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        
        MenuBar menu = new MenuBar();
        
        Panel view = new Panel();
        
        MenuBar.Command mycommand = new MenuBar.Command() {

			@Override
			public void menuSelected(MenuItem selectedItem) {
				if(selectedItem.getText().equals("Rezept Suchen")){
					System.out.println("True");
					view.setContent(new RezeptSuche());
				}
				else if(selectedItem.getText().equals("Rezept hinzufügen"))
					view.setContent(null);
			}
			
        };
        
        menu.addItem("Rezept Suchen", mycommand);
        menu.addItem("Rezept hinzufügen", mycommand);
        
        layout.addComponent(menu);
        layout.addComponent(view);
        
        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyStartUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyStartUI.class, productionMode = false)
    public static class MyStartUIServlet extends VaadinServlet {
    }
}

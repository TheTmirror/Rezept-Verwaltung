package de.ai.rezeptverwaltung;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import de.ai.rezeptverwaltung.panelview.RezeptHinzufuegenOld;
import de.ai.rezeptverwaltung.panelview.RezeptHinzufuegen;
import de.ai.rezeptverwaltung.panelview.RezeptSuche;
import de.ai.rezeptverwaltung.panelview.SpeisekartePflegen;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyStartUI extends UI {
	
	private Connection connection;
	
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        initConnection();
    	
    	final VerticalLayout layout = new VerticalLayout();
        
        MenuBar menu = new MenuBar();
//        menu.setWidth(30.0f, Unit.PERCENTAGE);
        
        Panel view = new Panel();
        
        MenuBar.Command mycommand = new MenuBar.Command() {

			@Override
			public void menuSelected(MenuItem selectedItem) {
				if(selectedItem.getText().equals("Rezept Suchen")){
					view.setContent(new RezeptSuche(connection));
				}
				else if(selectedItem.getText().equals("Rezept hinzufügen"))
					view.setContent(new RezeptHinzufuegen(connection));
				else if(selectedItem.getText().equals("Speisekarte Pflegen"))
					view.setContent(new SpeisekartePflegen(connection));
			}
			
        };
        
        menu.addItem("Rezept Suchen", mycommand);
        menu.addItem("Rezept hinzufügen", mycommand);
        menu.addItem("Speisekarte Pflegen", mycommand);
        
        Button end = new Button("Alles beenden!");
        end.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				closeConnection();
				System.exit(0);
			}
        	
        });
        
        Button rollback = new Button("Rollback");
        rollback.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					connection.rollback();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
        	
        });
        
//        layout.addComponent(end);
//        layout.addComponent(rollback);
        layout.addComponent(menu);
        layout.addComponent(view);
        
        setContent(layout);
    }
    
    private void initConnection() {
    	
    	Properties prop = new Properties();
    	InputStream input = null;
    	
    	String url = null;
    	String user = null;
    	String pass = null;
    	
    	try {
			input = new FileInputStream("src/main/resources/data.properties");
			
			prop.load(input);
			
			url = prop.getProperty("url");
			user = prop.getProperty("user");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	
    	try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    	
//    	System.out.println("Bitte Passwort eingeben: ");
//    	Scanner sc = new Scanner(System.in);
//    	pass = sc.nextLine();
    	pass = prop.getProperty("pass");
    	
    	try {
			connection = DriverManager.getConnection(url, user, pass);
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    }
    
    private void closeConnection() {
    	
    	if(connection != null)
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	
    }

    @WebServlet(urlPatterns = "/*", name = "MyStartUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyStartUI.class, productionMode = false)
    public static class MyStartUIServlet extends VaadinServlet {
    }
}

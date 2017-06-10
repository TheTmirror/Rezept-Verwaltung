package rezeptverwaltung;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Assert;
import org.junit.Test;

public class RSTest {

	@Test
	public void DataBaseConnection() {
		
		Connection c = null;
		Statement s = null;
		
		try {
			
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			c = DriverManager.getConnection("jdbc:oracle:thin:@studidb.gm.fh-koeln.de:1521:VLESUNG", "ORA_AI_1_10", "");
			
			s = c.createStatement();
			
			s.executeQuery("SELECT * FROM rezept");
			
			ResultSet r = s.getResultSet();
			
			System.out.println(r.getRow());
			
			Assert.assertTrue(r.next());
			
//			System.out.println(r);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
			try {
				c.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

}

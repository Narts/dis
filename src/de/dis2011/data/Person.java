package de.dis2011.data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.dis2011.data.DB2ConnectionManager;

public class Person {
    private int personID = -1;
	private String vorname;
	private String nachname;
	private String adresse;

	/**
	 * @return the personID
	 */
	public int getPersonID() {
		return personID;
	}


	/**
	 * @param personID the personID to set
	 */
	public void setPersonID(int personID) {
		this.personID = personID;
	}


	/**
	 * @return the vorname
	 */
	public String getVorname() {
		return vorname;
	}


	/**
	 * @param vorname the vorname to set
	 */
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}


	/**
	 * @return the nachname
	 */
	public String getNachname() {
		return nachname;
	}


	/**
	 * @param nachname the nachname to set
	 */
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}


	/**
	 * @return the adresse
	 */
	public String getAdresse() {
		return adresse;
	}


	/**
	 * @param adresse the adresse to set
	 */
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}


//	/**
//	 * Lädt einen Immobilie aus der Datenbank
//	 * @param id immobID des zu ladenden Immobilie
//	 * @return Immobilie-Instanz
//	 */
//	public static Person load(int personID) {
//		try {
//			// Hole Verbindung
//			Connection con = DB2ConnectionManager.getInstance().getConnection();
//
//			// Erzeuge Anfrage
//			String selectSQL = "SELECT * FROM person WHERE personID = ?";
//			PreparedStatement pstmt = con.prepareStatement(selectSQL);
//			pstmt.setInt(1, personID);
//
//			// Führe Anfrage aus
//			ResultSet rs = pstmt.executeQuery();
//			if (rs.next()) {
//				Person psn = new Person();
//              	psn.setPersonID(personID);
//              	psn.setVorname(rs.getString("vorname"));
//              	psn.setNachname(rs.getString("nachname"));
//              	psn.setAdresse(rs.getString("adresse"));
//				
//				rs.close();
//				pstmt.close();
//				return psn;
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	
	/**
	 * Speichert den Immobilie in der Datenbank. Ist noch keine ImmobID vergeben
	 * worden, wird die generierte ImmobID von DB2 geholt und dem Model übergeben.
	 */
	public void save() {
		// Hole Verbindung
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		try {
			// Fuege neues Element hinzu, wenn das Objekt noch keine ImmobID hat.
			if (getPersonID() == -1) {
				// Achtung, hier wird noch ein Parameter mitgegeben,
				// damit spaeter generierte IDs zurueckgeliefert werden!
				String insertSQL = 
					"INSERT INTO Person (vorname, nachname, adresse) VALUES ( ?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				// Setze Anfrageparameter und fuehre Anfrage aus

				// Setze Anfrageparameter und fuehre Anfrage aus
				pstmt.setString(1, getVorname());
				pstmt.setString(2, getNachname());
				pstmt.setString(3, getAdresse());
				pstmt.executeUpdate();
System.out.println("update fertig");

				// Hole die Id des engefuegten Datensatzes
				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					setPersonID(rs.getInt(1));
				}
				
				rs.close();
			    pstmt.close();
			}else {
				// Falls schon eine ID vorhanden ist, mache ein Update...
				String updateSQL = 
					"UPDATE person SET vorname = ?, nachname = ?, adresse = ? WHERE personid = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter
				pstmt.setString(1, getVorname());
				pstmt.setString(2, getNachname());
				pstmt.setString(3, getAdresse());
				pstmt.setInt(4, getPersonID());

				pstmt.executeUpdate();

				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Lädt einen Makler aus der Datenbank
	 * @param id ID des zu ladenden Maklers
	 * @return Makler-Instanz
	 */
	public static void delete(int personID) {
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "DELETE FROM person WHERE personID = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, personID);

			// Führe Anfrage aus
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

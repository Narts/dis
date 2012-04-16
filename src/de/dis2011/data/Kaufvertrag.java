package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Kaufvertrag {
	private int kaufnr = -1;
	private int anzahlRaten;
	private double ratenZins;

	/**
	 * @return the kaufnr
	 */
	public int getKaufnr() {
		return kaufnr;
	}


	/**
	 * @param kaufnr the kaufnr to set
	 */
	public void setKaufnr(int kaufnr) {
		this.kaufnr = kaufnr;
	}


	/**
	 * @return the anzahlRaten
	 */
	public int getAnzahlRaten() {
		return anzahlRaten;
	}


	/**
	 * @param anzahlRaten the anzahlRaten to set
	 */
	public void setAnzahlRaten(int anzahlRaten) {
		this.anzahlRaten = anzahlRaten;
	}


	/**
	 * @return the ratenZins
	 */
	public double getRatenZins() {
		return ratenZins;
	}


	/**
	 * @param ratenZins the ratenZins to set
	 */
	public void setRatenZins(double ratenZins) {
		this.ratenZins = ratenZins;
	}


//	/**
//	 * Lädt einen Immobilie aus der Datenbank
//	 * @param id immobID des zu ladenden Immobilie
//	 * @return Immobilie-Instanz
//	 */
//	public static Kaufvertrag load(int kaufnr) {
//		try {
//			// Hole Verbindung
//			Connection con = DB2ConnectionManager.getInstance().getConnection();
//
//			// Erzeuge Anfrage
//			String selectSQL = "SELECT * FROM kaufvertrag WHERE kaufnr = ?";
//			PreparedStatement pstmt = con.prepareStatement(selectSQL);
//			pstmt.setInt(1, kaufnr);
//
//			// Führe Anfrage aus
//			ResultSet rs = pstmt.executeQuery();
//			if (rs.next()) {
//				Kaufvertrag kvtg = new Kaufvertrag();
//				kvtg.setKaufnr(kaufnr);
//				kvtg.setAnzahlRaten(rs.getInt("anzahlraten"));
//				kvtg.setRatenZins(rs.getInt("ratenzins"));
//
//				rs.close();
//				pstmt.close();
//				return kvtg;
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
			String selectSQL = "SELECT kaufnr FROM kaufvertrag";
            PreparedStatement pstmtSel = con.prepareStatement(selectSQL);
			ResultSet rs = pstmtSel.executeQuery();
			
			ArrayList<Integer> tmpKaufNr = new ArrayList<Integer>();
            while (rs.next()) {
            	tmpKaufNr.add(Integer.valueOf(rs.getString("kaufnr")));
            }
            
			if (tmpKaufNr.isEmpty() || !tmpKaufNr.contains(getKaufnr()))
			{
				// Achtung, hier wird noch ein Parameter mitgegeben,
				// damit spaeter generierte IDs zurueckgeliefert werden!
				String insertSQL = 
					"INSERT INTO kaufvertrag (kaufnr, anzahlraten, ratenzins) VALUES ( ?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL);

				// Setze Anfrageparameter und fuehre Anfrage aus
				pstmt.setInt(1, getKaufnr());
				pstmt.setInt(2, getAnzahlRaten());
				pstmt.setDouble(3, getRatenZins());
				pstmt.executeUpdate();
				pstmt.close();
			} else {
				// Falls schon eine ID vorhanden ist, mache ein Update...
				String updateSQL = 
					"UPDATE kaufvertrag SET anzahlraten = ?, ratenzins = ? WHERE kaufnr = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter
				pstmt.setInt(1, getAnzahlRaten());
				pstmt.setDouble(2, getRatenZins());
				pstmt.setInt(3, getKaufnr());
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
	public static void delete(int kaufnr) {
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "DELETE FROM kaufvertrag WHERE kaufnr = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, kaufnr);

			// Führe Anfrage aus
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Zeigt die Vertragverwaltung
	 * @return 
	 */
    public static ResultSet showVertragUebersicht() {
		
    	ResultSet rs = null;
	    Connection con = DB2ConnectionManager.getInstance().getConnection();
		String selectSQL = "SELECT * FROM kaufvertrag ";
		try {
	        PreparedStatement pstmtSel = con.prepareStatement(selectSQL);
			rs = pstmtSel.executeQuery();
	        System.out.println("Kaufvertrag:"); 
	        while (rs.next()) {
	        	System.out.println("Kaufvertrag Nummer:"+rs.getInt(1)+", "+"Anzahl Raten:"+rs.getInt(2)+", "
	        			+"Ratenzins:"+ rs.getDouble(3));
	        }	
		} catch (SQLException  e) {
			e.printStackTrace();
		}
	return rs; 
	}
}

package de.dis2011.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Mietvertrag {
	private int mietnr = -1;
	private String mietBeginn;
	private int dauer;
	private double nebenKosten;

	/**
	 * @return the mietnr
	 */
	public int getMietnr() {
		return mietnr;
	}


	/**
	 * @param mietnr the mietnr to set
	 */
	public void setMietnr(int mietnr) {
		this.mietnr = mietnr;
	}


	/**
	 * @return the mietBeginn
	 */
	public String getMietBeginn() {
		return mietBeginn;
	}


	/**
	 * @param mietBeginn the mietBeginn to set
	 */
	public void setMietBeginn(String mietBeginn) {
		this.mietBeginn = mietBeginn;
	}


	/**
	 * @return the dauer
	 */
	public int getDauer() {
		return dauer;
	}


	/**
	 * @param dauer the dauer to set
	 */
	public void setDauer(int dauer) {
		this.dauer = dauer;
	}


	/**
	 * @return the nebenKosten
	 */
	public double getNebenKosten() {
		return nebenKosten;
	}


	/**
	 * @param nebenKosten the nebenKosten to set
	 */
	public void setNebenKosten(double nebenKosten) {
		this.nebenKosten = nebenKosten;
	}


	/**
	 * Lädt einen Immobilie aus der Datenbank
	 * @param id immobID des zu ladenden Immobilie
	 * @return Immobilie-Instanz
	 */
	public static Mietvertrag load(int mietnr) {
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM mietvertrag WHERE mietnr = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, mietnr);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Mietvertrag mvtg = new Mietvertrag();
				mvtg.setMietnr(mietnr);
				mvtg.setMietBeginn(rs.getString("mietbeginn"));
				mvtg.setDauer(rs.getInt("dauer"));
				mvtg.setNebenKosten(rs.getDouble("nebenkosten"));

				rs.close();
				pstmt.close();
				return mvtg;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Speichert den Immobilie in der Datenbank. Ist noch keine ImmobID vergeben
	 * worden, wird die generierte ImmobID von DB2 geholt und dem Model übergeben.
	 */
	public void save() {
		// Hole Verbindung
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		try {
			// Fuege neues Element hinzu, wenn das Objekt noch keine ImmobID hat.
			String selectSQL = "SELECT mietnr FROM mietvertrag";
            PreparedStatement pstmtSel = con.prepareStatement(selectSQL);
			ResultSet rs = pstmtSel.executeQuery();
			
			ArrayList<Integer> tmpMietNr = new ArrayList<Integer>();
            while (rs.next()) {
            	tmpMietNr.add(Integer.valueOf(rs.getString("mietnr")));
            }
            
			if (tmpMietNr.isEmpty() || !tmpMietNr.contains(getMietnr()))
			{
				// Achtung, hier wird noch ein Parameter mitgegeben,
				// damit spaeter generierte IDs zurueckgeliefert werden!
				String insertSQL = 
					"INSERT INTO mietvertrag (mietnr, mietbeginn, dauer, nebenkosten) VALUES ( ?, ?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL);

				// Setze Anfrageparameter und fuehre Anfrage aus
				pstmt.setInt(1, getMietnr());
				pstmt.setString(2, getMietBeginn());
				pstmt.setInt(3, getDauer());
				pstmt.setDouble(4, getNebenKosten());

				pstmt.executeUpdate();
System.out.println("update fertig");
				pstmt.close();
			} else {
				// Falls schon eine ID vorhanden ist, mache ein Update...
				String updateSQL = 
					"UPDATE mietvertrag SET mietbeginn = ?, dauer = ?,nebenkosten= ? WHERE mietnr = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter
				pstmt.setString(1, getMietBeginn());
				pstmt.setInt(2, getDauer());
				pstmt.setDouble(3, getNebenKosten());
				pstmt.setInt(4, getMietnr());

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
	public static void delete(int mietnr) {
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "DELETE FROM mietvertrag WHERE mietnr = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, mietnr);

			// Führe Anfrage aus
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

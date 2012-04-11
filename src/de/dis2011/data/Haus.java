package de.dis2011.data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import de.dis2011.data.DB2ConnectionManager;

public class Haus {

	private int hausID = -1;
	private int stockwerk;
	private double kaufPreis;
	private boolean garten;

	/**
	 * @return the hausID
	 */
	public int getHausID() {
		return hausID;
	}


	/**
	 * @param hausID the hausID to set
	 */
	public void setHausID(int hausID) {
		this.hausID = hausID;
	}


	/**
	 * @return the stockwerk
	 */
	public int getStockwerk() {
		return stockwerk;
	}


	/**
	 * @param stockwerk the stockwerk to set
	 */
	public void setStockwerk(int stockwerk) {
		this.stockwerk = stockwerk;
	}


	/**
	 * @return the kaufPreis
	 */
	public double getKaufPreis() {
		return kaufPreis;
	}


	/**
	 * @param kaufPreis the kaufPreis to set
	 */
	public void setKaufPreis(double kaufPreis) {
		this.kaufPreis = kaufPreis;
	}


	/**
	 * @return the garten
	 */
	public boolean isGarten() {
		return garten;
	}


	/**
	 * @param garten the garten to set
	 */
	public void setGarten(boolean garten) {
		this.garten = garten;
	}


	/**
	 * Lädt einen Immobilie aus der Datenbank
	 * @param id immobID des zu ladenden Immobilie
	 * @return Immobilie-Instanz
	 */
	public static Haus load(int hausID) {
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM haus WHERE hausID = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, hausID);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Haus hs = new Haus();
              	hs.setHausID(hausID);
              	hs.setStockwerk(Integer.valueOf(rs.getString("stockwerk")));
              	hs.setKaufPreis(Double.valueOf(rs.getString("kaufpreis")));
              	hs.setGarten(Boolean.valueOf(rs.getString("garten")));
				
				rs.close();
				pstmt.close();
				return hs;
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
			String selectSQL = "SELECT hausid FROM haus ";
            PreparedStatement pstmtSel = con.prepareStatement(selectSQL);
			ResultSet rs = pstmtSel.executeQuery();
			
			ArrayList<Integer> tmpHausID = new ArrayList<Integer>();
            while (rs.next()) {
			    tmpHausID.add(Integer.valueOf(rs.getString("hausID")));
            }
            
			if (tmpHausID.isEmpty() || !tmpHausID.contains(getHausID())) // 好像还有问题，待查，tmpHausid会被覆盖？？
			{
System.out.println("gethausId"+getHausID());
for (int i = 0; i < tmpHausID.size(); i++) {
	System.out.println("tmpHausID"+tmpHausID.get(i));

}

				// Achtung, hier wird noch ein Parameter mitgegeben,
				// damit spaeter generierte IDs zurueckgeliefert werden!
				String insertSQL = 
					"INSERT INTO Haus (HausID, stockwerk, kaufpreis, garten) VALUES ( ?, ?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL);

				// Setze Anfrageparameter und fuehre Anfrage aus
				pstmt.setInt(1, getHausID());
				pstmt.setInt(2, getStockwerk());
				pstmt.setDouble(3, getKaufPreis());
				pstmt.setBoolean(4, isGarten());
				pstmt.executeUpdate();
System.out.println("update fertig");
				// Hole die Id des engefuegten Datensatzes
//					ResultSet rs = pstmt.getGeneratedKeys();
//					if (rs.next()) {
//						setImmobId(rs.getInt(1));
//					}
//
//					rs.close();
				pstmt.close();
			} else {
				// Falls schon eine ID vorhanden ist, mache ein Update...
				String updateSQL = 
					"UPDATE haus SET stockwerk = ?, kaufpreis = ?, garten = ? WHERE Hausid = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter
				pstmt.setInt(1, getStockwerk());
				pstmt.setDouble(2, getKaufPreis());
				pstmt.setBoolean(3, isGarten());
				pstmt.setInt(4, getHausID());

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
	public static void delete(int hausID) {
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "DELETE FROM haus WHERE hausID = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, hausID);

			// Führe Anfrage aus
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

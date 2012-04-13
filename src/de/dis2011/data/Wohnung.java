package de.dis2011.data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import de.dis2011.data.DB2ConnectionManager;

public class Wohnung {
	private int wohnungID = -1;
	private int stockwerk;
	private double mietPreis;
	private int zimmer;
	private boolean balkon;
	private boolean ebk;
	

	
	/**
	 * @return the wohnungID
	 */
	public int getWohnungID() {
		return wohnungID;
	}


	/**
	 * @param wohnungID the wohnungID to set
	 */
	public void setWohnungID(int wohnungID) {
		this.wohnungID = wohnungID;
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
	 * @return the mietPreis
	 */
	public double getMietPreis() {
		return mietPreis;
	}


	/**
	 * @param mietPreis the mietPreis to set
	 */
	public void setMietPreis(double mietPreis) {
		this.mietPreis = mietPreis;
	}


	/**
	 * @return the zimmer
	 */
	public int getZimmer() {
		return zimmer;
	}


	/**
	 * @param zimmer the zimmer to set
	 */
	public void setZimmer(int zimmer) {
		this.zimmer = zimmer;
	}


	/**
	 * @return the balkon
	 */
	public boolean isBalkon() {
		return balkon;
	}


	/**
	 * @param balkon the balkon to set
	 */
	public void setBalkon(boolean balkon) {
		this.balkon = balkon;
	}


	/**
	 * @return the ebk
	 */
	public boolean isEbk() {
		return ebk;
	}


	/**
	 * @param ebk the ebk to set
	 */
	public void setEbk(boolean ebk) {
		this.ebk = ebk;
	}


//	/**
//	 * Lädt einen Wohnung aus der Datenbank
//	 * @param id wohnungID des zu ladenden Immobilie
//	 * @return wohnung-Instanz
//	 */
//	public static Wohnung load(int wohnungid) {
//		try {
//			// Hole Verbindung
//			Connection con = DB2ConnectionManager.getInstance().getConnection();
//	
//			// Erzeuge Anfrage
//			String selectSQL = "SELECT * FROM wohnung WHERE wohnungid = ?";
//			PreparedStatement pstmt = con.prepareStatement(selectSQL);
//			pstmt.setInt(1, wohnungid);
//	
//			// Führe Anfrage aus
//			ResultSet rs = pstmt.executeQuery();
//			if (rs.next()) {
//				Wohnung whn = new Wohnung();
//				whn.setWohnungID(wohnungid);
//				whn.setStockwerk(Integer.valueOf(rs.getString("stockwerk")));
//				whn.setMietPreis(Double.valueOf(rs.getString("mietpreis")));
//				whn.setZimmer(Integer.valueOf(rs.getString("zimmer")));
//				whn.setBalkon(Boolean.valueOf(rs.getString("balkon")));
//				whn.setEbk(Boolean.valueOf(rs.getString("ebk")));
//
//				rs.close();
//				pstmt.close();
//				return whn;
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
			String selectSQL = "SELECT wohnungid FROM wohnung ";
	        PreparedStatement pstmtSel = con.prepareStatement(selectSQL);
			ResultSet rs = pstmtSel.executeQuery();
			
			ArrayList<Integer> wohnungID = new ArrayList<Integer>();
	        while (rs.next()) {
	        	wohnungID.add(Integer.valueOf(rs.getString("wohnungid")));
	        }
	        
			if (wohnungID.isEmpty() || !wohnungID.contains(getWohnungID())) 
				{
//	System.out.println("gethausId"+getHausID());
//	for (int i = 0; i < tmpHausID.size(); i++) {
//		System.out.println("tmpHausID"+tmpHausID.get(i));
//	
//	}
	
					// Achtung, hier wird noch ein Parameter mitgegeben,
				// damit spaeter generierte IDs zurueckgeliefert werden!
				String insertSQL = 
					"INSERT INTO wohnung (wohnungID, stockwerk, mietpreis, zimmer, balkon, ebk) VALUES (?, ?, ?, ?, ?, ?)";
	
				PreparedStatement pstmt = con.prepareStatement(insertSQL);
	
				// Setze Anfrageparameter und fuehre Anfrage aus
					pstmt.setInt(1, getWohnungID());
					pstmt.setInt(2, getStockwerk());
					pstmt.setDouble(3, getMietPreis());
					pstmt.setInt(4, getZimmer());
					pstmt.setBoolean(5, isBalkon());
					pstmt.setBoolean(6, isEbk());
					pstmt.executeUpdate();
	System.out.println("update fertig");
	
				// Hole die Id des engefuegten Datensatzes
	//						ResultSet rs = pstmt.getGeneratedKeys();
	//						if (rs.next()) {
	//							setImmobId(rs.getInt(1));
	//						}
	//
	//						rs.close();
				pstmt.close();
			} else {
				// Falls schon eine ID vorhanden ist, mache ein Update...
				String updateSQL = 
					"UPDATE wohnung SET stockwerk = ?, mietpreis = ?, zimmer = ?, balkon = ?, ebk = ? WHERE wohnungid = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);
	
				// Setze Anfrage Parameter
				pstmt.setInt(1, getStockwerk());
				pstmt.setDouble(2, getMietPreis());
				pstmt.setInt(3, getZimmer());
				pstmt.setBoolean(4,isBalkon());
				pstmt.setBoolean(5, isEbk());
				pstmt.setInt(6, getWohnungID());

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
	public static void delete(int wohnungid) {
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();
	
			// Erzeuge Anfrage
			String selectSQL = "DELETE FROM wohnung WHERE wohnungID = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, wohnungid);
	
			// Führe Anfrage aus
			pstmt.execute();
	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

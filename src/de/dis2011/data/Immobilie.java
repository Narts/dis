package de.dis2011.data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.dis2011.data.DB2ConnectionManager;

public class Immobilie {



	/**
	 * Immobilie-Bean
	 */

		private int immobId = -1;
		private String ort;
		private int plz;
		private String strasse;
		private int hausNr;
		private double flaeche;
		private int maklerId;
		
		
		/**
		 * @return the immobId
		 */
		public int getImmobId() {
			return immobId;
		}


		/**
		 * @param immobId the immobId to set
		 */
		public void setImmobId(int immobId) {
			this.immobId = immobId;
		}


		/**
		 * @return the ort
		 */
		public String getOrt() {
			return ort;
		}


		/**
		 * @param ort the ort to set
		 */
		public void setOrt(String ort) {
			this.ort = ort;
		}


		/**
		 * @return the plz
		 */
		public int getPlz() {
			return plz;
		}


		/**
		 * @param plz the plz to set
		 */
		public void setPlz(int plz) {
			this.plz = plz;
		}


		/**
		 * @return the strasse
		 */
		public String getStrasse() {
			return strasse;
		}


		/**
		 * @param strasse the strasse to set
		 */
		public void setStrasse(String strasse) {
			this.strasse = strasse;
		}


		/**
		 * @return the hausNr
		 */
		public int getHausNr() {
			return hausNr;
		}


		/**
		 * @param hausNr the hausNr to set
		 */
		public void setHausNr(int hausNr) {
			this.hausNr = hausNr;
		}


		/**
		 * @return the flaeche
		 */
		public double getFlaeche() {
			return flaeche;
		}


		/**
		 * @param flaeche the flaeche to set
		 */
		public void setFlaeche(double flaeche) {
			this.flaeche = flaeche;
		}


		/**
		 * @return the maklerId
		 */
		public int getMaklerId() {
			return maklerId;
		}


		/**
		 * @param maklerId the maklerId to set
		 */
		public void setMaklerId(int maklerId) {
			this.maklerId = maklerId;
		}


		/**
		 * Lädt einen Immobilie aus der Datenbank
		 * @param id immobID des zu ladenden Immobilie
		 * @return Immobilie-Instanz
		 */
		public static Immobilie load(int immobId) {
			try {
				// Hole Verbindung
				Connection con = DB2ConnectionManager.getInstance().getConnection();

				// Erzeuge Anfrage
				String selectSQL = "SELECT * FROM Immobilie WHERE ImmobID = ?";
				PreparedStatement pstmt = con.prepareStatement(selectSQL);
				pstmt.setInt(1, immobId);

				// Führe Anfrage aus
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					Immobilie imb = new Immobilie();
                    imb.setImmobId(immobId);
                    imb.setOrt(rs.getString("ort"));
                    imb.setPlz(Integer.valueOf(rs.getString("plz")));
                    imb.setStrasse(rs.getString("strasse"));
                    imb.setHausNr(Integer.valueOf(rs.getString("hausnr")));
                    imb.setFlaeche(Double.valueOf(rs.getString("flaeche")));
                    imb.setMaklerId(Integer.valueOf(rs.getString(rs.getString("maklerid"))));
                  				
					rs.close();
					pstmt.close();
					return imb;
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
				if (getImmobId() == -1) {
					// Achtung, hier wird noch ein Parameter mitgegeben,
					// damit spaeter generierte IDs zurueckgeliefert werden!
					String insertSQL = 
						"INSERT INTO immobilie (ort, plz, strasse, hausnr, flaeche, maklerId) VALUES ( ?, ?, ?, ?, ?, ?)";

					PreparedStatement pstmt = con.prepareStatement(insertSQL,
							Statement.RETURN_GENERATED_KEYS);

					// Setze Anfrageparameter und fuehre Anfrage aus
					pstmt.setString(1, getOrt());
					pstmt.setInt(2, getPlz());
					pstmt.setString(3, getStrasse());
					pstmt.setInt(4, getHausNr());
					pstmt.setDouble(5, getFlaeche());
					pstmt.setInt(6, getMaklerId());
					pstmt.executeUpdate();

					// Hole die Id des engefuegten Datensatzes
					ResultSet rs = pstmt.getGeneratedKeys();
					if (rs.next()) {
						setImmobId(rs.getInt(1));
					}

					rs.close();
					pstmt.close();
				} else {
					// Falls schon eine ID vorhanden ist, mache ein Update...
					String updateSQL = 
						"UPDATE immobilie SET ort = ?, plz = ?, strasse = ?, hausnr = ?,flaeche = ?, maklerid = ? WHERE immobid = ?";
					PreparedStatement pstmt = con.prepareStatement(updateSQL);

					// Setze Anfrage Parameter
					pstmt.setString(1, getOrt());
					pstmt.setInt(2, getPlz());
					pstmt.setString(3, getStrasse());
					pstmt.setInt(4, getHausNr());
					pstmt.setDouble(5, getFlaeche());
					pstmt.setInt(6, getMaklerId());
					pstmt.setInt(7, getImmobId());
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
		public static void delete(int immobId) {
			try {
				// Hole Verbindung
				Connection con = DB2ConnectionManager.getInstance().getConnection();

				// Erzeuge Anfrage
				String selectSQL = "DELETE FROM immobilie WHERE immobId = ?";
				PreparedStatement pstmt = con.prepareStatement(selectSQL);
				pstmt.setInt(1, immobId);

				// Führe Anfrage aus
				pstmt.execute();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	

}

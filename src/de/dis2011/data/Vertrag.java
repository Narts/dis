package de.dis2011.data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import de.dis2011.data.DB2ConnectionManager;

public class Vertrag {
    
	private int vertragnr = -1;
    private int personId ;
    private int immobId ;
	private String datum;
	private String ort;

	/**
	 * @return the vertragnr
	 */
	public int getVertragnr() {
		return vertragnr;
	}


	/**
	 * @param vertragnr the vertragnr to set
	 */
	public void setVertragnr(int vertragnr) {
		this.vertragnr = vertragnr;
	}


	/**
	 * @return the personId
	 */
	public int getPersonId() {
		return personId;
	}


	/**
	 * @param personId the personId to set
	 */
	public void setPersonId(int personId) {
		this.personId = personId;
	}


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
	 * @return the datum
	 */
	public String getDatum() {
		return datum;
	}


	/**
	 * @param datum the datum to set
	 */
	public void setDatum(String datum) {
		this.datum = datum;
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
	 * Lädt einen Immobilie aus der Datenbank
	 * @param id immobID des zu ladenden Immobilie
	 * @return Immobilie-Instanz
	 */
	public static Vertrag load(int vertragnr) {
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM vertrag WHERE vertragnr = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, vertragnr);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Vertrag vtg = new Vertrag();
				vtg.setVertragnr(Integer.valueOf(rs.getString("vertragnr")));
				vtg.setPersonId(Integer.valueOf(rs.getString("perid")));
				vtg.setImmobId(Integer.valueOf(rs.getString("immobid")));
                vtg.setDatum(rs.getString("datum"));
                vtg.setOrt(rs.getString("ort"));

				rs.close();
				pstmt.close();
				return vtg;
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
			if (getVertragnr() == -1) {
				// Achtung, hier wird noch ein Parameter mitgegeben,
				// damit spaeter generierte IDs zurueckgeliefert werden!
				String insertSQL = 
					"INSERT INTO vertrag (perid, immobid, datum, ort) VALUES ( ?, ?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				// Setze Anfrageparameter und fuehre Anfrage aus
				pstmt.setInt(1, getPersonId());
				pstmt.setInt(2, getImmobId());
				pstmt.setString(3, getDatum());
				pstmt.setString(4, getOrt());

				pstmt.executeUpdate();

				// Hole die Id des engefuegten Datensatzes
				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					setVertragnr(rs.getInt(1));
				}

				rs.close();
				pstmt.close();
			} else {
				// Falls schon eine ID vorhanden ist, mache ein Update...
				String updateSQL = 
					"UPDATE vertrag SET perid = ?, immobid = ?, datum = ?, ort = ? WHERE vertragnr = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter
				pstmt.setInt(1, getPersonId());
				pstmt.setInt(2, getImmobId());
				pstmt.setString(3, getDatum());
				pstmt.setString(4, getOrt());
				pstmt.setInt(5, getVertragnr());
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
	public static void delete(int vertragnr) {
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "DELETE FROM vertrag WHERE vertragnr = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, vertragnr);

			// Führe Anfrage aus
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

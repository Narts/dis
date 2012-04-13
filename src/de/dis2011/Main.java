package de.dis2011;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import de.dis2011.data.DB2ConnectionManager;
import de.dis2011.data.Haus;
import de.dis2011.data.Immobilie;
import de.dis2011.data.Kaufvertrag;
import de.dis2011.data.Makler;
import de.dis2011.data.Mietvertrag;
import de.dis2011.data.Person;
import de.dis2011.data.Vertrag;
import de.dis2011.data.Wohnung;

/**
 * Hauptklasse
 */
public class Main {
	/**
	 * Startet die Anwendung
	 */
	public static void main(String[] args) {
		showMainMenu();
	}
	
	/**
	 * Zeigt das Hauptmenü
	 */
	public static void showMainMenu() {
		//Menüoptionen
		final int MENU_MAKLER = 0;
		final int MENU_IMMOBILIE = 1;
		final int MENU_VERTRAG = 2;
		final int QUIT = 3;
		
		//Erzeuge Menü
		Menu mainMenu = new Menu("Hauptmenü");
		mainMenu.addEntry("Makler-Verwaltung", MENU_MAKLER);
		mainMenu.addEntry("Immobilie-Verwaltung", MENU_IMMOBILIE);
		mainMenu.addEntry("Vertrag-Verwaltung", MENU_VERTRAG);
		mainMenu.addEntry("Beenden", QUIT);
		
		//Verarbeite Eingabe
		while(true) {
			int response = mainMenu.show();
			
			switch(response) {
				case MENU_MAKLER:
					loginVerify(MENU_MAKLER);
					break;
				case MENU_IMMOBILIE:
					loginVerify(MENU_IMMOBILIE);
					break;
				case MENU_VERTRAG:
					loginVerify(MENU_VERTRAG);
					break;
				case QUIT:
					return;
			}
		}
	}
	
	
	/**
	 * Zeigt das loginVerify menue
	 * Flag : MENU_MAKLER = 0;
	 * Flag : MENU_IMMOBILIE = 1;
	 * Flag : MENU_VERTRAG = 2;
	 */
	public static void loginVerify(int flag) {
		//Menüoptionen
		final int id = 0;
		final int QUIT = 1;
		
		//Erzeuge Menü
		Menu mainMenu = new Menu("ID eingeben");
		mainMenu.addEntry("ID", id);
		mainMenu.addEntry("Zurueck", QUIT);
		
		//Verarbeite Eingabe
		while(true) {
			int response = mainMenu.show();
			switch(response) {
				case id:
					int loginId = Integer.valueOf(FormUtil.readString("ID"));//schom mal geandert 10.mai
					Makler loginMakler = Makler.load(loginId);
					int pin = Integer.valueOf(loginMakler.getPassword());
					pinVerify(pin,flag);
					break;
				case QUIT:
					return;
			}
		}
	}
	
	
	/**
	 * Zeigt das pinVerify menue
	 * Flag : MENU_MAKLER = 0;
	 * Flag : MENU_IMMOBILIE = 1;
	 * Flag : MENU_VERTRAG = 2;
	 */
	public static void pinVerify(int loginPin,int flag) {
		//Menüoptionen
		
		//Erzeuge Menü
		new Menu("Pin Nummer eingeben");
		int loginPassword = 
			Integer.valueOf(FormUtil.readString("Password"));
		if (loginPin == loginPassword && flag ==  0) {        
		    showMaklerMenu();
	    }else if(loginPin == loginPassword && flag ==  1){

	    	final int HAUS = 0;
	    	final int WOHNUNG = 1;
			final int QUIT = 2;
			
			//Erzeuge Menü
			Menu mainMenu = new Menu("Typ eingeben");
			mainMenu.addEntry("Haus", HAUS);
			mainMenu.addEntry("Wohnung", WOHNUNG);
			mainMenu.addEntry("Zurueck", QUIT);
			
			//Verarbeite Eingabe
			while(true) {
				int response = mainMenu.show();
				switch(response) {
					case HAUS:
				    	showImmobilieMenu(HAUS);
						break;
					case WOHNUNG:
				    	showImmobilieMenu(WOHNUNG);
						break;
					case QUIT:
						return;
				}
			}
	    }else if(loginPin == loginPassword && flag ==  2){
	    	final int PERSON = 0;
	    	final int VERTRAG_ABSCHLUSS = 1;
	    	final int VERTRAG_UEBERSICHT = 2;
			final int QUIT = 3;
	    	
	    	Menu mainMenu = new Menu("Typ auswaehlen");
	    	mainMenu.addEntry("Eintragen von Person", PERSON);
	    	mainMenu.addEntry("Bearbeitung von Vertraegen", VERTRAG_ABSCHLUSS);
	    	mainMenu.addEntry("Uebersicht ueber Vertraege", VERTRAG_UEBERSICHT);
	    	mainMenu.addEntry("Zurueck", QUIT);
	    	
			//Verarbeite Eingabe
			while(true) {
				int response = mainMenu.show();
				switch(response) {
					case PERSON:
						showPersonMenu();
						break;
					case VERTRAG_ABSCHLUSS:
				    	showVertragMenu();
						break;
					case VERTRAG_UEBERSICHT:
						showVertragUebersicht();
						break;
					case QUIT:
						return;
				}
			}
	    }else System.out.println("Ungueltiges Password");
	}
	
	/**
	 * Zeigt die Maklerverwaltung
	 */
	public static void showMaklerMenu() {
		//Menüoptionen
		final int NEW_MAKLER = 0;
		final int CHANGE_MAKLER = 1;
		final int DELETE_MAKLER = 2;
		final int BACK = 3;
		
		//Maklerverwaltungsmenü
		Menu maklerMenu = new Menu("Makler-Verwaltung");
		maklerMenu.addEntry("Neuer Makler", NEW_MAKLER);
		maklerMenu.addEntry("Aendern Makler", CHANGE_MAKLER);
		maklerMenu.addEntry("Loeschen Makler", DELETE_MAKLER);
		maklerMenu.addEntry("Zurück zum Hauptmenü", BACK);
		
		//Verarbeite Eingabe
		while(true) {
			int response = maklerMenu.show();
			
			switch(response) {
				case NEW_MAKLER:
					newMakler();
					break;
				case CHANGE_MAKLER:
					changeMakler();
					break;
				case DELETE_MAKLER:
					deleteMakler();
					break;
				case BACK:
					return;
			}
		}
	}
	
	/**
	 * Legt einen neuen Makler an, nachdem der Benutzer
	 * die entprechenden Daten eingegeben hat.
	 */
	public static void newMakler() {
		Makler m = new Makler();
		//m.setId(Integer.valueOf(FormUtil.readString("ID")));//schom mal geandert 10.mai
		m.setName(FormUtil.readString("Name"));
		m.setAddress(FormUtil.readString("Adresse"));
		m.setLogin(FormUtil.readString("Login"));
		m.setPassword(FormUtil.readString("Passwort"));
		m.save();
		
		System.out.println("Makler mit der ID "+m.getId()+" wurde erzeugt.");
	}
	
	/**
	 * Legt einen neuen Makler an, nachdem der Benutzer
	 * die entprechenden Daten eingegeben hat.
	 */
	public static void changeMakler() {
		Makler m = new Makler();
		
		m.setId(Integer.valueOf(FormUtil.readString("ID")));//schom mal geandert 10.mai
		m.setName(FormUtil.readString("Name"));
		m.setAddress(FormUtil.readString("Adresse"));
		m.setLogin(FormUtil.readString("Login"));
		m.setPassword(FormUtil.readString("Passwort"));
		m.save();
		
		System.out.println("Makler mit der ID "+m.getId()+" wurde geaendert.");
	}
	
	/**
	 * Legt einen neuen Makler an, nachdem der Benutzer
	 * die entprechenden Daten eingegeben hat.
	 */
	public static void deleteMakler() {
		int tmpId = Integer.valueOf(FormUtil.readString("ID"));//schom mal geandert 10.mai
		Makler.delete(tmpId);
		System.out.println("Makler mit der ID "+ tmpId +" wurde geloescht.");
	}
	
	/**
	 * Zeigt die Maklerverwaltung
	 */
	public static void showImmobilieMenu(int typFlag) {
		//Menüoptionen
		final int NEW_IMMOBILIE = 0;
		final int CHANGE_IMMOBILIE = 1;
		final int DELETE_IMMOBILIE = 2;
		final int BACK = 3;
		
		//Maklerverwaltungsmenü
		Menu maklerMenu = new Menu("Immobilie-Verwaltung");
		maklerMenu.addEntry("Neuer Immobilie", NEW_IMMOBILIE);
		maklerMenu.addEntry("Aendern Immobilie", CHANGE_IMMOBILIE);
		maklerMenu.addEntry("Loeschen Immobilie", DELETE_IMMOBILIE);
		maklerMenu.addEntry("Zurück zum Hauptmenü", BACK);
		
		//Verarbeite Eingabe
		while(true) {
			int response = maklerMenu.show();
			
			switch(response) {
				case NEW_IMMOBILIE:
					newImmobilie(typFlag);
					break;
				case CHANGE_IMMOBILIE:
					changeImmobilie(typFlag);
					break;
				case DELETE_IMMOBILIE:
					deleteImmobilie(typFlag);
					break;
				case BACK:
					return;
			}
		}
	}
	
	/**
	 * Legt einen neuen Immobilie an, nachdem der Benutzer
	 * die entprechenden Daten eingegeben hat.
	 * HAUS = 0;
	 * WOHNUNG = 1;
	 */
	public static void newImmobilie(int typFlag) {
		
		Immobilie imb = new Immobilie();
		//m.setId(Integer.valueOf(FormUtil.readString("ID")));//schom mal geandert 10.mai
		imb.setOrt(FormUtil.readString("Ort"));
		imb.setPlz(Integer.valueOf(FormUtil.readString("PLZ")));
		imb.setStrasse(FormUtil.readString("Strasse"));
		imb.setHausNr(Integer.valueOf(FormUtil.readString("Hausnr")));
		imb.setFlaeche(Double.valueOf(FormUtil.readString("Flaeche")));
		imb.setMaklerId(Integer.valueOf(FormUtil.readString("MaklerID")));
     
		imb.save();
		
		if (typFlag == 0) {
			Haus hs = new Haus();
			hs.setHausID(imb.getImmobId());
			hs.setStockwerk(Integer.valueOf(FormUtil.readString("stockwerk")));
			hs.setKaufPreis(Double.valueOf(FormUtil.readString("kaufpreis")));
			hs.setGarten(Boolean.valueOf(FormUtil.readString("garten")));
			hs.save();
		}else if (typFlag == 1) {
			Wohnung whn = new Wohnung();
			whn.setWohnungID(imb.getImmobId());
			whn.setStockwerk(Integer.valueOf(FormUtil.readString("stockwerk")));
			whn.setMietPreis(Double.valueOf(FormUtil.readString("mietpreis")));
			whn.setZimmer(Integer.valueOf(FormUtil.readString("zimmer")));
			whn.setBalkon(Boolean.valueOf(FormUtil.readString("balkon")));
			whn.setEbk(Boolean.valueOf(FormUtil.readString("ebk")));
			whn.save();
		}else System.out.println("Ungueltig Eingabe");
		
		System.out.println("Immobilie mit der ImmobID "+imb.getImmobId()+" wurde erzeugt.");
	}
	
	/**
	 * andern einen Immobilie, nachdem der Benutzer
	 * die entprechenden Daten eingegeben hat.
	 * HAUS = 0;
	 * WOHNUNG = 1;
	 */
	public static void changeImmobilie(int typFlag) {
		Immobilie imb = new Immobilie();
		
		imb.setOrt(FormUtil.readString("Ort"));
		imb.setPlz(Integer.valueOf(FormUtil.readString("PLZ")));
		imb.setStrasse(FormUtil.readString("Strasse"));
		imb.setHausNr(Integer.valueOf(FormUtil.readString("Hausnr")));
		imb.setFlaeche(Double.valueOf(FormUtil.readString("Flaeche")));
		imb.setMaklerId(Integer.valueOf(FormUtil.readString("MaklerID")));
		imb.setImmobId(Integer.valueOf(FormUtil.readString("immobid")));
		
		imb.save();
		
		if (typFlag == 0) {
			Haus hs = new Haus();
			hs.setHausID(imb.getImmobId());
			hs.setStockwerk(Integer.valueOf(FormUtil.readString("stockwerk")));
			hs.setKaufPreis(Double.valueOf(FormUtil.readString("kaufpreis")));
			hs.setGarten(Boolean.valueOf(FormUtil.readString("garten")));
			hs.save();
		}else if (typFlag == 1) {
			Wohnung whn = new Wohnung();
			whn.setWohnungID(imb.getImmobId());
			whn.setStockwerk(Integer.valueOf(FormUtil.readString("stockwerk")));
			whn.setMietPreis(Double.valueOf(FormUtil.readString("mietpreis")));
			whn.setZimmer(Integer.valueOf(FormUtil.readString("zimmer")));
			whn.setBalkon(Boolean.valueOf(FormUtil.readString("balkon")));
			whn.setEbk(Boolean.valueOf(FormUtil.readString("ebk")));
			whn.save();
		}else System.out.println("Ungueltig Eingabe");
		
		System.out.println("Immobilie mit der ImmobId "+imb.getImmobId()+" wurde geaendert.");
	}
	
	/**
	 * loeschen einen  Immobilie , nachdem der Benutzer
	 * die entprechenden ImmobilieID eingegeben hat.
	 * HAUS = 0;
	 * WOHNUNG = 1;
	 */
	public static void deleteImmobilie(int typFlag) {
		int tmpImmobId = Integer.valueOf(FormUtil.readString("immobId"));//schom mal geandert 10.mai
		if (typFlag == 0) {
			Haus.delete(tmpImmobId);
		}else if (typFlag == 1) {
			Wohnung.delete(tmpImmobId);
		}else System.out.println("Ungueltig Eingabe");
		Immobilie.delete(tmpImmobId);
		System.out.println("Immobilie mit der ImmobId "+ tmpImmobId +" wurde geloescht.");
	}
	
	/**
	 * Zeigt die Person Verwaltung
	 */
	public static void showPersonMenu() {
		//Menüoptionen
		final int NEW_PERSON = 0;
		final int CHANGE_PERSON = 1;
		final int DELETE_PERSON = 2;
		final int BACK = 3;
		
		//Maklerverwaltungsmenü
		Menu maklerMenu = new Menu("Person-Verwaltung");
		maklerMenu.addEntry("Neuer Person", NEW_PERSON);
		maklerMenu.addEntry("Aendern Person", CHANGE_PERSON);
		maklerMenu.addEntry("Loeschen Person", DELETE_PERSON);
		maklerMenu.addEntry("Zurück zum Hauptmenü", BACK);
		
		//Verarbeite Eingabe
		while(true) {
			int response = maklerMenu.show();
			
			switch(response) {
				case NEW_PERSON:
					newPerson();
					break;
				case CHANGE_PERSON:
					changePerson();
					break;
				case DELETE_PERSON:
					deletePerson();
					break;
				case BACK:
					return;
			}
		}
	}
	
	/**
	 * Legt einen neuen Person an, nachdem der Benutzer
	 * die entprechenden Daten eingegeben hat.
	 */
	public static void newPerson() {
		
		Person psn = new Person();
		//m.setId(Integer.valueOf(FormUtil.readString("ID")));//schom mal geandert 10.mai
		psn.setVorname(FormUtil.readString("vorname"));
		psn.setNachname(FormUtil.readString("nachname"));
		psn.setAdresse(FormUtil.readString("adresse"));
     
		psn.save();
		
		System.out.println("Person mit der personID "+psn.getPersonID()+" wurde erzeugt.");
	}
	
	/**
	 * andern einen Person, nachdem der Benutzer
	 * die entprechenden Daten eingegeben hat.
	 */
	public static void changePerson() {
		Person psn = new Person();
		psn.setVorname(FormUtil.readString("vorname"));
		psn.setNachname(FormUtil.readString("nachname"));
		psn.setAdresse(FormUtil.readString("adresse"));
		psn.setPersonID(Integer.valueOf(FormUtil.readString("personid")));
		
		psn.save();
		
		System.out.println("Person mit der personID "+psn.getPersonID()+" wurde geaendert.");
	}
	
	/**
	 * loeschen einen  Person , nachdem der Benutzer
	 * die entprechenden Person ID eingegeben hat.
	 */
	public static void deletePerson() {
		int tmpPersonId = Integer.valueOf(FormUtil.readString("personId"));//schom mal geandert 10.mai
		Person.delete(tmpPersonId);
		System.out.println("Person mit der PersonId "+ tmpPersonId +" wurde geloescht.");
	}
	
	/**
	 * Zeigt die Vertrag Verwaltung
	 */
	public static void showVertragMenu() {
		//Menüoptionen
		final int NEW_HAUS_VERTRAG = 0;
		final int NEW_WOHNUNG_VERTRAG = 1;
		final int DELETE_VERTRAG = 2;
		final int BACK = 3;
		//Maklerverwaltungsmenü
		Menu vertragMenu = new Menu("Vertrag-Verwaltung");
		vertragMenu.addEntry("Neuer Haus Vertrag", NEW_HAUS_VERTRAG);
		vertragMenu.addEntry("Neuer Wohnung Vertrag", NEW_WOHNUNG_VERTRAG);
		vertragMenu.addEntry("Loeschen Vertrag", DELETE_VERTRAG);
		vertragMenu.addEntry("Zurück zum Hauptmenü", BACK);
		
		//Verarbeite Eingabe
		while(true) {
			int response = vertragMenu.show();
			
			switch(response) {
				case NEW_HAUS_VERTRAG:
					newVertrag(NEW_HAUS_VERTRAG);
					break;
				case NEW_WOHNUNG_VERTRAG:
					newVertrag(NEW_WOHNUNG_VERTRAG);
					break;
				case DELETE_VERTRAG:
					deleteVertrag();
					break;
				case BACK:
					return;
			}
		}
	}
	
	/**
	 * Legt einen neuen Vertrag an, nachdem der Benutzer
	 * die entprechenden Daten eingegeben hat.
	 * FLAG HAUS = 0;
	 * FLAG WOHNUNG = 1;
	 * 
	 */
	public static void newVertrag(int flag) {
		
		Vertrag vtg = new Vertrag();
		//m.setId(Integer.valueOf(FormUtil.readString("ID")));//schom mal geandert 10.mai
		vtg.setPersonId(FormUtil.readInt("perid"));
		vtg.setImmobId(FormUtil.readInt("immobid"));
		vtg.setDatum(FormUtil.readString("datum"));
		vtg.setOrt(FormUtil.readString("ort"));
     
		vtg.save();
		
		if (flag==0) {
			Kaufvertrag kvtg = new Kaufvertrag();
			kvtg.setKaufnr(vtg.getVertragnr());
			kvtg.setAnzahlRaten(FormUtil.readInt("anzahlraten"));
			kvtg.setRatenZins(Double.valueOf(FormUtil.readString("ratenzins")));

			kvtg.save();
		}else if(flag == 1){
			Mietvertrag mvtg = new Mietvertrag();
			mvtg.setMietnr(vtg.getVertragnr());
			mvtg.setMietBeginn(FormUtil.readString("mietbeginn"));
			mvtg.setDauer(FormUtil.readInt("dauer"));
			mvtg.setNebenKosten(Double.valueOf(FormUtil.readString("nebenkosten")));
			
			mvtg.save();
		}
		System.out.println("Vertrag mit der vertragnr "+vtg.getVertragnr()+" wurde erzeugt.");
	}
	
	/**
	 * loeschen einen  Vertrag , nachdem der Benutzer
	 * die entprechenden Vertrag Nummer eingegeben hat.
	 */
	public static void deleteVertrag() {
		int tmpVertragNr = FormUtil.readInt("vertragnr");//schom mal geandert 10.mai
		Mietvertrag.delete(tmpVertragNr);
		Kaufvertrag.delete(tmpVertragNr);
		Vertrag.delete(tmpVertragNr);
		System.out.println("vertrag mit der vertragNr "+ tmpVertragNr +" wurde geloescht.");
		
	}
	
	/**
	 * Zeigt die Vertrag Uebersicht
	 */
	public static void showVertragUebersicht() {
		Vertrag.showVertragUebersicht();
		Kaufvertrag.showVertragUebersicht();
		Mietvertrag.showVertragUebersicht();
	}
}

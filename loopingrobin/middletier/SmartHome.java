package de.loopingrobin.middletier;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.loopingrobin.backend.Dachboden;
import de.loopingrobin.backend.Farbregler;
import de.loopingrobin.backend.Luftqualitaetsregler;
import de.loopingrobin.backend.Schalter;
import de.loopingrobin.backend.SmarteHeizung;
import de.loopingrobin.backend.SmarteLeuchte;
import de.loopingrobin.backend.SmarteRgbLeuchte;
import de.loopingrobin.backend.SmarterRaum;
import de.loopingrobin.backend.SmartesFenster;
import de.loopingrobin.backend.SmartesGeraet;
import de.loopingrobin.backend.Temperaturregler;
import de.loopingrobin.backend.VerwalterDesDachbodens;

/**
 * <pre>
 * Die Klasse SmartHome hat einen Speicher für smarte Geräte und eine Uhrzeit.
 * 
 * Die Klasse SmartHome kann smarte Geräte hinzufügen und entfernen.
 * </pre>
 * @author Robin Wagner
 *
 */
public class SmartHome {
	
// *************************************************************
// *** Attribute
// *************************************************************
	
	/** Enthält den Dachboden, auf dem alle smarten Geräte zu finden sind. */
	private Dachboden dachboden;
	
	/** Enthält die Uhrzeit, nach der sich alle zeitgesteuerten smarten Geräte richten. */
	private transient Uhrzeit uhrzeit;


// *************************************************************
// *** Konstruktoren
// *************************************************************
	
	/**
	 * Erstellt ein Smart-Home und erstellt sich eine Uhrzeit.
	 * Der Dachboden wird geladen und die Initialisierung wird durchgeführt.
	 */
	public SmartHome() {
		uhrzeit = new Uhrzeit();
		dachbodenLaden();
		initialisieren();
	}


// *************************************************************
// *** Methoden
// *************************************************************

	/**
	 * Der Dachboden wird aus dem Dateispeicher geladen.
	 * Das Dachboden-Objekt wird gespeichert.
	 * Alle smarten Geräte werden aus dem Dachboden importiert.
	 */
	private void dachbodenLaden() {
		dachboden = VerwalterDesDachbodens.ausDateiLesen();
	}

	/**
	 * Die Uhr wird im eigenen Thread gestartet.
	 * Alle smarten Geräte bekommen die Uhr übergeben und  werden in ihren Threads gestartet.
	 */
	private void initialisieren() {

		List<SmartesGeraet> smarteGeraete = dachboden.getAllSmartenGeraete();
		Thread uhrThread = new Thread(uhrzeit);
		uhrThread.start();
		
		for (SmartesGeraet smartesGeraet : smarteGeraete) {
			smartesGeraet.setUhrzeit(uhrzeit);
			
			Thread geraetThread = new Thread(smartesGeraet);
			geraetThread.start();
		}
	}
	
	/**
	 * Fügt ein smartes Gerät der internen Liste hinzu.
	 * @param geraeteBezeichnung Die Kennung, anhand derer der Klassentyp erkannt wird.
	 * @param name Der Name den das smarte Gerät bekommen soll.
	 * @param raum Der Raum, in den das Gerät eingesetzt werden soll.
	 */
	public void addSmartesGeraet(String geraeteBezeichnung, String name, SmarterRaum raum) {
		
		switch (geraeteBezeichnung) {
		
		case "Smarte Leuchte":
			Schalter schalter = new Schalter(name + "Schalter");
			SmartesGeraet leuchte = new SmarteLeuchte(name, schalter, uhrzeit);
			
			dachboden.getSmarteLeuchten().add( (SmarteLeuchte) leuchte);
			leuchte.setRaum(raum);
			Thread leuchteThread = new Thread(leuchte);
			leuchteThread.start();
			break;
			
		case "Smarte RGB-Leuchte":
			Schalter schalterRgb = new Schalter(name + "Schalter");
			Farbregler reglerRgb = new Farbregler(name + "Farbregler", Color.WHITE);
			SmartesGeraet leuchteRgb = new SmarteRgbLeuchte(name, schalterRgb, reglerRgb, uhrzeit);
			
			dachboden.getSmarteRgbLeuchten().add( (SmarteRgbLeuchte) leuchteRgb);
			leuchteRgb.setRaum(raum);
			Thread leuchteRgbThread = new Thread(leuchteRgb);
			leuchteRgbThread.start();
			break;
			
		case "Smarte Heizung":
			Schalter schalterHeizung = new Schalter(name + "Schalter");
			Temperaturregler reglerHeizung = new Temperaturregler(name + "Temp.Regler", 18, 22);
			SmartesGeraet heizung = new SmarteHeizung(name, schalterHeizung, reglerHeizung, uhrzeit);
			
			dachboden.getSmarteHeizungen().add( (SmarteHeizung) heizung);
			heizung.setRaum(raum);
			Thread heizungThread = new Thread(heizung);
			heizungThread.start();
			break;
			
		case "Smartes Fenster":
			Schalter schalterFenster = new Schalter(name + "Schalter");
			Luftqualitaetsregler reglerFenster = new Luftqualitaetsregler(name + "Luftregler", 500, 1000);
			SmartesGeraet fenster = new SmartesFenster(name, schalterFenster, reglerFenster, uhrzeit);
			
			dachboden.getSmarteFenster().add( (SmartesFenster) fenster);
			fenster.setRaum(raum);
			Thread fensterThread = new Thread(fenster);
			fensterThread.start();
			break;
		}
	}
	
	/**
	 * Entfernt ein smartes Gerät aus der Liste smarte Geräte.
	 * Entfernt ein smartes Gerät aus dem Dachboden.
	 * @param geraet Das smarte Gerät, das entfernt wird.
	 * @param raum Der Raum, aus dem das Gerät genommen werden soll.
	 */
	public void deleteSmartesGeraet(SmartesGeraet geraet, SmarterRaum raum) {
		
		raum.deleteSmartesGeraet(geraet);
		
		if (geraet instanceof SmarteLeuchte) {
			List<SmarteLeuchte> leuchten = dachboden.getSmarteLeuchten();
			leuchten.removeIf(t -> t == geraet);
			((SmarteLeuchte) geraet).setThreadAmLaufen(false);
			
		} else if (geraet instanceof SmarteRgbLeuchte) {
			List<SmarteRgbLeuchte> rgbLeuchten = dachboden.getSmarteRgbLeuchten();
			rgbLeuchten.removeIf(t -> t == geraet);
			((SmarteRgbLeuchte) geraet).setThreadAmLaufen(false);
			
		} else if (geraet instanceof SmarteHeizung) {
			List<SmarteHeizung> heizungen = dachboden.getSmarteHeizungen();
			heizungen.removeIf(t -> t == geraet);
			((SmarteHeizung) geraet).setThreadAmLaufen(false);
			
		} else if (geraet instanceof SmartesFenster) {
			List<SmartesFenster> fenster = dachboden.getSmarteFenster();
			fenster.removeIf(t -> t == geraet);
			((SmartesFenster) geraet).setThreadAmLaufen(false);
		}
	}
	
	/**
	 * Fügt dem Smart-Home einen neuen Raum hinzu.
	 * @param raum Der neue Raum.
	 */
	public void addRaum(SmarterRaum raum) {
		dachboden.getSmarteRaeume().add(raum);
	}
	
	/**
	 * Entfernt einen Raum aus dem Smart-Home System.
	 * @param raum Der zu entfernende Raum.
	 */
	public void deleteRaum(SmarterRaum raum) {
		dachboden.getSmarteRaeume().removeIf(t -> t == raum);
	}
	
	/**
	 * Weist einem smarten Gerät einen smarten Raum zu.
	 * Im Gerät wird der Raum eingetragen.
	 * Im Raum wird das Gerät eingetragen.
	 * @param geraet Das Gerät, das zugewiesen wird
	 * @param raum Der Raum, der zugewiesen wird
	 */
	public void geraetEinemRaumZuweisen(SmartesGeraet geraet, SmarterRaum raum) {
		geraet.setRaum(raum);
		raum.addSmartesGeraet(geraet);
	}
	
	/** Speichert den Dachboden im Dateisystem. */
	public void speichern() {
		VerwalterDesDachbodens.inDateiSchreiben(dachboden);
	}

	/** Anzeige des Objekts bei Ausgabe. */
	@Override
	public String toString() {
		return "SmartHome [dachboden=" + dachboden + ", \nuhrzeit=" + uhrzeit + "]";
	}


// *************************************************************
// *** Methoden wegen Datenkapselung
// *************************************************************

	/**
	 * @return the dachboden
	 */
	public Dachboden getDachboden() {
		return dachboden;
	}
	
	/**
	 * Gibt die Liste aller Geräte weiter.
	 * @return Die Liste aller smarten Geräte.
	 */
	public List<SmartesGeraet> getSmarteGeraete() {
		return dachboden.getAllSmartenGeraete();
	}
	
	/**
	 * @return the uhrzeit
	 */
	public Uhrzeit getUhrzeit() {
		return uhrzeit;
	}

	/**
	 * Gibt die smarten Leuchten weiter.
	 * @return Die Liste aller smarten Leuchten.
	 */
	public List<SmarteLeuchte> getSmarteLeuchten() {
		return dachboden.getSmarteLeuchten();
	}

	/**
	 *  Gibt die smarten RGB-Leuchten weiter.
	 * @return Die Liste aller smarten RGB-Leuchten.
	 */
	public List<SmarteRgbLeuchte> getSmarteRgbLeuchten() {
		return dachboden.getSmarteRgbLeuchten();
	}

	/**
	 *  Gibt die smarten Heizungen weiter.
	 * @return Die Liste aller smarten Heizungen.
	 */
	public List<SmarteHeizung> getSmarteHeizungen() {
		return dachboden.getSmarteHeizungen();
	}

	/**
	 *  Gibt die smarten Fenster weiter.
	 * @return Die Liste aller smarten Fenster.
	 */
	public List<SmartesFenster> getSmarteFenster() {
		return dachboden.getSmarteFenster();
	}
	
	/**
	 * Gibt die smarten Räume weiter.
	 * @return Die Map mit allen smarten Räumen.
	 */
	public Map<Integer, SmarterRaum> getSmarteRaeume() {
		Map<Integer, SmarterRaum> rueckgabe = new HashMap<>();
		
		List<SmarterRaum> raeume = dachboden.getSmarteRaeume();
		int index = 10;
		for (SmarterRaum raum : raeume) {
			rueckgabe.put(index++, raum);
		}
		return rueckgabe;
	}
	
	/**
	 * Gibt die Gerätetypen weiter.
	 * @return Die Map mit allen Gerätetypen.
	 */
	public Map<Integer, String> getGeraeteTypen() {
		Map<Integer, String> rueckgabe = new HashMap<>();
		
		Set<String> geraeteTypen = dachboden.getGeraeteTypen();
		int index = 1;
		for (String geraeteTyp : geraeteTypen) {
			rueckgabe.put(index++, geraeteTyp);
		}
		return rueckgabe;
	}

}

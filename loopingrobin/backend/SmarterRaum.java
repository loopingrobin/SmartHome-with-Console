package de.loopingrobin.backend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * Der smarte Raum hat eine Liste der für ihn verfügbaren smarten Geräte.
 * Der smarte Raum hat eine Temperatur.
 * </pre>
 * @author Robin Wagner
 * 
 */
public class SmarterRaum implements Serializable {
	
// *************************************************************
// *** Attribute
// *************************************************************
	
	/** Enthält die serialVersionUID. */
	private static final long serialVersionUID = 5202243806504451854L;

	/** Enthält den Namen des Raums. */
	private String name;
	
	/** Enthält alle smarten Geräte dieses Raums. */
	private List<SmartesGeraet> smarteGeraete = new ArrayList<>();
	
	/** Enthält die Umgebungstemperatur des Raums in °C. */
	private int raumTemperatur;
	
	/** Enthält den CO2-Gehalt der Luft im Raum in ppm. */
	private int kohlenstoffDioxidGehaltInDerLuft;


// *************************************************************
// *** Konstruktoren
// *************************************************************
	
	/** 
	 * Erstellt den smarten Raum mit 20°C Umgebungstemperatur.
	 * @param name Der Name des Raums
	 */
	public SmarterRaum(String name) {
		this.name = name;
		this.raumTemperatur = 15;
		this.kohlenstoffDioxidGehaltInDerLuft = 500;
	}


// *************************************************************
// *** Methoden
// *************************************************************
	
	/**
	 * Fügt ein weiteres smartes Gerät hinzu.
	 * @param geraet Das neue smarte Gerät.
	 */
	public void addSmartesGeraet(SmartesGeraet geraet) {
		smarteGeraete.add(geraet);
		if (this != geraet.getRaum()) {
			geraet.setRaum(this);
		}
	}
	
	/**
	 * Entfernt ein Smartes Gerät aus dem Smart-Home System.
	 * @param geraet Das zu entfernende smarte Gerät.
	 */
	public void deleteSmartesGeraet(SmartesGeraet geraet) {
		smarteGeraete.removeIf(t -> t == geraet);
	}

	/** Die Anzeige des Objekts bei Ausgabe. */
	@Override
	public String toString() {
		return "SmarterRaum [name=" + name + ", temperatur=" + raumTemperatur + ", \nsmarteGerate=" + smarteGeraete + "]";
	}


// *************************************************************
// *** Methoden wegen Datenkapselung
// *************************************************************

	/**
	 * Gibt den Namen des Raums zurück.
	 * @return Der Name als String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gibt alle Ausführungszeiten zurück, mit einem Index, angefangen bei 10, zurück.
	 * @return die Map aller Ausführungszeiten
	 */
	public Map<Integer, SmartesGeraet> getSmarteGeraete() {
		Map<Integer, SmartesGeraet> rueckgabe = new HashMap<>();
		
		int index = 10;
		for (SmartesGeraet geraet : smarteGeraete) {
			rueckgabe.put(index++, geraet);
		}
		return rueckgabe;
	}

	/**
	 * Gibt die Temperatur des Raums zurück.
	 * @return Die Temperatur als int
	 */
	public int getRaumTemperatur() {
		return raumTemperatur;
	}

	/**
	 * Gibt den CO2-Gehalt des Raums zurück.
	 * @return Der CO2-Gehalt als int
	 */
	public int getKohlenstoffDioxidGehaltInDerLuft() {
		return kohlenstoffDioxidGehaltInDerLuft;
	}

	/**
	 * Ersetzt den Namen des Raums.
	 * @param name Der neue Name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Ersetzt den Wert der Raumtemperatur.
	 * @param temperatur Die neue Temperatur
	 */
	public void setRaumTemperatur(int temperatur) {
		this.raumTemperatur = temperatur;
	}

	/**
	 * Ersetzt den Wert des CO2-Gehalts im Raum.
	 * @param kohlenstoffDioxidGehaltInDerLuft Der neue CO2-Gehalt
	 */
	public void setKohlenstoffDioxidGehaltInDerLuft(int kohlenstoffDioxidGehaltInDerLuft) {
		this.kohlenstoffDioxidGehaltInDerLuft = kohlenstoffDioxidGehaltInDerLuft;
	}

}

package de.loopingrobin.backend;

import java.awt.Color;
import java.io.Serializable;

/**
 * <pre>
 * Der Farbregler steuert die Farbeinstellung von einer oder mehrerer smarter RGB-Leuchten und ist serialisierbar.
 * 
 * Der Farbregler hat: 
 * 		einen Namen und
 * 		einen Farbwert als Zustand.
 * 
 * Der Farbregler kann:
 * 		die Farbe anpassen.
 * </pre>
 * @author Robin Wagner
 *
 */
public class Farbregler implements Serializable {
	
// *************************************************************
// *** Attribute
// *************************************************************
	
	/** Enthält die serialVersionUID. */
	private static final long serialVersionUID = -5724736096832002657L;

	/** Enthält den Namen des Farbreglers. */
	private String name;
	
	/** Enthält den Farbzustand des Farbreglers. */
	private Color farbe;


// *************************************************************
// *** Konstruktoren
// *************************************************************
	
	/**
	 * Erstellt einen Farbregler mit Namen und Farbe.
	 * @param name Der Name des Farbreglers.
	 * @param farbe Die voreingestellte Farbe für die smarten RGB-Leuchten.
	 */
	public Farbregler (String name, Color farbe) {
		this.name = name;
		this.farbe = farbe;
	}


// *************************************************************
// *** Methoden
// *************************************************************


// *************************************************************
// *** Methoden wegen Datenkapselung
// *************************************************************

	/**
	 * Gibt den Namen des Farbreglers zurück.
	 * @return der Name als String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gibt die Farbe als Zustand zurück.
	 * @return die Farbe als Color-Objekt
	 */
	public Color getFarbe() {
		return farbe;
	}

	/**
	 * Ersetzt den Namen des Farbreglers.
	 * @param name Der Name als String der gesetzt wird
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Ersetzt den Farbzustand des Farbreglers.
	 * @param farbe Die Farbe als Color-Objekt die gesetzt wird
	 */
	public void setFarbe(Color farbe) {
		this.farbe = farbe;
	}

}

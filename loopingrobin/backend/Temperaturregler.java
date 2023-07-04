package de.loopingrobin.backend;

import java.io.Serializable;

/**
 * <pre>
 * Der Temperaturregler regelt eine oder mehrere smarte Heizungen und ist serialisierbar.
 * 
 * Der Temperaturregler hat:
 * 		einen Namen und
 * 		eine Ziel- und Einstiegstemperatur.
 * 
 * Der Temperaturregler kann:
 * 		die Ziel- und Einstiegstemperatur ändern.
 * </pre>
 * @author Robin Wagner
 *
 */
public class Temperaturregler implements Serializable {

// *************************************************************
// *** Attribute
// *************************************************************
	
	/** Enthält die serialVersionUID. */
	private static final long serialVersionUID = -5684224166437988680L;
	
	/** Enthält den Namen des Temperaturreglers. */
	private String name;
	
	/** Enthält die Zieltemperatur für die smarte Heizung. */
	private int zieltemperatur;
	
	/** Enthält die Einstiegstemperatur für die smarte Heizung. */
	private int einstiegstemperatur;


// *************************************************************
// *** Konstruktoren
// *************************************************************
	
	/**
	 * Erstellt einen Temperaturregler mit Namen, Ziel- und Einstiegstemperaturwert.
	 * @param name Der Name des Temperaturreglers.
	 * @param zieltemperatur Der Wert der Zieltemperatur, bei der die smarte Heizung aufhört zu heizen.
	 * @param einstiegstemperatur Der Wert der Einstiegstemperatur, bei der die smarte Heizung anfängt zu heizen.
	 */
	public Temperaturregler(String name, int zieltemperatur, int einstiegstemperatur) {
		this.name = name;
		this.zieltemperatur = zieltemperatur;
		this.einstiegstemperatur = einstiegstemperatur;
	}


// *************************************************************
// *** Methoden
// *************************************************************


// *************************************************************
// *** Methoden wegen Datenkapselung
// *************************************************************

	/**
	 * Gibt den Namen des Temperaturreglers zurück.
	 * @return der Name als String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gibt den Wert der Zieltemperatur zurück.
	 * @return die Zieltemperatur als int
	 */
	public int getZieltemperatur() {
		return zieltemperatur;
	}

	/**
	 * Gibt den Wert der Zieltemperatur zurück.
	 * @return die Einstiegstemperatur als int
	 */
	public int getEinstiegstemperatur() {
		return einstiegstemperatur;
	}

	/**
	 * Ersetzt den Namen des Farbreglers.
	 * @param name Der Name als String der gesetzt wird
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Ersetzt den Wert der Zieltemperatur, bei der die smarte Heizung aufhört zu heizen.
	 * @param zieltemperatur Der Wert der Zieltemperatur als int, der gesetzt wird.
	 */
	public void setZieltemperatur(int zieltemperatur) {
		this.zieltemperatur = zieltemperatur;
	}

	/**
	 * Ersetzt den Wert der Einstiegstemperatur, bei der die smarte Heizung anfängt zu heizen.
	 * @param einstiegstemperatur Der Wert der Einstiegstemperatur als int, der gesetzt wir.
	 */
	public void setEinstiegstemperatur(int einstiegstemperatur) {
		this.einstiegstemperatur = einstiegstemperatur;
	}

}

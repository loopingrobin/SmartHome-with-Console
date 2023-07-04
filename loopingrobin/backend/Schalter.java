package de.loopingrobin.backend;

import java.io.Serializable;

/**
 * <pre>
 * Der Schalter schaltet ein oder mehrere smarte Geräte an und aus und ist serialisierbar.
 * 
 * Der Schalter hat:
 * 		einen Namen und
 * 		einen Schaltzustand.
 * 
 * Der Schalter kann:
 * 		seinen Zustand umschalten.
 * </pre>
 * @author Robin Wagner
 *
 */
public class Schalter implements Serializable {
	
// *************************************************************
// *** Attribute
// *************************************************************
	
	/** Enthält die serialVersionUID. */
	private static final long serialVersionUID = 3429905067831194080L;

	/** Enthält den Namen des Schalters. */
	private String name;
	
	/** Enthält den Zustand des Schalter. */
	private boolean zustand;


// *************************************************************
// *** Konstruktoren
// *************************************************************
	
	/**
	 * Erstellt einen Schalter mit Namen und dem Schaltzustand false.
	 * @param name Der Name des Schalters.
	 */
	public Schalter (String name) {
		this.name = name;
		zustand = false;
	}


// *************************************************************
// *** Methoden
// *************************************************************
	
	/** Schaltet den Zustand um und macht eine Ausgabe in der Konsole. */
	public void schalten() {
		zustand = !zustand;
		System.out.println(name + " schaltet auf " + zustand);
	}


// *************************************************************
// *** Methoden wegen Datenkapselung
// *************************************************************

	/**
	 * Gibt den Namen des Schalters zurück.
	 * @return der Name als String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gibt den Schaltzustand des Schalters zurück.
	 * @return der Schaltzustand als boolean
	 */
	public boolean isZustand() {
		return zustand;
	}

	/**
	 * Ersetzt den Namen des Schalters.
	 * @param name Der Name als String der gesetzt wird
	 */
	public void setName(String name) {
		this.name = name;
	}

}

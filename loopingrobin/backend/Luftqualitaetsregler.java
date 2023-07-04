package de.loopingrobin.backend;

import java.io.Serializable;

/**
 * <pre>
 * Der Luftqualitätsregler regelt ein oder mehrere smarte Fenster und ist serialisierbar.
 * 
 * Der Luftqualitätsregler hat:
 * 		einen Namen und
 * 		einen Ziel- und Einstiegs-CO2-Gehaltswert.
 * 
 * Der Luftqualitätsregler kann:
 * 		den Ziel- und Einstiegs-CO2-Gehaltswert ändern.
 * </pre>
 * @author Robin Wagner
 * 
 */
public class Luftqualitaetsregler implements Serializable {

// *************************************************************
// *** Attribute
// *************************************************************
	
	/** Enthält die serialVersionUID. */
	private static final long serialVersionUID = -4219334062146255301L;
	
	/** Enthält den Namen des Reglers. */
	private String name;
	
	/** Enthält den Ziel-CO2-Gehalt in der Luft, bis zu dem gelüftet wird, in ppm. */
	private int zielKohlenstoffDioxidGehalt;
	
	/** Enthält den Einstiegs-CO2-Gehalt in der Luft, ab wann das Fenster geöffnet wird, in ppm. */
	private int einstiegsKohlenstoffDioxidGehalt;


// *************************************************************
// *** Konstruktoren
// *************************************************************

	/**
	 * Erzeugt ein Objekt mit Namen, Ziel- und Einstiegs-CO2-Gehalt.
	 * @param name Der Name des Reglers.
	 * @param zielKohlenstoffDioxidGehalt Der Ziel-CO2-Gehaltswert.
	 * @param einstiegsKohlenstoffDioxidGehalt Der Einstiegs-CO2-Gehaltswert.
	 */
	public Luftqualitaetsregler(String name, int zielKohlenstoffDioxidGehalt, int einstiegsKohlenstoffDioxidGehalt) {
		super();
		this.name = name;
		this.zielKohlenstoffDioxidGehalt = zielKohlenstoffDioxidGehalt;
		this.einstiegsKohlenstoffDioxidGehalt = einstiegsKohlenstoffDioxidGehalt;
	}


// *************************************************************
// *** Methoden
// *************************************************************

	/** Gibt einen String wieder, der das Lufqualitätsregler-Objekt representiert. */
	@Override
	public String toString() {
		return "Luftqualitaetsregler [name=" + name + ", zielKohlenstoffDioxidGehalt=" + zielKohlenstoffDioxidGehalt
				+ ", einstiegsKohlenstoffDioxidGehalt=" + einstiegsKohlenstoffDioxidGehalt + "]";
	}


// *************************************************************
// *** Methoden wegen Datenkapselung
// *************************************************************


	/**
	 * Gibt den Namen des Luftqualitätsreglers zurück.
	 * @return der Name als String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gibt den Wert des Ziel-CO2-Gehalts zurück, ab dem das smarte Fenster sich schließt.
	 * @return der Wert des Ziel-CO2-Gehalts als int
	 */
	public int getZielKohlenstoffDioxidGehalt() {
		return zielKohlenstoffDioxidGehalt;
	}

	/**
	 * Gibt den Wert des Einstiegs-CO2-Gehalts zurück, ab dem das smarte Fenster sich öffnet.
	 * @return der Wert des Einstiegs-CO2-Gehalt als int
	 */
	public int getEinstiegsKohlenstoffDioxidGehalt() {
		return einstiegsKohlenstoffDioxidGehalt;
	}

	/**
	 * Ersetzt den Namen des Luftqualitätsreglers.
	 * @param name Der Name als String der gesetzt wird
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Ersetzt den Wert des Ziel-CO2-Gehalts, ab dem das smarte Fenster sich schließt.
	 * @param zielKohlenstoffDioxidGehalt Der Wert des Ziel-CO2-Gehalts als int, der gesetzt wird
	 */
	public void setZielKohlenstoffDioxidGehalt(int zielKohlenstoffDioxidGehalt) {
		this.zielKohlenstoffDioxidGehalt = zielKohlenstoffDioxidGehalt;
	}

	/**
	 * Ersetzt den Wert des Einstiegs-CO2-Gehalts, ab dem das smarte Fenster sich schließt.
	 * @param einstiegsKohlenstoffDioxidGehalt Der Wert des Einstiegs-CO2-Gehalts als int, der gesetzt wird
	 */
	public void setEinstiegsKohlenstoffDioxidGehalt(int einstiegsKohlenstoffDioxidGehalt) {
		this.einstiegsKohlenstoffDioxidGehalt = einstiegsKohlenstoffDioxidGehalt;
	}

}

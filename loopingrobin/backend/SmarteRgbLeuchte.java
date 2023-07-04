package de.loopingrobin.backend;

import java.awt.Color;

import de.loopingrobin.middletier.Uhrzeit;

/**
 * <pre>
 * Die smarte RGB-Leuchte ist eine smarte Leuchte.
 * 
 * Die smarte RGB-Leuchte hat:
 * 		einen Farbwert und
 * 		den zuletzt gespeicherten Farbwert vom Regler.
 * 
 * Die smarte RGB-Leuchte braucht:
 * 		einen Farbregler.
 * 
 * Die smarte RGB-Leuchte kann:
 * 		ihre Farbe ändern.
 * </pre>
 * @author Robin Wagner
 * 
 */
public class SmarteRgbLeuchte extends SmarteLeuchte {
	
// *************************************************************
// *** Attribute
// *************************************************************
	
	/** Enthält die serialVersionUID. */
	private static final long serialVersionUID = 3265632871947337725L;

	/** Enthält den Farbwert für die Anzeige der RGB-Leuchte. */
	protected Color farbe;
	
	/** Enthält den Farbregler zur änderung der Farbe. */
	protected Farbregler regler;
	
	/** Enthält den letzten Zustand des Farbreglers. */
	protected Color letzeFarbeVonRegler;


// *************************************************************
// *** Konstruktoren
// *************************************************************

	/** 
	 * Erstellt ein Objekt mit Name, Schalter und Regler und Uhrzeit.
	 * @param name Der Name der smarte RGB-Leuchte.
	 * @param schalter Der Schalter, der die RGB-Leuchte schaltet.
	 * @param regler Der Farbregler, der die Farbe der RGB-Leuchte einstellt.
	 * @param uhrzeit Die Uhrzeit, nach der sich die Ausführzeiten richten.
	 */
	public SmarteRgbLeuchte(String name, Schalter schalter, Farbregler regler, Uhrzeit uhrzeit) {
		super(name, schalter, uhrzeit);
		this.regler = regler;
		letzeFarbeVonRegler = regler.getFarbe();
		farbe = letzeFarbeVonRegler;
	}


// *************************************************************
// *** Methoden
// *************************************************************
	
	/** 
	 * Führt die verschiedenen "hören"-Methoden aus.
	 */
	@Override
	protected void hoeren() {
		super.hoeren();
		hoerenAufFarbregler();
	}

	/** 
	 * "Hört" auf die Farbe des Farbreglers und macht eine Ausgabe in der Konsole.
	 * Bei Änderung der Farbe wird diese übernommen.
	 */
	private void hoerenAufFarbregler() {
		if (letzeFarbeVonRegler != regler.getFarbe()) {
			farbe = regler.getFarbe();
			letzeFarbeVonRegler = regler.getFarbe();
			System.out.println(name + " ändert die Farbe zu " + farbe);
		}
	}

	/** Gibt Text in der Konsole aus, um den aktuellen "eingeschaltet"-Zustand und die Farbe zu zeigen. */
	@Override
	public void ausgabeDialog() {
		if (farbe.equals(Color.YELLOW)) {
			System.out.println(uhrzeit.getUhrzeit() + " Uhr" + " - " + raum.getName() + ": " + name + " ist " + (eingeschaltet ? "\u001B[33mAN\u001B[0m" : "AUS") + " geschaltet");
		} else if (farbe.equals(Color.GREEN)) {
			System.out.println(uhrzeit.getUhrzeit() + " Uhr" + " - " + raum.getName() + ": " + name + " ist " + (eingeschaltet ? "\u001B[32mAN\u001B[0m" : "AUS") + " geschaltet");
		} else if (farbe.equals(Color.RED)) {
			System.out.println(uhrzeit.getUhrzeit() + " Uhr" + " - " + raum.getName() + ": " + name + " ist " + (eingeschaltet ? "\u001B[31mAN\u001B[0m" : "AUS") + " geschaltet");
		} else {
			System.out.println(uhrzeit.getUhrzeit() + " Uhr" + " - " + raum.getName() + ": " + name + " ist " + (eingeschaltet ? "AN" : "AUS") + " geschaltet");
		}
	}

	/** Gibt einen String wieder, der das SmarteHeizung-Objekt repräsentiert. */
	@Override
	public String toString() {
		return "SmarteRgbLeuchte [farbe=" + farbe + "]";
	}


// *************************************************************
// *** Methoden wegen Datenkapselung
// *************************************************************

	/**
	 * Gibt die eingestellte Farbe der RGB-Leuchte.
	 * @return die Farbe als Color-Objekt
	 */
	public Color getFarbe() {
		return farbe;
	}

	/**
	 * Gibt den Farbregler zurück, auf den die RGB-Leuchte "hört".
	 * @return das Farbregler-Objekt
	 */
	@Override
	public Farbregler getFarbregler() {
		return regler;
	}

}

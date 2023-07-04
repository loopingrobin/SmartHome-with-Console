package de.loopingrobin.backend;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;

/**
 * <pre>
 * Die Ausführungszeit gibt die Zeit an, in der ein smartes Gerät ausgeführt wird oder arbeitet.
 * 
 * Die Ausführungszeit hat eine anfangsZeit, endeZeit und Dauer.
 * Die Ausführungszeit kann sich je nach Eingabe die endeZeit oder Dauer berechnen.
 * </pre>
 * @author Robin Wagner
 * 
 */
public class Ausfuehrungszeit implements Serializable {
	
// *************************************************************
// *** Attribute
// *************************************************************
	
	/** Enthält die serialVersionUID. */
	private static final long serialVersionUID = -7982230694685460884L;

	/** Enthält die Uhrzeit, zu der das smarte Gerät seine Arbeit anfängt. */
	private LocalTime anfangsZeit;
	
	/** Enthält die Uhrzeit, zu der das smarte Gerät seine Arbeit beendet. */
	private LocalTime endeZeit;
	
	/** Enthält die Stunden, Minuten und Sekunden der Arbeitsdauer. */
	private Duration dauer;


// *************************************************************
// *** Konstruktoren
// *************************************************************
	
	/**
	 * Erstellt eine Ausfuehrungszeit mit Anfangs- und Endzeit.
	 * Die Dauer wird automatisch berechnet.
	 * @param anfangsZeit Die Uhrzeit, zu der das smarte Gerät seine Arbeit anfängt.
	 * @param endeZeit Die Uhrzeit, zu der das smarte Gerät seine Arbeit beendet.
	 */
	public Ausfuehrungszeit(LocalTime anfangsZeit, LocalTime endeZeit) {
		this.anfangsZeit = anfangsZeit;
		this.endeZeit = endeZeit;
		dauer = Duration.between(anfangsZeit, endeZeit);
	}


// *************************************************************
// *** Methoden
// *************************************************************

	/** Gibt einen String wieder, der das Ausführungs-Objekt repräsentiert. */
	@Override
	public String toString() {
		return "Ausfuehrungszeit [anfangsZeit=" + anfangsZeit + ", endeZeit=" + endeZeit + ", dauer=" + dauer + "]";
	}


// *************************************************************
// *** Methoden wegen Datenkapselung
// *************************************************************


	/**
	 * Gibt die Uhrzeit zurück, zu der das smarte Gerät seine Arbeit anfängt.
	 * @return die Anfangszeit als LocalTime-Objekt
	 */
	public LocalTime getAnfangsZeit() {
		return anfangsZeit;
	}

	/**
	 * Gibt die Uhrzeit zurück, zu der das smarte Gerät seine Arbeit beendet.
	 * @return die Endezeit als LocalTime-Objekt
	 */
	public LocalTime getEndeZeit() {
		return endeZeit;
	}

	/**
	 * Gibt die Dauer zurück, die zwischen der Anfangs- und Endezeit entsteht.
	 * @return die Dauer als Duration-Objekt
	 */
	public Duration getDauer() {
		return dauer;
	}

	/**
	 * Ersetzt die Uhrzeit, zu der das smarte Gerät mit seiner Arbeit anfängt.
	 * @param anfangsZeit Die Anfangszeit als LocalTime-Objekt die gesetzt wird
	 */
	public void setAnfangsZeit(LocalTime anfangsZeit) {
		this.anfangsZeit = anfangsZeit;
	}

	/**
	 * Ersetzt die Uhrzeit, zu der das smarte Gerät seine Arbeit beendet.
	 * @param endeZeit Die Endezeit als LocalTime-Objekt die gesetzt wird
	 */
	public void setEndeZeit(LocalTime endeZeit) {
		this.endeZeit = endeZeit;
	}

}

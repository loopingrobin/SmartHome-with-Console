package de.loopingrobin.backend;

import java.io.Serializable;
import java.util.Map;

import de.loopingrobin.middletier.Uhrzeit;

/**
 * <pre>
 * Dieses Interface muss von jedem smarten Gerät für das Smart-Home implementiert werden.
 * </pre>
 * @author Robin Wagner
 * 
 */
public interface SmartesGeraet extends Serializable, Runnable {

// *************************************************************
// *** Methoden
// *************************************************************
	
	/** 
	 * Fügt dem smarten Gerät einen Ausführungszeitraum hinzu.
	 * @param ausfuehrungszeit Der Ausführungszeitraum.
	 */
	void addAusfuehrungszeit(Ausfuehrungszeit ausfuehrungszeit);
	
	/**
	 * Entfernt einen Ausführungszeitraum aus dem smarten Gerät.
	 * @param ausfuehrungszeit Der zu entfernende Ausführungszeitraum.
	 */
	void deleteAusfuehrungszeit(Ausfuehrungszeit ausfuehrungszeit);
	
	/**
	 * Gibt den Namen des smarten Geräts zurück.
	 * @return Der String des Namen.
	 */
	String getName();
	
	/**
	 * Gibt den Schalter des smarten Geräts zurück.
	 * @return Der Schalter.
	 */
	Schalter getSchalter();
	
	/**
	 * Gibt den Farbregler des smarten Geräts zurück.
	 * @return Der Farbregler.
	 */
	Farbregler getFarbregler();
	
	/**
	 * Gibt den Temperaturregler des smarten Geräts zurück.
	 * @return Der Temperaturrelger.
	 */
	Temperaturregler getTemperaturregler();
	
	/**
	 * Gibt den Luftqualitätsregler des smarten Geräts zurück.
	 * @return Der Luftqualitätsregler.
	 */
	Luftqualitaetsregler getLuftqualitaetsregler();
	
	/**
	 * Gibt die Ausführungszeiten weiter.
	 * @return Die Map mit allen Ausführungszeiten.
	 */
	Map<Integer, Ausfuehrungszeit> getAusfuehrungszeiten();
	
	/**
	 * Gibt den smarten Raum weiter.
	 * @return Der smarte Raum.
	 */
	SmarterRaum getRaum();
	
	/** 
	 * Übergibt die Uhrzeit, nach der sich Ausführungszeiten richten.
	 * @param uhrzeit Die Uhrzeit, die gesetzt wird
	 */
	void setUhrzeit(Uhrzeit uhrzeit);
	
	/** 
	 * Legt den Raum fest, in dem das smarte Gerät arbeitet.
	 * @param raum Der Raum, der gesetzt wird
	 */
	void setRaum(SmarterRaum raum);

}

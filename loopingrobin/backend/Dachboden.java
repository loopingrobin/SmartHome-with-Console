package de.loopingrobin.backend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <pre>
 * Die Dachboden-Klasse ist zur Aufbewahrung und Speicherung aller smarten Geräte gedacht und ist serialisierbar.
 * 
 * Der Dachboden hat Listen aller smarten Geräte, Räume und Gerätetypen.
 * 
 * Der Dachboden kann jede Liste ausgeben und neu setzten.
 * </pre>
 * @author Robin Wagner
 *
 */
public class Dachboden implements Serializable {

// *************************************************************
// *** Attribute
// *************************************************************
	
	/** Enthält die serialVersionUID. */
	private static final long serialVersionUID = -3908550324994762360L;
	
	/** Enthält die Liste aller smarten Leuchten. */
	private List<SmarteLeuchte> smarteLeuchten = new ArrayList<>();
	
	/** Enthält die Liste aller smarten RGB-Leuchten. */
	private List<SmarteRgbLeuchte> smarteRgbLeuchten = new ArrayList<>();
	
	/** Enthält die Liste aller smarten Heizungen. */
	private List<SmarteHeizung> smarteHeizungen = new ArrayList<>();
	
	/** Enthält die Liste aller smarten Fenster. */
	private List<SmartesFenster> smarteFenster = new ArrayList<>();
	
	/** Enthält die Liste aller smarten Räume. */
	List<SmarterRaum> smarteRaeume = new ArrayList<>();
	
	/** Enthält die Liste aller Gerätetypen. */
	Set<String> geraeteTypen;


// *************************************************************
// *** Konstruktoren
// *************************************************************
	
	/** 
	 * Der Dachboden wird erstellt und die Gerätetypenliste erstellt.
	 */
	public Dachboden() {
		geraeteTypenSchreiben();
	}


// *************************************************************
// *** Methoden
// *************************************************************

	/** Schreibt alle verfügbaren Gerätetypen in die Gerätetypenliste. */
	private void geraeteTypenSchreiben() {
		geraeteTypen = new HashSet<>();
		geraeteTypen.add("Smarte Leuchte");
		geraeteTypen.add("Smarte RGB-Leuchte");
		geraeteTypen.add("Smarte Heizung");
		geraeteTypen.add("Smartes Fenster");
	}
	
	/**
	 * Gibt alle smarten Geräte zurück, die im Dachboden gespeichert sind.
	 * @return die Liste aller smarten Geräte
	 */
	public List<SmartesGeraet> getAllSmartenGeraete(){
		List<SmartesGeraet> rueckgabe = new ArrayList<>();
		
		if (smarteLeuchten != null) {
			rueckgabe.addAll(smarteLeuchten);
		}
		if (smarteRgbLeuchten != null) {
			rueckgabe.addAll(smarteRgbLeuchten);
		}
		if (smarteHeizungen != null) {
			rueckgabe.addAll(smarteHeizungen);
		}
		if (smarteFenster != null) {
			rueckgabe.addAll(smarteFenster);
		}
		
		return rueckgabe;
	}

	/** Gibt einen String wieder, der das Dachboden-Objekt repräsentiert. */
	@Override
	public String toString() {
		return "Dachboden [smarteLeuchten=" + smarteLeuchten 
				+ ", \nsmarteRgbLeuchten=" + smarteRgbLeuchten 
				+ ", \nsmarteHeizungen=" + smarteHeizungen
				+ ", \nsmarteFenster=" + smarteFenster
				+ ", \nsmarteRaeume=" + smarteRaeume  + "]";
	}


// *************************************************************
// *** Methoden wegen Datenkapselung
// *************************************************************

	/**
	 * Gibt alle smarten Leuchten zurück, die im Dachboden gespeichert sind.
	 * @return die Liste aller smarten Leuchten
	 */
	public List<SmarteLeuchte> getSmarteLeuchten() {
		return smarteLeuchten;
	}

	/**
	 * Gibt alle smarten RGB-Leuchten zurück, die im Dachboden gespeichert sind.
	 * @return die Liste aller smarten RGB-Leuchten
	 */
	public List<SmarteRgbLeuchte> getSmarteRgbLeuchten() {
		return smarteRgbLeuchten;
	}
	
	/**
	 * Gibt alle smarten Heizungen zurück, die im Dachboden gespeichert sind.
	 * @return die Liste aller smarten Heizungen
	 */
	public List<SmarteHeizung> getSmarteHeizungen() {
		return smarteHeizungen;
	}
	
	/**
	 * Gibt alle smarten Fenster zurück, die im Dachboden gespeichert sind.
	 * @return die Liste aller smarten Fenster
	 */
	public List<SmartesFenster> getSmarteFenster() {
		return smarteFenster;
	}

	/**
	 * Gibt alle smarten Räume zurück, die im Dachboden gespeichert sind.
	 * @return die Liste aller smarten Räume
	 */
	public List<SmarterRaum> getSmarteRaeume() {
		return smarteRaeume;
	}

	/**
	 * Gibt alle Gerätetypen zurück, die im Dachboden gespeichert sind.
	 * @return die Liste aller Gerätetypen
	 */
	public Set<String> getGeraeteTypen() {
		return geraeteTypen;
	}

	/**
	 * Ersetzt alle smarten Leuchten im Dachboden.
	 * @param smarteLeuchten Die Liste der smarten Leuchten die gesetzt wird
	 */
	public void setSmarteLeuchten(List<SmarteLeuchte> smarteLeuchten) {
		this.smarteLeuchten = smarteLeuchten;
	}

	/**
	 * Ersetzt alle smarten RGB-Leuchten im Dachboden.
	 * @param smarteRgbLeuchten Die Liste der smarten RGB-Leuchten die gesetzt wird
	 */
	public void setSmarteRgbLeuchten(List<SmarteRgbLeuchte> smarteRgbLeuchten) {
		this.smarteRgbLeuchten = smarteRgbLeuchten;
	}

	/**
	 * Ersetzt alle smarten Heizungen im Dachboden.
	 * @param smarteHeizungen Die Liste der smarten Heizungen die gesetzt wird
	 */
	public void setSmarteHeizungen(List<SmarteHeizung> smarteHeizungen) {
		this.smarteHeizungen = smarteHeizungen;
	}

	/**
	 * Ersetzt alle smarten Fenster im Dachboden.
	 * @param smarteFenster Die Liste der smarten Fenster die gesetzt wird
	 */
	public void setSmarteFenster(List<SmartesFenster> smarteFenster) {
		this.smarteFenster = smarteFenster;
	}

	/**
	 * Ersetzt alle smarten Räume im Dachboden.
	 * @param smarteRaeume Die Liste der smarten Räume die gesetzt wird
	 */
	public void setSmarteRaeume(List<SmarterRaum> smarteRaeume) {
		this.smarteRaeume = smarteRaeume;
	}

	/**
	 * Ersetzt alle Gerätetypen im Dachboden.
	 * @param geraeteTypen Die Liste der Gerätetypen die gesetzt wird
	 */
	public void setGeraeteTypen(Set<String> geraeteTypen) {
		this.geraeteTypen = geraeteTypen;
	}

}

package de.loopingrobin.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.loopingrobin.middletier.Uhrzeit;

/**
 * <pre>
 * Das smarte Fenster ist ein smartes Gerät.
 * 
 * Das smarte Fenster hat:
 * 		einen Namen,
 * 		einen Schalter und einen geöffnet-Zustand,
 * 		einen Ziel- und Einstiegs-CO2-Gehalt,
 * 		Ausführungszeiten und eine Uhrzeit und
 * 		einen smarten Raum in dem es verbaut ist.
 * 
 * Das smarte Fenster kann:
 * 		sich öffnen und schließen,
 * 		im geöffneten Zustand die Temperatur und den CO2-Gehalt senken und
 * 		im geschlossenen Zustand den CO2-Gehalt erhöhen.
 * </pre>
 * @author Robin Wagner
 * 
 */
public class SmartesFenster implements SmartesGeraet {

// *************************************************************
// *** Attribute
// *************************************************************
	
	/** Enthält die serialVersionUID. */
	private static final long serialVersionUID = -8780054523428771231L;

	/** Enthält den Namen des smarten Fensters. */
	private String name;
	
	/** Enthält den Zustand, ob das Fenster offen (true) oder geschlossen (false) ist. */
	private boolean geoeffnet;
	
	/** Enthält den Schalter, der das Fenster "manuell" öffnet und schliesst. */
	private Schalter schalter;
	
	/** Enthält den Fenster-Zustand, ob es sich öffnen dar. */
	private boolean darfOeffnen;
	
	/** Enthält den Regler der die Grenzwerte der Luftqualität für den Raum vorgibt. */
	private Luftqualitaetsregler regler;
	
	/** Enthält den Ziel-CO2-Gehaltswert für die Luft im Raum, bis zu dem gelüftet wird, in ppm. */
	private int zielKohlenstoffDioxidGehalt;
	
	/** Enthält den Einstiegs-CO2-Gehaltswert für die Luft im Raum, ab dem das Fenster geöffnet wird, in ppm. */
	private int einstiegsKohlenstoffDioxidGehalt;
	
	/** Enthält die Zeiten, in den das smarte Fenster sich öffnet. */
	protected List<Ausfuehrungszeit> ausfuehrungszeiten = new ArrayList<>();
	
	/** Enthält die Uhrzeit, nach der sich die Ausführungszeiten richten wird. */
	protected transient Uhrzeit uhrzeit;
	
	/** Enthält den Raum, in dem das smarte Fenster verbaut ist. */
	protected SmarterRaum raum;
	
	/** Enthält den Lauf-Befehl für den Thread das smarten Fenster. */
	protected boolean threadAmLaufen;


// *************************************************************
// *** Konstruktoren
// *************************************************************
	
	/**
	 * Erstellt ein smartes Fenster mit Namen, Schalter und Uhrzeit.
	 * @param name Der Name des smarten Fensters.
	 * @param schalter Der Schalter auf den das smarte Fenster "hört".
	 * @param regler Der Luftqualitätsregler, der die luftqualität regelt.
	 * @param uhrzeit Die Uhrzeit nach der sich die Ausführungszeiten richten.
	 */
	public SmartesFenster(String name, Schalter schalter, Luftqualitaetsregler regler, Uhrzeit uhrzeit) {
		this.name = name;
		this.schalter = schalter;
		darfOeffnen = schalter.isZustand();
		geoeffnet = darfOeffnen;
		this.regler = regler;
		zielKohlenstoffDioxidGehalt = regler.getZielKohlenstoffDioxidGehalt();
		einstiegsKohlenstoffDioxidGehalt = regler.getEinstiegsKohlenstoffDioxidGehalt();
		this.uhrzeit = uhrzeit;
	}


// *************************************************************
// *** Methoden
// *************************************************************

	/** Der Thread wird bis zur Löschung des Objekts ausgeführt und "hört" währenddessen. */
	@Override
	public void run() {
		threadAmLaufen = true;
		while (threadAmLaufen) {
			hoeren();
		}
	
	}

	/** Führt die verschiedenen hören-Methoden aus. */
	private void hoeren() {
		hoerenAufSchalter();
		hoerenAufLuftqualitaetsregler();
		
		if (darfOeffnen) {
			hoerenAufSchaltzeiten();
			hoerenAufLuftqualitaet();
			
			if (geoeffnet) {
				raumLueften();
			} else {
				luftqualitaetSinkt();
			}
		}
	}

	/** Hört auf den Zustandswechsel des Schalters. */
	private void hoerenAufSchalter() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException ausnahme) {
			ausnahme.printStackTrace();
		}
		
		if (!schalter.isZustand() != darfOeffnen) {
			darfOeffnen = !darfOeffnen;
			System.out.println(schalter.getName() + (darfOeffnen ? "ÖFFNET" : "SCHLIESST") + " das Fenster");
		}
	}

	/** Hört auf die aktuelle Uhrzeit und schaltet nach Vorgabe der Ausführungszeiten. */
	private void hoerenAufSchaltzeiten() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException ausnahme) {
			ausnahme.printStackTrace();
		}
		
		for (Ausfuehrungszeit ausfuehrungszeit : ausfuehrungszeiten) {
			if (geoeffnet && ausfuehrungszeit.getEndeZeit() == uhrzeit.getUhrzeit()) {
				umschalten();
				ausgabeDialog();
			} else if (!geoeffnet && ausfuehrungszeit.getAnfangsZeit() == uhrzeit.getUhrzeit()){
				umschalten();
				ausgabeDialog();
			}
		}
	}

	/** Schaltet das Fenster auf den nicht vorherrschenden Zustand um. */
	private void umschalten() {
		geoeffnet = !geoeffnet;
	}

	/** Gibt Text in der Konsole aus, um den aktuellen Zustand zu zeigen. */
	private void ausgabeDialog() {
		if (geoeffnet) {
			System.out.println(uhrzeit.getUhrzeit() + " Uhr" + " - " + raum.getName() + ": " + name + " ist GEÖFFNET");
		} else {
			System.out.println(uhrzeit.getUhrzeit() + " Uhr" + " - " + raum.getName() + ": " + name + " ist GESCHLOSSEN");
		}
	}

	/** Eine hoeren()-Methode die auf den Luftqualitätsregler hört. */
	private void hoerenAufLuftqualitaetsregler() {
		if (zielKohlenstoffDioxidGehalt != regler.getZielKohlenstoffDioxidGehalt() || einstiegsKohlenstoffDioxidGehalt != regler.getEinstiegsKohlenstoffDioxidGehalt()) {
			zielKohlenstoffDioxidGehalt = regler.getZielKohlenstoffDioxidGehalt();
			einstiegsKohlenstoffDioxidGehalt = regler.getEinstiegsKohlenstoffDioxidGehalt();
			System.out.println(name + " ändert die Temperaturen zu Ziel:" + zielKohlenstoffDioxidGehalt + ", Einstieg: " + einstiegsKohlenstoffDioxidGehalt);
		}
	}

	/** Eine hoeren()-Methode die auf die Luftqualität im Raum hört. */
	private void hoerenAufLuftqualitaet() {
		boolean zuStickig = raum.getKohlenstoffDioxidGehaltInDerLuft() > einstiegsKohlenstoffDioxidGehalt;
		boolean zuFrisch = raum.getKohlenstoffDioxidGehaltInDerLuft() < zielKohlenstoffDioxidGehalt;
		
		if ((zuStickig && !geoeffnet) || (zuFrisch && geoeffnet)) {
			umschalten();
			ausgabeDialog();
		}
	}

	/** Lüftet den Raum, wenn das Fenster offen ist. */
	private void raumLueften() {
		raum.setRaumTemperatur(raum.getRaumTemperatur() - 1);
		raum.setKohlenstoffDioxidGehaltInDerLuft(raum.getKohlenstoffDioxidGehaltInDerLuft() - 100);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ausnahme) {
			ausnahme.printStackTrace();
		}
	}
	
	/** Erhöht den CO2-Gehalt, wenn das Fenster geschlossen ist. */
	private void luftqualitaetSinkt() {
		raum.setKohlenstoffDioxidGehaltInDerLuft(raum.getKohlenstoffDioxidGehaltInDerLuft() + 50);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ausnahme) {
			ausnahme.printStackTrace();
		}
	}

	/**
	 * Fügt eine neuen Ausführungszeitraum dem smarten Fenster hinzu.
	 * @param ausfuehrungszeit Der hinzuzufügende Ausführungszeitraum.
	 */
	@Override
	public void addAusfuehrungszeit(Ausfuehrungszeit ausfuehrungszeit) {
		ausfuehrungszeiten.add(ausfuehrungszeit);
	}
	
	/**
	 * Entfernt einen Ausführungszeitraum des smarten Fensters.
	 * @param ausfuehrungszeit Der zu entfernende Ausführungszeitraum.
	 */
	@Override
	public void deleteAusfuehrungszeit(Ausfuehrungszeit ausfuehrungszeit) {
		ausfuehrungszeiten.removeIf(t -> t == ausfuehrungszeit);
	}

	/** Die Anzeige des Objekts bei der Ausgabe. */
	@Override
	public String toString() {
		return "SmartesFenster [name=" + name + ", geoeffnet=" + geoeffnet + ", darfOeffnen=" + darfOeffnen
				+ ", zielKohlenstoffDioxidGehalt=" + zielKohlenstoffDioxidGehalt + ", einstiegsKohlenstoffDioxidGehalt="
				+ einstiegsKohlenstoffDioxidGehalt + ", ausfuehrungszeiten=" + ausfuehrungszeiten 
				+ ", raum=" + (raum == null ? "Nicht zugewiesen" : raum.getName() ) + "]";
	}


	// *************************************************************
// *** Methoden wegen Datenkapselung
// *************************************************************
	/**
	 * Gibt den Namen des Fensters zurück.
	 * @return Der Name als String
	 */
	@Override
	public String getName() {
		return name;
	}
	
	/**
	 * Gibt den Schalter zurück, auf den das Fenster "hört".
	 * @return Das Schalter-Objekt
	 */
	@Override
	public Schalter getSchalter() {
		return schalter;
	}
	
	/**
	 * Diese Methode haben alle smarten Geräte, gibt hier aber null zurück.
	 * @return null
	 */
	@Override
	public Farbregler getFarbregler() {
		return null;
	}
	
	/**
	 * Diese Methode haben alle smarten Geräte, gibt hier aber null zurück.
	 * @return null
	 */
	@Override
	public Temperaturregler getTemperaturregler() {
		return null;
	}

	/**
	 * Gibt den Luftqualitätsregler zurück, auf den das Fenster "hört".
	 * @return Das Luftqualitätsregler-Objekt
	 */
	public Luftqualitaetsregler getLuftqualitaetsregler() {
		return regler;
	}
	
	/**
	 * 
	 * Gibt alle Ausführungszeiten zurück, mit einem Index, angefangen bei 10, zurück.
	 * @return die Map aller Ausführungszeiten
	 */
	@Override
	public Map<Integer, Ausfuehrungszeit> getAusfuehrungszeiten() {
		Map<Integer, Ausfuehrungszeit> rueckgabe = new HashMap<>();
		
		int index = 10;
		for (Ausfuehrungszeit ausfuehrungszeit : ausfuehrungszeiten) {
			rueckgabe.put(index++, ausfuehrungszeit);
		}
		return rueckgabe;
	}
	
	/**
	 * Gibt den Raum zurück, in dem das Fenster eingesetzt ist.
	 * @return Das SmarterRaum-Objekt
	 */
	@Override
	public SmarterRaum getRaum() {
		return raum;
	}

	/**
	 * Gibt den Wert des Ziel-CO2-Gehalts zurück.
	 * @return der Wert des Ziel-CO2-Gehalt als int
	 */
	public int getZielKohlenstoffDioxidGehalt() {
		return zielKohlenstoffDioxidGehalt;
	}

	/**
	 * Gibt den Wert des Einstiegs-CO2-Gehalts zurück.
	 * @return der Wert des Einstiegs-CO2-Gehalt als int
	 */
	public int getEinstiegsKohlenstoffDioxidGehalt() {
		return einstiegsKohlenstoffDioxidGehalt;
	}
	
	/**
	 * Ersetzt die Uhrzeit, nach der das Fenster sich richtet.
	 * @param uhrzeit Das Uhrzeit-Objekt, das gesetzt wird. 
	 */
	@Override
	public void setUhrzeit(Uhrzeit uhrzeit) {
		this.uhrzeit = uhrzeit;
	}
	
	/**
	 * Ersetzt den smarten Raum, in dem das Fenster eingesetzt wird.
	 * Fenster setzt sich in den Raum ein, wenn sie das nicht bereits ist.
	 * @param raum Das SmarterRaum-Objekt, das gesetzt wird.
	 */
	@Override
	public void setRaum(SmarterRaum raum) {
		this.raum = raum;
		Map<Integer, SmartesGeraet> geraete = raum.getSmarteGeraete();
		if (!geraete.containsValue(this)) {
			raum.addSmartesGeraet(this);
		}
	}

	/**
	 * Ersetzt den Namen des smarten Fensters.
	 * @param name Der Name als String der gesetzt wird
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Ersetzt den "geöffnet"-Zustand.
	 * @param geoeffnet true, wenn das Fenster geöffnet werden soll
	 */
	public void setGeoeffnet(boolean geoeffnet) {
		this.geoeffnet = geoeffnet;
	}

	/**
	 * Ersetzt den Schalter, auf den das Fenster "hört".
	 * @param schalter Das Schalter-Objekt
	 */
	public void setSchalter(Schalter schalter) {
		this.schalter = schalter;
	}

	/**
	 * @param darfOeffnen the darfOeffnen to set
	 */
	public void setDarfOeffnen(boolean darfOeffnen) {
		this.darfOeffnen = darfOeffnen;
	}

	/**
	 * @param zielKohlenstoffDioxidGehalt the zielKohlenstoffDioxidGehalt to set
	 */
	public void setZielKohlenstoffDioxidGehalt(int zielKohlenstoffDioxidGehalt) {
		this.zielKohlenstoffDioxidGehalt = zielKohlenstoffDioxidGehalt;
	}

	/**
	 * @param einstiegsKohlenstoffDioxidGehalt the einstiegsKohlenstoffDioxidGehalt to set
	 */
	public void setEinstiegsKohlenstoffDioxidGehalt(int einstiegsKohlenstoffDioxidGehalt) {
		this.einstiegsKohlenstoffDioxidGehalt = einstiegsKohlenstoffDioxidGehalt;
	}

	/**
	 * @param ausfuehrungszeiten the ausfuehrungszeiten to set
	 */
	public void setAusfuehrungszeiten(List<Ausfuehrungszeit> ausfuehrungszeiten) {
		this.ausfuehrungszeiten = ausfuehrungszeiten;
	}

	/**
	 * @param regler the regler to set
	 */
	public void setRegler(Luftqualitaetsregler regler) {
		this.regler = regler;
	}

	/**
	 * @param threadAmLaufen the threadAmLaufen to set
	 */
	public void setThreadAmLaufen(boolean threadAmLaufen) {
		this.threadAmLaufen = threadAmLaufen;
	}

}

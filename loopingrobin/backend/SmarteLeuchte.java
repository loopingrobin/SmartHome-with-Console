package de.loopingrobin.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.loopingrobin.middletier.Uhrzeit;

/**
 * <pre>
 * Die smarte Leuchte ist ein smartes Gerät.
 * 
 * Die smarte Leuchte hat:
 * 		einen Namen und
 * 		einen "eingeschaltet"- und "darf leuchten"-Zustand,
 * 		einen "Thread am Laufen"-Zustand und
 * 		Ausführungszeiten.
 * 		
 * Die smarte Leuchte braucht:
 * 		einen Schalter,
 * 		eine Uhrzeit und
 * 		einen Raum.
 * 
 * Die smarte Leuchte kann:
 * 		leuchten,
 * 		auf den Schalter "hören" und
 * 		Ausführungszeiten hinzufügen und entfernen.
 * 
 * </pre>
 * @author Robin Wagner
 *
 */
public class SmarteLeuchte implements SmartesGeraet {
	
// *************************************************************
// *** Attribute
// *************************************************************
	
	/** Enthält die serialVersionUID. */
	private static final long serialVersionUID = 7215031595691129190L;

	/** Enthält den Namen der smarten Leuchte. */
	protected String name;
	
	/** Enthält den Status, ob die Leuchte leuchtet. */
	protected boolean eingeschaltet;
	
	/** Enthält den Zustand des Schalters, der überprüft wird. */
	protected boolean darfLeuchten;
	
	/** Enthält die Zeiten, in den die smarte Leuchte sich einschaltet. */
	protected List<Ausfuehrungszeit> ausfuehrungszeiten = new ArrayList<>();
	
	/** Enthält den Lauf-Befehl für den Thread der smarten Leuchte. */
	protected boolean threadAmLaufen;
	
	/** Enthält den Schalter, den die Leuchte auf Zustandsänderungen überprüft. */
	protected Schalter schalter;
	
	/** Enthält die Uhrzeit, nach der sich die Ausführungszeiten richten wird. */
	protected transient Uhrzeit uhrzeit;
	
	/** Enthält den Raum, in dem die smarte Leuchte verbaut ist. */
	protected SmarterRaum raum;

// *************************************************************
// *** Konstruktoren
// *************************************************************
	
	/**
	 * Erstellt eine smarte Leuchte mit Namen, Schalter und Uhrzeit.
	 * Die Zustände "eingeschaltet" und "darf leuchten" werden vom Schalter-Zustand übernommen.
	 * @param name Der Name der smarte Leuchte.
	 * @param schalter Der Schalter, auf den die smarte Leuchte "hört".
	 * @param uhrzeit Die Uhrzeit, nach der die Ausführungszeiten sich richten.
	 */
	public SmarteLeuchte(String name, Schalter schalter, Uhrzeit uhrzeit) {
		super();
		this.name = name;
		this.schalter = schalter;
		darfLeuchten = schalter.isZustand();
		eingeschaltet = darfLeuchten;
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
	
	/** Führt die verschiedenen "hören"-Methoden aus. */
	protected void hoeren() {
		hoerenAufSchalter();
		hoerenAufSchaltzeiten();
	}

	/** "Hört" auf den Zustandswechsel des Schalters und macht eine Ausgabe in der Konsole. */
	private void hoerenAufSchalter() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException ausnahme) {
			ausnahme.printStackTrace();
		}
		
		if (schalter.isZustand() != darfLeuchten) {
			umschalten();
			ausgabeDialog();
		} 
	}
	
	/** 
	 * Schaltet die Leuchte auf den nicht vorherrschenden "eingeschaltet"-Zustand um.
	 * Der "darf leuchten"-Zustand bekommt den neuen Schalter-Zustand.
	 */
	private void umschalten() {
		eingeschaltet = !eingeschaltet;
		darfLeuchten = schalter.isZustand();
	}

	/** Gibt Text in der Konsole aus, um den aktuellen "eingeschaltet"-Zustand zu zeigen. */
	public void ausgabeDialog() {
		if (eingeschaltet) {
			System.out.println(uhrzeit.getUhrzeit() + " Uhr" + " - " + raum.getName() + ": " + name + " ist AN geschaltet");
		} else {
			System.out.println(uhrzeit.getUhrzeit() + " Uhr" + " - " + raum.getName() + ": " + name + " ist AUS geschaltet");
		}
	}

	/** "Hört" auf die aktuelle Uhrzeit und schaltet nach Vorgabe der Ausführungszeiten. */
	private void hoerenAufSchaltzeiten() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException ausnahme) {
			ausnahme.printStackTrace();
		}
		
		for (Ausfuehrungszeit ausfuehrungszeit : ausfuehrungszeiten) {
			if (eingeschaltet && ausfuehrungszeit.getEndeZeit() == uhrzeit.getUhrzeit()) {
				umschalten();
				ausgabeDialog();
			} else if (!eingeschaltet && ausfuehrungszeit.getAnfangsZeit() == uhrzeit.getUhrzeit()){
				umschalten();
				ausgabeDialog();
			}
		}
	}
	
	/**
	 * Fügt eine neue Ausführungszeit der smarten Leuchte hinzu.
	 * @param ausfuehrungszeit Die Ausführungszeit, die hinzugefügt wird
	 */
	@Override
	public void addAusfuehrungszeit(Ausfuehrungszeit ausfuehrungszeit) {
		ausfuehrungszeiten.add(ausfuehrungszeit);
	}
	
	/**
	 * Entfernt eine Ausführungszeit von der smarten Leuchte.
	 * @param ausfuehrungszeit Die Ausführungszeit, die entfernt wird
	 */
	@Override
	public void deleteAusfuehrungszeit(Ausfuehrungszeit ausfuehrungszeit) {
		ausfuehrungszeiten.removeIf(t -> t == ausfuehrungszeit);
	}

	/** Gibt einen String wieder, der das SmarteLeuchte-Objekt repräsentiert. */
	@Override
	public String toString() {
		return "SmarteLeuchte [name=" + name + ", zustand=" + darfLeuchten + ", ausfuehrungszeiten=" + ausfuehrungszeiten
				+ ", raum=" + (raum == null ? "Nicht zugewiesen" : raum.getName() ) + "]";
	}
	

// *************************************************************
// *** Methoden wegen Datenkapselung
// *************************************************************

	/**
	 * Gibt den Namen der Leuchte zurück.
	 * @return der Name als String
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Gibt den "eingeschaltet"-Zustand zurück.
	 * @return true, wenn die Leuchte leuchtet
	 */
	public boolean isEingeschaltet() {
		return eingeschaltet;
	}
	
	/**
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
	 * Gibt den Schalter zurück, auf den die Leuchte "hört".
	 * @return Das Schalter-Objekt
	 */
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
	 * Diese Methode haben alle smarten Geräte, gibt hier aber null zurück.
	 * @return null
	 */
	@Override
	public Luftqualitaetsregler getLuftqualitaetsregler() {
		return null;
	}
	
	/**
	 * Gibt den smarten Raum zurück, in dem die Leuchte eingesetz ist.
	 * @return Das SmarterRaum-Objekt
	 */
	@Override
	public SmarterRaum getRaum() {
		return raum;
	}

	/**
	 * Ersetzt den Namen der Leuchte.
	 * @param name Der Name als String der gesetzt wird
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Ersetzt den "Thread am laufen"-Zustand der Leuchte.
	 * @param threadAmLaufen false, wenn der Thread beendet werden soll.
	 */
	public void setThreadAmLaufen(boolean threadAmLaufen) {
		this.threadAmLaufen = threadAmLaufen;
	}
	
	/**
	 * Ersetzt den Schalter, auf den die Leuchte "hört".
	 * @param schalter Das Schalter-Objekt, das gesetzt wird
	 */
	public void setSchalter(Schalter schalter) {
		this.schalter = schalter;
	}

	/**
	 * Ersetzt die Uhrzeit, nach der die Leuchte sich richtet.
	 * @param uhrzeit Das Uhrzeit-Objekt, das gesetzt wird. 
	 */
	@Override
	public void setUhrzeit(Uhrzeit uhrzeit) {
		this.uhrzeit = uhrzeit;
	}

	/**
	 * Ersetzt den smarten Raum, in dem die Leuchte eingesetzt wird.
	 * Leuchte setzt sich in den Raum ein, wenn sie das nicht bereits ist.
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

}

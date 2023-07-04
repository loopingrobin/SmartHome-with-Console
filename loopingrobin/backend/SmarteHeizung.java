package de.loopingrobin.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.loopingrobin.middletier.Uhrzeit;

/**
 * <pre>
 * Die smarte Heizung ist ein smartes Gerät.
 * 
 * Die smarte Heizung hat:
 * 		einen Namen und einen Raum,
 * 		einen "am heizen"- und "darf heizen" -Zustand,
 * 		einen "Thread am Laufen"-Zustand,
 * 		eine Ziel und eine Einstiegstemperatur und
 * 		Ausführungszeiten.
 * 
 * Die smarte Heizung braucht:
 * 		einen Schalter,
 * 		einen Temperaturregler,
 * 		eine Uhrzeit und
 * 		einen Raum.
 * 
 * Die smarte Heizung kann: 
 * 		die Raumtemperatur erhöhen,
 * 		auf die Temperatur "hören",
 * 		auf den Schalter und Temperaturregler "hören" und
 * 		Ausführungszeiten hinzufügen und entfernen.
 * </pre>
 * @author Robin Wagner
 *
 */
public class SmarteHeizung implements SmartesGeraet {
	
// *************************************************************
// *** Attribute
// *************************************************************

	/** Enthält die serialVersionUID. */
	private static final long serialVersionUID = 4526575548212993336L;

	/** Enthält den Namen der smarten Heizung. */
	private String name;
	
	/** Enthält den Heizzustand der smarten Heizung. */
	private boolean amHeizen;
	
	/** Enthält den Zustand der bestimmt, ob die smarte Heizung heizen darf. */
	private boolean darfHeizen;
	
	/** Enthält die Zieltemperatur, bis zu der geheizt wird. */
	private int zieltemperatur;
	
	/** Enthält die Einstiegstemperatur, ab der angefangen wird zu heizen. */
	private int einstiegstemperatur;
	
	/** Enthält die Zeiten, in den die smarte Heizung sich einschaltet. */
	protected List<Ausfuehrungszeit> ausfuehrungszeiten = new ArrayList<>();
	
	/** Enthält den Lauf-Befehl für den Thread der smarten Heizung. */
	protected boolean threadAmLaufen;
	
	/** Enthält den Schalter, der die Heizung "manuell" ein- und ausschaltet. */
	private Schalter schalter;
	
	/** Enthält den Temperaturregler, der die Ziel- und Einstiegstemperatur anpasst. */
	private Temperaturregler regler;
	
	/** Enthält die Uhrzeit, nach der sich die Ausführungszeiten richten wird. */
	protected transient Uhrzeit uhrzeit;
	
	/** Enthält den Raum, in dem die smarte Heizung verbaut ist. */
	protected SmarterRaum raum;


// *************************************************************
// *** Konstruktoren
// *************************************************************
	
	/**
	 * Erstellt eine smarte Heizung mit Namen, Schalter, Regler und Uhrzeit.
	 * Die Zustände "am heizen" und "darf heizen" werden vom Schalter-Zustand übernommen.
	 * Die Werte für Ziel- und Einstiegstemperatur werden vom Temperaturregler übernommen.
	 * @param name Der Name der smarten Heizung.
	 * @param schalter Der Schalter der die smarte Heizung schaltet.
	 * @param regler Der Temperaturregler der die Temperaturen der smarte Heizung regelt.
	 * @param uhrzeit Die Uhrzeit, nach der sich die Ausführzeiten richten.
	 */
	public SmarteHeizung(String name, Schalter schalter, Temperaturregler regler, Uhrzeit uhrzeit) {
		this.name = name;
		this.schalter = schalter;
		darfHeizen = schalter.isZustand();
		amHeizen = darfHeizen;
		this.regler = regler;
		zieltemperatur = regler.getZieltemperatur();
		einstiegstemperatur = regler.getEinstiegstemperatur();
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
	
	/** 
	 * Führt die verschiedenen "hören"-Methoden aus. 
	 * "Hört" nur auf die Ausführungszeiten und die Temperatur wenn die Heizung "heizen darf".
	 * Der Raum wird nur dann geheizt, wenn die Heizung "am heizen" ist.
	 */
	protected void hoeren() {
		hoerenAufSchalter();
		hoerenAufTemperaturregler();
		
		if (darfHeizen) {
			hoerenAufSchaltzeiten();
			hoerenAufTemperatur();
			
			if (amHeizen) {
				raumHeizen();
			}
		}
	}

	/** "Hört" auf den Zustandswechsel des Schalters und macht eine Ausgabe in der Konsole. */
	private void hoerenAufSchalter() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException ausnahme) {
			ausnahme.printStackTrace();
		}
		
		if (schalter.isZustand() == darfHeizen) {
			darfHeizen = !darfHeizen;
			System.out.println(schalter.getName() + " ist " + (darfHeizen ? "AN" : "AUS") + " geschaltet");
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
			if (amHeizen && ausfuehrungszeit.getEndeZeit() == uhrzeit.getUhrzeit()) {
				umschalten();
				ausgabeDialog();
			} else if (!amHeizen && ausfuehrungszeit.getAnfangsZeit() == uhrzeit.getUhrzeit()){
				umschalten();
				ausgabeDialog();
			}
		}
	}
	
	/** Schaltet die Heizung auf den nicht vorherrschenden "am heizen"-Zustand um. */
	private void umschalten() {
		amHeizen = !amHeizen;
	}

	/** Macht eine Ausgabe in der Konsole, um den aktuellen "am heizen"-Zustand zu zeigen. */
	public void ausgabeDialog() {
		if (amHeizen) {
			System.out.println(uhrzeit.getUhrzeit() + " Uhr" + " - " + raum.getName() + ": " + name + " ist AUF HEIZEN geschaltet");
		} else {
			System.out.println(uhrzeit.getUhrzeit() + " Uhr" + " - " + raum.getName() + ": " + name + " ist AUS geschaltet");
		}
	}

	/** 
	 * "Hört" auf die Temperaturwerte des Temperaturreglers und macht eine Ausgabe in der Konsole.
	 * Bei Änderung der Temperaturwerte werden diese übernommen.
	 */
	private void hoerenAufTemperaturregler() {
		if (zieltemperatur != regler.getZieltemperatur() || einstiegstemperatur != regler.getEinstiegstemperatur()) {
			zieltemperatur = regler.getZieltemperatur();
			einstiegstemperatur = regler.getEinstiegstemperatur();
			System.out.println(name + " ändert die Temperaturen zu Ziel:" + zieltemperatur + ", Einstieg: " + einstiegstemperatur);
		}
	}

	/** "Hört" auf die Temperatur des Raums und schaltet nach Vorgabe der Temperaturwerte. */
	private void hoerenAufTemperatur() {
		boolean zuKalt = raum.getRaumTemperatur() < einstiegstemperatur;
		boolean zuWarm = raum.getRaumTemperatur() > zieltemperatur;
		
		if ((zuKalt && !amHeizen) || (zuWarm && amHeizen)) {
			umschalten();
			ausgabeDialog();
		}
	}

	/** Heizt den Raum um 1°C in der Stunde auf. */
	private void raumHeizen() {
		raum.setRaumTemperatur(raum.getRaumTemperatur() + 1);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ausnahme) {
			ausnahme.printStackTrace();
		}
	}

	/**
	 * Fügt eine neue Ausführungszeit der smarten Heizung hinzu.
	 * @param ausfuehrungszeit Die Ausführungszeit, die hinzugefügt wird.
	 */
	@Override
	public void addAusfuehrungszeit(Ausfuehrungszeit ausfuehrungszeit) {
		ausfuehrungszeiten.add(ausfuehrungszeit);
	}

	/**
	 * Entfernt eine Ausführungszeit von der smarten Heizung.
	 * @param ausfuehrungszeit Die Ausführungszeit, die entfernt wird.
	 */
	@Override
	public void deleteAusfuehrungszeit(Ausfuehrungszeit ausfuehrungszeit) {
		ausfuehrungszeiten.removeIf(t -> t == ausfuehrungszeit);
	}

	/** Gibt einen String wieder, der das SmarteHeizung-Objekt repräsentiert. */
	@Override
	public String toString() {
		return "SmarteHeizung [name=" + name + ", amHeizen=" + amHeizen + ", zieltemperatur=" + zieltemperatur
				+ ", einstiegstemperatur=" + einstiegstemperatur + ", ausfuehrungszeiten=" + ausfuehrungszeiten
				+ ", raum=" + (raum == null ? "Nicht zugewiesen" : raum.getName() ) + "]";
	}


// *************************************************************
// *** Methoden wegen Datenkapselung
// *************************************************************

	/**
	 * Gibt den Namen der Heizung zurück.
	 * @return der Name als String
	 */
	@Override
	public String getName() {
		return name;
	}
	
	/**
	 * Gibt den "am heizen"-Zustand zurück.
	 * @return true, wenn die Heizung heizt.
	 */
	public boolean isAmHeizen() {
		return amHeizen;
	}

	/**
	 * Gibt den Wert der Zieltemperatur zurück, bei der die Heizung aufhört zu heizen.
	 * @return Die Zieltemperatur als int
	 */
	public int getZieltemperatur() {
		return zieltemperatur;
	}

	/**
	 * Gibt den Wert der Einstiegstemperatur zurück, bei der die Heizung aufhört zu heizen.
	 * @return Die Einstiegstemperatur als int
	 */
	public int getEinstiegstemperatur() {
		return einstiegstemperatur;
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
	 * Gibt den Schalter zurück, auf den die Heizung "hört".
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
	 * Gibt den Temperaturregler zurück, auf den die Heizung "hört".
	 * @return regler Das Temperaturregler-Objekt
	 */
	@Override
	public Temperaturregler getTemperaturregler() {
		return regler;
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
	 * Gibt den smarten Raum zurück, in dem die Heizung eingesetz ist.
	 * @return Das SmarterRaum-Objekt
	 */
	public SmarterRaum getRaum() {
		return raum;
	}

	/**
	 * Ersetzt den Namen der smarten Heizung.
	 * @param name Der Name als String der gesetzt wird
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Ersetzt den "Thread am laufen"-Zustand der Heizung.
	 * @param threadAmLaufen false, wenn der Thread beendet werden soll.
	 */
	public void setThreadAmLaufen(boolean threadAmLaufen) {
		this.threadAmLaufen = threadAmLaufen;
	}

	/**
	 * Ersetzt den Schalter, auf den die Heizung "hört".
	 * @param schalter Das Schalter-Objekt, das gesetzt wird
	 */
	public void setSchalter(Schalter schalter) {
		this.schalter = schalter;
	}

	/**
	 * Ersetzt den Temperaturregler, auf den die Heizung "hört".
	 * @param regler Das Temperaturregler-Objekt, das gesetzt wird
	 */
	public void setRegler(Temperaturregler regler) {
		this.regler = regler;
	}

	/**
	 * Ersetzt die Uhrzeit, nach der die Heizung sich richtet.
	 * @param uhrzeit Das Uhrzeit-Objekt, das gesetzt wird. 
	 */
	@Override
	public void setUhrzeit(Uhrzeit uhrzeit) {
		this.uhrzeit = uhrzeit;
	}

	/**
	 * Ersetzt den smarten Raum, in dem die Heizung eingesetzt wird.
	 * Heizung setzt sich in den Raum ein, wenn sie das nicht bereits ist.
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

package de.loopingrobin.frontend;

import java.awt.Color;
import java.time.LocalTime;
import java.util.Map;
import java.util.Scanner;

import de.loopingrobin.backend.Ausfuehrungszeit;
import de.loopingrobin.backend.SmarteHeizung;
import de.loopingrobin.backend.SmarteRgbLeuchte;
import de.loopingrobin.backend.SmarterRaum;
import de.loopingrobin.backend.SmartesFenster;
import de.loopingrobin.backend.SmartesGeraet;
import de.loopingrobin.middletier.SmartHome;

/**
 * <pre>
 * Das KonsolenUi ist ein Frontend für das SmartHome.
 * 
 * Wird aus der main()-Methode gestartet.
 * </pre>
 * @author Robin Wagner
 *
 */
public class KonsolenUi {
	
// *************************************************************
// *** Attribute
// *************************************************************
	
	/** Enthält das Smart-Home System. */
	private SmartHome jarvis;

	/** Enthält den Leser für die Text-Tastatureingabe. */
	Scanner leserText = new Scanner(System.in);


// *************************************************************
// *** Konstruktoren
// *************************************************************
	
	/**
	 * Erstellt das KonsolenUi-Objekt und verknüpft es mit dem Smart-Home System. 
	 * @param jarvis Das Smart-Home System.
	 */
	public KonsolenUi(SmartHome jarvis) {
		this.jarvis = jarvis;
	}


// *************************************************************
// *** Methoden
// *************************************************************
	
	/** Startet die Interaktion. */
	public void launch() {
		System.out.println("**** Willkommen, dein smartes Home liegt dir zu Füßen ****");
		navigieren();
	}

	/** Startet die Navigations-Schleife mit der Raumauswahl (Rekursion). */
	private void navigieren() {
		
		Map<Integer, SmarterRaum> raeume = jarvis.getSmarteRaeume();
		
		System.out.println("\n---- Smart-Home Übersicht ----");
		System.out.println("1: neuen Raum hinzufügen");
		System.out.println("5: Programm beenden\n");
		System.out.println("Raum auswählen:");
		for (Map.Entry<Integer, SmarterRaum> raum : raeume.entrySet()) {
			System.out.println(raum.getKey() + ": Raum " + raum.getValue().getName());
		}
		System.out.println("---------------------------------\n");
		
		int auswahl = eingabeZahlEntgegennehmen();
		
		if (auswahl == 1) {
			raumHinzufuegen();
		} else if (auswahl == 5) {
			System.out.println("---- Auf Wiedersehen ----");
			jarvis.speichern();
			System.exit(-1);
		} else if (raeume.containsKey(auswahl)) {
			
			try {
				raumOptionen(raeume.getOrDefault(auswahl, null));
			} catch (AbbruchException ausnahme) {
				System.out.println(ausnahme.getMessage());
				navigieren();
			}
		} else {
			System.out.println("\nFalsche Eingabe! Bitte nochmal\n");
		}
		
		navigieren();
	}


	/**
	 * Navigationsmenü für die Raum-Optionen.
	 * @param raum Der verwaltete Raum.
	 * @throws AbbruchException Die Abbruch-Exception
	 */
	private void raumOptionen(SmarterRaum raum) throws AbbruchException {
		
		Map<Integer, SmartesGeraet> geraete = raum.getSmarteGeraete();
		
		System.out.println("\n---- " + raum.getName() + ": Gerät auswählen ----");
		System.out.println("1: den Raum " + raum.getName() + " entfernen");
		System.out.println("5: neues Gerät hinzufügen\n");
		System.out.println("Gerät auswählen:");
		for (Map.Entry<Integer, SmartesGeraet> geraet : geraete.entrySet()) {
			System.out.println(geraet.getKey() + ": " + geraet.getValue().getName());			
		}
		System.out.println("\nAbbruch mit jedem anderen Zeichen");
		System.out.println("---------------------------------\n");
		
		int auswahl = eingabeZahlEntgegennehmen();
		if (auswahl == 1) {
			raumEntfernen(raum);
		} else if (auswahl == 5) {
			geraetHinzufuegen(raum);
		} else if (geraete.containsKey(auswahl)) {
			geraeteOptionen(geraete.getOrDefault(auswahl, null), raum);
		} else {
			throw new AbbruchException("ABBRUCH! Zurück ins Hauptmenü");
		}

	}


	/**
	 * Navigationsmenü für die Geräte-Optionen.
	 * @param geraet Das verwaltete Gerät.
	 * @param raum Der betrachtete Raum
	 * @throws AbbruchException Die Abbruch-Exception
	 */
	private void geraeteOptionen(SmartesGeraet geraet, SmarterRaum raum) throws AbbruchException {
		
		Map<Integer, Ausfuehrungszeit> ausfuehrungszeiten = geraet.getAusfuehrungszeiten();

		System.out.println("\n---- Verwaltung von " + geraet.getName() + " ----");
		System.out.println("1: das Gerät " + geraet.getName() + " entfernen");
		System.out.println("2: Gerät " + geraet.getName() + " einstellen");
		System.out.println("5: neue Ausführungszeit hinzufügen\n");
		System.out.println("Ausführungszeit auswählen:");
		for (Map.Entry<Integer, Ausfuehrungszeit> ausfuehrungszeit : ausfuehrungszeiten.entrySet()) {
			System.out.println(ausfuehrungszeit.getKey() + ": " + ausfuehrungszeit.getValue());
		}
		System.out.println("\nAbbruch mit jedem anderen Zeichen");
		System.out.println("---------------------------------\n");
		
		int auswahl = eingabeZahlEntgegennehmen();
		if (auswahl == 1) { //Gerät entfernen.
			geraetEntfernen(geraet, raum);
		} else if (auswahl == 2) { //Gerät einstellen.
			geraetEinstellen(geraet);
		} else if (auswahl == 5) { //Ausführungszeit hinzufügen.
			ausfuehrungszeitHinzufuegen(geraet);
		} else if (ausfuehrungszeiten.containsKey(auswahl)) {
			ausfuehrungszeitenOptionen(ausfuehrungszeiten.getOrDefault(auswahl, null), geraet);
		} else {
			throw new AbbruchException("ABBRUCH! Zurück ins Hauptmenü");
		}
	}

	/**
	 * Navigationsmenü für die Ausführungszeiten-Optionen.
	 * @param ausfuehrungszeit Die verwaltete Ausführungszeit.
	 * @param geraet Das betrachtete Gerät
	 * @throws AbbruchException Die Abbruch-Exception
	 */
	private void ausfuehrungszeitenOptionen(Ausfuehrungszeit ausfuehrungszeit, SmartesGeraet geraet) throws AbbruchException {
		
		System.out.println("\n---- Verwaltung von " + ausfuehrungszeit + " ----");
		System.out.println("1: die Ausführungszeit " + ausfuehrungszeit + " entfernen");
		System.out.println("\nAbbruch mit jedem anderen Zeichen");
		System.out.println("---------------------------------\n");
		
		int auswahl = eingabeZahlEntgegennehmen();
		if (auswahl == 1) { //Ausführungszeit entfernen.
			ausfuehrungszeitEntfernen(ausfuehrungszeit, geraet);
		} else {
			throw new AbbruchException("ABBRUCH! Zurück ins Hauptmenü");
		}
	}


	/** Fügt einen neuen smarten Raum dem Smart-Home System hinzu. */
	private void raumHinzufuegen() {
		System.out.println("\n---- Neuen Raum hinzufügen ----");
		System.out.println("Bitte den Namen eingeben:");
		System.out.println("---------------------------------\n");
		
		String name = leserText.nextLine();
		SmarterRaum raum = new SmarterRaum(name);
		jarvis.addRaum(raum);
		
		navigieren();
	}

	/** 
	 * Entfernt einen smarten Raum aus dem Smart-Home System.
	 * @param raum Der zu entfernende Raum
	 */
	private void raumEntfernen(SmarterRaum raum) {
		
		Map<Integer, SmartesGeraet> geraete = raum.getSmarteGeraete();
		
		for (Map.Entry<Integer, SmartesGeraet> geraet : geraete.entrySet()) {
			raum.deleteSmartesGeraet(geraet.getValue());
			jarvis.deleteSmartesGeraet(geraet.getValue(), raum);
		}
		
		jarvis.deleteRaum(raum);
	}


	/**
	 * Fügt ein neues smartes Gerät einem Raum hinzu. 
	 * @param raum Der hinzuzufügende Raum
	 * @throws AbbruchException Die Abbruch-Exception
	 */
	private void geraetHinzufuegen(SmarterRaum raum) throws AbbruchException {
		
		Map<Integer, String> geraeteTypen = jarvis.getGeraeteTypen();

		System.out.println("\n---- Welcher Gerättyp soll hinzugefügt werden? ----");
		for (Map.Entry<Integer, String> geraeteTyp : geraeteTypen.entrySet()) {
			System.out.println(geraeteTyp.getKey() + ": " + geraeteTyp.getValue());
		}
		System.out.println("---------------------------------\n");
		
		int auswahlTyp = eingabeZahlEntgegennehmen();
		if (geraeteTypen.containsKey(auswahlTyp)) {

			System.out.println("\n---- " + geraeteTypen.getOrDefault(auswahlTyp, null)
							+ " Raum " + raum.getName() + " hinzufügen ----");
			System.out.println("Bitte den Namen eingeben:");
			System.out.println("---------------------------------\n");
			
			String name = leserText.nextLine();
			jarvis.addSmartesGeraet(geraeteTypen.getOrDefault(auswahlTyp, null), name, raum);
			
			navigieren();
			
		} else {
			throw new AbbruchException("ABBRUCH! Zurück ins Hauptmenü");
		}
	}

	/** 
	 * Führt durch die Einstellungsmöglichkeiten des smarten Geräts.
	 * @param geraet Das betrachtete Gerät
	 * @throws AbbruchException Die Abbruch-Exception
	 */
	private void geraetEinstellen(SmartesGeraet geraet) throws AbbruchException {
		System.out.println("\n---- Einstellung von " + geraet.getName() + " ----");
		System.out.println("1: Umschalten");
		if (geraet instanceof SmarteRgbLeuchte) {
			System.out.println("2: Farbe wechseln");
		}
		if (geraet instanceof SmarteHeizung) {
			System.out.println("3: Heizzeiten einstellen");
		}
		if (geraet instanceof SmartesFenster) {
			System.out.println("4: Lüftungszeiten einstellen");
		}
		System.out.println("---------------------------------\n");
		
		int auswahl = eingabeZahlEntgegennehmen();
		if (auswahl == 1) {
			geraet.getSchalter().schalten();
			
		} else if (geraet instanceof SmarteRgbLeuchte && auswahl == 2) {
			System.out.println("\n---- Farbe auswählen für " + geraet.getName() + " ----");
			System.out.println("1: Weiß");
			System.out.println("2: Gelb");
			System.out.println("3: Grün");
			System.out.println("4: Rot");
			System.out.println("---------------------------------\n");
			
			auswahl = eingabeZahlEntgegennehmen();
			Color farbe = null;
			if (auswahl == 1) {
				farbe = Color.WHITE;
			} else if (auswahl == 2) {
				farbe = Color.YELLOW;
			} else if (auswahl == 3) {
				farbe = Color.GREEN;
			} else if (auswahl == 4) {
				farbe = Color.RED;
			} else {
				throw new AbbruchException("ABBRUCH! Zurück ins Hauptmenü");
			}
			geraet.getFarbregler().setFarbe(farbe);
			
		} else if (geraet instanceof SmarteHeizung && auswahl == 3) {
			System.out.println("\n---- " + geraet.getName() + ": Zieltemperatur ----");
			System.out.println("Bitte die Zieltemperatur eingeben:");
			System.out.println("---------------------------------\n");
			
			int zieltemperatur = eingabeZahlEntgegennehmen();

			System.out.println("\n---- " + geraet.getName() + ": Einstiegstemperatur ----");
			System.out.println("Bitte die Einstiegstemperatur eingeben:");
			System.out.println("---------------------------------\n");
			
			int einstiegstemperatur = eingabeZahlEntgegennehmen();
			
			geraet.getTemperaturregler().setZieltemperatur(zieltemperatur);
			geraet.getTemperaturregler().setEinstiegstemperatur(einstiegstemperatur);
			
		} else if (geraet instanceof SmartesFenster && auswahl == 4) {
			System.out.println("\n---- " + geraet.getName() + ": Ziel-CO2-Gehalt ----");
			System.out.println("Bitte den Zeil-CO2-Gehalt eingeben:");
			System.out.println("---------------------------------\n");
			
			int zielKohlenstoffDioxidGehalt = eingabeZahlEntgegennehmen();

			System.out.println("\n---- " + geraet.getName() + ": Einstiegs-CO2-Gehalt ----");
			System.out.println("Bitte den Einstiegs-CO2-Gehalt eingeben:");
			System.out.println("---------------------------------\n");
			
			int einstiegKohlenstoffDioxidGehalt = eingabeZahlEntgegennehmen();
			
			geraet.getLuftqualitaetsregler().setZielKohlenstoffDioxidGehalt(zielKohlenstoffDioxidGehalt);
			geraet.getLuftqualitaetsregler().setEinstiegsKohlenstoffDioxidGehalt(einstiegKohlenstoffDioxidGehalt);
			
		} else {
			throw new AbbruchException("ABBRUCH! Zurück ins Hauptmenü");
		}
	}

	/** 
	 * Entfernt ein smartes Gerät aus einem Raum.
	 * @param geraet Das zu entfernende Gerät
	 * @param raum Der Raum, aus dem das Gerät genommen werden soll.
	 */
	private void geraetEntfernen(SmartesGeraet geraet, SmarterRaum raum) {
		jarvis.deleteSmartesGeraet(geraet, raum);
	}

	/** 
	 * Fügt eine neues Ausführungszeit einem smarten Gerät hinzu.
	 * @param geraet Das hinzuzufügende Gerät
	 */
	private void ausfuehrungszeitHinzufuegen(SmartesGeraet geraet) {
		System.out.println("\n---- " + geraet.getName() + ": Einschaltzeitpunkt ----");
		System.out.println("Bitte die Einschaltzeit eingeben:");
		System.out.println("---------------------------------\n");
		
		int einschalten = eingabeStundeEntgegennehmen();
		LocalTime einschaltzeit = LocalTime.of(einschalten, 0);

		System.out.println("\n---- " + geraet.getName() + ": Ausschaltzeitpunkt ----");
		System.out.println("Bitte die Ausschaltzeit eingeben:");
		System.out.println("---------------------------------\n");
		
		int ausschalten = eingabeStundeEntgegennehmen();
		LocalTime ausschaltzeit = LocalTime.of(ausschalten, 0);
		
		Ausfuehrungszeit ausfuehrungszeit = new Ausfuehrungszeit(einschaltzeit, ausschaltzeit);
		geraet.addAusfuehrungszeit(ausfuehrungszeit);
		
	}

	/** 
	 * Entfernt eine Ausführungszeit eines smarten Geräts.
	 * @param ausfuehrungszeit Die zu entfernende Ausführungszeit
	 * @param geraet Das Gerät, aus dem die Ausführungszeit entfernd wird
	 */
	private void ausfuehrungszeitEntfernen(Ausfuehrungszeit ausfuehrungszeit, SmartesGeraet geraet) {
		geraet.deleteAusfuehrungszeit(ausfuehrungszeit);
	}
	
	/**
	 * Nimmt die Eingabe des Anwenders entgegen und gibt sie zurück.
	 * Prüft ob die Eingabe eine Zahl ist.
	 * @return Die Eingabe.
	 */
	@SuppressWarnings("resource")
	private int eingabeZahlEntgegennehmen() {
		int rueckgabe = 0;
		Scanner leser = new Scanner(System.in);
		while (!leser.hasNextInt()) {
			System.out.println("Bitte eine ZAHL eingeben!");
			leser.next();
		}
		rueckgabe = leser.nextInt();
		return rueckgabe;
	}
	
	/**
	 * Nimmt die Eingabe einer Stundenangabe vom Anwender entgegen und gibt sie zurück.
	 * Prüft ob die Eingabe zwischen 0 und 23 ist.
	 * @return Die Eingabe der Stunde
	 */
	@SuppressWarnings("resource")
	private int eingabeStundeEntgegennehmen() {
		int rueckgabe = 0;
		Scanner leser = new Scanner(System.in);
		
		while (true) {
			if (leser.hasNextInt()) {
				rueckgabe = leser.nextInt();
				if (rueckgabe < 24 && rueckgabe >= 0) {
					System.out.println("Passt");
					return rueckgabe;
				}
			}
			System.out.println("Bitte eine ZAHL eingeben, die zwischen 0 und 24 liegt!");
			leser.next();
		}
	}

}

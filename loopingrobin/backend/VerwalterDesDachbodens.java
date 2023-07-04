package de.loopingrobin.backend;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;

/**
 * <pre>
 * Die VerwalterDesDachbodens-Klasse ist eine Util-Klasse und hat nur statische Methoden.
 * 
 * Die Klasse kann ein gespeichertes Dachboden-Objekt aus dem Dateilsystem laden.
 * Die Klasse kann ein Dachboden-Objekt ins Dateisystem speichern.
 * </pre>
 * @author Robin Wagner
 *
 */
public class VerwalterDesDachbodens {

// *************************************************************
// *** Konstruktoren
// *************************************************************
	
	/**
	 * Die Klasse braucht kein Objekt zu erstellen.
	 */
	private VerwalterDesDachbodens() {}

// *************************************************************
// *** Methoden
// *************************************************************
	
	/**
	 * Liest die Properties-Datei und holt den Wert für den mitgegebenen Schlüssel.
	 * @param key Der Schlüssel für den gesuchten Wert.
	 * @return ein File mit dem relativen Dateipfad.
	 */
	private static File propertiesLesen(String key) {
		File rueckgabe = null;
		Properties properties = new Properties();
		
		try(BufferedInputStream stream = new BufferedInputStream(new FileInputStream("dachboden.properties"))) {
			
			properties.load(stream);
			rueckgabe = new File(properties.getProperty(key)); 
			
		} catch (IOException ausnahme) {
			ausnahme.printStackTrace();
		}
		
		return rueckgabe;
	}
	
	/**
	 * Liest den Dachboden aus dem Dateisystem.
	 * @return das Dachboden-Objekt.
	 */
	public static Dachboden ausDateiLesen() {
		Dachboden rueckgabe = null;
		File speicherDatei = propertiesLesen("objekt");
		
		if (speicherDatei.exists()) {

			try(ObjectInputStream leser = new ObjectInputStream(new FileInputStream(speicherDatei))) {
				
				Object gelesenesObjekt = leser.readObject();
				rueckgabe = (Dachboden) gelesenesObjekt;
				
			} catch (IOException | ClassNotFoundException ausnahme) {
				ausnahme.printStackTrace();
			}
		} else {
			rueckgabe = new Dachboden();
		}
		
		return rueckgabe;
	}

	/**
	 * Schreibt den Dachboden ins Dateisystem.
	 * @param dachboden Das ausgelesene Dachboden-Objekt.
	 */
	public static void inDateiSchreiben(Dachboden dachboden) {
		File speicherDatei = propertiesLesen("objekt");
		File ordner = propertiesLesen("ordner");
		
		if (!ordner.exists()) {
			ordner.mkdir();
		}

		try (ObjectOutputStream schreiber = new ObjectOutputStream(new FileOutputStream(speicherDatei))){
			
			schreiber.writeObject(dachboden);
			
		} catch (IOException ausnahme) {
			ausnahme.printStackTrace();
		}
	}

}

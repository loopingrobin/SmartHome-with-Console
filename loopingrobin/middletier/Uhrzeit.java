package de.loopingrobin.middletier;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * <pre>
 * Die Uhrzeit ist runnable und serialisierbar.
 * 
 * Die Uhrzeit hat eine Uhrzeit die sie für smarte Geräte vorgibt.
 * </pre>
 * @author Robin Wagner
 *
 */
public class Uhrzeit implements Runnable, Serializable {
// *************************************************************
// *** Attribute
// *************************************************************
	
	/** Enthält die serialVersionUID. */
	private static final long serialVersionUID = 6742011311947648021L;
	
	/** Enthält die Uhrzeit. */
	private LocalTime uhrzeit;


// *************************************************************
// *** Konstruktoren
// *************************************************************
	
	/** Erstellt ein Objekt, mit der voreingestellten Uhrzeit 00:00 Uhr. */
	public Uhrzeit() {
		uhrzeit = LocalTime.of(0,  0);
	}


// *************************************************************
// *** Methoden
// *************************************************************
	
	/**
	 * Der Thread wird bis zum Ende des Programms ausgeführt.
	 * Pro Sekunde wird eine Stunde erhöht.
	 */
	@Override
	public void run() {
		while(true) {
			for (int i = 0; i < 24; i++) {
				uhrzeit = LocalTime.of(i, 0);
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ausnahme) {
					ausnahme.printStackTrace();
				}
			}
		}
	}


// *************************************************************
// *** Methoden wegen Datenkapselung
// *************************************************************

	/**
	 * @return the uhrzeit
	 */
	public LocalTime getUhrzeit() {
		return uhrzeit;
	}

	/**
	 * @param uhrzeit the uhrzeit to set
	 */
	public void setUhrzeit(LocalTime uhrzeit) {
		this.uhrzeit = uhrzeit;
	}

}

package de.loopingrobin.main;

import de.loopingrobin.frontend.KonsolenUi;
import de.loopingrobin.middletier.SmartHome;

/**
 * <pre>
 * Die Main-Klasse führt das Smart-Home System aus.
 * </pre>
 * @author Robin Wagner
 * 
 */
public class Main {

	/** 
	 * Die ausführende main()-Methode.
	 * @param args Main-Parameter (ungenutzt)
	 */
	public static void main(String[] args) {
		System.out.println("ABS");
		SmartHome smartHome = new SmartHome();
		
		KonsolenUi jarvis = new KonsolenUi(smartHome);
		jarvis.launch();
	}

}

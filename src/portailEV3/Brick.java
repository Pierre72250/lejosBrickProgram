package portailEV3;

import ca.ualberta.awhittle.ev3btrc.MessageBluetooth;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;
import portailEV3.enumeration.EtatPortail;
import portailEV3.hardware.CapteurContact;
import portailEV3.hardware.CapteurPresence;
import portailEV3.hardware.Porte;
import portailEV3.log.LogEV3;
import portailEV3.runnable.MoteurRunnable;
import portailEV3.runnable.PortailRunnable;
import portailEV3.runnable.SoundRunnable;


public class Brick{

	private static MessageBluetooth msgTelecommande;
	private static boolean app_alive;
	
	private static CapteurContact capteurGaucheOuvert = new CapteurContact(SensorPort.S1);
	private static CapteurContact capteurDroitOuvert = new CapteurContact(SensorPort.S2);
	private static CapteurContact capteurPortailFerme = new CapteurContact(SensorPort.S3);

	private static CapteurPresence capteurPresence = new CapteurPresence(SensorPort.S4);

	private static Porte porteGauche = new Porte(MotorPort.A);
	private static Porte porteDroite = new Porte(MotorPort.B);

	private static MoteurRunnable moteurDroit;
	private static MoteurRunnable moteurGauche;
	private static PortailRunnable portail;
	
	private static SoundRunnable sound;
	
	public static void main(String[] args) throws InterruptedException{
		EcouteBT EBT = new EcouteBT();
		
		sound = new SoundRunnable();
		new Thread(sound).start();
		
		moteurDroit = new MoteurRunnable(capteurDroitOuvert, capteurPortailFerme, porteDroite, capteurPresence, sound, "droit");
		moteurGauche = new MoteurRunnable(capteurGaucheOuvert, capteurPortailFerme, porteGauche, capteurPresence, sound, "gauche");
		
		// INITIALISATION DES THREADS
		new Thread(moteurGauche).start();
		new Thread(moteurDroit).start();
		
		portail = new PortailRunnable(moteurDroit, moteurGauche);
		new Thread(portail).start();
		
		System.out.println("Début de l'initialisation");
		
		// Initialisation du portail
		moteurDroit.setAction(1);
		moteurGauche.setAction(1);
		moteurDroit.resumeThread();
		moteurGauche.resumeThread();
		
		while (moteurDroit.getIsRunning() || moteurGauche.getIsRunning()) {
			Thread.sleep(1000);
		}
		
		if (portail.getEtatPortail() == EtatPortail.OUVERT) {
			moteurDroit.setAction(2);
			moteurGauche.setAction(2);
			moteurDroit.resumeThread();
			moteurGauche.resumeThread();
		}
		
		while (moteurDroit.getIsRunning() || moteurGauche.getIsRunning()) {
			Thread.sleep(1000);
		}
		
		if (!capteurPortailFerme.contact()) {
			LogEV3.addError("Erreur lors de l'initialisation");
			LCD.clear();
			LCD.drawString("Erreur lors de l'initialisation", 0, 5);
			Delay.msDelay(5000);
			LCD.clear();
			LCD.refresh();
		} else {
			
			System.out.println("Succes de l'initialisation");
			
			EBT.start();
			app_alive = true;
			
			int codeTelecommandePrecedent = 0;
			MessageBluetooth msgTelecommandePrecedent = null;
			
			while(app_alive){
				
				msgTelecommande = EBT.msg;
				if (msgTelecommande != null && msgTelecommande != msgTelecommandePrecedent) {
					msgTelecommandePrecedent = msgTelecommande;
					
					switch(msgTelecommande.getCommande()){
					
						case "commande":
							
							switch(msgTelecommande.getParams().get(0)){
								case "ouvTotale":
									portail.majEtatPortail();
									ouvertureTotale();
									break;
								case "ouvPartielle":
									portail.majEtatPortail();
									ouverturePartielle();
									break;
								default:
									break;
							}
							
						case "connexion":
							System.out.println(msgTelecommande.getParams().get(0) + " " + msgTelecommande.getParams().get(0));
						default:
							break;
							
					}
					
				}
			}
		}
	}

	public static void ouvertureTotale() {
		switch (portail.getEtatPortail()) {
			case FERME:
				moteurDroit.setAction(1);
				moteurGauche.setAction(1);
				moteurDroit.resumeThread();
				moteurGauche.resumeThread();
				break;
			case ARRETE_EN_FERMETURE:
				moteurDroit.setAction(1);
				moteurGauche.setAction(1);
				moteurDroit.resumeThread();
				moteurGauche.resumeThread();
				break;
			case OUVERT:
				moteurDroit.setAction(2);
				moteurGauche.setAction(2);
				moteurDroit.resumeThread();
				moteurGauche.resumeThread();
				break;
			case ARRETE_EN_OUVERTURE:
				moteurDroit.setAction(2);
				moteurGauche.setAction(2);
				moteurDroit.resumeThread();
				moteurGauche.resumeThread();
				break;
			case OUVERT_PARTIELLEMENT:
				moteurGauche.setAction(2);
				moteurGauche.resumeThread();
				break;
			case EN_OUVERTURE_PARTIELLE:
				moteurDroit.stopThread();
				moteurGauche.stopThread();
				break;
			case EN_OUVERTURE_TOTALE:
				moteurDroit.stopThread();
				moteurGauche.stopThread();
				break;
			case EN_FERMETURE_TOTALE:
				moteurDroit.stopThread();
				moteurGauche.stopThread();
				break;
			case EN_FERMETURE_PARTIELLE:
				moteurGauche.stopThread();
				break;
			default:
				break;
		}
	}
	
	public static void ouverturePartielle() {
		switch (portail.getEtatPortail()) {
			case FERME:
				moteurGauche.setAction(1);
				moteurGauche.resumeThread();
				break;
			case ARRETE_EN_FERMETURE:
				moteurDroit.setAction(2);
				moteurGauche.setAction(2);
				moteurDroit.resumeThread();
				moteurGauche.resumeThread();
				break;
			case OUVERT:
				moteurDroit.setAction(2);
				moteurGauche.setAction(2);
				moteurDroit.resumeThread();
				moteurGauche.resumeThread();
				break;
			case ARRETE_EN_OUVERTURE:
				moteurDroit.setAction(2);
				moteurGauche.setAction(2);
				moteurDroit.resumeThread();
				moteurGauche.resumeThread();
				break;
			case OUVERT_PARTIELLEMENT:
				moteurGauche.setAction(2);
				moteurGauche.resumeThread();
				break;
			case EN_OUVERTURE_TOTALE:
				moteurDroit.stopThread();
				moteurGauche.stopThread();
				break;
			case EN_OUVERTURE_PARTIELLE:
				moteurDroit.stopThread();
				moteurGauche.stopThread();
				break;
			case EN_FERMETURE_TOTALE:
				moteurDroit.stopThread();
				moteurGauche.stopThread();
				break;
			case EN_FERMETURE_PARTIELLE:
				moteurGauche.stopThread();
				break;
			default:
				break;
		}
	}
	
}
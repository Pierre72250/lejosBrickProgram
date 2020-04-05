package portailEV3;

import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;


public class Brick{

	private static int codeTelecommade = 0;
	private static boolean app_alive;
	
	private static CapteurContact capteurGaucheOuvert = new CapteurContact(SensorPort.S1);
	private static CapteurContact capteurDroitOuvert = new CapteurContact(SensorPort.S2);
	private static CapteurContact capteurPortailFerme = new CapteurContact(SensorPort.S3);

	//private static CapteurPresence capteurPresence = new CapteurP(SensorPort.S4);

	private static Porte porteGauche = new Porte(MotorPort.A);
	private static Porte porteDroite = new Porte(MotorPort.B);

	private static Etat etatPortail;

	private static MoteurRunnable moteurDroit;
	private static MoteurRunnable moteurGauche;
	
	public static void main(String[] args){
		etatPortail = Etat.valueOf("INCONNU");
		EcouteBT EBT = new EcouteBT();
		
		etatPortail = Etat.FERME;
		
		moteurDroit = new MoteurRunnable(capteurDroitOuvert, capteurPortailFerme, porteDroite);
		moteurGauche = new MoteurRunnable(capteurGaucheOuvert, capteurPortailFerme, porteGauche);
		new Thread(moteurDroit).start();
		new Thread(moteurGauche).start();
		
		EBT.start();
		app_alive = true;
		while(app_alive){
			codeTelecommade = EBT.byteRecu;
			switch(codeTelecommade){
				// Forwards
				case 1:
					ouverturePartielle();
					break;

					// Backwards
				case 2: 
					ouvertureTotale();
					break;		
			}
		}
	}
	
	public static void ouvertureTotale() {
		System.out.println("Etat = " + etatPortail.name());
		switch (etatPortail) {
			case FERME:
				System.out.println("Le  portail est ferme");
				moteurDroit.setAction(1);
				moteurGauche.setAction(1);
				
				moteurDroit.resumeThread();
				moteurGauche.resumeThread();
				etatPortail = Etat.OUVERT;
				break;
				
			case OUVERT:
				moteurDroit.setAction(2);
				moteurGauche.setAction(2);
				
				moteurDroit.resumeThread();
				moteurGauche.resumeThread();
				etatPortail = Etat.FERME;
				break;
				
			default:
				break;
		}
	}
	
	public static void ouverturePartielle() {
		switch (etatPortail) {
			case FERME:
				moteurGauche.setAction(1);
				
				moteurGauche.resumeThread();
				etatPortail = Etat.OUVERT_PARTIELLE;
				break;
				
			case OUVERT:
				moteurGauche.setAction(2);
				
				moteurGauche.resumeThread();
				etatPortail = Etat.FERME;
				break;
				
			default:
				break;
		}
	}
	
}
package portailEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;


public class Brick{

	private static int codeTelecommade = 0;
	private static boolean app_alive;
	
	private static CapteurContact capteurGaucheOuvert = new CapteurContact(SensorPort.S1);
	private static CapteurContact capteurPortailFerme = new CapteurContact(SensorPort.S3);

	//private static CapteurPresence capteurPresence = new CapteurP(SensorPort.S4);

	private static Porte porteGauche = new Porte(MotorPort.A);
	private static Porte porteDroite = new Porte(MotorPort.B);

	private static Etat etatPortail;

	public static void main(String[] args) throws InterruptedException{
		
		etatPortail = Etat.valueOf("INCONNU");

		
		EcouteBT EBT = new EcouteBT();
		
	
		initialisation();
		

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
	
	


	
	

	@SuppressWarnings("unlikely-arg-type")
	public static void initialisation() {
		
	
		ouvertureGauche();
		fermetureP();
	
		if (capteurPortailFerme.contact()) {
			etatPortail = Etat.valueOf("FERME");
		} 
		// Si le capteur de postion ferm�e du portail ne detecte pas de contact alors il y a une erreur
		else {
			LCD.clear();
			LCD.drawString("Erreur lors de l'initialisation", 0, 5);
				Delay.msDelay(5000);
				LCD.clear();
				LCD.refresh();
		}
	
	}
	
	public static void ouvertureTotale() {
	
		if (etatPortail.name().equals("FERME")) {
			while(!capteurGaucheOuvert.contact() /*&& !capteurDroitOuvert.contact()*/) {
				System.out.println("En ouverture totale.");
				porteDroite.ouvrir();
				porteGauche.ouvrir();
			}
			porteDroite.stop(true);
			porteGauche.stop(true);
			etatPortail = Etat.valueOf("OUVERT");
		}else if(etatPortail.name().equals("OUVERT")) {
			System.out.println("En fermeture.");
			fermetureT();
			etatPortail = Etat.valueOf("FERME");
		}else if(etatPortail.name().equals("OUVERT_PARTIELLE")) {
			System.out.println("En fermeture.");
			fermetureP();
			etatPortail = Etat.valueOf("FERME");
		}
	}
	
	public static void ouverturePartielle() {

		if ( etatPortail.name().equals("FERME")) {
			while(!capteurGaucheOuvert.contact()) {
				System.out.println("En ouverture partielle.");
				porteGauche.ouvrir();
			}
			porteGauche.stop(true);
			etatPortail = Etat.valueOf("OUVERT_PARTIELLE");
		}
		else if (etatPortail.name().equals("OUVERT_PARTIELLE")) {
			System.out.println("En fermeture.");
			fermetureP();
			etatPortail = Etat.valueOf("FERME");
		}else if (etatPortail.name().equals("OUVERT")) {
			System.out.println("En fermeture.");
			fermetureT();
			etatPortail = Etat.valueOf("FERME");
		}
		
	}
	
	public static void ouvertureGauche() {
		
		while(!capteurGaucheOuvert.contact()) {
			porteGauche.ouvrir();
		}
		porteGauche.stop(true);
			
	}
	
	public static void fermetureP() {
		while (!capteurPortailFerme.contact()) {
			porteGauche.fermer();
		}
		porteGauche.stop(false);
	}
	
	public static void fermetureT() {
		while (!capteurPortailFerme.contact()) {
			porteGauche.fermer();
			porteDroite.fermer();
		}
		porteGauche.stop(false);
		porteDroite.stop(false);
	}


}
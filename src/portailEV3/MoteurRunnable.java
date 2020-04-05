package portailEV3;

import lejos.hardware.Sound;
import lejos.hardware.port.SensorPort;

public class MoteurRunnable implements Runnable {
	
	private static CapteurContact capteurGaucheOuvert;
	private static CapteurContact capteurPortailFerme;
	//private static CapteurPresence capteurPresence = new CapteurP(SensorPort.S4);
	private static Porte porteGauche;
	private static Porte porteDroite;
	
	private static Etat etatPortail;
	
	public MoteurRunnable(CapteurContact capteurGaucheOuvert, CapteurContact capteurPortailFerme, Porte porteGauche, Porte porteDroite) {
		this.capteurGaucheOuvert = capteurGaucheOuvert;
		this.capteurPortailFerme = capteurPortailFerme;
		this.porteGauche = porteGauche;
		this.porteDroite = porteDroite;
	}
	
	private volatile boolean isRunning = false;    
	
	@Override
    public void run() {
		System.out.println("On passe bien dans la fonction run");
		MoteurRunnable.etatPortail = Etat.FERME;
		while(true) {
	        while(isRunning) {
	            try {
					ouvertureTotale();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
		}
    }
    
    public void stopThread() {
        this.isRunning = false;
    }
    
    public void resumeThread() {
        this.isRunning = true;
    }
    public void setEtat(Etat etat) {
    	this.etatPortail = etat;
    }
    
    public void ouvertureTotale() throws InterruptedException {
		if (etatPortail.name().equals("FERME")) {
			// playTune();
			while(!capteurGaucheOuvert.contact()) {
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
		stopThread();
	}
    
    static void playTune() throws InterruptedException
    {
    	int c = 261;
    	int d = 294;
    	int e = 329;
    	int f = 349;
    	int g = 391;
    	int gS = 415;
    	int a = 440;
    	int aS = 455;
    	int b = 466;
    	int cH = 523;
    	int cSH = 554;
    	int dH = 587;
    	int dSH = 622;
    	int eH = 659;
    	int fH = 698;
    	int fSH = 740;
    	int gH = 784;
    	int gSH = 830;
    	int aH = 880;
    	
    	Sound.playTone(a, 500); 
        Sound.playTone(a, 500);     
        Sound.playTone(a, 500); 
        Sound.playTone(f, 350); 
        Sound.playTone(cH, 150);
        
        Sound.playTone(a, 500);
        Sound.playTone(f, 350);
        Sound.playTone(cH, 150);
        Sound.playTone(a, 1000);
        //first bit
        
        Sound.playTone(eH, 500);
        Sound.playTone(eH, 500);
        Sound.playTone(eH, 500);    
        Sound.playTone(fH, 350); 
        Sound.playTone(cH, 150);
        
        Sound.playTone(gS, 500);
        Sound.playTone(f, 350);
        Sound.playTone(cH, 150);
        Sound.playTone(a, 1000);
        //second bit...
        
        Sound.playTone(aH, 500);
        Sound.playTone(a, 350); 
        Sound.playTone(a, 150);
        Sound.playTone(aH, 500);
        Sound.playTone(gSH, 250); 
        Sound.playTone(gH, 250);
        
        Sound.playTone(fSH, 125);
        Sound.playTone(fH, 125);    
        Sound.playTone(fSH, 250);
        
        Thread.sleep(250);
        
        Sound.playTone(aS, 250);    
        Sound.playTone(dSH, 500);  
        Sound.playTone(dH, 250);  
        Sound.playTone(cSH, 250);  
        //start of the interesting bit
        
        Sound.playTone(cH, 125);  
        Sound.playTone(b, 125);  
        Sound.playTone(cH, 250);      
        Thread.sleep(250);
        Sound.playTone(f, 125);  
        Sound.playTone(gS, 500);  
        Sound.playTone(f, 375);  
        Sound.playTone(a, 125); 
        
        Sound.playTone(cH, 500); 
        Sound.playTone(a, 375);  
        Sound.playTone(cH, 125); 
        Sound.playTone(eH, 1000); 
        //more interesting stuff (this doesn't quite get it right somehow)
        
        Sound.playTone(aH, 500);
        Sound.playTone(a, 350); 
        Sound.playTone(a, 150);
        Sound.playTone(aH, 500);
        Sound.playTone(gSH, 250); 
        Sound.playTone(gH, 250);
        
        Sound.playTone(fSH, 125);
        Sound.playTone(fH, 125);    
        Sound.playTone(fSH, 250);
        Thread.sleep(250);
        Sound.playTone(aS, 250);    
        Sound.playTone(dSH, 500);  
        Sound.playTone(dH, 250);  
        Sound.playTone(cSH, 250);  
        //repeat... repeat
        
        Sound.playTone(cH, 125);  
        Sound.playTone(b, 125);  
        Sound.playTone(cH, 250);      
        Thread.sleep(250);
        Sound.playTone(f, 250);  
        Sound.playTone(gS, 500);  
        Sound.playTone(f, 375);  
        Sound.playTone(cH, 125); 
               
        Sound.playTone(a, 500);            
        Sound.playTone(f, 375);            
        Sound.playTone(c, 125);            
        Sound.playTone(a, 1000); 
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

package portailEV3;

import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;

public class MoteurRunnable implements Runnable {
	
	private CapteurContact capteurOuvert;
	private CapteurContact capteurFerme;
	private Porte porte;
	
	private volatile boolean isRunning = false;
	private volatile int action; // 1 = ouvrir, 2 = fermer
	
	public MoteurRunnable(CapteurContact capteurOuvert, CapteurContact capteurFerme, Porte porte) {
		this.capteurOuvert = capteurOuvert;
		this.capteurFerme = capteurFerme;
		this.porte = porte;
	}
	
	@Override
    public void run() {
		while(true) {
			while(this.isRunning) {
				switch (this.action) {
					case 1:
						ouvrir();
						break;
					case 2:
						fermer();
						break;
						
					default:
						break;
				}
		    	
		    	this.action = 0;
				this.stopThread();
			}
		}
    }
	
	public void ouvrir() {
		while(!this.capteurOuvert.contact()) {
			this.porte.ouvrir();
		}
		this.porte.stop(true);
	}
	
	public void fermer() {
		while (!this.capteurFerme.contact()) {
			this.porte.fermer();
		}
		this.porte.stop(true);
	}
	
	public void setAction(int action) {
		this.action = action;
	}
    
    public void stopThread() {
        this.isRunning = false;
    }
    
    public void resumeThread() {
        this.isRunning = true;
    }

}
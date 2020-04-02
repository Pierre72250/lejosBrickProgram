package portailEV3;

import lejos.hardware.port.Port;

public class Porte {

	Moteur moteur;
	Boolean ouvert, ferme, enOuverture, enFermeture, arret;

	Porte(Port port) {
		moteur = new Moteur(port);
		ouvert = ferme = enOuverture = enFermeture = arret = false;
	}
	
	public Boolean getEnOuverture() {
		return enOuverture;
	}

	public void setEnOuverture(Boolean enOuverture) {
		this.enOuverture = enOuverture;
	}

	public Boolean getEnFermeture() {
		return enFermeture;
	}

	public void setEnFermeture(Boolean enFermeture) {
		this.enFermeture = enFermeture;
	}

	void ouvrir() {

		this.moteur.pousser();
		this.enOuverture = true;
		this.arret = false;

	}
	
	void stop(boolean ouvert) {
		this.moteur.stop();
		this.enOuverture = this.enFermeture = false;
		this.ouvert = ouvert;
		this.arret = true;
	}

	void fermer() {

		this.moteur.tirer();
		this.enFermeture = true;
		this.arret = false;

	}

	boolean positionOuvert() {
		return this.ouvert;
	}

	boolean positionFerme() {
		return this.ferme;
	}
	
}

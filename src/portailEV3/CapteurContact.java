package portailEV3;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;

public class CapteurContact {

	EV3TouchSensor capteurContact;

	CapteurContact(Port port) {
		this.capteurContact = new EV3TouchSensor(port);
	}

	boolean contact() {

		float[] sample = new float[capteurContact.sampleSize()];
		capteurContact.fetchSample(sample, 0);

		float etat = sample[0];
		
		if (etat == 1)
			return true;
		else
			return false;

	}

}

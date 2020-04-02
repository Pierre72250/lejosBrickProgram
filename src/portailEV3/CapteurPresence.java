package portailEV3;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class CapteurPresence {

	EV3UltrasonicSensor CapteurPresence;

	CapteurPresence(Port port) {
		this.CapteurPresence = new EV3UltrasonicSensor(port);
	}

	boolean presence() {

		float[] sample = new float[CapteurPresence.sampleSize()];
		CapteurPresence.fetchSample(sample, 0);

		float etat = sample[0];
		
		if (etat < 0.5)
			return true;
		else
			return false;

	}
}

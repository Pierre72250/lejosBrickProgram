package portailEV3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.hardware.Bluetooth;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;

public class EcouteBT extends Thread {

	private static DataOutputStream dataOut; 
	private static DataInputStream dataIn;
	private static BTConnection BTLink;
	private static boolean app_alive;
	

	volatile int byteRecu=0;
	
	
		public void run() {
			
			connect();
			app_alive = true;
			while(app_alive){
				try {
					byteRecu = (int) dataIn.readByte(); 
					
					Thread.sleep(100);
					System.out.println("Recu " + byteRecu);
					
					byteRecu = 0;
					
				}

				catch (IOException ioe) {
					System.out.println("IO Exception readInt");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		public int getTransmit() {
			return this.byteRecu;
		}
		
		public static void connect()
		{  
			System.out.println("En écoute");
			BTConnector ncc = (BTConnector) Bluetooth.getNXTCommConnector();
			BTLink = (BTConnection) ncc.waitForConnection(30, NXTConnection.RAW);
			dataOut = BTLink.openDataOutputStream();
			dataIn = BTLink.openDataInputStream();
		}
}

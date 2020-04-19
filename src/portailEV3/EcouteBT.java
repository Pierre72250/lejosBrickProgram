package portailEV3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import ca.ualberta.awhittle.ev3btrc.MessageBluetooth;
import lejos.hardware.Bluetooth;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;

public class EcouteBT extends Thread {

	private static DataOutputStream dataOut; 
	private static InputStream dataIn;
	private static ObjectInputStream objectIn;
	private static BTConnection BTLink;
	private static boolean app_alive;
	
	volatile MessageBluetooth msg;
	
	
		public void run() {
			
			connect();
			app_alive = true;
			while(app_alive){
				try {
					dataIn = BTLink.openInputStream();
					if ( dataIn.available() > 0 ) {
						objectIn = new ObjectInputStream(dataIn);
						msg = (MessageBluetooth) objectIn.readObject();
						Thread.sleep(100);
						objectIn.close();
					}
					
					dataIn.close();
					msg = null;

				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		public static void connect()
		{  
			System.out.println("En ecoute");
			BTConnector ncc = (BTConnector) Bluetooth.getNXTCommConnector();
			BTLink = (BTConnection) ncc.waitForConnection(30, NXTConnection.RAW);
			dataOut = BTLink.openDataOutputStream();
			dataIn = BTLink.openInputStream();
		}
}

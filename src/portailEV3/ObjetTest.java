package portailEV3;

import java.io.Serializable;

public class ObjetTest implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String un;
    private String deux;

    public ObjetTest(String un, String deux) {
        this.un = un;
        this.deux = deux;
    }
    
    public String texte() {
	   return "L'objet est " + this.un + " et " + this.deux;
   }
}

package scene;

import java.awt.Color;

public class Settings {
	private Color bgCol;			// Background color (r,g,b)
	private int numOfShadowRays;	// Root number of shadow rays (N^2 rays will be shot) (between 1 and 10)
	private int maxRecursion;		// Maximum number of recursions
	private int supSampLvl;			//Super sampling level
	
	public Settings(Color bgCol, int numOfShadowRays, int maxRecursion, int supSampLvl) {
	this.bgCol = bgCol;
	this.numOfShadowRays = numOfShadowRays;
	this.maxRecursion = maxRecursion;
	this.supSampLvl = supSampLvl;
	}
	
}

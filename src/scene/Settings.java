package scene;

import java.util.Arrays;

public class Settings {
	private double[] bgCol;			// Background color (r,g,b)
	private int numOfShadowRays;	// Root number of shadow rays (N^2 rays will be shot) (between 1 and 10)
	private int maxRecursion;		// Maximum number of recursions
	private int supSampLvl;			//Super sampling level
	
	public Settings(double[] bgCol, int numOfShadowRays, int maxRecursion, int supSampLvl) {
	this.bgCol = Arrays.copyOf(bgCol, 3);
	this.numOfShadowRays = numOfShadowRays;
	this.maxRecursion = maxRecursion;
	this.supSampLvl = supSampLvl;
	}
	
}

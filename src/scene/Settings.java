package scene;

import java.util.Arrays;

public class Settings {
	private double[] bgCol;			// Background color (r,g,b)
	private int numOfShadowRays;	// Root number of shadow rays (N^2 rays will be shot) (between 1 and 10)
	private int maxRecursion;		// Maximum number of recursions
	private int supSampLvl;			//Super sampling level
	private int imageWidth;
	private int imageHeight;

	
	public Settings(double[] bgCol, int numOfShadowRays, int maxRecursion, int supSampLvl) {
	this.bgCol = Arrays.copyOf(bgCol, 3);
	this.numOfShadowRays = numOfShadowRays;
	this.maxRecursion = maxRecursion;
	this.supSampLvl = supSampLvl;
	}
	
	public int getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}

	public double[] getBgCol() {
		return bgCol;
	}

	public int getNumOfShadowRays() {
		return numOfShadowRays;
	}

	public int getMaxRecursion() {
		return maxRecursion;
	}

	public int getSupSampLvl() {
		return supSampLvl;
	}
	
}

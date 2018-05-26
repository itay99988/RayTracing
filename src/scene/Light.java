package scene;

import java.util.Arrays;

import utils.Vector;

public class Light {
	private Vector position;		// Position of the light
	private double[] lgtCol;			// Light color
	private double specIntensity;	// Specular intensity
	private double shadowIntensity;	// Shadow intensity
	private double lgtWidth;		// Light width
	
	public Light(Vector position, double[] lgtCol, double specIntensity,
				 double shadowIntensity, double lgtWidth) {
		this.position = position;
		this.lgtCol = Arrays.copyOf(lgtCol, 3);
		this.specIntensity = specIntensity;
		this.shadowIntensity = shadowIntensity;
		this.lgtWidth = lgtWidth;
	}
	
}

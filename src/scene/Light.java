package scene;

import java.awt.Color;

import utils.Vector;

public class Light {
	private Vector position;		// Position of the light
	private Color lgtCol;			// Light color
	private double specIntensity;	// Specular intensity
	private double shadowIntensity;	// Shadow intensity
	private double lgtWidth;		// Light width
	
	public Light(Vector position, Color lgtCol, double specIntensity,
				 double shadowIntensity, double lgtWidth) {
		this.position = position;
		this.lgtCol = lgtCol;
		this.specIntensity = specIntensity;
		this.shadowIntensity = shadowIntensity;
		this.lgtWidth = lgtWidth;
	}
	
}

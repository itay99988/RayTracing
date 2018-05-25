package surfaces;

import java.awt.Color;

public class Material {
	private Color difCol;			// Diffuse Color (r,g,b)
	private Color specCol;			// Specular color (r,g,b)
	private Color refCol;			// Reflection color (r,g,b)
	private double phongCoef;		// Phong specularity coefficient (shininess)
	private double transparency;	// Transparency value (between 0 and 1)
	
	public Material(Color difCol, Color specCol, Color refCol, double phongCoef, double transparency) {
		this.difCol = difCol;
		this.specCol = specCol;
		this.refCol = refCol;
		this.phongCoef = phongCoef;
		this.transparency = transparency;
	}
}

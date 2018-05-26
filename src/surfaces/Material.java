package surfaces;

import java.util.Arrays;

public class Material {
	private double[] difCol;			// Diffuse Color (r,g,b)
	private double[] specCol;			// Specular color (r,g,b)
	private double[] refCol;			// Reflection color (r,g,b)
	private double phongCoef;		// Phong specularity coefficient (shininess)
	private double transparency;	// Transparency value (between 0 and 1)
	
	public Material(double[] difCol, double[] specCol, double[] refCol, double phongCoef, double transparency) {
		this.difCol = Arrays.copyOf(difCol, 3);
		this.specCol = Arrays.copyOf(specCol, 3);
		this.refCol = Arrays.copyOf(refCol, 3);
		this.phongCoef = phongCoef;
		this.transparency = transparency;
	}
}

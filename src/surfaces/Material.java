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

	public double[] getDifCol() {
		double[] newdifCol = {this.difCol[0], this.difCol[1], this.difCol[2]};
		return newdifCol;
	}

	public double[] getSpecCol() {
		double[] newSpecCol = {this.specCol[0], this.specCol[1], this.specCol[2]};
		return newSpecCol;
	}

	public double[] getRefCol() {
		double[] newRefCol = {this.refCol[0], this.refCol[1], this.refCol[2]};
		return newRefCol;
	}

	public double getPhongCoef() {
		return this.phongCoef;
	}

	public double getTransparency() {
		return this.transparency;
	}
	
}

package surfaces;

import utils.Vector;

public class Plane {
	
	private Vector normal;
	private double offset;
	private int matIndex;	
	
	/**
	 * 
	 * @param x the x component of the plane normal.
	 * @param y the y component of the plane normal.
	 * @param z the z component of the plane normal.
	 * @param offset the point that is used to define the plane.
	 */
	public Plane(Vector normal, double offset, int matIndex) {
		this.normal = normal;
		this.offset = offset;
		this.matIndex = matIndex;
	}
	
}

package surfaces;

import utils.Vector;

public class Sphere {
	private Vector center;
	private double radius;
	private int matIndex;
	
	public Sphere(Vector position , double radius, int matIndex){
		this.center = position;
		this.radius = radius;
		this.matIndex = matIndex;
	}
	
}

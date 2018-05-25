package RayTracing;

import utils.Vector;

public class Ray {
	Vector source;
	Vector direction;
	
	
	public Ray(Vector source, Vector direction) {
		this.source = source;
		this.direction = Vector.normalized(direction);
	}
}

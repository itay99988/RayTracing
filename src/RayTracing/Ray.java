package RayTracing;

import utils.Vector;

public class Ray {
	Vector source;
	Vector direction;
	
	
	public Ray(Vector source, Vector direction) {
		this.source = source;
		this.direction = Vector.normalized(direction);
	}
	
	public Vector getPointAtDistance(double distance)
	{
		Vector result;
		result=Vector.scalarMult(direction, distance);
		result=Vector.vecAdd(result, source);
		return result;
	}
	public Vector getSource() {
		return new Vector(this.source.X(),this.source.Y(),this.source.Z());
	}

	public void setSource(Vector source) {
		this.source = new Vector(source.X(),source.Y(),source.Z());
	}

	public Vector getDirection() {
		return new Vector(this.direction.X(),this.direction.Y(),this.direction.Z());
	}

	public void setDirection(Vector direction) {
		this.direction =  new Vector(direction.X(),direction.Y(),direction.Z());
		Vector.normalized(this.direction);
	}
}

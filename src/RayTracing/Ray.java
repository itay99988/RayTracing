package RayTracing;

import java.util.List;

import surfaces.GeneralObject;
import utils.Vector;

public class Ray {
	Vector source;
	Vector direction;
	
	
	public Ray(Vector source, Vector direction) {
		this.source = source;
		this.direction = direction.normalized();
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
		this.direction.normalized();
	}
	
	
	/**
	 * Finds and returns the closest object intersected by the ray.
	 * Returns null if no intersections at all.
	 * @param objects A list containing all objects from the scene.
	 * @param ray 
	 * @return
	 */
	public GeneralObject findIntersectedObject(List<GeneralObject> objects, Ray ray) {
		double distanceFromObject = Double.MAX_VALUE;
		double curDistance;
		Vector iPoint;
		GeneralObject iObject = null;
		for(GeneralObject object : objects) {
			iPoint = object.findIntersectionPoint(ray);
			if(iPoint == null) { // No intersection
				continue;
			}
			curDistance = Vector.calculateDistance(ray.getSource(), iPoint);
			if(curDistance < distanceFromObject) {
				distanceFromObject = curDistance;
				iObject = object;
			}
		}
		return iObject;
	}
	
/**
 * Finds the intersection point of ray with the closest object, when given as an arguement.
 * @param closestObject
 * @return
 */
	public Vector findIntersectionPointForClosestObj(Ray ray, GeneralObject closestObject) {
		return closestObject.findIntersectionPoint(ray);		
	}
	
	public Intersection createIntersectionObject(GeneralObject closestObject, Vector iPoint) {
		double distance = Vector.calculateDistance(this.source, iPoint);
		return new Intersection(closestObject, iPoint, distance);
	}
}

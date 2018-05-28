package surfaces;

import RayTracing.Ray;
import utils.MathServices;
import utils.Vector;

public class Sphere extends GeneralObject{
	private Vector center;
	private double radius;
	private int matIndex;
	
	public Sphere(Material material, Vector position, double radius, int matIndex){
		this.setMaterial(material);
		this.center = position;
		this.radius = radius;
		this.matIndex = matIndex;
	}
	
	public Vector findIntersectionPoint(Ray ray) {
		double a,b,c;
		double[] result;
		Vector direction=Vector.vecSubtract(ray.getSource(), center);
	
		a=1;
		b= 2* Vector.dotProduct(ray.getDirection(),direction);
		c=Vector.dotProduct(direction, direction) - (radius*radius);
		
		result=MathServices.quadraticSolver(a, b, c);
		//if no intersection or sphere is behind the ray, return null
		if (result==null || (result[0] < 0 && result[1]<0)) return null;
		if (result[0]<0) return ray.getPointAtDistance(result[1]);
		if (result[1]<0) return ray.getPointAtDistance(result[0]);
		if(result[0] <result[1]) return ray.getPointAtDistance(result[0]);
		else  return ray.getPointAtDistance(result[1]);
	}
	
	//gets a point on the sphere and returns the normal direction in that point
	protected Vector findNormalVector(Vector p) {
		Vector normal = Vector.vecSubtract(p, this.center);
		normal = Vector.normalized(normal);
		return normal;
	}
}

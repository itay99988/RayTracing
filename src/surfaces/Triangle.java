package surfaces;

import RayTracing.Ray;
import utils.Vector;

public class Triangle extends GeneralObject{
	private Vector u;
	private Vector v;
	private Vector w;
	private int matIndex;	
	
	public Triangle(Material material, Vector u, Vector v, Vector w, int matIndex) {
		this.setMaterial(material);
		this.u = u;
		this.v = v;
		this.w = w;
		this.matIndex = matIndex;
	}
	
	public Vector findIntersectionPoint(Ray ray) {
		//build a plane out of the points
		Vector vec1 = Vector.vecSubtract(this.v, this.u);
		Vector vec2 = Vector.vecSubtract(this.v, this.w);
		Vector normal = Vector.normalized(Vector.crossProduct(vec1, vec2));
		double offset = (-1) * Vector.dotProduct(normal, u);
		Plane trianglePlane = new Plane(null,normal,offset,0);
		
		//intersection between the plane and the ray
		Vector p = trianglePlane.findIntersectionPoint(ray);
		if(p==null) return null;
		
		//check side 1
		Vector v1 = Vector.vecSubtract(this.u, ray.getSource());
		Vector v2 = Vector.vecSubtract(this.v, ray.getSource());
		Vector n1 = Vector.normalized(Vector.crossProduct(v1, v2));
		double d1 = (-1) * Vector.dotProduct(ray.getSource(), n1);
		double res = (Vector.dotProduct(p, n1) + d1);
		if(res < 0)
			return null;
		
		//check side 2
		v1 = Vector.vecSubtract(this.v, ray.getSource());
		v2 = Vector.vecSubtract(this.w, ray.getSource());
		n1 = Vector.normalized(Vector.crossProduct(v1, v2));
		d1 = (-1) * Vector.dotProduct(ray.getSource(), n1);
		res = (Vector.dotProduct(p, n1) + d1);
		if(res < 0)
			return null;
		
		//check side 3
		v1 = Vector.vecSubtract(this.w, ray.getSource());
		v2 = Vector.vecSubtract(this.u, ray.getSource());
		n1 = Vector.normalized(Vector.crossProduct(v1, v2));
		res = (Vector.dotProduct(p, n1) + d1);
		if(res < 0)
			return null;
				
		return p;
	}
	
	//gets a point on the triangle and returns the normal direction in that point
	protected Vector findNormalVector(Vector p) {
		//build a normal out of the triangle points (by cross-product of the two vectors)
		Vector vec1 = Vector.vecSubtract(this.v, this.u);
		Vector vec2 = Vector.vecSubtract(this.v, this.w);
		Vector normal = Vector.normalized(Vector.crossProduct(vec1, vec2));
		
		return normal;
	}
}

package surfaces;

import RayTracing.Ray;

import utils.Vector;

public abstract class GeneralObject {

	protected Material material;
	
	//finds intersection point with a given ray 
	abstract public Vector findIntersectionPoint(Ray ray);
	
	//gets a point on the object and returns the normal direction in that point
	protected abstract Vector findNormalVector(Vector p);
	
	//there are two options for the normal direction. we have to return
	//the normal which comes toward the out point - the source of the beamed ray
	public Vector findNormalVector(Vector on, Vector out){
		Vector normal=this.findNormalVector(on);
		Vector pright=Vector.vecAdd(on, normal);
		Vector pwrong=Vector.vecSubtract(normal, on);
		if (Vector.calculateDistance(out, pright)>Vector.calculateDistance(out, pwrong))
			return (Vector.scalarMult(normal, -1));
		else
			return normal;
	}
		
	public void setMaterial(Material material) {
		this.material = material;
	}
	public Material getMaterial()
	{
		return material;
	}
}

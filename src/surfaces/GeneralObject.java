package surfaces;

import RayTracing.Ray;
import utils.Vector;

public abstract class GeneralObject {

	protected Material material;
	
	//finds intersection point with a given ray 
	abstract public Vector findIntersectionPoint(Ray ray);
	
	public void setMaterial(Material material) {
		this.material = material;
	}
	public Material getMaterial()
	{
		return material;
	}
}

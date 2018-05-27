package RayTracing;

import surfaces.GeneralObject;
import utils.Vector;

public class Intersection {
	 private Vector intersection; //intersection point in our cartesian 3d system
	 private GeneralObject object; //the intersected obeject (material is included inside)
	 private double distance;//distance between the cameras ray and the object
	 
	 public Intersection(GeneralObject object, Vector intersection, double distance)
	 {
		 this.intersection= new Vector(intersection.X(), intersection.Y(), intersection.Z());
		 this.object=object;
		 this.distance=distance;
	 }
	 
	 public Vector getIntersectionPoint()
	 {
		 return new Vector(this.intersection.X(), this.intersection.Y(), this.intersection.Z());
	 }
	 
	 public GeneralObject getGeneralObject()
	 {
		 return object;
	 }
	
	 public double getDistance()
	 {
		 return distance;
	 }

}

package scene;

import java.util.Arrays;
import java.util.List;

import RayTracing.Intersection;
import RayTracing.Ray;
import RayTracing.RayTracer;
import surfaces.GeneralObject;
import utils.Vector;

public class Light {
	private Vector position;		// Position of the light
	private double[] lgtCol;		// Light color
	private double specIntensity;	// Specular intensity
	private double shadowIntensity;	// Shadow intensity
	private double lgtWidth;		// Light width
	
	public Light(Vector position, double[] lgtCol, double specIntensity,
				 double shadowIntensity, double lgtWidth) {
		this.position = position;
		this.lgtCol = Arrays.copyOf(lgtCol, 3);
		this.specIntensity = specIntensity;
		this.shadowIntensity = shadowIntensity;
		this.lgtWidth = lgtWidth;
	}
	
	public Vector getLightPosition() {
		return new Vector(this.position.X(), this.position.Y(), this.position.Z());
	}
	
	public double[] getLightColor() {
		double[] newLgtColor = {this.lgtCol[0], this.lgtCol[1], this.lgtCol[2]};
		return newLgtColor;
	}
	
	//returns null if no intersection was found
	public double[] lightCheck(List<GeneralObject> allObjects,Ray ray,Intersection iPoint) {
		Vector L=Vector.vecSubtract(iPoint.getIntersectionPoint(),this.position);
		L = Vector.normalized(L);
		
		Ray lightRay=new Ray(this.position,Vector.scalarMult(L,1));//create ray from light source to intersection point
		GeneralObject iObject = lightRay.findIntersectedObject(allObjects, lightRay);
		if(iObject == null) {
			return null;
		}
		Vector lightIntersectionPoint = lightRay.findIntersectionPointForClosestObj(lightRay, iObject);
		Intersection lightIPoint = lightRay.createIntersectionObject(iObject, lightIntersectionPoint);
		
		//check if lights intersects same point upto epsilon
		if((lightIPoint!=null) && (Vector.calculateDistance(lightIPoint.getIntersectionPoint(),iPoint.getIntersectionPoint())<0.0000001F)){
			return calculateColor(iPoint.getIntersectionPoint(),iPoint.getGeneralObject(), ray);
		}
		else {
			//there is not direct path between the light and the intersection point
			//this is a reason to use the shadow intensity parameter
			double[] sumDiffSpec = calculateColor(iPoint.getIntersectionPoint(),iPoint.getGeneralObject(), ray);
			sumDiffSpec[0]*=(1-this.shadowIntensity);
			sumDiffSpec[1]*=(1-this.shadowIntensity);
			sumDiffSpec[2]*=(1-this.shadowIntensity);
			
			return sumDiffSpec;
		}
	}
	
	//calculate the sum of the diffuse color and the specular color, base on phong-shading-model
	//will need to take into consideration the following: beamed ray from camera, light ray, light color,
	//phong-coeff,intersection point, intersected object's color properties.
	//the actual params of the function: the point we want to sample its color, the object of that point, and
	//the ray from the camera
	//calculations were done according to the phong-shading model presentation with some minor changes
	private double[] calculateColor(Vector iPoint,GeneralObject iObject,Ray ray) {
		//diffuse-color calculation
		double[] KD=iObject.getMaterial().getDifCol();
		Vector N=iObject.findNormalVector(iPoint,ray.getSource());
		N=Vector.normalized(N);
		Vector L = Vector.vecSubtract(iPoint,this.position);
		L=Vector.normalized(L);
		double NL=Vector.dotProduct(N,L);
		if(NL<=0) NL*=-1;
		
		double[] diffColor={0.0,0.0,0.0};
		
		for(int i=0;i<3;i++)
		{
			diffColor[i]= (this.lgtCol[i])*(KD[i]*NL);
		}
		
		//specular-color calculation
		double[] KS= iObject.getMaterial().getSpecCol();
		double n=iObject.getMaterial().getPhongCoef();
		Vector R=Vector.reflectVec(L,N);
		R = Vector.normalized(R);
		Vector V=Vector.scalarMult(ray.getDirection(),-1);
		V = Vector.normalized(V);
		double VR=Vector.dotProduct(V,R);
		if(VR<=0) VR*=-1;
		
		double[] specColor={0.0,0.0,0.0};
		for(int i=0;i<3;i++)
		{
			specColor[i]= (this.lgtCol[i] * this.specIntensity)*(KS[i]*Math.pow(VR,n));
		}
		
		//sum the two colors
		double[] sumDiffSpec = {0.0,0.0,0.0};
		for(int i=0;i<3;i++)
		{
			sumDiffSpec[i]= diffColor[i] + specColor[i];
		}
		
		return sumDiffSpec;
	}
	
}

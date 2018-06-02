package scene;

import java.util.Arrays;
import java.util.List;

import RayTracing.Intersection;
import RayTracing.Ray;
import surfaces.GeneralObject;
import surfaces.Plane;
import utils.ArrayServices;
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
	
	//calculate the light influece on the pixel color
	public double[] lightCheck(List<GeneralObject> allObjects,Ray ray,Intersection iPoint,int numOfShRays) {
		Vector L = Vector.vecSubtract(iPoint.getIntersectionPoint(),this.position).normalized();
		
		//create a perpendicular plane to the light ray and compute the two vectors
		//that defines it, and normalize these vectors
		Vector planeNormal = new Vector(L.X(),L.Y(),L.Z());
		double planeD = (-1) * Vector.dotProduct(planeNormal, this.position);
		Plane lightPlane = new Plane(null,planeNormal,planeD);
		Vector planeVec1 = lightPlane.findProjectionFromPoint(this.position, new Vector(1,2,3));
		Vector planeVec2 = Vector.crossProduct(planeNormal, planeVec1).normalized();
		
		double hitRaysCount=0;
		double radius = lgtWidth;
		double unit = radius/numOfShRays;
		//initialize start points on the matrix that is on the light plane
		Vector tempPosition = Vector.vecAdd(this.position, Vector.scalarMult(planeVec2, -1*(radius/2)));
		Vector startPoint = Vector.vecAdd(tempPosition, Vector.scalarMult(planeVec1, -1*(radius/2)));
		//new unit-normalization to the plane's vectors
		planeVec1 = Vector.scalarMult(planeVec1, unit);
		planeVec2 = Vector.scalarMult(planeVec2, unit);
		
		//count how many rays directly hit the intersection point
		for(int i=0;i<numOfShRays;i++) {
			for(int j=0;j<numOfShRays;j++) {
				double sampX = Math.random(); // values between 0 and 1.
				double sampY = Math.random();
				
				//create a randomized point inside the pixel
				tempPosition = Vector.vecAdd(startPoint, Vector.scalarMult(planeVec1, sampX));
				Vector randomizedPoint = Vector.vecAdd(tempPosition, Vector.scalarMult(planeVec2, sampY));
				L = Vector.vecSubtract(iPoint.getIntersectionPoint(),randomizedPoint).normalized();
				
				Ray lightRay=new Ray(randomizedPoint, L);//create ray from light source to intersection point
				GeneralObject iObject = lightRay.findIntersectedObject(allObjects, lightRay);
				if(iObject == null) {
					return null;
				}
				Vector lightIntersectionPoint = lightRay.findIntersectionPointForClosestObj(lightRay, iObject);
				Intersection lightIPoint = lightRay.createIntersectionObject(iObject, lightIntersectionPoint);
				
				//check if lights intersects same point upto epsilon
				if((lightIPoint!=null) && (Vector.calculateDistance(lightIPoint.getIntersectionPoint(),iPoint.getIntersectionPoint())<0.0000001F)){
					//if it does, increment counter by one
					hitRaysCount++;
				}
				//move the startpoint to the next pixel in row
				startPoint = Vector.vecAdd(startPoint, planeVec1);
			}
			//move the startpoint to the next row
			startPoint = Vector.vecAdd(startPoint, Vector.scalarMult(planeVec1, -1*numOfShRays*(radius)));
			startPoint = Vector.vecAdd(startPoint,planeVec2);
		}
		
		//now calculate colors and multiply by the ratio between hit and blocked rays
		L = Vector.vecSubtract(iPoint.getIntersectionPoint(),this.position).normalized();
		double hitBlockedRatio = hitRaysCount/(numOfShRays*numOfShRays);
		double[] sumDiffSpec;
		if(hitBlockedRatio > 0) {
			sumDiffSpec = calculateColor(iPoint.getIntersectionPoint(),iPoint.getGeneralObject(), ray);
			ArrayServices.arrScalarMult(sumDiffSpec, hitBlockedRatio);
		}
		else {
			//there is no direct path between the light and the intersection point
			//this is a reason to use the shadow intensity parameter
			sumDiffSpec = calculateColor(iPoint.getIntersectionPoint(),iPoint.getGeneralObject(), ray);
			ArrayServices.arrScalarMult(sumDiffSpec, 1-this.shadowIntensity);
		}
		return sumDiffSpec;
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
		Vector N = iObject.findNormalVector(iPoint,ray.getSource()).normalized();
		Vector L = Vector.vecSubtract(iPoint,this.position).normalized();
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
		Vector R=Vector.reflectVec(L,N).normalized();
		Vector V = Vector.scalarMult(ray.getDirection(),-1).normalized();
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

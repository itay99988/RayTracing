package scene;

import RayTracing.Ray;
import RayTracing.RayTracer;
import utils.Vector;

public class Camera {
	private Vector position; 		// Position of the camera
	private Vector lookPosition; 	// Look-at position of the camera
	private Vector upVector; 		// Up vector of the camera
	private Vector direction;		// The direction of the camera
	private double screenDistance;	// Screen distance from the camera
	private double screenWidth;		// Screen width
	private Vector screenCenter;		// The coordinate for the center of the screen.
	
	private Vector rightDirection;	// This vector points to the direction that will be interpreted as 'right' on the screen.
	private Vector upDirection;		// This vector points to the direction that will be interpreted as 'up' on the screen.
	
	public Camera(Vector position, Vector lookPosition, Vector upVector,
				  double screenDistance, double screenWidth) {
		this.position = position;
		this.lookPosition = lookPosition;
		this.direction = Vector.normalized(Vector.vecSubtract(lookPosition, position));
		this.upVector = upVector;
		this.screenDistance = screenDistance;
		this.screenWidth = screenWidth;
		this.rightDirection = Vector.normalized(Vector.crossProduct(this.direction, upVector));
		this.upDirection = Vector.normalized(Vector.crossProduct(this.rightDirection, this.direction));
		Ray ray = new Ray(position, direction);
		this.screenCenter = ray.getPointAtDistance(screenDistance);
		}
	

	public Vector getPosition() {
		return this.position;
	}

	public Vector getLookPosition() {
		return this.lookPosition;
	}

	public Vector getUpVector() {
		return this.upVector;
	}

	public Vector getDirection() {
		return this.direction;
	}

	public double getScreenDistance() {
		return this.screenDistance;
	}

	public double getScreenWidth() {
		return this.screenWidth;
	}
	
	public Vector getFovCenter() {
		return screenCenter;
	}

	public Vector getRightDirection() {
		return rightDirection;
	}

	public Vector getUpDirection() {
		return upDirection;
	}
	
	public Ray makeRay(Vector dest) {
		return new Ray(this.position, dest);
	}

	public Ray ConstructRayThroughPixel(double x, double y) {
		Settings settings = RayTracer.getSettings();
		double imageWidth = settings.getImageWidth();
		double imageHeight = settings.getImageHeight();
		double pixelWidth = this.getScreenWidth()/imageWidth;
		double pixelHeight = pixelWidth * imageHeight/imageWidth;
		
		double upDistance = (imageHeight/2 - y) * pixelHeight;
		double rightDistance = (imageWidth/2 - x) * pixelWidth;
		
		Vector upMovement = Vector.scalarMult(this.upDirection, upDistance); 			// This vector indicates how much you have to move up from the center of the screen.
		Vector rightMovement = Vector.scalarMult(this.rightDirection, rightDistance);	// This vector indicates how much you have to move right from the center of the screen.
		
		Vector dest;
		dest = Vector.vecAdd(this.screenCenter, upMovement);
		dest = Vector.vecAdd(dest, rightMovement);
		dest = Vector.vecSubtract(dest, this.position);
		return makeRay(dest);
	}
	

}

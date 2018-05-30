package scene;

import RayTracing.Ray;
import RayTracing.RayTracer;
import utils.Vector;

public class Camera {
	private Vector position; 		// Position of the camera
	private Vector direction;		// The direction of the camera
	private double screenWidth;		// Screen width
	private Vector screenCenter;	// The coordinate for the center of the screen.
	private Vector rightDirection;	// This vector points to the direction that will be interpreted as 'right' on the screen.
	private Vector upDirection;		// This vector points to the direction that will be interpreted as 'up' on the screen.
	
	public Camera(Vector position, Vector lookPosition, Vector upVector,
				  double screenDistance, double screenWidth) {
		this.position = position;
		this.direction = Vector.vecSubtract(lookPosition, position).normalized();
		this.screenWidth = screenWidth;
		this.rightDirection = Vector.crossProduct(this.direction, upVector).normalized();
		this.upDirection = Vector.crossProduct(this.rightDirection, this.direction).normalized();
		Ray ray = new Ray(position, direction);
		this.screenCenter = ray.getPointAtDistance(screenDistance);
		}
	
	
	public Ray makeRay(Vector dest) {
		return new Ray(this.position, dest);
	}
	

	public Ray constructRayThroughPixel(double x, double y) {
		Settings settings = RayTracer.getSettings();
		double imageWidth = settings.getImageWidth();
		double imageHeight = settings.getImageHeight();
		double pixelWidth = this.screenWidth/imageWidth;
		double pixelHeight = pixelWidth * imageWidth/imageHeight;
		
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

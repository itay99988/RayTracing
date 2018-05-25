package scene;

import RayTracing.Ray;
import utils.Vector;

public class Camera {
	private Vector position; 		// Position of the camera
	private Vector lookPosition; 	// Look-at position of the camera
	private Vector upVector; 		// Up vector of the camera
	private Vector direction;		// The direction of the camera
	private double screenDistance;	// Screen distance from the camera
	private double screenWidth;		// screen width
	
	public Camera(Vector position, Vector lookPosition, Vector upVector,
				  double screenDistance, double screenWidth) {
		this.position = position;
		this.lookPosition = lookPosition;
		this.direction = Vector.vecSubtract(position, lookPosition);
		this.upVector = upVector;
		this.screenDistance = screenDistance;
		this.screenWidth = screenWidth;	}
	
	public Ray makeRay(Vector dest) {
		return new Ray(position, dest);
	}
}

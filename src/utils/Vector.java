package utils;

public class Vector {
	
	private double x;
	private double y;
	private double z;
	
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	
	public double X() {
		return this.x;
	}
	
	
	public double Y() {
		return this.y;
	}
	
	
	public double Z() {
		return this.z;
	}
	
	
	public static double dotProduct(Vector u, Vector v) {
		return u.X()*v.X() + u.Y()*v.Y() + u.Z()*v.Z();
	}
	
	
	public double dotProduct(Vector other) {
		return dotProduct(this, other);
	}
	
	
	public static Vector crossProduct(Vector u, Vector v) {
		double x = u.Y()*v.Z() - u.Z()*v.Y();
		double y = u.Z()*v.X() - u.X()*v.Z();
		double z = u.X()*v.Y() - u.Y()*v.X();
		
		return new Vector(x,y,z);
	}
	
	public Vector crossProduct(Vector other) {
		return crossProduct(this, other);
	}
	
	
	/**
	 * Returns scalar*v
	 * @param v
	 * @param scalar
	 * @return
	 */
	public static Vector scalarMult(Vector v, double scalar) {
		double x = v.X()*scalar;
		double y = v.Y()*scalar;
		double z = v.Z()*scalar;
		
		return new Vector(x,y,z);
	}
	
	
	/**
	 * Returns the result of the multiplication of two vector componentwise
	 * Used for RGB color calculation
	 * @param u
	 * @param v
	 * @return
	 */
	public static Vector vecMult(Vector u, Vector v) {
		double x = u.X()*v.X();
		double y = u.Y()*v.Y();
		double z = u.Z()*v.Z();
		
		return new Vector(x,y,z);
	}
	
	
	/**
	 * Returns u+v
	 * @param u
	 * @param v
	 * @return
	 */
	public static Vector vecAdd(Vector u, Vector v) {
		double x = u.X() + v.X();
		double y = u.Y() + v.Y();
		double z = u.Z() + v.Z();
		
		return new Vector(x,y,z);
	}
	
	
	/**
	 * Returns u-v
	 * @param u
	 * @param v
	 * @return
	 */
	public static Vector vecSubtract(Vector u, Vector v) {
		double x = u.X() - v.X();
		double y = u.Y() - v.Y();
		double z = u.Z() - v.Z();
		
		return new Vector(x,y,z);
	}
	
	
	/**
	 * @param v
	 * @return the magnitude of the vector.
	 */
	public double getMagnitude() {
		double sqSum = this.x*this.x + this.y*this.y + this.z*this.z;
		return Math.sqrt(sqSum);
	}
	
	
	/**
	 * returns the normalized vector (such that ||@ret|| = 1)
	 * @param v
	 * @return
	 */
	public Vector normalized() {
		double magnitude = this.getMagnitude();
		return scalarMult(this, 1.0/magnitude);
		
	}
	
	/**
	 * Calculates the vector of the reflection as defined in the ray tracing
	 * presentation, slide 23: R = V - 2(V.dot(N))*N .
	 * @param v
	 * @param normal
	 * @return
	 */
	public static Vector reflectVec(Vector v, Vector normal) {
		v = v.normalized();		// Normalize the vector
		Vector n = normal.normalized();	// Normalize the normal
		return vecSubtract(v, scalarMult(n, 2*dotProduct(v,n))); 
	}

	//distance between two points
	public static double calculateDistance(Vector p1, Vector p2)
	{
		double result = 0;

		result+=Math.pow(p1.X()-p2.X(),2);
		result+=Math.pow(p1.Y()-p2.Y(),2);
		result+=Math.pow(p1.Z()-p2.Z(),2);
		
		return Math.sqrt(result);
	}

}

package surfaces;

import utils.Vector;

public class Triangle {
	private Vector u;
	private Vector v;
	private Vector w;
	private int matIndex;	
	
	public Triangle(Vector u, Vector v, Vector w, int matIndex) {
		this.u = u;
		this.v = v;
		this.w = w;
		this.matIndex = matIndex;
	}
}

package utils;

import java.util.List;

import scene.*;
import surfaces.*;

public class Parser {
	
	public static Camera parseCamera(String[] params) {
		if(params.length < 11) {
			System.out.println("Camera loading error: Not enough parameters given.");
			return null;
		}
		try {
		double x = Double.parseDouble(params[0]);
		double y = Double.parseDouble(params[1]);
		double z = Double.parseDouble(params[2]);
		Vector position = new Vector(x, y, z);
		
		x = Integer.parseInt(params[3]);
		y = Integer.parseInt(params[4]);
		z = Integer.parseInt(params[5]);
		Vector lookPosition = new Vector(x,y,z);
		
		x = Integer.parseInt(params[6]);
		y = Integer.parseInt(params[7]);
		z = Integer.parseInt(params[8]);
		Vector upVector = new Vector(x,y,z);
		
		double screenDistance = Double.parseDouble(params[9]);
		double screenWidth = Double.parseDouble(params[10]);
		
		return new Camera(position, lookPosition, upVector, screenDistance, screenWidth);
		} catch(Exception e) {
			System.out.println("Camera loading error: "+e.getMessage());
			return null;
		}		
	}
	
	
	public static Settings parseSettings(String[] params) {
		if(params.length < 5) {
			System.out.println("General settings loading error: Not enough parameters given.");
			return null;
		}
		try {
		double r = Double.parseDouble(params[0]);
		double g = Double.parseDouble(params[1]);
		double b = Double.parseDouble(params[2]);
		int numOfShadowRays = Integer.parseInt(params[3]);
		if(numOfShadowRays < 1 || numOfShadowRays > 10) {
			throw new Exception("Number of shadow rays is not between 1 and 10.");
		}
		int maxRecursion = Integer.parseInt(params[4]);
		int supSampLvl;
		if(params.length == 5) {
			supSampLvl = 1;
		}
		else {
			supSampLvl = Integer.parseInt(params[5]);
		}
		return new Settings(createColor(r,g,b), numOfShadowRays, maxRecursion, supSampLvl);
		} catch (Exception e) {
			System.out.println("General settings loading error: "+e.getMessage());
			return null;
		}
	}
	
	
	public static Material parseMaterial(String[] params) {
		if(params.length < 11) {
			System.out.println("Material loading error: Not enough parameters given.");
			return null;
		}
		try {
			double r = Double.parseDouble(params[0]);
			double g = Double.parseDouble(params[1]);
			double b = Double.parseDouble(params[2]);
			double[] difCol = createColor(r,g,b);
			
			r = Double.parseDouble(params[3]);
			g = Double.parseDouble(params[4]);
			b = Double.parseDouble(params[5]);
			double[] specCol = createColor(r,g,b);
			
			r = Double.parseDouble(params[6]);
			g = Double.parseDouble(params[7]);
			b = Double.parseDouble(params[8]);
			double[] refCol = createColor(r,g,b);
			
			double phongCoef = Double.parseDouble(params[9]);
			double transparency = Double.parseDouble(params[10]);
			if(transparency < 0 || transparency > 1) {
				throw new Exception("Transparency is not between 0 and 1.");
			}
			return new Material(difCol, specCol, refCol, phongCoef, transparency);
		} catch (Exception e) {
			System.out.println("Material loading error: "+e.getMessage());
			return null;
		}
	}
	
	
	public static Sphere parseSphere(String[] params, int numOfMaterials, List<Material> materials) {
		if(params.length < 5) {
			System.out.println("Sphere loading error: Not enough parameters given.");
			return null;
		}
		try {
			double x = Double.parseDouble(params[0]);
			double y = Double.parseDouble(params[1]);
			double z = Double.parseDouble(params[2]);
			double radius = Double.parseDouble(params[3]);
			int matIndex = Integer.parseInt(params[4]);
			if(numOfMaterials < matIndex) {
				throw new Exception("Material not found");
			}
			
			return new Sphere(materials.get(matIndex), new Vector(x,y,z), radius, matIndex);
		} catch (Exception e) {
			System.out.println("Sphere loading error: "+e.getMessage());
			return null;
		}
	}
	
	
	public static Plane parsePlane(String[] params, int numOfMaterials, List<Material> materials) {
		if(params.length < 5) {
			System.out.println("Plane loading error: Not enough parameters given.");
			return null;
		}
		try {
			double x = Double.parseDouble(params[0]);
			double y = Double.parseDouble(params[1]);
			double z = Double.parseDouble(params[2]);
			double offset = Double.parseDouble(params[3]);
			int matIndex = Integer.parseInt(params[4]);
			if(numOfMaterials < matIndex) {
				throw new Exception("Material not found");
			}
			
			return new Plane(materials.get(matIndex-1), new Vector(x,y,z), offset, matIndex);
		} catch (Exception e) {
			System.out.println("Plane loading error: "+e.getMessage());
			return null;
		}
	}
	
	
	public static Triangle parseTriangle(String[] params, int numOfMaterials, List<Material> materials) {
		if(params.length < 10) {
			System.out.println("Triangle loading error: Not enough parameters given.");
			return null;
		}
		try {
			double x = Double.parseDouble(params[0]);
			double y = Double.parseDouble(params[1]);
			double z = Double.parseDouble(params[2]);
			Vector u = new Vector(x,y,z);
			
			x = Double.parseDouble(params[3]);
			y = Double.parseDouble(params[4]);
			z = Double.parseDouble(params[5]);
			Vector v = new Vector(x,y,z);
			
			x = Double.parseDouble(params[6]);
			y = Double.parseDouble(params[7]);
			z = Double.parseDouble(params[8]);
			Vector w = new Vector(x,y,z);
			
			int matIndex = Integer.parseInt(params[9]);
			if(numOfMaterials < matIndex) {
				throw new Exception("Material not found");
			}
			
			return new Triangle(materials.get(matIndex), u, v, w, matIndex); 
		} catch (Exception e) {
			System.out.println("Triangle loading error: "+e.getMessage());
			return null;
		}
	}
	

	public static Light parseLight(String[] params) {
		if(params.length < 9) {
			System.out.println("Material loading error: Not enough parameters given.");
			return null;
		}
		try {
			double x = Double.parseDouble(params[0]);
			double y = Double.parseDouble(params[1]);
			double z = Double.parseDouble(params[2]);
			
			double r = Double.parseDouble(params[3]);
			double g = Double.parseDouble(params[4]);
			double b = Double.parseDouble(params[5]);
			double specIntensity = Double.parseDouble(params[6]);
			double shadowIntensity = Double.parseDouble(params[7]);
			if(shadowIntensity < 0 || shadowIntensity > 1) {
				throw new Exception("Shadow intensity is not between 0 and 1.");
			}

			double lgtWidth = Double.parseDouble(params[8]);
			return new Light(new Vector(x,y,z), createColor(r,g,b), specIntensity, shadowIntensity, lgtWidth);
		} catch (Exception e) {
			System.out.println("Light loading error: "+e.getMessage());
			return null;
		}
	}
	
	/**
	 * Converts colors from range 0-1 (double) to 0-255 (integer)
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	private static double[] createColor(double r, double g, double b) {
//		int newR = (int)Math.round(255*r);
//		newR = Math.max(0,Math.min(255,newR));
//		
//		int newG = (int)Math.round(255*g);
//		newG = Math.max(0,Math.min(255,newG));
//		
//		int newB = (int)Math.round(255*b);
//		newB = Math.max(0,Math.min(255,newB));
		double[] colorArr = {r,g,b};
		return colorArr;
	}
}

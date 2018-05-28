package RayTracing;

import java.awt.Transparency;
import java.awt.color.*;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import scene.*;
import surfaces.*;
import utils.*;

/**
 *  Main class for ray tracing exercise.
 */
public class RayTracer {

	public static int imageWidth;
	public static int imageHeight;
	
	private Camera				camera;
	private static Settings		settings;
	private List<Material>		materials = new ArrayList<>();
	private List<GeneralObject> spheres = new ArrayList<>();
	private List<GeneralObject>	planes = new ArrayList<>();
	private List<GeneralObject>	triangles = new ArrayList<>();
	private List<Light>			lights = new ArrayList<>();
	
	/**
	 * Runs the ray tracer. Takes scene file, output image file and image size as input.
	 */
	public static void main(String[] args) {

		try {

			RayTracer tracer = new RayTracer();

                        // Default values:
			tracer.imageWidth = 500;
			tracer.imageHeight = 500;

			if (args.length < 2)
				throw new RayTracerException("Not enough arguments provided. Please specify an input scene file and an output image file for rendering.");

			String sceneFileName = args[0];
			String outputFileName = args[1];

			if (args.length > 3)
			{
				tracer.imageWidth = Integer.parseInt(args[2]);
				tracer.imageHeight = Integer.parseInt(args[3]);
			}


			// Parse scene file:
			tracer.parseScene(sceneFileName);

			// Render scene:
			tracer.renderScene(outputFileName);

//		} catch (IOException e) {
//			System.out.println(e.getMessage());
		} catch (RayTracerException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}


	}

	/**
	 * Parses the scene file and creates the scene. Change this function so it generates the required objects.
	 */
	public void parseScene(String sceneFileName) throws IOException, RayTracerException
	{
		FileReader fr = new FileReader(sceneFileName);

		BufferedReader r = new BufferedReader(fr);
		String line = null;
		int lineNum = 0;
		System.out.println("Started parsing scene file " + sceneFileName);



		while ((line = r.readLine()) != null)
		{
			line = line.trim();
			++lineNum;

			if (line.isEmpty() || (line.charAt(0) == '#'))
			{  // This line in the scene file is a comment
				continue;
			}
			else
			{
				String code = line.substring(0, 3).toLowerCase();
				// Split according to white space characters:
				String[] params = line.substring(3).trim().toLowerCase().split("\\s+");

				if (code.equals("cam"))
				{
					camera = Parser.parseCamera(params);
					if(camera == null) {
						return;
					}
					System.out.println(String.format("Parsed camera parameters (line %d)", lineNum));
				}
				else if (code.equals("set"))
				{
					settings = Parser.parseSettings(params);
					settings.setImageWidth(imageWidth);
					settings.setImageHeight(imageHeight);
					if(settings == null) {
						return;
					}					System.out.println(String.format("Parsed general settings (line %d)", lineNum));
				}
				else if (code.equals("mtl"))
				{
					Material material = Parser.parseMaterial(params);
					if(camera == null) {
						return;
					}
					this.materials.add(material);

					System.out.println(String.format("Parsed material (line %d)", lineNum));
				}
				else if (code.equals("sph"))
				{
					Sphere sphere = Parser.parseSphere(params, materials.size(), materials);
					if(sphere == null) {
						return;
					}
					this.spheres.add(sphere);

					System.out.println(String.format("Parsed sphere (line %d)", lineNum));
				}
				else if (code.equals("pln"))
				{
					Plane plane = Parser.parsePlane(params, materials.size(), materials);
					if(plane == null) {
						return;
					}
					this.planes.add(plane);

					System.out.println(String.format("Parsed plane (line %d)", lineNum));
				}
				else if (code.equals("trg"))
				{
					Triangle triangle = Parser.parseTriangle(params, materials.size(), materials);
					if(triangle == null) {
						return;
					}
					this.triangles.add(triangle);
					
					System.out.println(String.format("Parsed light (line %d)", lineNum));
				}
				else if (code.equals("lgt"))
				{
					Light light = Parser.parseLight(params);
					if(light == null) {
						return;
					}
					this.lights.add(light);
					
					System.out.println(String.format("Parsed light (line %d)", lineNum));
				}
				else
				{
					System.out.println(String.format("ERROR: Did not recognize object: %s (line %d)", code, lineNum));
				}
			}
		}
		
		r.close();

                // It is recommended that you check here that the scene is valid,
                // for example camera settings and all necessary materials were defined.

		System.out.println("Finished parsing scene file " + sceneFileName);

	}

	/**
	 * Renders the loaded scene and saves it to the specified file location.
	 */
	public void renderScene(String outputFileName)
	{
		long startTime = System.currentTimeMillis();

		// Create a byte array to hold the pixel data:
		byte[] rgbData = new byte[this.imageWidth * this.imageHeight * 3];
		
		Ray ray;
		int sampleSize = settings.getSupSampLvl();
		Vector iPoint;			// intersection point
		List<GeneralObject> allObjects = new ArrayList<>();
		allObjects.addAll(this.spheres);
		allObjects.addAll(this.planes);
		allObjects.addAll(this.triangles);
		GeneralObject iObject;	// intersected object
		
		
		////////////////////// Ray Tracing pipeline //////////////////////		
		
		//for each pixel:
		for(int y = 0; y < imageHeight; y++) {
			for(int x = 0; x < imageWidth; x++) {
				double[] bgCol = settings.getBgCol();	// Background color
				double[] accuCol = {0,0,0};				// Accumulated color
				
				//for each sample:
				for(int i = 0; i < sampleSize; i++) {
					for(int j = 0; j < sampleSize; j++) {
						double sampX = Math.random(); // values between 0 and 1.
						double sampY = Math.random();
						ray = camera.ConstructRayThroughPixel(x+sampX, y+sampY);
						
						// Find intersection
						iObject = ray.findIntersectedObject(allObjects, ray);

						if(iObject == null) { // Not intersected by any object. Add only background color.
							ArrayServices.arrAdd(accuCol,bgCol);
						}
						else { // Intersected. Calculate color returned by intersected object.
							iPoint = ray.findIntersectionPointForClosestObj(ray, iObject);
							Intersection intersection = ray.createIntersectionObject(ray, iObject, iPoint);
							// Accumulate the color given from this specific sample
							ArrayServices.arrAdd(accuCol, computeColor(allObjects, ray, intersection, settings));
						}
					}
					
					// calculate the average color for all samples of that pixel
					ArrayServices.arrScalarMult(accuCol, 1/(sampleSize*sampleSize));
					
					setColorToPixel(x, y, accuCol[0], accuCol[1], accuCol[2], rgbData);
				}
				
				
			}
		}
		/////////////////// End of ray tracing pipeline //////////////////
		

		long endTime = System.currentTimeMillis();
		Long renderTime = endTime - startTime;

                // The time is measured for your own conveniece, rendering speed will not affect your score
                // unless it is exceptionally slow (more than a couple of minutes)
		System.out.println("Finished rendering scene in " + renderTime.toString() + " milliseconds.");

                // This is already implemented, and should work without adding any code.
		saveImage(this.imageWidth, rgbData, outputFileName);

		System.out.println("Saved file " + outputFileName);

	}
	

	
	/**
	 * Computes the color returned from the GeneralObject intersected by a ray.
	 * @param allObjects A list containing all GeneralObject's from the scene
	 * @param ray The ray that intersecters the object
	 * @param intersection The intersection information
	 * @param settings The general settings of the scene.
	 * @return The final color returned from the object.
	 */
	private double[] computeColor(List<GeneralObject> allObjects,Ray ray,Intersection intersection, Settings settings) {
		double[] bgCol = settings.getBgCol();
		int maxRecLvl = settings.getMaxRecursion();
		double[] finalCol = new double[3];
		finalCol = recComputeCol(allObjects, ray, intersection, bgCol, 1, maxRecLvl);
		return finalCol;
	}
	
	/**
	 * Recursively computes the color returned from the GeneralObject intersected by a ray.
	 * @param allObjects
	 * @param ray The ray that intersecters the object
	 * @param intersection The intersection information
	 * @param bgCol Color of the background
	 * @param curRecLvl Current recursion level.
	 * @param maxRecLvl Maximum recursion level.
	 * @return
	 */
	private double[] recComputeCol(List<GeneralObject> allObjects,Ray ray,Intersection intersection,  double[] bgCol, int curRecLvl, int maxRecLvl) {
		double[] curCol = {0,0,0};
		double transparency = intersection.getGeneralObject().getMaterial().getTransparency();
		double[] reflectionCol =  intersection.getGeneralObject().getMaterial().getRefCol();
		double[] bgTimesTrans = Arrays.copyOf(bgCol, 3);
		double[] lightCol;
		for(Light light : this.lights) { // Calculate the value of diffuse+specular from all lights.
			lightCol = light.lightCheck(allObjects, ray, intersection);
			if(lightCol != null) {
				ArrayServices.arrAdd(curCol, lightCol);
			}
		}
		ArrayServices.arrScalarMult(curCol, 1-transparency); // Calculate (diffuse+specular)*(1-transparency)
		
		// Haven't reached max recursion level
		if(curRecLvl != maxRecLvl) {
			Vector iPoint = intersection.getIntersectionPoint();
			GeneralObject iObject = intersection.getGeneralObject();

			// Transparency calculation
			Ray transRay = new Ray(iPoint, ray.getDirection()); // Change the source point but not the direction.
			GeneralObject transIObject = ray.findIntersectedObject(allObjects, transRay);
			if(transIObject != null) { // Found intersection
				Vector transIPoint = ray.findIntersectionPointForClosestObj(transRay, transIObject);
				Intersection transIntersection = transRay.createIntersectionObject(transRay, transIObject, transIPoint);
				ArrayServices.arrAdd(bgTimesTrans, recComputeCol(allObjects, transRay, transIntersection, bgCol, curRecLvl+1, maxRecLvl)); // Add the transparency color returned by other objects to the bg color
			}

			// Reflection calculation
			Vector normal = iObject.findNormalVector(iPoint, ray.getSource());
			Ray refRay = new Ray(iPoint, Vector.reflectVec(ray.getDirection(), normal)); // Change the source point and the direction.
			GeneralObject refIObject = ray.findIntersectedObject(allObjects, refRay);
			if(refIObject != null) { // Found intersection
				Vector refIPoint = ray.findIntersectionPointForClosestObj(refRay, refIObject);
				Intersection refIntersection = refRay.createIntersectionObject(refRay, refIObject, refIPoint);
				ArrayServices.arrMult(reflectionCol, recComputeCol(allObjects, refRay, refIntersection, bgCol, curRecLvl+1, maxRecLvl)); // Multiply object's reflection color by the reflected color
			}
		}
		
		
		ArrayServices.arrScalarMult(bgTimesTrans, transparency); // Calculate (background color)*transparency.
		ArrayServices.arrAdd(curCol, bgTimesTrans); // Calculate (background color)*transparency + (diffuse+specular)*(1-transparency)
		ArrayServices.arrAdd(curCol, reflectionCol); // Calculate (background color)*transparency + (diffuse+specular)*(1-transparency + (reflection color)
		
		return curCol;
	}
	
	
	/**
	 * Converts r,g,b data of Pixel[x,y] from a floating point between 0 and 1 to a byte between 0 and 255 and stores in rgbData
	 * 
	 * @param x 		The x coordinate of the pixel.
	 * @param y 		The y coordinate of the pixel.
	 * @param r 		The red component of the pixel.
	 * @param g 		The green component of the pixel.
	 * @param b 		The blue component of the pixel.
	 * @param rgbData	Pixel color data will be stored in this array.
	 */
	private static void setColorToPixel(int x, int y, double r, double g, double b, byte[] rgbData) {
		byte newR = (byte)Math.max(0, Math.min(255, 255*r));
		byte newG = (byte)Math.max(0, Math.min(255, 255*g));
		byte newB = (byte)Math.max(0, Math.min(255, 255*b));

		rgbData[(y * imageWidth + x) * 3] = newR;
		rgbData[(y * imageWidth + x) * 3 + 1] = newG;
		rgbData[(y * imageWidth + x) * 3 + 2] = newB;
		}




	//////////////////////// FUNCTIONS TO SAVE IMAGES IN PNG FORMAT //////////////////////////////////////////

	/*
	 * Saves RGB data as an image in png format to the specified location.
	 */
	public static void saveImage(int width, byte[] rgbData, String fileName)
	{
		try {

			BufferedImage image = bytes2RGB(width, rgbData);
			ImageIO.write(image, "png", new File(fileName));

		} catch (IOException e) {
			System.out.println("ERROR SAVING FILE: " + e.getMessage());
		}

	}

	/*
	 * Producing a BufferedImage that can be saved as png from a byte array of RGB values.
	 */
	public static BufferedImage bytes2RGB(int width, byte[] buffer) {
	    int height = buffer.length / width / 3;
	    ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
	    ColorModel cm = new ComponentColorModel(cs, false, false,
	            Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
	    SampleModel sm = cm.createCompatibleSampleModel(width, height);
	    DataBufferByte db = new DataBufferByte(buffer, width * height);
	    WritableRaster raster = Raster.createWritableRaster(sm, db, null);
	    BufferedImage result = new BufferedImage(cm, raster, false, null);

	    return result;
	}

	public static class RayTracerException extends Exception {

		public RayTracerException(String msg) {  super(msg); }
	}


	public static Settings getSettings() {
		return settings;
	}
	
}

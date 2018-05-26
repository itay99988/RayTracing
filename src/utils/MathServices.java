package utils;

import static java.lang.Math.sqrt;

public class MathServices {

	public static double[] quadraticSolver(double a, double b, double c)
	{
		double[] result=new double[2];
		
		double delta= b*b - 4*a*c;
		if (delta<0) return null;
		
		result[0]=((-1*b + sqrt(delta)) / (2*a));
		result[1]=((-1*b - sqrt(delta)) / (2*a));
		
		return result;
	}
}

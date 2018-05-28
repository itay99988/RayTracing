package utils;

public class ArrayServices {
	/**
	 * adds the values of arr2 to arr1 (assumes arr1.length == arr2.length and arr1 != null, arr2 != null)
	 * @param arr
	 */
	public static void arrAdd(double[] arr1, double[] arr2) {
		for(int i=0; i < arr1.length; i++) {
			arr1[i] += arr2[i];
		}
	}
	
	/**
	 * multiplies the values of arr2 with arr1 and stores the result in arr1 (assumes arr1.length == arr2.length and arr1 != null, arr2 != null)
	 * @param arr
	 */
	public static void arrMult(double[] arr1, double[] arr2) {
		for(int i=0; i < arr1.length; i++) {
			arr1[i] *= arr2[i];
		}
	}
	
	/**
	 * multiplies the values of arr with scalar
	 * @param arr
	 */
	public static void arrScalarMult(double[] arr, double scalar) {
		for(int i=0; i < arr.length; i++) {
			arr[i] *= scalar;
		}
	}
}

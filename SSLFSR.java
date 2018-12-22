/**
 * emulates a self-shrinking LFSR
 * and some other helpful functions
 * @author stan_ostrovskii
 *
 */
package rsa;
public class SSLFSR {	
	/**
	 * @param length how many bits of output you want
	 * @param fill and polynomial you are using
	 * @return the output stream as an array of booleans
	 */
	public static boolean[] outputLFSR(int length, boolean[] fill, boolean[] poly)
	{
		int currlength = 0;
		boolean [] output = new boolean [length];
		while(currlength<length)
		{
			if(fill[0]==true)
			{
				output[currlength] = fill[1];
				currlength++;
			}
			//step twice
			fill = step(poly, fill); 
			fill = step(poly, fill);
		}
		return output;
	}

	/**
	 * @param tapPolynomial represented as boolean array where the true represents a tap, false represents no tap on the bit
	 * @param fill polynomial represented as boolean array where true=1, false=0
	 * @return fill polynomial stepped one bit
	 */
	public static boolean [] step(boolean[] tapPolynomial, boolean[] fill)
	{
		//perform shift
		if(fill.length != tapPolynomial.length-1) {
			System.out.println("Invalid tap polynomial or fill"); System.exit(1); }
		boolean[] shiftedfill = new boolean[fill.length];
		for(int i=0; i<(fill.length-1);i++)
		{
			shiftedfill[i] = fill[i+1];
		}
		
		//determine last bit
		int sum = 0; 
		for(int i=0; i<(fill.length);i++)
		{
			if(tapPolynomial[i] && fill[i])
				sum++;
		}
		if(sum%2==0) shiftedfill[fill.length-1] = false;
		else shiftedfill[fill.length-1] = true;
		
		return shiftedfill;
	}
	

	/**
	 * @param binaryString representation of polynomial in string form of 0's and 1's 
	 * @return it's boolean representation
	 */
	public static boolean [] convert(String binaryString)
	{
		boolean [] poly = new boolean[binaryString.length()];
		for(int i=0; i<poly.length; i++)
		{
			if(binaryString.charAt(i)=='1')
				poly[i] = true;
			else if(binaryString.charAt(i)=='0')
				poly[i] = false;
			else { System.out.println("Invalid polynomial entry"); System.exit(1); }
		}
		return poly;
	}
	
	/**mostly for easier debugging
	 * @param booleans - the array of booleans to be printed as a binary string
	 */
	public static void printBooleans(boolean [] booleans)
	{
		for(boolean e: booleans)
		{
			if(e) System.out.print(1);
			else System.out.print(0);
		}
		System.out.println();
	}

}

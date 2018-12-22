package rsa;
import java.io.*;
import java.util.ArrayList;

import java.util.Scanner;


public class idk {

	//primitive tap polynomials of degree 12
	//list taken from https://baylor-ir.tdl.org/baylor-ir/bitstream/handle/2104/8792/GF2%20Polynomials.pdf?sequence=1&isAllowed=y
	public static final String[] DEGREE12POLYNOMIALSA = 
	{
		"1000001010011", "1000001101001", 
		"1000001111011", "1000001111101", 
		"1000010011001", "1000011010001",
		"1000011101011", "1000100000111", 
		"1000100011111", "1000100100011",
		"1000100111011", "1000101001111",
		"1000101010111", "1000101100001", 
		"1000101101011", "1000110000101", 
		"1000110110011", "1000111011001", 
		"1000111011111", "1001000001101", 
		"1001000110111", "1001000111101",
		"1001001100111", "1001001110011"
		,"1001001111111", "1001010111001","1001011000001", "1001011001011", "1001100001111",
		"1001100011101", "1001100100001","1001100111001", "1001100111111", "1001101001101"
		,"1001101110001", "1001110011001", "1001110100011", "1001110101001","1010000000111","1010000110001",
		"1010000110111","1010001001111","1010001011101","1010001100111","1010001110101","1010010100111","1010010101101",
		"1010011010011","1010100001111", "1010100011101", "1010101001101", "1010110010011", "1010111000101","1010111010111"
		,"1010111011101","1010111101011","1011000001001","1011001000111","1011001010101","1011001011001","1011010100101","1011010111101"
		,"1011100010101","1011100011001","1011101000011","1011101000101","1011101110101","1011110001001","1011110101101","1011110110011"
		,"1011110111111","1011111000001","1100001010111", "1100001011101","1100010010001","1100010010111","1100010111001","1100011101111"
		,"1100100011011","1100100110101","1100101000001","1100101100101","1100101111011","1100110001011","1100110110001","1100110111101",
		"1100111001001","1100111001111","1100111100111","1101000011011","1101000101011","1101000110011","1101001101001","1101010001011"
		,"1101011010001","1101011100001","1101011110101","1101100001011","1101100010011","1101100011111","1101101010111","1101110010001","1101110100111","1101110111111"
		,"1101111000001","1101111010011","1110000000101","1110000010001","1110000010111","1110000100111","1110001001101","1110010000111",
		"1110010011111","1110010100101","1110010111011","1110011000101","1110011001001","1110011001111","1110011110011","1110100000111","1110100100011",
		"1110101000011","1110101010001","1110101011011","1110101110101","1110110000101","1110110001001","1111000010101","1111000011001","1111000101111",
		"1111001000101","1111001010001","1111001100111","1111001110011","1111010001111","1111011100011","1111100010001","1111100011011","1111100100111",
		"1111101110001","1111110011001","1111110111011","1111110111101","1111111001001"
		//first 20
		
		
		
	};
	public static String[] DEGREE12POLYNOMIALS = new String[145];
	 
	
	public static final boolean [][] NONZEROFILLS = nonZeroFills();
	
	
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		/**
		boolean[] poly = convert(DEGREE12POLYNOMIALS[1]);
		boolean[] output = SSLFSR.outputLFSR(11, NONZEROFILLS[0], poly );
		printBooleans(output);
		*/
		
		 File file = new File("D://REAL.txt");

		    try {

		        Scanner sc = new Scanner(file);
		        
		        int i = 0;
		        while (sc.hasNextLine()) {
		        	DEGREE12POLYNOMIALS[i] = sc.nextLine();
		        	i++;
		        }
		        sc.close();
		    } 
		    catch (FileNotFoundException e) {
		        e.printStackTrace();
		    }

		
		ArrayList<boolean[]> legitKeyList = legitKeys();
		double prob = probOfOnes(legitKeyList);
		System.out.println(prob);
		System.out.println(longestK());
	
	}
	public static int longestK() {
		int k=0;
		for(String p: DEGREE12POLYNOMIALS)
		{
			boolean[] poly = convert(p);
			for(boolean [] f: NONZEROFILLS)
			{
				//counts the number of 0s before a 1 is first encountered in the output
				boolean oneFound = false;
				int count = 0;
				while(!oneFound)
				{
					boolean[] output = SSLFSR.outputLFSR(count+1, f, poly);
					if(output[count]==true) oneFound=true;
					count++;
				}
				if(count>k) k=count;
			}
		}
		return k;
	}

	
	public static ArrayList<boolean []> legitKeys()
	{
		ArrayList<boolean []> legitKeys = new ArrayList<boolean []>();
		for(String p: DEGREE12POLYNOMIALS)
		{
			boolean[] poly = convert(p);
			for(boolean [] f: NONZEROFILLS)
			{
				boolean[] output = SSLFSR.outputLFSR(11, f, poly);
				
				//this represents the key, first 12 members are the polynomial (the last 1 excluded), second 12 are the fill
				boolean[] key = new boolean[24];
				for(int i=0; i<12; i++)
				{
					key[i] = poly[i];
					key[i+12] = f[i];
				}
				
				//if consider remains true means the given key returns 0 for the first eleven bits
				boolean consider = true;
				for(boolean e: output)
				{
					if(e == true) consider = false;
				}
				if(consider)
					legitKeys.add(key);
			}
		}
		//debug
		for(boolean[] e: legitKeys)
		{
			printBooleans(e);
		}
		return legitKeys;
	}
	
	
	public static double probOfOnes(ArrayList<boolean[]> keys)
	{
		boolean [] origFill = new boolean[12];
		
		//the tap polynomial always ends with a 1 or else it is divisible by X
		boolean [] poly = new boolean[13];
		poly[12] = true;
	
		double ones = 0;
		for(boolean[] k: keys)
		{
			for(int i=0; i<12; i++)
			{
				poly[i] = k[i];
				origFill[i] = k[i+12];
			}
			boolean [] output = SSLFSR.outputLFSR(12, origFill, poly);
			printBooleans(output);
			if(output[11]==true) ones++;
		}
		System.out.println(ones + "/" + keys.size());
		return ones/keys.size();
	}
	/**
	 * possibly the ugliest piece of code I have ever written
	 * but for small numbers like 2^12 (4096) it will do
	 * @return array of all possible non zero fills length 12 represented as boolean. True = 1. False =0;
	 */
	public static boolean [][] nonZeroFills()
	{
		boolean [][] fills = new boolean[4095][12];
		
		//this ugliness represents first nonzero fill 0000 0000 0001 at puts it as fill 0
		for(int i=0; i<11; i++)
		{
			fills[0][i] = false;
		}
		fills[0][11] = true;
		
		//this fills in the rest
		for (int i=1; i<4095; i++)
		{
			//sets last bit based on previous last bit
		   boolean[] f = new boolean[12];
		   if(fills[i-1][11]==true) f[11] = false; 
		   else f[11] = true; 
		   
		   //sets each remaining bit based on what the lower bits were
		   for(int j=10; j>=0; j--)
		   {
			   f[j] = fills[i-1][j];
			   //if flag is true, change bit, else keep previous bit
			   boolean flag = true; 
			   for(int k=11; k>j; k--)
			   {
				   if(fills[i-1][k]==false) flag = false;   	
			   }
			   if(flag && f[j]) f[j] = false;
			   else if (flag && !f[j]) f[j] = true;
		   }
		   fills[i] = f;
		}
		/**debugging to prove it works
		for(boolean [] e: fills)
		{
			printBooleans(e);
		}
		*/
		return fills;
	}
	
	/**
	 * @param binaryString representation of polynomial in string form of 0's and 1's 
	 * see the DEGREE12POLYNOMIALS array
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

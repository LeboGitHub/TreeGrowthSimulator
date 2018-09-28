import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class Parallel {
	
	// variable used throughout the program
	//array of trees and a terrain matrix(2D array)
	static Tree [] trees;
	static double [][] terrain;
	static int terrainX;
	static int terrainY;
	static double total;
	static double startTime;
	/**
	 * Method used to read data from a text files
	 * @param input (The name/path of the text file)
	 */
	public static void setUp(String input) {

		//get the file name
		String inputFileName = input;
		//int terrainX; int terrainY; 
		int numTrees=0;;
		try {
			//create a scanner to read from the text file
			Scanner sc = new Scanner(System.in);
			File file = new File(inputFileName);
			sc = new Scanner(file);
			// get the x and y parameters for the terrain matrix
			terrainX = sc.nextInt();
			terrainY = sc.nextInt();
			//create the terrain object
			int inputs =0;
			terrain = new double[terrainX][terrainY];
			//populate the terrain matrix
			for(int outter = 0 ; outter<terrainX ; outter++)
				for(int inner = 0; inner < terrainY; inner++)
				{
					terrain[inner][outter] = sc.nextFloat();
					inputs++;
				}
			//get the number of trees and set the size of the trees array
			numTrees = sc.nextInt();
			trees = new Tree[numTrees];
			//populate the trees array
			for(int iLoop= 0; iLoop < numTrees ; iLoop++) {
				int xPos = sc.nextInt();
				int yPos = sc.nextInt();
				int e = sc.nextInt();
				Tree tree = new Tree(xPos,yPos,e);
				trees[iLoop] = tree;
			}

			sc.close();
		}catch(Exception e)
		{
			System.out.println("Error reading file");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	/**
	 * Method to get the sum of the sunlight values for a single tree
	 * @param tree
	 * @return the total sunlight received by a tree
	 */
	public static float canopySum(Tree tree) {
		//initialise values
		float sum =0;
		int xPos = tree.getXPos();
		int yPos = tree.getYPos();
		int extent = tree.getE();
		
		//get the total sunlight for a tree
		for(int outter= xPos; outter<=(xPos+extent-1);outter++ )
			for(int inner = yPos; inner <= (yPos+extent-1);inner++) {
				//ensure that you do not go outside the bounds of the terrain
				if((outter < terrainX)&&(inner < terrainY)) {
					sum+= terrain[outter][inner];
				}
			}
		
		return sum;
	}
	
	static double sumArray(Tree[] array) {
		return (double) ForkJoinPool.commonPool().invoke(new SumArray(array,0,array.length));
	}
	
	public static void output(String name) {
		String fileName = name;
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,true));
			writer.write(String.valueOf(total/trees.length));
			writer.newLine();
			writer.write(String.valueOf(trees.length));
			writer.newLine();
			for(int iLoop =0; iLoop< trees.length; iLoop++) {
				double sun = trees[iLoop].getTotal();
				writer.write(String.valueOf(sun));
				writer.newLine();
			}
			writer.close();
		}catch(Exception e)
		{
			System.out.println("Cant write to missing file");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private static void tick() {
		startTime = System.currentTimeMillis();
	}
	
	private static double tock() {
		return ((System.currentTimeMillis()-startTime)/1000.0f);
	}
	
	public static void experiment() {
		double startTime = 0;
		//tick();
		//double sum = sumArray(trees);
		//double time = tock();
		//System.out.println("1. "+String.valueOf(time));
		System.out.println("Parallel data");
		for(int iLoop = 1; iLoop<11;iLoop++) {
			tick();
			double sum = sumArray(trees);
			double time = tock();
			System.out.println(String.valueOf(iLoop)+". "+ String.valueOf(time));
		}
		
		
	}
	
    public static void main(String args[]) {
		//get the inputs
		//setUp(args[0]);
		setUp("src/sample_input.txt");
		total = (double)sumArray(trees);
		System.out.println(String.valueOf(total/trees.length));
		System.out.println(String.valueOf(trees.length));
		output("output.txt");
		//output(args[1]);
		experiment();
	}

}

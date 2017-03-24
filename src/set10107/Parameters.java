package set10107;

import java.util.Random;

import set10107.data.Reader;

public class Parameters {

	public static int numInput = 1;
	public static int numHidden = 4;
	public static int numOutput = 1;
	public final static double minGene = -5; // specifies minimum and maximum weight values
	public final static double maxGene = +5;
	public final static int popSize = 50;
	public final static int maxGeneration = 10000;
	public final static double exitError = 0.0; // terminate if MSE smaller than this value
	public final static double mutateRate = 0.10; // mutation rate for mutation operator
	public final static double mutateChange = 0.01; // mutation rate for mutation operator
	public static final long seed = System.currentTimeMillis();
	public static double[][] trainData;
	public static double[][] testData;
	public static int numGenes;
	public final static Random random = new Random(seed);
	//New Parameters
	//Parameter for number of individual to have tournment
	public final static int numTournamentSelection = 2;
	//number of runs to carry out for each operator variation
	public final static int numRuns = 50;
	//Elitism parameters
	//Set maximum elitism to 10% of the maximum population
	public final static int eliteNum = (int) (popSize * 0.10);
	
	public static void setDataSet(String dataSet)
	{
		trainData = Reader.getTrainingData(dataSet);
		testData = Reader.getTestData(dataSet);		
		if (dataSet.equals("C")) {
			numInput = 2;
		}		
	}
}

package set10107;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.*;

public class EvolutionaryTrainer extends NeuralNetwork {

	public EvolutionaryTrainer() {
		super();
	}
	
	public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException, NoSuchFieldException 
	{
		//Datasets to use for function approximation
		String[] dataSets = {"A","B","C"};
		
		//Operator Variations to run
		String[] operatorVariations = 
			{
					"SR_UMUT_CSP",
					"SR_NMUT_CDP",
					"SR_UMUT_CDP",
					"SR_NMUT_CSP",
					"ST_UMUT_CSP",
					"ST_NMUT_CDP",
					"ST_UMUT_CDP",
					"ST_NMUT_CSP"	
			};
		
		//Array to hold best 50 individuals from all operator variations
		Individual[] outputData = new Individual[operatorVariations.length * dataSets.length];
		
		//Start training for all datasets over set operator variations
		for(int i = 0; i < dataSets.length; ++i)
		{
			//Set data set
			Parameters.setDataSet(dataSets[i]);
			
			//Temporary individual to hold total average error
			Individual tempIndividual = new Individual();
			
			//train over set operator variations		
			for(int j = 0; j < operatorVariations.length; ++j)
			{	
				//Local string array to save 50 best individuals
				Individual[] lastFiftyBest = new Individual[50];
				
				//Get 50 best 
				for(int k = 0; k < Parameters.numRuns; ++k)
				{
					/**
					 * train the NN using our EA
					 */
					EvolutionaryTrainer nn = new EvolutionaryTrainer();
					Individual bestIndividual = nn.train(operatorVariations[j]);

					/**
					 * Show best weights found
					 */
					System.out.println("Training complete: Function " + dataSets[i] + "\nOperators: " + operatorVariations[j]);
					System.out.println("\nFinal weights and bias values:");
					bestIndividual.weights = showVector(bestIndividual.chromosome, 10, 3, true);

					/**
					 * Show accuracy on training data
					 */
					nn.setWeights(bestIndividual.chromosome);
					double trainAcc = nn.testNetwork(Parameters.trainData);
					System.out.print("\nAccuracy on training data = ");
					System.out.println(trainAcc);
					bestIndividual.trainingError = trainAcc;

					/**
					 * Show accuracy on unseen test data
					 */
					double testAcc = nn.testNetwork(Parameters.testData);
					System.out.print("\nAccuracy on test data = ");
					System.out.println(testAcc);
					System.out.println("\nEnd NN training demo");
					bestIndividual.testError = testAcc;
					
					//Get best individual of this run
					lastFiftyBest[k] = bestIndividual;
					
					//Add running averages to local individual 
					tempIndividual.testError += bestIndividual.testError;
				}
				
				//Set temp individuals datasetvariation string for running averages & dataset
				tempIndividual.testError = tempIndividual.testError / Parameters.numRuns;
				tempIndividual.dataSetVariation = operatorVariations[j];
				tempIndividual.dataset = dataSets[i];
				
				//Add tempIndividual to running averages individual array
				outputData[j] = tempIndividual;
				
				//Output last 50 runs, dataset and operator variation to csv
				try 
				{
					//Write 
					writeOutputCSV(dataSets[i], operatorVariations[j], lastFiftyBest);
					
				} catch (IOException e) { }
				
			}
			
		}
		
		//Write out averages for each function and operator variation
		try 
		{
			writeOutputCSV("allaverages", null, outputData);
		} catch (IOException e) {}

	}

	public Individual train(String operationVariation) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException, NoSuchFieldException {
		/**
		 *  initialize the population
		 */
		Individual[] population = initialise();
		
		/**
		 * used to store a copy of the best Individual in the population
		 */
		Individual bestIndividual = getBest(population);
		
		//Get methods for each operator
		List<String> operatorList = Arrays.asList(operationVariation.split("_"));
		Method selection = Selections.class.getMethod(Constants.class.getDeclaredField(operatorList.get(0).trim()).get(null).toString(), Individual[].class);
		Method mutation = Mutations.class.getMethod(Constants.class.getDeclaredField(operatorList.get(1).trim()).get(null).toString(), Individual[].class);
		Method crossover = Crossovers.class.getMethod(Constants.class.getDeclaredField(operatorList.get(2).trim()).get(null).toString(), Individual.class, Individual.class);

		//Running Best Error
	
		/**
		 * main EA processing loop
		 */
		int gen = 0;
		boolean done = false;
		while (gen < Parameters.maxGeneration && done == false) 
		{
			
			//Select 2 good Individuals 
			Individual parent1 = (Individual) selection.invoke(null, new Object[] {population});	
			Individual parent2 = (Individual) selection.invoke(null, new Object[] {population});
			
			//Generate 2 new children by crossover 
			Individual[] children = (Individual[]) crossover.invoke(null, (Object)parent1 , (Object)parent2);
			
			//Mutate children
			children = 	(Individual[]) mutation.invoke(null, (Object)children);
			
			//Evaluate the new individuals
			evaluateIndividuals(children);
			
			//Replace (sorts the population and replaces the two worst individuals)
			replace(children[0], children[1], population); 
			
			//introduce some diversity
			injectImmigrant(population);

			//check that the best hasn't improved
			bestIndividual = getBest(population);
		
			//check our termination criteria
			if(bestIndividual.error < Parameters.exitError){
				done = true;
			}
			
			++gen;
		}
		
		return bestIndividual;
	} // Train

	/**
	 * Method to output run data to CSV
	 * @param operatorVariation - Containing Operator Variation constant
	 * @param outputIndividuals - 50 Best individuals for that the run
	 * @param dataSet - the dataset being used
	 * @throws IOException 
	 */
	@SuppressWarnings("resource")
	private static void writeOutputCSV(String dataSet, String operatorVariation, Individual[] outputIndividuals) throws IOException
	{
		//If individual dataset print
		if(dataSet != "allaverages")
		{
		//New file for test
		File file = new File(dataSet + "_" + operatorVariation + "_" + Parameters.numRuns + ".csv");
		file.createNewFile();
		
		//Used for calculating total MSE
		double avgMSE = 0;
		
		//Calculate average Training accuracy
		double avgTrainAccuracy = 0;
				
		//Write contents to file readable in CSV
		FileWriter writer = new FileWriter(file);
		
		for(int i = 0; i < Parameters.numRuns; ++i)
		{
			//Hold individual 
			Individual tempInd = (Individual) outputIndividuals[i];
			
			//Test Error
			double testError = tempInd.testError;
			
			//Add error to MSE for calculating total MSE
			avgMSE += testError;
			
			//add training accuracy for average
			avgTrainAccuracy += tempInd.trainingError;
			
			//Write to File
			writer.write(dataSet + "," + operatorVariation + "," + i + "," + testError + "," + tempInd.weights + "\n");	
		}
		
		//Calculate total MSE and training accuracy
		avgMSE = avgMSE / Parameters.numRuns;
		avgTrainAccuracy = avgTrainAccuracy / Parameters.numRuns;
		writer.write("Mean Squared Error = " + avgMSE + "\n" + "Training Accuracy Average = " + avgTrainAccuracy);
	    writer.flush();
	    writer.close();
		}
		//If not individual dataset print all mean average squared error for all datasets
		else
		{
			//New file for all averages
			File file = new File(dataSet + "_num_of_variations_" + outputIndividuals.length + ".csv");
			file.createNewFile();
			
			//Write contents to file readable in CSV
			FileWriter writer = new FileWriter(file);
			
			//print out averages for variations
			for(Individual individual : outputIndividuals)
			{
				//Write to File
				writer.write(individual.dataset + "," + individual.dataSetVariation + "," + individual.testError + "\n");	
			}
			
			//Close writer
		    writer.flush();
		    writer.close();
		}
		
	}
	
	/**
	 * Sets the fitness (mean squared error) of the individual passed as parameters
	 * 
	 */
	private void evaluateIndividuals(Individual[] individuals) {
		for(Individual individual : individuals){
			individual.error = meanSquaredError(Parameters.trainData, individual.chromosome);
		}		
	}

	/**
	 * Returns the individual with the lowest error (best fitness) from the array passed
	 *  
	 */
	private Individual getBest(Individual[] population) {
		Individual bestIndividual = null;
		for (Individual individual : population) {
			if (bestIndividual == null) {
				bestIndividual = individual.copy();
			} else if (individual.error < bestIndividual.error) {
				bestIndividual = individual.copy();
			}
		}
		return bestIndividual;
	}

	/**
	 * Generates a randomly initialised population
	 * 
	 */
	private Individual[] initialise() {
		Individual[] population = new Individual[Parameters.popSize];
		for (int i = 0; i < population.length; ++i) {
			population[i] = new Individual();
			double error = meanSquaredError(Parameters.trainData, population[i].chromosome);
			population[i].error = error;
		}
		return population;
	}
	

	/**
	 * 
	 * Replaces the worst two members of the population with the 2 children
	 * 
	 */
	private void replace(Individual child1, Individual child2, Individual[] population) {
		// place child1 and child2 replacing two worst individuals
		int popSize = population.length;
		Arrays.sort(population);
		population[popSize - 1] = child1;
		population[popSize - 2] = child2;
	}

	/**
	 * Replaces the third worst member of the population with a new randomly initialised individual 
	 * @param population
	 */
	private void injectImmigrant(Individual[] population) {

		Arrays.sort(population);
		Individual immigrant = new Individual();
		evaluateIndividuals(new Individual[] {immigrant});
		
		// replace third worst individual	
		population[population.length - 3] = immigrant; 	
	}

	static String showVector(double[] vector, int valsPerRow, int decimals, boolean newLine) 
	{
		StringBuilder returnVector = new StringBuilder();
		for (int i = 0; i < vector.length; ++i) 
		{
			if (i % valsPerRow == 0)
			{
				System.out.println("");
				returnVector.append("");
			}
			if (vector[i] >= 0.0)
			{
				System.out.print(" ");
				returnVector.append(" ");
			}
			returnVector.append(vector[i] + " ");
			System.out.print(vector[i] + " ");
		}
		if (newLine == true)
		{
			returnVector.append("");
			System.out.println("");
		}
		
		//Return weights
		return returnVector.toString();
	}

	static void showMatrix(double[][] matrix, int numRows, int decimals, boolean newLine) {
		for (int i = 0; i < numRows; ++i) {
			System.out.print(i + ": ");
			for (int j = 0; j < matrix[i].length; ++j) {
				if (matrix[i][j] >= 0.0)
					System.out.print(" ");
				else
					System.out.print("-");
				;
				System.out.print(Math.abs(matrix[i][j]) + " ");
			}
			System.out.println("");
		}
		if (newLine == true)
			System.out.println("");
	}
}

package set10107;

import java.util.*;

public class Individual implements Comparable<Individual> {

	public double[] chromosome; // represents a solution
	public double error; // smaller values are better for minimization
	//Hold copy of weights
	public String weights;
	//Hold tested Training error
	double trainingError;
	//Hold tested test error
	double testError;
	//Hold dataset variation for running average individual
	String dataSetVariation;
	//Hold dataset A,B,C
	String dataset;
	
	public Individual() {
		this.chromosome = new double[Parameters.numGenes];
		for (int i = 0; i < this.chromosome.length; i++) {
			this.chromosome[i] = (Parameters.maxGene - Parameters.minGene) * Parameters.random.nextDouble()
					+ Parameters.minGene;
		}
	}

	@Override
	public int compareTo(Individual other) // smallest error to largest
	{
		if (this.error < other.error)
			return -1;
		else if (this.error > other.error)
			return 1;
		else
			return 0;
	}

	public Individual copy() {
		// Individual copy = new Individual(numGenes, minGene, maxGene);
		Individual copy = new Individual();
		copy.chromosome = Arrays.copyOf(chromosome, chromosome.length);
		copy.error = error;
		return copy;
	}
	
	@Override
	public String toString() {
		
		String str = "" + error + "\t";
		for(int i = 0; i < chromosome.length - 2; i++){
			str += chromosome[i] + ", ";
		}
		str += chromosome[chromosome.length - 1];
		return str;
	}

}

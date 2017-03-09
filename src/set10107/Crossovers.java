package set10107;

public class Crossovers 
{
	
	/**
	 * Crossover / Reproduction - Original crossover function
	 * NEEDS REPLACED with proper method
	 * this code just returns exact copies of the parents
	 */
	///* Original Crossover function
	static Individual[] reproduce(Individual parent1, Individual parent2) {
		int numGenes = parent1.chromosome.length;
		int cross = Parameters.random.nextInt(numGenes);

		Individual child1 = new Individual();
		Individual child2 = new Individual();

		for (int i = 0; i < numGenes; ++i)
			child1.chromosome[i] = parent1.chromosome[i];

		for (int i = 0; i < numGenes; ++i)
			child2.chromosome[i] = parent2.chromosome[i];

		mutate(child1);
		mutate(child2);

		Individual[] result = new Individual[2];
		result[0] = child1;
		result[1] = child2;

		return result;
	} // Reproduce
	
	/*
	 * Single-Point crossover
	 */
	static Individual[] crossoverSinglePoint(Individual parent1, Individual parent2)
	{
		int numGenes = parent1.chromosome.length;
		int crossPoint = Parameters.random.nextInt(numGenes);
		
		Individual child1 = new Individual();
		Individual child2 = new Individual();
		
		for (int i = 0; i < numGenes; ++i)
		{
			if(i < crossPoint)
			{
			child1.chromosome[i] = parent1.chromosome[i];
			child2.chromosome[i] = parent2.chromosome[i];
			} else
			{
		    child1.chromosome[i] = parent2.chromosome[i];
		    child2.chromosome[i] = parent1.chromosome[i];
			}
		}
		
		Individual[] result = new Individual[2];
		result[0] = child1;
		result[1] = child2;
		return result;
		
	}

}

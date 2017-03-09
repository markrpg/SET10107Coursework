package set10107;

import java.util.Random;

public class Mutations 
{
	/**
	 * Uniform mutation randomly mutates children's genes within bounds
	 * @param children - children for mutation.
	 * @return - mutated children.
	 */
	static Individual[] uniformMutation(Individual[] children)
	{
		Random rand = new Random();
		
		Individual child1 = new Individual();
		Individual child2 = new Individual();
		
		for(int i = 0; i < child1.chromosome.length; ++i )
		{
			if(rand.nextDouble() < Parameters.mutateRate)
			{
				child1.chromosome[i] = Parameters.minGene + Math.random() * (Parameters.minGene - Parameters.maxGene);
			}
			
			if(rand.nextDouble() < Parameters.mutateRate)
			{
				child2.chromosome[i] = Parameters.minGene + Math.random() * (Parameters.minGene - Parameters.maxGene);
			}
		}
		
		Individual[] result = new Individual[2];
		result[0] = child1;
		result[1] = child2;
		return result;
	}

}

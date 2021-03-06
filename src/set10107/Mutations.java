package set10107;

import java.util.Random;

public class Mutations 
{
	/**
	 * Uniform mutation randomly mutates children's genes within bounds
	 * @param children - children for mutation.
	 * @return - mutated children.
	 */
	public static Individual[] uniformMutation(Individual[] children)
	{
		Random rand = new Random();
		
		Individual child1 = children[0];
		Individual child2 = children[1];
		
		for(int i = 0; i < child1.chromosome.length; ++i )
		{
			if(rand.nextDouble() < Parameters.mutateRate)
			{
				child1.chromosome[i] = Parameters.minGene + Math.random() * (Parameters.maxGene - Parameters.minGene);
				child2.chromosome[i] = Parameters.minGene + Math.random() * (Parameters.maxGene - Parameters.minGene);
			}		

		}
		
		Individual[] result = new Individual[2];
		result[0] = child1;
		result[1] = child2;
		return result;
	}
	
	/**
	 * Negative inversion mutation randomly inverts individual chromosome values
	 * @param children - children for mutation.
	 * @return - mutated children.
	 */
	public static Individual[] negativeInversionMutation(Individual[] children)
	{
		Random rand = new Random();
		
		Individual child1 = children[0];
		Individual child2 = children[1];
		
		for(int i = 0; i < child1.chromosome.length; ++i )
		{
			if(rand.nextDouble() < Parameters.mutateRate)
			{
				child1.chromosome[i] = Math.min(Parameters.maxGene, Math.max(Parameters.minGene, child1.chromosome[i] * -1));
				child2.chromosome[i] = Math.min(Parameters.maxGene, Math.max(Parameters.minGene, child2.chromosome[i] * -1));
			}		

		}
		
		Individual[] result = new Individual[2];
		result[0] = child1;
		result[1] = child2;
		return result;
	}
	
	

}

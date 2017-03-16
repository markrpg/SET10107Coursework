package set10107;

import java.util.Random;

public class Selections 
{
    /**
     * Selection -- Original Selection function
     *
     * NEEDS REPLACED with proper selection
     * this just returns a copy of a random member of the population
     */
	static Individual select(Individual[] population) 
	{		
		int popSize = population.length;
		Individual parent = population[Parameters.random.nextInt(popSize)].copy();
		return parent;
	}
	

	/**
	 * Selection method - Roulette Wheel Selection
	 * @param population - the population to select from.
	 * @return - selected individual.
	 */
	public static Individual rouletteSelection(Individual[] population)
	{
		//Get total fitness
		double totalFitness = 0;
		for(int i = 0; i < Parameters.popSize; ++i)
			totalFitness += population[i].error;
		
		//Get slice value
		double sliceValue = Math.abs(new Random().nextDouble() * totalFitness);
		
		//Hold selection index
		int j = 0;
		
		//Select individual
		for(j = 0; j < Parameters.popSize && sliceValue > 0; ++j) sliceValue -= Math.abs(population[j].error);
		return population[Parameters.popSize - j];
	}
	
	/**
	 * Selects the best individual from two random individuals
	 * @param population - the population to select from
	 * @return - return best found individual
	 */
	public static Individual tournamentSelection(Individual[] population)
	{
		//Holds best individual, select random individual at start
		Individual bestYet = population[Parameters.random.nextInt(population.length)];;
		
		//Iterate through population finding the best individual according to k number
		for(int i = 0; i<Parameters.numTournamentSelection; ++i)
		{
			//Randomly select a competitor
			Individual competitorIndividual = population[Parameters.random.nextInt(population.length)];
			
			//If competitor beats best individual yet set new best
			if(competitorIndividual.error < bestYet.error)
			{
				bestYet = competitorIndividual;
			}
		}
		
		//After iteration return best individual
		return bestYet;
	}
	
	
	
	

}

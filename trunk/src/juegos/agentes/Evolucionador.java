package juegos.agentes;

import java.util.Arrays;


import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.DeltaFitnessEvaluator;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;

/** Resuelve el problema de las ocho reinas utilizando un algoritmo genético.
 * Los individuos se representan mediante vectores de 8 números del 1 al 8,
 * representando cada celda una columna del tablero y cada número la fila donde
 * se ubica cada reina.
 * La función de aptitud se define como la cantidad de reinas que comparten una
 * misma línea, por cada horizontal y diagonal del tablero. Ergo, lo que se 
 * busca es minimizar este valor.
 * La población es simple y homogénea, con 100 individuos iniciados al azar.
 * Los operadores genéticos son los de la configuración por defecto de JGAP.
 * La evolución termina cuando se encuentra una solución (aptitud 0) o luego de 
 * 1000 generaciones.
 */
public class Evolucionador {
	public static final int POPULATION_SIZE = 100;
	public static final long LOG_TIME = 100; // msecs
	public static final int TAMANO_GENOTIPO=7;
	public static final FitnessFunction FITNESS_FUNCTION = new Aptitud();
	
	public static boolean endEvolution(Genotype population, int generation) {
		return generation > 1000 || population.getFittestChromosome().getFitnessValue() == 0.0;
	}
	
	public static void main(String[] args) throws InvalidConfigurationException {
		Configuration conf = new DefaultConfiguration();
		Configuration.reset();
		conf.setFitnessEvaluator(new DeltaFitnessEvaluator());
		//conf.setPreservFittestIndividual(true);
	    conf.setFitnessFunction(FITNESS_FUNCTION);
	    
	    Gene[] sampleGenes = new Gene[TAMANO_GENOTIPO];
	    for (int i = 0; i < TAMANO_GENOTIPO; i++) {
	    	sampleGenes[i] = new IntegerGene(conf, 1, TAMANO_GENOTIPO);
	    }
	    IChromosome sampleChromosome = new Chromosome(conf, sampleGenes);
	    conf.setSampleChromosome(sampleChromosome);
	    
	    
	    conf.setPopulationSize(POPULATION_SIZE);
	    Genotype population = Genotype.randomInitialGenotype(conf);
	    
	    for (int i=0;i<POPULATION_SIZE;i++){
	    	System.out.println("poblador "+i+": "+ population.getPopulation().getChromosome(i));
	    	//System.out.println("poblador "+i+": "+ population.getPopulation().getChromosome(i));

	    }
	    
	    
	    System.out.println("Evolving.");
	    long startTime = System.currentTimeMillis();
	    long lastTime = startTime;
	    for (int i = 1; !endEvolution(population, i); i++) {
	    	if (System.currentTimeMillis() - lastTime > LOG_TIME) {
	    		lastTime = System.currentTimeMillis();
	    		IChromosome fittest = population.getFittestChromosome();
	    		System.out.println("\tEvolution time: "+ (lastTime - startTime) 
	    				+" ms, generation "+ i +", fittest = "+
	    				Arrays.toString(Aptitud.chromosomeToArray(fittest))
	    				+" with "+ fittest.getFitnessValue() +" fitness.");	
	    	}

	    	population.evolve();
	    }
	    long endTime = System.currentTimeMillis();
	    
	    System.out.println("Total evolution time: "+ (endTime - startTime) +" ms");
	    IChromosome fittest = population.getFittestChromosome();
	    System.out.println("Fittest solution is "+ 
	    		Arrays.toString(Aptitud.chromosomeToArray(fittest)) 
	    		+" with "+ fittest.getFitnessValue() +" fitness.");
	}

}

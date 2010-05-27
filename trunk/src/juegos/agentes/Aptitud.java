package juegos.agentes;

import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

public class Aptitud extends FitnessFunction {
	private static final long serialVersionUID = -1974363396150195364L;
	public static final int TAMANO_GENOTIPO=7;
	/** Convert from IChromosome to int[TAMANO_GENOTIPO]. */
	public static final int[] chromosomeToArray(IChromosome chromosome) {
		int[] genes = new int[TAMANO_GENOTIPO];
		for (int i = 0; i < TAMANO_GENOTIPO; i++) {
			genes[i] = (Integer)chromosome.getGene(i).getAllele();
		}
		return genes;
	}
	
	@Override protected double evaluate(IChromosome chromosome) {
		int[] genes = chromosomeToArray(chromosome);

		// Test for attacks.
		double attackCount = 0.0;
		for (int pos = 0; pos < TAMANO_GENOTIPO; pos++) {
			int currentGene = genes[pos];
			// Backwards
			for (int i = 1; pos - i >= 0; i++) {
				int thisGene = genes[pos - i];
				if (thisGene == currentGene 
						|| thisGene == currentGene - i 
						|| thisGene == currentGene + i) {
					attackCount++;
				}
			}
			// Forward
			for (int i = 1; pos + i <TAMANO_GENOTIPO; i++) {
				int thisGene = genes[pos + i];
				if (thisGene == currentGene 
						|| thisGene == currentGene - i 
						|| thisGene == currentGene + i) {
					attackCount++;
				}
			}
		}
		
		return attackCount;
	}

}

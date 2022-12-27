package evolution.genomes;

import evolution.util.Direction;
import java.util.Random;

/**
 * When mutating genes' values are picked randomly. Normal mix behavior.
 */
public class RandomGenome extends Genome {

    // overrides

    /**
     * Every mutated gene's value is picked randomly (can't be the same one).
     * @param times how many genes are mutated
     * @throws RuntimeException if mutation count is invalid
     * @return itself
     */
    @Override
    public Genome mutate(int times) throws RuntimeException {
        if (times < 0 || times > this.getSize()) throw new RuntimeException(String.format("invalid mutate() 'times' argument value: '%s' (must be between 0 and gene's size)", times));
        for(int i = 0; i < times; i++) {
            int oldValue = this.genes[i];
            while (this.genes[i] == oldValue)
                this.genes[i] = new Random().nextInt(Direction.size());
        }

        return this;
    }

    /**
     * Create exact copy of this object.
     * @return new copied object
     */
    @Override
    public Genome copy() {
        return new RandomGenome(this.genes.clone());
    }

    // constructors

    public RandomGenome() {
        super();
    }

    public RandomGenome(int[] genes) {
        super(genes);
    }
}

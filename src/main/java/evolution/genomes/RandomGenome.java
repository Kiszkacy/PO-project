package evolution.genomes;

import evolution.util.Direction;
import java.util.Random;

public class RandomGenome extends Genome {

    // overrides

    /**
     * Every mutated gene's value is picked randomly (can't be the same one).
     * @param times how many genes are mutated
     * @return itself
     */
    @Override
    public Genome mutate(int times) {
        // TODO check times >= 0
        for(int i = 0; i < times; i++) {
            int oldValue = this.genes[i];
            while (this.genes[i] == oldValue)
                this.genes[i] = new Random().nextInt(Direction.size());
        }

        return this;
    }

    @Override
    public Genome copy() {
        return new RandomGenome(this.genes);
    }

    // constructors

    public RandomGenome() {
        this.genes = new Random().ints(this.size, 0, Direction.size()).toArray();
    }

    public RandomGenome(int[] genes) { // TODO check genes size
        this.genes = genes;
    }
}

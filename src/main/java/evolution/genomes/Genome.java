package evolution.genomes;

import evolution.util.Direction;
import evolution.util.Config;
import java.util.Arrays;
import java.util.Random;


public abstract class Genome {
    protected int[] genes;
    protected int size = Config.getGenomeSize();

    /**
     * Mutates this by changing its genes.
     * @param times how many genes are mutated
     * @return itself
     */
    abstract public Genome mutate(int times);

    /**
     * Mixes this genome with another by given ratio.
     * @param with genome that is being mixed with this one
     * @param ratio how much of the other genome is mixed with this one (i.e. value of 0.0 does not change anything)
     * @param fromLeft determines from which side the other genome is being mixed
     * @return itself
     */
    public Genome mix(Genome with, double ratio, boolean fromLeft) { // TODO exceptions (ratio check | genome size check)
        int left = fromLeft ? 1 : 0;
        int swapCount = (int)Math.round(ratio * this.size);

        for(int i = 0; i < swapCount; i++)
            this.genes[i + (1-left)*(this.size-swapCount)] = with.getGene(i);

        return this;
    }

    /**
     * Returns the exact new copy of itself.
     * @return new copy
     */
    abstract public Genome copy();

    // overrides

    @Override
    public String toString() {
        return Arrays.toString(this.genes);
    }

    // constructors

    public Genome() {
        this.genes = new Random().ints(this.size, 0, Direction.size()).toArray();
    }

    public Genome(int[] genes) { // TODO check genes size
        this.genes = genes;
    }

    // getters/setters

    public int getSize() {
        return this.size;
    }

    public int getGene(int at) { // TODO check range
        return this.genes[at];
    }
}
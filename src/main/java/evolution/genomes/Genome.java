package evolution.genomes;

import evolution.util.Direction;
import evolution.util.Config;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

/**
 * Class which acts as a container for genes. Is also responsible for mutate() and mix() methods
 * that can be used to define more complex behavior on a wider scope.
 */
public abstract class Genome {
    protected int[] genes;
    protected int size;

    /**
     * Mutates this by changing its genes.
     * @param times how many genes are mutated
     * @throws RuntimeException if mutation count is invalid
     * @return itself
     */
    abstract public Genome mutate(int times) throws RuntimeException;

    /**
     * Mixes this genome with another by given ratio.
     * @param with genome that is being mixed with this one
     * @param ratio how much of the other genome is mixed with this one (i.e. value of 0.0 does not change anything)
     * @param fromLeft determines from which side the other genome is being mixed
     * @throws RuntimeException if other genome is of different size or ratio argument is invalid
     * @return itself
     */
    public Genome mix(Genome with, double ratio, boolean fromLeft) throws RuntimeException {
        if (ratio < 0.0 || ratio > 1.0) throw new RuntimeException(String.format("invalid mix() 'ratio' argument value: '%s' (must be between 0.0 and 1.0)", ratio));
        if (with.getSize() != this.size) throw new RuntimeException(String.format("invalid mix() 'with' argument: '%s' (other genome must be the same size)", with));

        int left = fromLeft ? 1 : 0;
        int swapCount = (int)Math.round(ratio * this.size);
        for(int i = 0; i < swapCount; i++)
            this.genes[i + (1-left)*(this.size-swapCount)] = with.getGene(i + (1-left)*(this.size-swapCount));

        return this;
    }

    /**
     * Create exact copy of this object.
     * @return new copied object
     */
    abstract public Genome copy();

    // overrides

    @Override
    public String toString() {
        return Arrays.toString(this.genes);
    }

    // constructors

    public Genome() {
        this.size = Config.getGenomeSize();
        this.genes = new Random().ints(this.size, 0, Direction.size()).toArray();
    }

    public Genome(int[] genes) {
        this.size = genes.length;
        this.genes = genes;
    }

    // getters/setters

    public int getSize() {
        return this.size;
    }


    public int getGene(int at) {
        return this.genes[at];
    }

    // hash & equals

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genome genome = (Genome) o;
        return this.size == genome.size && Arrays.equals(this.genes, genome.genes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(this.genes), this.size);
    }
}

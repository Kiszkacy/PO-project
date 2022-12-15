package evolution.genomes;

import evolution.util.Direction;
import evolution.util.Config;
import java.util.Arrays;
import java.util.Random;

public abstract class Genome {
    protected int[] genes;
    protected int size = Config.getGenomeSize();


    abstract public Genome mutate(int times);


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

    public int getGeneAt(int at) { // TODO check range
        return this.genes[at];
    }
}

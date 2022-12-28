package evolution.genomes;

import evolution.util.Direction;
import java.util.LinkedList;
import java.util.Random;

/**
 * When mutating genes' values are increased or decreased by one. Normal mix behavior.
 */
public class CorrectiveGenome extends Genome {

    // overrides

    /**
     * Mutated gene's value increases or decreases by one (even chance).
     * @param times how many genes are mutated
     * @throws RuntimeException if mutation count is invalid
     * @return itself
     */
    @Override
    public Genome mutate(int times) throws RuntimeException {
        if (times < 0 || times > this.getSize()) throw new RuntimeException(String.format("invalid mutate() 'times' argument value: '%s' (must be between 0 and gene's size)", times));

        LinkedList<Integer> mutated = new LinkedList<>(); // holds indexes of already mutated genes
        for(int i = 0; i < times; i++) {
            int picked = new Random().nextInt(this.size);
            while (mutated.contains(picked)) picked = new Random().nextInt(this.size);
            mutated.push(picked);

            Direction dir = Direction.values()[this.genes[picked]];
            if (new Random().nextBoolean()) dir.next();
            else                            dir.prev();
            this.genes[picked] = dir.ordinal();
        }

        return this;
    }

    /**
     * Create exact copy of this object.
     * @return new copied object
     */
    @Override
    public Genome copy() {
        return new CorrectiveGenome(this.genes.clone());
    }

    // constructors

    public CorrectiveGenome() {
        super();
    }

    public CorrectiveGenome(int[] genes) {
        super(genes);
    }
}

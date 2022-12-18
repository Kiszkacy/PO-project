package evolution.genomes;

import evolution.util.Direction;
import java.util.Random;

public class CorrectiveGenome extends Genome {

    // overrides

    /**
     * Mutated gene's value increases or decreases by one (even chance).
     * @param times how many genes are mutated
     * @return itself
     */
    @Override
    public Genome mutate(int times) {
        // TODO check times >= 0
        // TODO this can be very slow
        for(int i = 0; i < times; i++) {
            int picked = new Random().nextInt(this.size);
            Direction dir = Direction.values()[picked];
            if (new Random().nextBoolean()) dir.next();
            else                            dir.prev();
            this.genes[picked] = dir.ordinal();
        }

        return this;
    }

    @Override
    public Genome copy() {
        return new CorrectiveGenome(this.genes);
    }

    // constructors

    public CorrectiveGenome() {
        super();
    }

    public CorrectiveGenome(int[] genes) { // TODO check genes size
        super(genes);
    }
}

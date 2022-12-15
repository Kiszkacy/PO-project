package evolution.genomes;

import evolution.util.Direction;
import java.util.Arrays;
import java.util.Random;

public class CorrectiveGenome extends Genome {

    // overrides

    @Override
    public Genome copy() {
        return new CorrectiveGenome(this.genes);
    }

    @Override
    public Genome mutate(int times) {
        // TODO check times >= 0

        int[] copy = Arrays.copyOf(this.genes, this.size);
        // TODO this can be very slow
        for(int i = 0; i < times; i++) {
            int picked = new Random().nextInt(this.size);
            Direction dir = Direction.values()[picked];
            if (new Random().nextBoolean()) dir.next(); // 50% chance
            else dir.prev();
            copy[picked] = dir.ordinal();
        }

        return new CorrectiveGenome(copy);
    }

    // constructors

    public CorrectiveGenome() {
        this.genes = new Random().ints(this.size, 0, Direction.size()).toArray();
    }

    public CorrectiveGenome(int[] genes) { // TODO check genes size
        this.genes = genes;
    }

    // getters/setters
}

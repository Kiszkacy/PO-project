package evolution.genomes;

import evolution.util.Direction;
import java.util.Random;

public class RandomGenome extends Genome {

    // overrides

    @Override 
    public Genome copy() {
        return new RandomGenome(this.genes);
    }
    
    @Override
    public Genome mutate(int times) {
        // TODO check times >= 0

        int[] copy = new int[this.size];
        for(int i = 0; i < times; i++)
            copy[i] = new Random().nextInt(Direction.size());
        
        return new RandomGenome(copy);
    }

    // constructors

    public RandomGenome() {
        this.genes = new Random().ints(this.size, 0, Direction.size()).toArray();
    }

    public RandomGenome(int[] genes) { // TODO check genes size
        this.genes = genes;
    }

    // getters/setters
}

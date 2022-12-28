package evolution.brains;

import evolution.genomes.Genome;
import evolution.memories.Memory;
import java.util.Random;

public class RandomBrain extends Brain {

    // overrides

    /**
     * There's 80% chance that new active gene is just the next one in genome order and
     * there's 20% chance that new active gene is picked randomly (can't be the same one).
     */
    @Override
    public void think() {
        if (new Random().nextInt(5) != 0) { // normal behavior (pick next) 80% chance
            this.activeGene = (this.activeGene+1) % this.genome.getSize();
        } else { // pick random 20% chance
            int oldActive = this.activeGene;
            while (this.activeGene == oldActive)
                this.activeGene = new Random().nextInt(this.genome.getSize());
        }
    }

    /**
     * Create exact copy of this object.
     * @return new copied object
     */
    @Override
    public Brain copy() {
        return new RandomBrain(this.memory.copy(), this.genome.copy(), this.activeGene);
    }

    // constructors

    public RandomBrain(Memory memory) {
        super(memory);
    }


    public RandomBrain(Memory memory, Genome genome) {
        super(memory, genome);
    }


    public RandomBrain(Memory memory, Genome genome, int activeGene) {
        super(memory, genome, activeGene);
    }
}

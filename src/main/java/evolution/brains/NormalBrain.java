package evolution.brains;

import evolution.genomes.Genome;
import java.util.Random;

public class NormalBrain extends Brain {

    // overrides

    /**
     * New active gene becomes the next one in genome order.
     */
    @Override
    public void think() {
        this.activeGene = (this.activeGene+1) % this.genome.getSize();
    }

    // constructors

    public NormalBrain() {
        this.activeGene = new Random().nextInt(this.genome.getSize());
    }

    public NormalBrain(Genome genome) {
        super(genome);
    }
}

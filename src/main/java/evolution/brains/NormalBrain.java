package evolution.brains;

import evolution.genomes.Genome;

public class NormalBrain extends Brain {

    // overrides

    /**
     * New active gene becomes the next one in genome order.
     */
    @Override
    public void think() {
        this.activeGene = (this.activeGene+1) % this.genome.getSize();
    }

    // constructor

    public NormalBrain() {
    }

    public NormalBrain(Genome genome) {
        super(genome);
    }
}

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

    @Override
    public Brain copy() {
        return new NormalBrain(this.genome.copy(), this.activeGene);
    }

    // constructors

    public NormalBrain() {
        super();
    }


    public NormalBrain(Genome genome) {
        super(genome);
    }


    public NormalBrain(Genome genome, int activeGene) {
        super(genome, activeGene);
    }
}

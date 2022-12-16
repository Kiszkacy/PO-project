package evolution.brains;

public class NormalBrain extends Brain {

    // overrides

    /**
     * New active gene becomes the next one in genome order.
     */
    @Override
    public void think() {
        this.activeGene = (this.activeGene+1) % this.genome.getSize();
    }
}

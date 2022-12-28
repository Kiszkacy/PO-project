package evolution.brains;

import evolution.genomes.Genome;
import evolution.memories.Memory;

public class NormalBrain extends Brain {

    // overrides

    /**
     * New active gene becomes the next one in genome order.
     */
    @Override
    public void think() {
        this.activeGene = (this.activeGene+1) % this.genome.getSize();
    }

    /**
     * Create exact copy of this object.
     * @return new copied object
     */
    @Override
    public Brain copy() {
        return new NormalBrain(this.memory.copy(), this.genome.copy(), this.activeGene);
    }

    // constructors

    public NormalBrain(Memory memory) {
        super(memory);
    }


    public NormalBrain(Memory memory, Genome genome) {
        super(memory, genome);
    }


    public NormalBrain(Memory memory, Genome genome, int activeGene) {
        super(memory, genome, activeGene);
    }
}

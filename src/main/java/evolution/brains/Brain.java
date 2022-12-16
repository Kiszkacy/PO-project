package evolution.brains;

import evolution.genomes.Genome;
import evolution.util.Config;
import java.util.Random;

public abstract class Brain {
    protected Genome genome;
    protected int activeGene;

    /**
     * Defines how next active gene is picked.
     */
    abstract public void think();

    // overrides

    @Override
    public String toString() {
        return String.format("<activeGene: (idx:'%d'|val:'%d'), genome: %s>", this.activeGene, this.genome.getGene(this.activeGene), this.genome);
    }

    // constructors

    public Brain() {
        try {
            this.genome = (Genome)Class.forName(Config.getGenomeType()).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(String.format("genome type: '%s' not found", Config.getGenomeType()));
        }
        this.activeGene = new Random().nextInt(this.genome.getSize());
    }


    public Brain(Genome genome) {
        this.genome = genome;
        this.activeGene = new Random().nextInt(this.genome.getSize());
    }

    // getters/setters

    public int getActiveGene() {
        return this.genome.getGene(this.activeGene);
    }


    public Genome getGenome() {
        return this.genome;
    }
}

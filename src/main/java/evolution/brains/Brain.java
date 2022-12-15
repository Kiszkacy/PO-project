package evolution.brains;

import evolution.genomes.Genome;
import evolution.util.Config;
import java.util.Random;

public abstract class Brain {
    protected Genome genome;
    protected int activeGene;


    abstract public void think();

    // overrides

    @Override
    public String toString() {
        return String.format("<active: %d at; genome: %s", this.genome.getGeneAt(this.activeGene), this.activeGene, this.genome);
    }

    // constructors

    public Brain() {
        this.activeGene = new Random().nextInt(this.genome.getSize());
        try {
            this.genome = (Genome)Class.forName(Config.getGenomeType()).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(String.format("genome type: '%s' not found", Config.getGenomeType()));
        }
    }

    // getters/setters

    public int getActiveGene() {
        return this.genome.getGeneAt(this.activeGene);
    }

    public Genome getGenome() {
        return this.genome;
    }
}

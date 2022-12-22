package evolution.brains;

import evolution.genomes.Genome;
import evolution.util.Config;

import java.util.Objects;
import java.util.Random;


/**
 * Class that is responsible for defining which gene is currently active. Different implementation of think() method
 * result in completely different behavior.
 */
public abstract class Brain {
    protected Genome genome;
    protected int activeGene;

    /**
     * Defines how next active gene is picked.
     */
    abstract public void think();

    abstract public Brain copy();

    // overrides

    @Override
    public String toString() {
        return String.format("<activeGene: (idx:'%d'|val:'%d'), genome: %s>", this.activeGene, this.genome.getGene(this.activeGene), this.genome);
    }

    // constructors

    public Brain() {
        try {
            this.genome = (Genome)Class.forName("evolution.genomes."+Config.getGenomeType()).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(String.format("genome type: '%s' not found", Config.getGenomeType()));
        }
        this.activeGene = new Random().nextInt(this.genome.getSize());
    }


    public Brain(Genome genome) {
        this.genome = genome;
        this.activeGene = new Random().nextInt(this.genome.getSize());
    }


    public Brain(Genome genome, int activeGene) {
        this.genome = genome;
        this.activeGene = activeGene;
    }

    // getters/setters

    public int getActiveGene() {
        return this.genome.getGene(this.activeGene);
    }


    public Genome getGenome() {
        return this.genome;
    }

    // hash & equals

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Brain brain = (Brain) o;
        return activeGene == brain.activeGene && genome.equals(brain.genome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genome, activeGene);
    }
}

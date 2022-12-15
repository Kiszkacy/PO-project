package evolution.brains;

public class NormalBrain extends Brain {

    @Override
    public void think() {
        this.activeGene = (this.activeGene+1) % this.genome.getSize();
    }
}

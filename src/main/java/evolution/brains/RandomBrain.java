package evolution.brains;

import java.util.Random;

public class RandomBrain extends Brain {

    // overrides

    @Override
    public void think() {
        if (new Random().nextInt(5) == 0) {
            int oldActive = this.activeGene;
            while (this.activeGene == oldActive)
                this.activeGene = new Random().nextInt(this.genome.getSize());
        } else {
            this.activeGene = (this.activeGene+1) % this.genome.getSize();
        }
    }
}

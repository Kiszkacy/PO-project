package evolution.ui;

import evolution.events.*;
import evolution.genomes.Genome;
import evolution.main.Animal;
import evolution.main.World;
import evolution.maps.AnimalMap;
import evolution.maps.PlantMap;
import evolution.util.Pair;
import evolution.util.Vector2;
import java.util.*;

public class SimulationTracker implements DeathObserver, ReproduceObserver {

    private final World world;
    private double averageLifespan;
    private int deathCount;

    /**
     * This method is required to be called at the end of constructor.
     */
    public void init() {
        for(Animal a : world.getEnvironment().getAnimalMap().getObjects()) {
            a.addObserver(this);
        }
    }


    public String[] dumpAllData() {
        LinkedList<String> list = new LinkedList<>();
        list.push(String.valueOf(this.getAverageAnimalLifespan()));
        list.push(String.valueOf(this.getAverageAnimalEnergy()));
        ArrayList<Pair<Genome, Integer>> genomes = this.getGenomesCount();
        if (genomes.size() == 0) {
            list.push("");
        } else {
            list.push(String.valueOf(genomes.get(0).second));
            list.push(genomes.get(0).first.toString());
        }
        list.push(String.valueOf(this.getEmptyTilesCount()));
        list.push(String.valueOf(this.getPlantCount()));
        list.push(String.valueOf(this.getAnimalCount()));

        return list.toArray(new String[0]);
    }

    // overrides

    @Override
    public void update(Event event) {
        if (event instanceof DeathEvent)
            this.onDeath((DeathEvent)event);
        else if (event instanceof ReproduceEvent)
            this.onReproduce((ReproduceEvent)event);
    }

    @Override
    public void onDeath(DeathEvent event) {
        this.deathCount += 1;
        this.averageLifespan += (((Animal)event.getDied()).getAge() - this.averageLifespan) / this.deathCount;
    }

    @Override
    public void onReproduce(ReproduceEvent event) {
        (event.getChild()).addObserver(this);
    }

    // constructors

    public SimulationTracker(World world) {
        this.world = world;
        this.init();
    }

    // getters/setters

    public int getAnimalCount() {
        return this.world.getEnvironment().getAnimalMap().getObjects().size();
    }


    public int getPlantCount() {
        return this.world.getEnvironment().getPlantMap().getObjects().size();
    }


    public int getEmptyTilesCount() {
        PlantMap plants = this.world.getEnvironment().getPlantMap();
        AnimalMap animals = this.world.getEnvironment().getAnimalMap();
        Vector2 size = this.world.getEnvironment().getSize();
        int sum = 0;
        for(int y = 0; y < size.y; y++) {
            for(int x = 0; x < size.x; x++) {
                if (plants.isEmpty(new Vector2(x, y)) && animals.isEmpty(new Vector2(x, y))) sum++;
            }
        }

        return sum;
    }


    public ArrayList<Pair<Genome, Integer>> getGenomesCount() {
        // count occurrences
        HashMap<Genome, Integer> count = new HashMap<>();
        for(Animal a : this.world.getEnvironment().getAnimalMap().getObjects()) {
            Genome genome = a.getBrain().getGenome();
            Integer currentCount = count.get(genome);
            count.put(genome, (currentCount == null) ? 1 : currentCount+1);
        }
        // sort by count
        SortedSet<Map.Entry<Genome, Integer>> sorted = new TreeSet<>( // sort descending
            (o1, o2) -> {
                if (o1.getValue() > o2.getValue())  return 1;
                else                                return -1;
            }
        );
        sorted.addAll(count.entrySet());
        // transform into Pair<> list
        LinkedList<Pair<Genome, Integer>> list = new LinkedList<>();
        Iterator<Map.Entry<Genome, Integer>> iterator = sorted.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Genome, Integer> entry = iterator.next();
            list.push(new Pair<>(entry.getKey(), entry.getValue()));
        }
        return new ArrayList<>(list);
    }


    public double getAverageAnimalEnergy() {
        int sum = 0;
        for(Animal a : this.world.getEnvironment().getAnimalMap().getObjects()) {
            sum += a.getEnergy();
        }

        return sum / (double)this.world.getEnvironment().getAnimalMap().getObjects().size();
    }


    public double getAverageAnimalLifespan() {
        return this.averageLifespan;
    }
}

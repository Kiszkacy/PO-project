package evolution.maps;

import evolution.events.DeathEvent;
import evolution.events.DeathObserver;
import evolution.events.Event;
import evolution.main.Animal;
import evolution.util.Vector2;
import java.util.*;

public class ToxicCorpses extends PlantMap implements DeathObserver {
    private final int[][] deathMap;
    private boolean checkValues = true;
    private final int[] minValues;

    // constructors

    public ToxicCorpses(){
        super();
        this.deathMap = new int[this.size.y][this.size.x];
        int amountOfTiles = size.y*size.x;
        int preferredAmount = (int) Math.ceil(amountOfTiles * 0.2);
        this.minValues = new int[preferredAmount];
        this.initializePreferTiles(preferredAmount);
        // called in case someone tried to access preferredTiles, before calling generatePreferredTiles first time
        this.notPreferredTiles = new ArrayList<>(Collections.nCopies(amountOfTiles - preferredAmount, (Vector2) null));
        this.generatePreferredTiles();
    }


    public ToxicCorpses(Vector2 size){
        super(size);
        this.deathMap = new int[size.y][size.x];
        int amountOfTiles = size.y*size.x;
        int preferredAmount = (int) Math.ceil(amountOfTiles * 0.2);
        this.minValues = new int[preferredAmount];
        this.initializePreferTiles(preferredAmount);
        // called in case someone tried to access preferredTiles, before calling generatePreferredTiles first time
        this.notPreferredTiles = new ArrayList<>(Collections.nCopies(amountOfTiles - preferredAmount, (Vector2) null));
        this.generatePreferredTiles();
    }

    /**
     * helper function, used to initialize array preferredTiles, with starting values
     * @param sizeOfPreferred size of array preferredTiles, equal to ceil of 0.2*amountOfTiles
     */
    private void initializePreferTiles(int sizeOfPreferred){
        this.preferredTiles = new ArrayList<>(sizeOfPreferred);
        for (int i = 0; i < sizeOfPreferred; i++) {
            preferredTiles.add(i, new Vector2(i % size.x, i / size.x));
        }
    }

    // overrides

    /**
     * Generates preferred and notPreferred tiles, based on deathMap
     */
    @Override
    public void generatePreferredTiles() {
        // if no animal died since last generation of preferredTiles there is no need to update them
        if (! checkValues) return;
        // can be optimized probably, by checking only places where animal died since last check

        // values are in increasing order (i.e 0 index has the smallest value)
        int notPreferredIndex = 0;

        //updating minValues
        preferredTiles.sort((v, u) -> deathMap[u.y][u.x] - deathMap[v.y][v.x]);

        for (int i = 0; i < preferredTiles.size(); i++) {
            minValues[i] = deathMap[preferredTiles.get(i).y][preferredTiles.get(i).x];
        }

        for(int y = 0; y < size.y; y++){
            for(int x = 0; x < size.x; x++){

                int amountOfDeath = deathMap[y][x];
                Vector2 position = new Vector2(x, y);

                if (amountOfDeath > minValues[0]) {
                    // value is smaller than min of minValues so goes to notPreferredTiles
                    notPreferredTiles.set(notPreferredIndex, position);
                    notPreferredIndex++;
                }
                else{
                    // don't have to add element if it is already in list
                    if (preferredTiles.contains(position)) continue;

                    // if element isn't in list and is equal to min of list we add it to notPreferredTiles instead
                    if(amountOfDeath == minValues[0]){
                        notPreferredTiles.set(notPreferredIndex, position);
                        notPreferredIndex++;
                        continue;
                        // if preferredTiles[0] is still to be iterated over, we don't add it to notPreferredTiles
                    } else if ((preferredTiles.get(0).x <= x && preferredTiles.get(0).y <= y )|| preferredTiles.get(0).y < y) {
                        notPreferredTiles.set(notPreferredIndex, preferredTiles.get(0));
                        notPreferredIndex++;
                    }

                    minValues[0] = amountOfDeath;
                    preferredTiles.set(0, position);

                    // insert new value in ordered manner
                    for (int k = 1; k < minValues.length && amountOfDeath < minValues[k]; k++) {
                        minValues[k - 1] = minValues[k];
                        minValues[k] = amountOfDeath;
                        preferredTiles.set(k - 1, preferredTiles.get(k));
                        preferredTiles.set(k, position);
                    }
                }
            }
        }
        checkValues = false;
    }

    /**
     * If event is death of animal, increment value of death map on index equal to its position
     * @param event death of killable object
     */
    @Override
    public void onDeath(DeathEvent event) {
        if (event.getDied() instanceof Animal){
            Vector2 position = ((Animal) event.getDied()).getPos();
            deathMap[position.y][position.x] += 1;
            checkValues = true;
        }
    }

    @Override
    public void update(Event event) {
        if (event instanceof DeathEvent)
            this.onDeath((DeathEvent) event);
    }
}
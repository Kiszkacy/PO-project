package evolution.memories;

import java.util.Objects;

/**
 * Memory of animal brains. Remembers how many children it has and how many plants it has eaten.
 */
public class AnimalMemory extends Memory {

    private int plantsEaten;
    private int childrenCount;
    
    
    // overrides
    
    @Override
    public Object[] getAllMemories() {
        return new Object[]{this.plantsEaten, this.childrenCount};
    }

    @Override
    public Memory copy() {
        return new AnimalMemory(this.plantsEaten, this.childrenCount);
    }
    
    // constructors
    
    public AnimalMemory() {
        this.plantsEaten = 0;
        this.childrenCount = 0;
    }


    public AnimalMemory(int plantsEaten, int childrenCount) {
        this.plantsEaten = plantsEaten;
        this.childrenCount = childrenCount;
    }
    
    // getters/setters 


    public int getPlantsEaten() {
        return this.plantsEaten;
    }

    
    public void setPlantsEaten(int plantsEaten) {
        this.plantsEaten = plantsEaten;
    }

    
    public int getChildrenCount() {
        return this.childrenCount;
    }

    
    public void setChildrenCount(int childrenCount) {
        this.childrenCount = childrenCount;
    }

    // hash & equals

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimalMemory that = (AnimalMemory) o;
        return this.plantsEaten == that.plantsEaten && this.childrenCount == that.childrenCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.plantsEaten, this.childrenCount);
    }
}

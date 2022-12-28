package evolution.memories;

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
}

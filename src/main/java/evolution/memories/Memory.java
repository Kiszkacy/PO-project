package evolution.memories;

/**
 * Class responsible for holding information which given objects knows about.
 */
public abstract class Memory {

    abstract public Object[] getAllMemories();

    abstract public Memory copy();
}

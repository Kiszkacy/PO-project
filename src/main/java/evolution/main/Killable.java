package evolution.main;

import evolution.events.Observable;

/**
 * Interface that defines organism which is alive and can die as a result of various circumstances.
 */
public interface Killable extends Observable {

    Killable die();
}

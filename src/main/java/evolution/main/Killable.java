package evolution.main;

import evolution.events.Observable;

public interface Killable extends Observable {

    Killable die();
}

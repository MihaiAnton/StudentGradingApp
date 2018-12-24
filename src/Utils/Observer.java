package Utils;

import Utils.Events.Event;

public interface Observer<E extends Event> {
    void notify(E event);
}

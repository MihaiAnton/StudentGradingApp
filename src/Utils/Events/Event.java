package Utils.Events;

public interface Event<E> {


    String getEventType();
    E getEntity();

}

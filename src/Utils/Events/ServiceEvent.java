package Utils.Events;

public class ServiceEvent implements Event<Event> {

    private String eventType;
    private Event specificEvent;

    public ServiceEvent(String eventType, Event event){
        this.specificEvent = event;
        this.eventType = eventType;
    }


    @Override
    public String getEventType() {
        return eventType;
    }

    @Override
    public Event getEntity() {
        return this.specificEvent;
    }

}

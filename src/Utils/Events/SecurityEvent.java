package Utils.Events;

public class SecurityEvent implements Event<Event> {

    private Event specificEvent;
    private String eventType;

    public SecurityEvent(Event specificEvent, String eventType) {
        this.specificEvent = specificEvent;
        this.eventType = eventType;
    }

    @Override
    public String getEventType() {
        return eventType;
    }

    @Override
    public Event getEntity() {
        return specificEvent;
    }
}

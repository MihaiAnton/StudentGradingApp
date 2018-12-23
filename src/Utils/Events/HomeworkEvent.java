package Utils.Events;

import Domain.Homework;

public class HomeworkEvent implements Event<Homework> {

    private String type;
    private Homework homework;

    public HomeworkEvent(String type, Homework homework) {
        this.type = type;
        this.homework = homework;
    }

    @Override
    public String getEventType() {
        return this.type;
    }

    @Override
    public Homework getEntity() {
        return this.homework;
    }
}

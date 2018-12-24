package Utils.Events;

import Domain.Grade;
import jdk.jfr.EventType;

public class GradeEvent implements Event<Grade> {

    private String type;
    private Grade grade;

    public GradeEvent(String type, Grade grade){
        this.type = type;
        this.grade = grade;
    }

    @Override
    public String getEventType() {
        return this.type;
    }

    @Override
    public Grade getEntity() {
        return this.grade;
    }
}

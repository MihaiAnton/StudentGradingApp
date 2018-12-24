package Utils.Events;

import Domain.Student;

public class StudentEvent implements Event<Student> {

    private String type;
    private Student student;

    public StudentEvent(String type, Student student) {
        this.type = type;
        this.student = student;
    }

    @Override
    public String getEventType() {
        return this.type;
    }

    @Override
    public Student getEntity() {
        return this.student;
    }
}

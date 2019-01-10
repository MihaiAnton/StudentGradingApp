package Tests;

import Domain.Student;
import org.junit.Assert;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    private Student student;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        student = new Student("2229","John",221,"john@scs.ubbcluj.ro","Teacher Sample");
    }

    @org.junit.jupiter.api.Test
    void getId() {
        Assert.assertEquals("2229",student.getId());
    }

    @org.junit.jupiter.api.Test
    void getName() {
        Assert.assertEquals("John",student.getName());
    }

    @org.junit.jupiter.api.Test
    void getEmail() {
        Assert.assertEquals("john@scs.ubbcluj.ro",student.getEmail());
    }

    @org.junit.jupiter.api.Test
    void getTeacher() {
        Assert.assertEquals("Teacher Sample",student.getTeacher());
    }

    @org.junit.jupiter.api.Test
    void getGroup() {
        Assert.assertEquals(221,student.getGroup());
    }

    @org.junit.jupiter.api.Test
    void setId() {
        student.setId("2228");
        Assert.assertEquals("2228",student.getId());
    }

    @org.junit.jupiter.api.Test
    void setName() {
        student.setName("Mike");
        Assert.assertEquals("Mike",student.getName());
    }

    @org.junit.jupiter.api.Test
    void setGroup() {
        student.setGroup(222);
        Assert.assertEquals(222,student.getGroup());
    }

    @org.junit.jupiter.api.Test
    void setEmail() {
        student.setEmail("mike@scs.ubbcluj.ro");
        Assert.assertEquals("mike@scs.ubbcluj.ro",student.getEmail());
    }

    @org.junit.jupiter.api.Test
    void setTeacher() {
        student.setTeacher("Teacher2");
        Assert.assertEquals("Teacher2",student.getTeacher());
    }
}
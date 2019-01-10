package Tests;

import Domain.Grade;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GradeTest {

    private Grade g;

    @BeforeEach
    void setUp() {
        g = new Grade("2229",1,7.8,"Good!");
        g.setStudTeacher("Teacher");
        g.setStudName("John");
        g.setId("22291");
        g.setStudGroup(221);
        g.setWeek(10);
    }

    @Test
    void getWeek() {
        Assert.assertEquals(10,g.getWeek());
    }

    @Test
    void setWeek() {
        g.setWeek(8);
        Assert.assertEquals(8,g.getWeek());
    }

    @Test
    void getStudId() {
        Assert.assertEquals("2229",g.getStudId());
    }

    @Test
    void setStudentId() {
        g.setStudentId("1234");
        Assert.assertEquals("1234",g.getStudId());
    }

    @Test
    void getHomeworkId() {
        Assert.assertEquals(1,g.getHomeworkId());
    }

    @Test
    void setHomeworkId() {
        g.setHomeworkId(2);
        Assert.assertEquals(2,g.getHomeworkId());
    }

    @Test
    void getGrade() {
        Assert.assertEquals(true,Math.abs(7.8 - g.getGrade()) < 0.01);
    }

    @Test
    void setGrade() {
        g.setGrade(8.9);
        Assert.assertEquals(true,Math.abs(8.9 - g.getGrade()) < 0.01);
    }

    @Test
    void setId() {
        g.setId("12345");
        Assert.assertEquals("12345",g.getId());
    }

    @Test
    void getId() {
        Assert.assertEquals("22291",g.getId());
    }

    @Test
    void getStudGroup() {
        Assert.assertEquals(221,g.getStudGroup());
    }

    @Test
    void setStudGroup() {
        g.setStudGroup(222);
        Assert.assertEquals(222,g.getStudGroup());
    }

    @Test
    void getStudTeacher() {
        Assert.assertEquals("Teacher",g.getStudTeacher());
    }

    @Test
    void setStudTeacher() {
        g.setStudTeacher("Teacher2");
        Assert.assertEquals("Teacher2",g.getStudTeacher());
    }

    @Test
    void getStudName() {
        Assert.assertEquals("John",g.getStudName());
    }

    @Test
    void setStudName() {
        g.setStudName("Marry");
        Assert.assertEquals("Marry",g.getStudName());
    }

    @Test
    void getFeedback() {
        Assert.assertEquals("Good!",g.getFeedback());
    }

    @Test
    void setFeedback() {
        g.setFeedback("OK");
        Assert.assertEquals("OK",g.getFeedback());
    }
}
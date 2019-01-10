package Tests;

import Domain.Homework;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HomeworkTest {

    private Homework h;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        h = new Homework(1234,"Homework Description",5,7,7);
    }


    @Test
    void getId() {
        Assert.assertEquals(1234,(int)h.getId());
    }

    @Test
    void setId() {
        h.setId(2345);
        Assert.assertEquals(2345,(int)h.getId());
    }

    @Test
    void getDescription() {
        Assert.assertEquals("Homework Description",h.getDescription());
    }

    @Test
    void setDescription() {
        h.setDescription("Changed description");
        Assert.assertEquals("Changed description",h.getDescription());
    }

    @Test
    void getTargetWeek() {
        Assert.assertEquals(5,h.getTargetWeek());
    }

    @Test
    void setTargetWeek() {
        h.setTargetWeek(10);
        Assert.assertEquals(10,h.getTargetWeek());
    }

    @Test
    void getDeadlineWeek() {
        Assert.assertEquals(7,h.getDeadlineWeek());
    }

    @Test
    void setDeadlineWeek() {
        h.setDeadlineWeek(10);
        Assert.assertEquals(10,h.getDeadlineWeek());
    }

    @Test
    void getAssignmentWeek() {
        Assert.assertEquals(7,h.getAssignmentWeek());
    }

    @Test
    void setAssignmentWeek() {
        h.setAssignmentWeek(10);
        Assert.assertEquals(10,h.getAssignmentWeek());
    }

}
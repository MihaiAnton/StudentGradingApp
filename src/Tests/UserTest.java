package Tests;

import Domain.AccesRight;
import Domain.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User u;

    @org.junit.jupiter.api.BeforeEach
    void setUp(){
        u = new User("user", AccesRight.FULL);
    }

    @Test
    void getRight() {
        Assert.assertEquals(AccesRight.FULL,u.getRight());
    }

    @Test
    void setRight() {
        u.setRight(AccesRight.ADMIN);
        Assert.assertEquals(AccesRight.ADMIN,u.getRight());
    }

    @Test
    void getUserName() {
        Assert.assertEquals("user",u.getUserName());
    }

    @Test
    void setUserName() {
        u.setUserName("newUser");
        Assert.assertEquals("newUser",u.getUserName());
    }

    @Test
    void setPassword() {
        u.setPassword("pass2");
    }

    @Test
    void setId() {
        u.setId("abc");
        Assert.assertEquals("abc",u.getId());
    }

    @Test
    void getId() {
        Assert.assertEquals("user",u.getId());
    }
}
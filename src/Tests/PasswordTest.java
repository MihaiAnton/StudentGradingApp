package Tests;

import Domain.Password;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {

    private Password p;

    @org.junit.jupiter.api.BeforeEach
    void setUp(){
        p = new Password();
        p.setPassword("pass");
        p.setId("1234");
    }

    @Test
    void setId() {
        p.setId("newId");
        Assert.assertEquals("newId",p.getId());
    }

    @Test
    void getPassword() {
        Assert.assertEquals("pass",p.getPassword());
    }

    @Test
    void setPassword() {
        p.setPassword("newPass");
        Assert.assertEquals("newPass",p.getPassword());
    }

    @Test
    void getId() {
        Assert.assertEquals("1234",p.getId());
    }
}
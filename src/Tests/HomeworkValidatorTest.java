package Tests;

import Domain.Homework;
import Validators.HomeworkValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HomeworkValidatorTest {

    HomeworkValidator validator;

    @BeforeEach
    void setUp() {
        validator = new HomeworkValidator();
    }

    @Test
    void validate() {
    }

    @Test
    void validateTargetWeek(){
        Homework h = new Homework(1,"d",5,4,5);

        try{
            validator.validate(h);
            assert false;
        }
        catch(Exception e){
            assert true;
        }
    }

    @Test
    void validateWeeks(){
        Homework h = new Homework(1,"d",12,-1,5);

        try{
            validator.validate(h);
            assert false;
        }
        catch(Exception e){
            assert true;
        }
    }

    @Test
    void validateDescription(){
        Homework h = new Homework(1,"",3,4,3);

        try{
            validator.validate(h);
            assert false;
        }
        catch(Exception e){
            assert true;
        }
    }
}
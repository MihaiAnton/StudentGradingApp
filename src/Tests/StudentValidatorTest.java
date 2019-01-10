package Tests;

import Domain.Student;
import Exceptions.ValidationException;
import Validators.GenericValidator;
import Validators.StudentValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentValidatorTest {

    StudentValidator validator;

    @org.junit.jupiter.api.BeforeEach
    void setUp(){
        validator = new StudentValidator();
    }

    @Test
    void validateGroup() {
        Student s = new Student("1234","n",-1,"mail@scs.ubbcluj.ro","t");

        try{
            validator.validate(s);
            assert false;
        }
        catch(ValidationException e){
            assert true;
        }


    }

    @Test
    void validateMail() {
        Student s = new Student("1234","n",221,"mail@yahoo.com","t");

        try{
            validator.validate(s);
            assert false;
        }
        catch(ValidationException e){
            assert true;
        }



    }

    @Test
    void validateId() {
        Student s = new Student("124","n",221,"mail@scs.ubbcluj.ro","t");

        try{
            validator.validate(s);
            assert false;
        }
        catch(ValidationException e){
            assert true;
        }

    }
}
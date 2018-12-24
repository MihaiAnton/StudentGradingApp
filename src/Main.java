import Domain.Homework;
import Domain.Student;
import Validators.HomeworkValidator;
import Validators.StudentValidator;
import Validators.Validator;

public class Main {


    public static void main(String[] args) {
        Validator validator = new HomeworkValidator();


        Homework h = new Homework(1 ,"", 6, 5, 5);

        try {
            validator.validate(h);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("aaaaa");
        }
    }


}

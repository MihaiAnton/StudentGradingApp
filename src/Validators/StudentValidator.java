package Validators;

import Domain.Student;
import Exceptions.ValidationException;

public class StudentValidator implements Validator<Student>{
    @Override
    public void validate(Student entity) {
        String err = "";
        try {
            err = err +
                    validateId(entity.getId())+
                    validateMail(entity.getEmail())+
                    validateGroup(entity.getGroup());

            if(!err.equals("") && !err.matches("[\n]*")){
                throw new ValidationException(err);
            }
        }
        catch(ValidationException e){
            throw new ValidationException(err);
        }
        catch(Exception e){
            throw new ValidationException("Student not valid.");
        }

    }

    private String validateId(String id){
        int ID;
        try{
            ID = Integer.parseInt(id);
            if(ID < 1000 || ID > 9999){
                throw new Exception();
            }
        }
        catch(Exception e){
            return "Id must be a 4 digit integer.\n";
        }
        return "";
    }

    private String validateMail(String mail){
        try{
            if(!mail.matches("[a-zA-Z][a-zA-Z0-9]*@scs.ubbcluj.ro*")){
                throw new Exception();
            }
        }
        catch(Exception e){
            return "Mail must follow the pattern 'name@scs.ubbcluj.ro'.\n";
        }
        return "";
    }

    private String validateGroup(int group){
        if(group < 221 || group > 227){
            return "Group must be between 221 and 227.\n";
        }
        return "";
    }
}

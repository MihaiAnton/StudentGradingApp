package Validators;

import Domain.Homework;
import Exceptions.ValidationException;

public class HomeworkValidator implements Validator<Homework> {
    @Override
    public void validate(Homework entity) {
        String err = "";
        try {
            err = err +
                    validateTargetWeek(entity.getTargetWeek(), entity.getDeadlineWeek()) +
                    validateWeeks(entity.getTargetWeek(), entity.getDeadlineWeek()) +
                    validateDescription(entity.getDescription());


            if(!err.equals("") && !err.matches("[\n]*")){
                throw new ValidationException(err);
            }
        }
        catch(ValidationException e){
            throw new ValidationException(err);
        }
        catch(Exception e){
            throw new ValidationException("Homework not valid.");
        }
    }

    private String validateTargetWeek(int tweek, int dweek){
        if(tweek > dweek){
            return "Target week must be lower than deadline.\n";
        }
        return "";
    }

    private String validateWeeks(int tweek, int dweek){
        if(tweek < 0 || tweek > 14 || dweek < 0 || dweek > 14){
            return "Week must be in interval 0 - 14.\n";
        }
        return "";
    }

    private String validateDescription(String description){
        if(description.equals(""))
            return "Description can't be empty.\n";
        return "";
    }


}

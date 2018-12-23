package UI;


import Domain.Student;
import Exceptions.ValidationException;
import Utils.Events.ServiceEvent;
import Utils.Events.StudentEvent;
import Validators.StudentValidator;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class StudentViewController extends TemplateController<Student>{


    @FXML
    TextField id, name, group, mail, teacher;


    @FXML
    public void handleAdd(){
        try {
            Student s = getEntityFromFields();
            if(this.service.addStudent(s) == null) {
                this.service.notifyObserver(new ServiceEvent("student",
                                                             new StudentEvent("add", s)));
            }
        }
        catch(Exception e){
            handleError(e.getMessage());
        }
    }

    @FXML
    public void handleUpdate(){
        try {
            Student s = getEntityFromFields();
            if(this.service.updateStudent(s) == null) {
                this.service.notifyObserver(new ServiceEvent("student",
                                                            new StudentEvent("update", s)));
            }
        }
        catch (Exception e){
            handleError(e.getMessage());
        }
    }

    @FXML
    public void handleDelete(){
        try{
            Student s = getEntityFromFields();
            if(this.service.deleteStudent(s.getId()) != null) {
                this.service.notifyObserver(new ServiceEvent("student",
                                                            new StudentEvent("delete", s)));
            }
        }
        catch(Exception e){
            handleError(e.getMessage());
        }
    }

    @FXML
    public void handleClear(){
        id.clear();
        name.clear();
        group.clear();
        mail.clear();
        teacher.clear();
    }



    @Override
    protected void addThisToServiceList() {
        this.service.addObserver(this);
    }

    @Override
    public void updateFields(Student newValue) {
        id.setText(newValue.getId());
        name.setText(newValue.getName());
        group.setText("" + newValue.getGroup());
        mail.setText(newValue.getEmail());
        teacher.setText(newValue.getTeacher());
    }

    @Override
    public Student getEntityFromFields() {
        StudentValidator validator = new StudentValidator();
        try{
            String sid = id.getText();
            String sname = name.getText();
            int sgroup = Integer.parseInt(group.getText());
            String smail = mail.getText();
            String steacher = teacher.getText();

            Student s = new Student(sid, sname, sgroup, smail, steacher);
            validator.validate(s);
            return s;
        }
        catch (ValidationException e){
            throw new RuntimeException("Data inserted not valid: " + e.getMessage());
        }
        catch(Exception e){
            throw new RuntimeException("Error creating a student from the data.");
        }

    }

    @Override
    public void populateList() {
        super.controllerModel.clear();
        for (Student s:service.getStudents()) {
            super.controllerModel.add(s);
        }
    }


    @Override
    public void notify(ServiceEvent event) {
        Student s = (Student)event.getEntity().getEntity();

        if(event.getEntity().getEventType().equals("add")){
            this.controllerModel.add(s);
        }
        else if(event.getEntity().getEventType().equals("update")){
            for(int i=0;i<controllerModel.size();i++){
                if(controllerModel.get(i).getId().equals(s.getId())){
                    controllerModel.remove(i);
                    break;
                }
            }
            this.controllerModel.add(s);
        }
        else if(event.getEntity().getEventType().equals("delete")){
            for(int i=0;i<controllerModel.size();i++){
                if(controllerModel.get(i).getId().equals(s.getId())){
                    controllerModel.remove(i);
                    break;
                }
            }
        }
    }
}

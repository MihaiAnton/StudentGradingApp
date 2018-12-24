package UI;


import Domain.Student;
import Exceptions.ValidationException;
import Utils.Events.ServiceEvent;
import Utils.Events.StudentEvent;
import Validators.StudentValidator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class StudentViewController extends TemplateController<Student>{


    @FXML
    TextField id, name, group, mail, teacher;

    @FXML
    RadioButton missingReason;

    @FXML
    TextField students, hid, week, fback, grade;


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
        if(!event.getEventType().equals("student")) {
            return;
        }

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

    @FXML
    public void handleGrade(){
        try {
            Double gr = Double.parseDouble(grade.getText());
            int week = Integer.parseInt(this.week.getText());
            String feedback = fback.getText();
            int hid = Integer.parseInt(this.hid.getText());
            String students = this.students.getText();

            if (service.findHomework(hid) == null) {
                throw new ValidationException("Inexistent Homework.");
            }

            if (week < 0 || week > 14) {
                throw new ValidationException("Incorrect week value.");
            }

            if(gr < 1 || gr > 10){
                throw new ValidationException("Incorrect grade value.");
            }

            if (missingReason.isSelected()) {
                week = service.findHomework(hid).getTargetWeek();
            }

            service.findHomework(hid).setAssignmentWeek(week);
            service.findHomework(hid).setGrade(gr);
            gr = service.findHomework(hid).getGrade();

            String[] sIds = students.split(",");

            for (String s : sIds) {
                Student student = searchStudent(s);
                if (student == null) {

                    gradeConfirmation("Can't find student " + s + ".");
                }
                else{
                    try{
                        if(service.findGrade(s, hid) != null){
                            throw new Exception("Grade already exists.");
                        }
                        else {

                            service.assignGrade(student.getId(), hid, gr, week, feedback);
                            gradeConfirmation("Student " + student.getName() + " graded " + gr + " at homework " + hid + ".");
                        }
                    }
                    catch (Exception e){
                        gradeConfirmation(e.getMessage());
                    }
                }
            }
        }
        catch(ValidationException ve){
            handleError("Invalid data: " + "\n" + ve.getMessage());
        }
        catch(Exception e){
            handleError("Error grading.");
        }
    }

    private Student searchStudent(String s) {

        for(Student st:service.getStudents()){
            if(s.equals(st.getId()) || st.getName().matches(s + ".*")){
                return st;
            }
        }
        return null;
    }

    public void gradeConfirmation(String text){
        Alert msg = new Alert(Alert.AlertType.CONFIRMATION);
        msg.setTitle("Grade confirmation.");
        msg.setContentText(text);
        msg.showAndWait();
    }
}























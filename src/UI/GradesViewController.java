package UI;

import Domain.AccesRight;
import Domain.Grade;
import Domain.Homework;
import Domain.Student;
import Exceptions.ValidationException;
import Service.MailService;
import Service.SecurityService;
import Service.TeacherService;
import Utils.Events.Event;
import Utils.Events.SecurityEvent;
import Utils.Events.ServiceEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static Domain.AccesRight.GENERIC;
import static Domain.AccesRight.RESTRICTED;

public class GradesViewController extends TemplateController<Grade>{




    @FXML
    ChoiceBox<String> groupCB, homeworkCB;

    @FXML
    TextField studentId;

    @FXML
    Text loginStatus;

    @FXML
    RadioButton missingReason, mailCheck;

    @FXML
    TextField students, hid, week, fback, grade;

    private SecurityService securityService;
    private LoginController mailLogin;

    public void setSecurityService(SecurityService securityService){

        this.securityService = securityService;
    }

    public void setLoginController(LoginController loginController){this.mailLogin = loginController;}

    @FXML
    public void handleLogin(){
        this.mailLogin.show();
    }

    public MailService getMailService(){
        return service.getMailService();
    }


    @Override
    public void setService(TeacherService service){
        this.service = service;
        addThisToServiceList();
        setChoices();
        setDefaultValues();
        populateList();
    }

    public void onOpen(){
        this.setChoices();
    }

    private void setChoices() {
        Set<Integer> hids = new HashSet<>();
        homeworkCB.getItems().clear();

        for(Grade grade: service.getAllGrades()){
            hids.add(grade.getHomeworkId());
        }

        for (Integer id:hids   ) {
            homeworkCB.getItems().add("" + id);
        }

        homeworkCB.getItems().add("None");

        groupCB.getItems().clear();

        Set<Integer> groups = new HashSet<>();

        for(Grade g:service.getAllGrades()){
            groups.add(service.findStudent(g.getStudId()).getGroup());
        }

        for(int group:groups){
            groupCB.getItems().add("" + group);
        }

        groupCB.getItems().add("None");

    }

    private void setDefaultValues() {

        if (securityService.getStudentId() != null)
            studentId.setText(securityService.getStudentId());

        else{
            studentId.setText("");
        }
        groupCB.setValue("None");
        homeworkCB.setValue("None");
        this.handleFilter();
    }


    @Override
    protected void addThisToServiceList() {
        this.service.addObserver(this);
        this.securityService.addObserver(this);
    }

    @Override
    public void updateFields(Grade newValue) {

        this.students.setText(newValue.getStudName());
        this.studentId.setText(newValue.getStudName());
    }

    @Override
    public Grade getEntityFromFields(){return null;}

    @Override
    public void populateList() {
        this.controllerModel.clear();
    }

    @Override
    public void notify(Event event) {
        this.setChoices();
        if(event.getEventType().equals("security")) {
            SecurityEvent se = (SecurityEvent) event;

            loginStatus.setText(securityService.getLogStatus());
        }
        setDefaultValues();
        handleFilter();
    }

    @FXML
    public void handleClear(){
        this.setDefaultValues();
    }

    private Iterable<Grade> applyFilters(Iterable<Grade> list){



        //homework id
        ArrayList<Grade> filtered1 = new ArrayList<>();
        String selectedHw = homeworkCB.getValue();

        if(selectedHw != null) {
            for (Grade g : list) {
                if (selectedHw.equals("None") || selectedHw.equals("" + g.getHomeworkId())) {
                    filtered1.add(g);
                }
            }
        }
        else{
            for (Grade g : list) {
                filtered1.add(g);
            }
        }

        //student name
        ArrayList<Grade> filtered2 = new ArrayList<>();
        for (Grade g: filtered1) {
            if (studentId.getText() == null || studentId.getText().equals("") || g.getStudId().matches(studentId.getText() + ".*") || g.getStudName().matches(studentId.getText() + ".*")) {//studentField.getText().equals(g.getStudId())){

                filtered2.add(g);
            }
        }


        //group
        ArrayList<Grade> filtered3 = new ArrayList<>();

        String selectedGroup = groupCB.getValue();
        if(selectedGroup != null) {
            for (Grade g : filtered2) {
                if (selectedGroup.equals("None") || selectedGroup.equals("" + g.getStudGroup())) {
                    filtered3.add(g);
                }
            }
        }
        else{
            for (Grade g : filtered2) {
                filtered3.add(g);
            }
        }

        return filtered3;

    }

    @FXML
    private void handleFilter(){
        //get all grades

        controllerModel.clear();

        Iterable<Grade> allgrades = service.getAllGrades();
        allgrades.forEach((x)-> {
                    Student student = service.findStudent(x.getStudId());
                    if (student != null) {
                        x.setStudGroup(student.getGroup());
                        x.setStudName(student.getName());
                        x.setStudTeacher(student.getTeacher());
                        x.setStudentId(student.getId());

                    }
                }
        );


        Iterable<Grade> filtered = applyFilters(allgrades);

        //set ok grades
        filtered.forEach((x)->{
            Student student = service.findStudent(x.getStudId());
            if(student != null) {
                x.setStudGroup(student.getGroup());
                x.setStudName(student.getName());
                x.setStudTeacher(student.getTeacher());
                x.setStudentId(student.getId());

                if(securityService.getAccesRight() == AccesRight.RESTRICTED) {
                    if(securityService.getStudentId() != null && securityService.getStudentId().equals(x.getStudId()))
                        controllerModel.add(x);
                }
                else{
                    controllerModel.add(x);
                }
            }
        });
    }

    @FXML
    public void handleGrade(){

        if(this.mailCheck.isSelected() && !this.getMailService().isLoggedIn()){
            handleError("Click on the Google icon to login or \n uncheck the mail button.");
            //
        }

        else {
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

                if (gr < 1 || gr > 10) {
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
                    } else {
                        try {
                            if (service.findGrade(student.getId(), hid) != null) {
                                throw new Exception("Grade already exists.");
                            } else {
                                boolean mailFlag = false;
                                if(mailCheck.isSelected()){
                                    mailFlag = true;
                                }

                                service.assignGrade(student.getId(), hid, gr, week, feedback,mailFlag);
                                gradeConfirmation("Student " + student.getName() + " graded " + gr + " at homework " + hid + ".");

                            }
                        } catch (Exception e) {
                            gradeConfirmation(e.getMessage());
                        }
                    }
                }
            } catch (ValidationException ve) {
                handleError("Invalid data: " + "\n" + ve.getMessage());
            } catch (Exception e) {
                handleError("Error grading.");
            }
            handleClear();
            handleFilter();
        }

    }

    public void gradeConfirmation(String text){
        Alert msg = new Alert(Alert.AlertType.CONFIRMATION);
        msg.setTitle("Grade confirmation.");
        msg.setContentText(text);
        msg.showAndWait();
    }

    private Student searchStudent(String s) {

        for(Student st:service.getStudents()){
            if(s.equals(st.getId()) || st.getName().matches(s + ".*")){
                return st;
            }
        }
        return null;
    }

    @FXML
    public void filterOnWriteGrade(){

        String name = students.getText();
        handleClear();
        studentId.setText(name);
        handleFilter();
    }

    @FXML
    public void filterOnWriteSearch(){
        String name = studentId.getText();
        students.setText(name);
        handleFilter();
    }



}

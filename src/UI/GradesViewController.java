package UI;

import Domain.Grade;
import Domain.Homework;
import Domain.Student;
import Service.SecurityService;
import Service.TeacherService;
import Utils.Events.ServiceEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GradesViewController extends TemplateController<Grade>{




    @FXML
    ChoiceBox<String> groupCB, homeworkCB;

    @FXML
    TextField studentId;

    private SecurityService securityService;

    public void setSecurityService(SecurityService securityService){
        this.securityService = securityService;
    }

    @Override
    public void setService(TeacherService service){
        this.service = service;
        addThisToServiceList();
        setChoices();
        setDefaultValues();
        populateList();
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

        studentId.setText("");
        groupCB.setValue("None");
        homeworkCB.setValue("None");
    }


    @Override
    protected void addThisToServiceList() {
        this.service.addObserver(this);
    }

    @Override
    public void updateFields(Grade newValue) {}

    @Override
    public Grade getEntityFromFields(){return null;}

    @Override
    public void populateList() {
        this.controllerModel.clear();
    }

    @Override
    public void notify(ServiceEvent event) {
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
        for (Grade g: list) {
            if(homeworkCB.getValue().equals("None") || homeworkCB.getValue().equals(""+g.getHomeworkId())){
                filtered1.add(g);
            }
        }

        //student id
        ArrayList<Grade> filtered2 = new ArrayList<>();
        for (Grade g: filtered1) {
            if (studentId.getText() == null || studentId.getText().equals("") || g.getStudId().matches(studentId.getText() + ".*")) {//studentField.getText().equals(g.getStudId())){

                filtered2.add(g);
            }
        }


        //group
        ArrayList<Grade> filtered3 = new ArrayList<>();
        for (Grade g: filtered2) {
            if(groupCB.getValue().equals("None") || groupCB.getValue().equals(""+g.getStudGroup())){
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
                controllerModel.add(x);
            }
        });
    }

}

package UI;

import Domain.AccesRight;
import Domain.Homework;
import Exceptions.ValidationException;
import Service.SecurityService;
import Utils.Events.Event;
import Utils.Events.HomeworkEvent;
import Utils.Events.SecurityEvent;
import Utils.Events.ServiceEvent;
import Validators.HomeworkValidator;
import Validators.Validator;
import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import static Domain.AccesRight.*;

public class HomeworkViewController extends TemplateController<Homework> {


    @FXML
    TextField tid, tdesc, ttrgWeek, tddlWeek;

    @FXML
    Button badd, bupdate, bdelete, bclear;

    @FXML
    Text loginStatus;

    private SecurityService securityService;

    public void setSecurityService(SecurityService securityService){
        this.securityService = securityService;
    }


    @FXML
    public void handleAdd(){

        try{
            Homework h = getEntityFromFields();
            if(this.service.addHomework(h) == null) {
                this.service.notifyObserver(new ServiceEvent("homework",
                        new HomeworkEvent("add", h)));
            }
        }
        catch (Exception e){
            handleError(e.getMessage());
        }
    }

    @FXML
    public void handleUpdate(){
        try{
            Homework h = getEntityFromFields();
            if(this.service.updateHomework(h) == null) {
                this.service.notifyObserver(new ServiceEvent("homework",
                        new HomeworkEvent("update", h)));
            }
        }
        catch (Exception e){
            handleError(e.getMessage());
        }
    }

    @FXML
    public void handleDelete(){
        try{
            Homework h = getEntityFromFields();
            if(this.service.findHomework(h.getId()) != null) {
                if(this.service.hasGradeAssigned(h.getId())){
                    handleError("Can't delete " + h.getDescription() + ".\nGrades assigned to the homework.");

                }
                else {
                    if (this.service.deleteHomework(h) != null) {
                        this.service.notifyObserver(new ServiceEvent("homework",
                                new HomeworkEvent("delete", h)));
                    }
                }
            }

        }
        catch (Exception e){
            handleError(e.getMessage());
        }
    }

    @FXML
    public void handleClear(){
        tid.clear();
        tdesc.clear();
        ttrgWeek.clear();
        tddlWeek.clear();
    }


    @Override
    protected void addThisToServiceList() {
        this.service.addObserver(this);
        this.securityService.addObserver(this);
    }

    @Override
    public void updateFields(Homework newValue) {
        tid.setText("" + newValue.getId());
        tdesc.setText(newValue.getDescription());
        ttrgWeek.setText("" + newValue.getTargetWeek());
        tddlWeek.setText("" + newValue.getDeadlineWeek());
    }

    @Override
    public Homework getEntityFromFields() {
        try{
            int id = Integer.parseInt(tid.getText());
            String desc = tdesc.getText();
            int targetW = Integer.parseInt(ttrgWeek.getText());
            int ddlW = Integer.parseInt(tddlWeek.getText());

            Homework h = new Homework(id, desc, targetW, ddlW, targetW);
            Validator validator = new HomeworkValidator();
            validator.validate(h);

            return h;
        }
        catch(ValidationException err){
            throw new RuntimeException("Invalid data for homework: " + err.getMessage());
        }
        catch(Exception e){
            throw new RuntimeException("Please insert valid data.");
        }
    }

    @Override
    public void populateList() {
        this.controllerModel.clear();
        for (Homework h:service.getHomeworkIterable()) {
            this.controllerModel.add(h);
        }
    }

    @Override
    public void notify(Event e) {
        if(e.getEventType().equals("security")){
            SecurityEvent se = (SecurityEvent)e;

            if(securityService.getAccesRight().equals(RESTRICTED) ||
               securityService.getAccesRight().equals(GENERIC)){

                blockButtons();
            }
            else{
                unblockButtons();
            }
            loginStatus.setText(securityService.getLogStatus());

        }
        else {


            ServiceEvent event = (ServiceEvent) e;


            if (!event.getEventType().equals("homework")) {
                return;
            }

            Homework h = (Homework) event.getEntity().getEntity();
            if (event.getEntity().getEventType().equals("add")) {
                this.controllerModel.add(h);
            } else if (event.getEntity().getEventType().equals("update")) {
                for (int i = 0; i < controllerModel.size(); i++) {
                    if (controllerModel.get(i).getId().equals(h.getId())) {
                        controllerModel.remove(i);
                        break;
                    }
                }
                controllerModel.add(h);
            } else if (event.getEntity().getEventType().equals("delete")) {
                for (int i = 0; i < controllerModel.size(); i++) {
                    if (controllerModel.get(i).getId().equals(h.getId())) {
                        controllerModel.remove(i);
                        break;
                    }
                }
            }
        }

    }

    private void blockButtons() {
        badd.setDisable(true);
        bclear.setDisable(true);
        bupdate.setDisable(true);
        bdelete.setDisable(true);
    }

    private void unblockButtons() {
        badd.setDisable(false);
        bclear.setDisable(false);
        bupdate.setDisable(false);
        bdelete.setDisable(false);
    }


}







































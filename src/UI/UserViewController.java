package UI;

import Domain.AccesRight;
import Domain.User;
import Service.SecurityService;
import Utils.Events.Event;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UserViewController extends TemplateController<User> {


    @FXML
    TextField userName,  userPassword;

    @FXML
    ChoiceBox<AccesRight> userRight;

    private SecurityService securityService;

    public void setSecurityService(SecurityService securityService){

        this.securityService = securityService;
        updateChoiceBoxFeatures();
    }

    private void updateChoiceBoxFeatures(){
        userRight.getItems().clear();
        for (AccesRight a:AccesRight.values()) {
            userRight.getItems().add(a);
        }
    }

    @Override
    protected void addThisToServiceList() {
    }

    @Override
    public void updateFields(User newValue) {
        this.userName.setText(newValue.getUserName());
        this.userRight.setValue(newValue.getRight());
    }

    @Override
    public User getEntityFromFields() {

        return securityService.findUser(this.userName.getText());

    }

    @Override
    public void populateList() {
        try {
            table.getSelectionModel().clearSelection();
            controllerModel.setAll(new ArrayList<>());
        }
        catch(Exception e){}
        for (User user: securityService.findAllUsers()) {
            super.controllerModel.add(user);
        }
    }

    @Override
    public void notify(Event event) {

    }

    @FXML
    public void handleAdd(ActionEvent actionEvent) {
        try{
            securityService.addUser(this.userName.getText(), this.userPassword.getText());
            populateList();
        }
        catch (Exception e){
            handleError(e.getMessage());
        }
    }

    @FXML
    public void handleUpdate(ActionEvent actionEvent) {
        try{
            securityService.updateUser(userName.getText(), userPassword.getText(), userRight.getValue());
            populateList();
        }
        catch(Exception e){
            handleError(e.getMessage());
        }
    }

    @FXML
    public void handleDelete(ActionEvent actionEvent) {
        try{
            securityService.deleteUser(userName.getText());
            populateList();
        }
        catch(Exception e){
            handleError(e.getMessage());
        }
    }

    @FXML
    public void handleClear(ActionEvent actionEvent) {
        this.userName.clear();
        this.userPassword.clear();
        this.userRight.setValue(AccesRight.RESTRICTED);
        populateList();
    }

    @FXML
    public void handleUsernameChanged(){
        String s = userName.getText();

        filterUsers(s);

    }

    private void filterUsers(String s) {

        controllerModel.clear();


        for (User user:securityService.findAllUsers()) {
            if(user.getUserName().matches(s + ".*"))
                controllerModel.add(user);
        }

    }
}




































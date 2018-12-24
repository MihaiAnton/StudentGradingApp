package UI;


import Service.TeacherService;
import Utils.Observer;
import Utils.Events.ServiceEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public abstract class TemplateController<E> implements Observer<ServiceEvent> {

    @FXML
    protected TableView<E> table;

    private Stage thisStage;

    public void setStage(Stage stage){
        this.thisStage = stage;
    }

    @FXML
    public void quitStage(){
        this.thisStage.close();
    }



    protected TeacherService service;
    protected ObservableList<E> controllerModel = null;

    public void setService(TeacherService service){
        this.service = service;
        addThisToServiceList();
        populateList();
    }

    protected abstract void addThisToServiceList();

    @FXML
    public void initialize(){

        controllerModel = FXCollections.observableArrayList();
        table.setItems(controllerModel);

        this.table.getSelectionModel().selectedItemProperty().addListener((observable, oldvalue, newValue) -> {
            this.updateFields(newValue);
        });
    }

    public abstract void updateFields(E newValue);

    public abstract E getEntityFromFields();

    public abstract void populateList();

    public void handleError(String err){
        Alert msg = new Alert(Alert.AlertType.ERROR);
        msg.setTitle("Error message");
        msg.setContentText(err);
        msg.showAndWait();
    }

}

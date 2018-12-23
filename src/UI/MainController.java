package UI;

import Service.TeacherService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainController {

    private TeacherService teacherService;

    private Stage leftStage, rightStage, raportStage;

    public void setServices(TeacherService teacherService){

        this.teacherService = teacherService;
        initComponents();
    }

    private void initComponents(){

        try {
            try {
                //left stage init
                this.leftStage = new Stage();
                FXMLLoader loaderLeft = new FXMLLoader(getClass().getResource("StudentsFXMLView.fxml"));
                Pane leftPane = (Pane) loaderLeft.load();
                StudentViewController studentViewController = loaderLeft.getController();
                studentViewController.setService(this.teacherService);
                leftStage.setScene(new Scene(leftPane));
            }catch(Exception e){}
/*
            try{
                //right stage init
                this.rightStage = new Stage();
                FXMLLoader loaderrRight = new FXMLLoader(getClass().getResource("FilterTemplateView.fxml"));
                Pane rightPane = (Pane) loaderrRight.load();
                filterTestController = loaderrRight.getController();
                filterTestController.setService(this.service);
                rightStage.setScene(new Scene(rightPane));
            }catch(Exception e){}

            //raport stage init
            this.raportStage = new Stage();
            FXMLLoader loaderRaport = new FXMLLoader(getClass().getResource("RaportTemplateView.fxml"));
            Pane raportPane = (Pane) loaderRaport.load();
            this.raportController = loaderRaport.getController();
            raportController.setService(service);
            raportStage.setScene(new Scene(raportPane));
*/
        }
        catch(Exception e){}
    }

    @FXML
    public void openStudentScene(){
        leftStage.show();
    }

    @FXML
    public void openRightScene(){
        rightStage.show();
    }

    @FXML
    public void openRaportScene(){raportStage.show();}


}

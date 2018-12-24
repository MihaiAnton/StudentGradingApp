package UI;

import Service.TeacherService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainController {

    private TeacherService teacherService;

    private Stage studentStage, homeworkStage, raportStage;

    public void setServices(TeacherService teacherService){

        this.teacherService = teacherService;
        initComponents();
    }

    private void initComponents(){

        try {
            try {

                //student stage init
                this.studentStage = new Stage();
                FXMLLoader loaderLeft = new FXMLLoader(getClass().getResource("StudentsFXMLView.fxml"));
                Pane leftPane = loaderLeft.load();
                StudentViewController studentViewController = loaderLeft.getController();
                studentViewController.setService(this.teacherService);
                studentStage.setScene(new Scene(leftPane));

            }catch(Exception e){}

            try{

                //homework stage init
                this.homeworkStage = new Stage();
                FXMLLoader loaderrRight = new FXMLLoader(getClass().getResource("HomeworkFXMLView.fxml"));
                Pane rightPane =  loaderrRight.load();
                HomeworkViewController homeworkViewController = loaderrRight.getController();
                homeworkViewController.setService(teacherService);
                homeworkStage.setScene(new Scene(rightPane));

            }catch(Exception e){}
/*
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
        studentStage.show();
    }

    @FXML
    public void openHomeworkScene(){
        homeworkStage.show();
    }

    @FXML
    public void openRaportScene(){raportStage.show();}


}

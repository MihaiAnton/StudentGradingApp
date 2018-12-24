package UI;

import Service.TeacherService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainController {

    private TeacherService teacherService;

    private Stage studentStage, homeworkStage, gradesStage;

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
                FXMLLoader loaderRight = new FXMLLoader(getClass().getResource("HomeworkFXMLView.fxml"));
                Pane rightPane =  loaderRight.load();
                HomeworkViewController homeworkViewController = loaderRight.getController();
                homeworkViewController.setService(teacherService);
                homeworkStage.setScene(new Scene(rightPane));

            }catch(Exception e){}

            try {
                //grades stage init
                this.gradesStage = new Stage();
                FXMLLoader loaderGrades = new FXMLLoader(getClass().getResource("GradesFXMLView.fxml"));
                Pane gradePane =  loaderGrades.load();
                GradesViewController gradesViewController = loaderGrades.getController();
                gradesViewController.setService(teacherService);
                gradesStage.setScene(new Scene(gradePane));
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
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
    public void openGradesScene(){
        gradesStage.show();
    }



}

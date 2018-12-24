package UI;

import Service.TeacherService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

//'file:M:\School\Metode Avansate de Programare\StudentGradingApp\src\UI\Backgrounds\download.jpg'


public class MainController {

    private TeacherService teacherService;
    private Stage studentStage, homeworkStage, gradesStage;

    private Stage thisStage;

    public void setStage(Stage stage){
        this.thisStage = stage;
    }

    public void quitStage(){
        this.thisStage.close();
    }

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
                studentViewController.setStage(studentStage);

            }catch(Exception e){}

            try{

                //homework stage init
                this.homeworkStage = new Stage();
                FXMLLoader loaderRight = new FXMLLoader(getClass().getResource("HomeworkFXMLView.fxml"));
                Pane rightPane =  loaderRight.load();
                HomeworkViewController homeworkViewController = loaderRight.getController();
                homeworkViewController.setService(teacherService);
                homeworkStage.setScene(new Scene(rightPane));
                homeworkViewController.setStage(homeworkStage);

            }catch(Exception e){}

            try {
                //grades stage init
                this.gradesStage = new Stage();
                FXMLLoader loaderGrades = new FXMLLoader(getClass().getResource("GradesFXMLView.fxml"));
                Pane gradePane =  loaderGrades.load();
                GradesViewController gradesViewController = loaderGrades.getController();
                gradesViewController.setService(teacherService);
                gradesStage.setScene(new Scene(gradePane));
                gradesViewController.setStage(gradesStage);
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


    public void handleExit(MouseEvent mouseEvent) {
        this.quitStage();
    }
}




import Repository.GradeRepository;
import Repository.HomeworkRepository;
import Repository.StudentRepository;
import Service.TeacherService;
import UI.MainController;
import Validators.GenericValidator;
import Validators.HomeworkValidator;
import Validators.StudentValidator;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Student Grading App");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UI/MainView.fxml"));
        Pane pane = (Pane) loader.load();
        MainController controller = loader.getController();

        controller.setServices(getTeacherService());
        Scene mainScene = new Scene(pane);
        primaryStage.setScene(mainScene);

        primaryStage.show();
    }

    static TeacherService getTeacherService(){

        //repo files
        String studentFile = "M:\\School\\Metode Avansate de Programare\\StudentGradingApp\\src\\DataFiles\\Students.XML";
        String homeworkFile = "M:\\School\\Metode Avansate de Programare\\StudentGradingApp\\src\\DataFiles\\Homework.XML";
        String gradeFile = "M:\\School\\Metode Avansate de Programare\\StudentGradingApp\\src\\DataFiles\\Grades.XML";

        //repos
        StudentRepository studentRepository = new StudentRepository(new StudentValidator(), studentFile, "student");
        HomeworkRepository homeworkRepository = new HomeworkRepository(new HomeworkValidator(), homeworkFile, "homework");
        GradeRepository gradeRepository = new GradeRepository(new GenericValidator(), gradeFile, "grade");

        //service
        TeacherService teacherService = new TeacherService(studentRepository,
                                                           homeworkRepository,
                                                           gradeRepository);


        return teacherService;
    }


    public static void main(String[] args) {

        launch(args);
    }


}

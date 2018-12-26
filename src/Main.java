


import Repository.*;
import Service.ReportService;
import Service.SecurityService;
import Service.TeacherService;
import UI.MainController;
import Validators.GenericValidator;
import Validators.HomeworkValidator;
import Validators.StudentValidator;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {

    static TeacherService teacherService;

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Student Grading App");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UI/MainView.fxml"));
        Pane pane = (Pane) loader.load();
        MainController controller = loader.getController();

        teacherService = getTeacherService();
        controller.setServices(teacherService, getSecurityService());
        Scene mainScene = new Scene(pane);
        primaryStage.setScene(mainScene);

        controller.setStage(primaryStage);

        primaryStage.initStyle(StageStyle.UNDECORATED);


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
        ReportService reportService = new ReportService(teacherService);
        reportService.createPDFReport("abc","./");

        return teacherService;
    }

    static SecurityService getSecurityService(){

        //repo files
        String userFile = "M:\\School\\Metode Avansate de Programare\\StudentGradingApp\\src\\DataFiles\\Users.XML";
        String loginInfoFile = "M:\\School\\Metode Avansate de Programare\\StudentGradingApp\\src\\DataFiles\\LoginInfo.XML";

        //repos
        UserRepository userRepository = new UserRepository(new GenericValidator(),userFile,"user");
        LoginInfo loginInfo = new LoginInfo(new GenericValidator(), loginInfoFile, "password");

        //service
        SecurityService securityService = new SecurityService(userRepository,
                                                              loginInfo);

        return securityService;
    }


    public static void main(String[] args) {




        launch(args);
    }



}

package UI;

import Domain.AccesRight;
import Service.ReportService;
import Service.SecurityService;
import Service.TeacherService;
import Utils.Clock;
import Utils.Events.SecurityEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static Domain.AccesRight.ADMIN;
import static Domain.AccesRight.FULL;
import static Domain.AccesRight.RESTRICTED;

//'file:M:\School\Metode Avansate de Programare\StudentGradingApp\src\UI\Backgrounds\download.jpg'


public class MainController {

    private TeacherService teacherService;
    private SecurityService securityService;
    private ReportService reportService;
    private Stage studentStage, homeworkStage, gradesStage, reportStage, googleLoginStage;

    private Stage thisStage;

    private Clock clock;

    @FXML
    private Text loginStatus,clockText;
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    String loginStatusText;

    public void setStage(Stage stage){
        this.thisStage = stage;
    }

    public void quitStage(){
        this.thisStage.close();
    }

    public void setServices(TeacherService teacherService, SecurityService securityService, ReportService reportService){

        this.teacherService = teacherService;
        this.securityService = securityService;
        this.reportService = reportService;
        loginStatusText = "Not logged in.";
        initComponents();
        initSecurityFeatures();

    }

    private void initSecurityFeatures() {
        loginStatus.setText(securityService.getLogStatus());
        clearFields();
    }



    private void clearFields(){
        username.clear();
        password.clear();
    }

    private LoginController getGoogleLoginController(){
        try{
            this.googleLoginStage = new Stage();
            FXMLLoader loaderLogin = new FXMLLoader(getClass().getResource("LoginFXMLView.fxml"));
            Pane loginPane = loaderLogin.load();
            LoginController ctrl = loaderLogin.getController();

            googleLoginStage.setScene(new Scene(loginPane));
            ctrl.setStage(googleLoginStage);
            googleLoginStage.initStyle(StageStyle.UNDECORATED);
            return ctrl;
        }
        catch(Exception e){

        }
        return null;
    }

    private void initComponents(){

        try {
            try {

                //student stage init
                this.studentStage = new Stage();
                FXMLLoader loaderLeft = new FXMLLoader(getClass().getResource("StudentsFXMLView.fxml"));
                Pane leftPane = loaderLeft.load();
                StudentViewController studentViewController = loaderLeft.getController();
                studentViewController.setSecurityService(this.securityService);
                studentViewController.setService(this.teacherService);

                studentStage.setScene(new Scene(leftPane));
                studentViewController.setStage(studentStage);
                studentStage.initStyle(StageStyle.UNDECORATED);

                //login substage
                LoginController loginController = getGoogleLoginController();
                studentViewController.setLoginController(loginController);
                if(loginController == null){
                    System.out.println("Error creating login scene.");
                }
                else{
                    loginController.setMailService(studentViewController.getMailService());
                }

            }catch(Exception e){}

            try{

                //homework stage init
                this.homeworkStage = new Stage();
                FXMLLoader loaderRight = new FXMLLoader(getClass().getResource("HomeworkFXMLView.fxml"));
                Pane rightPane =  loaderRight.load();
                HomeworkViewController homeworkViewController = loaderRight.getController();
                homeworkViewController.setSecurityService(this.securityService);
                homeworkViewController.setService(teacherService);
                homeworkViewController.setSecurityService(this.securityService);
                homeworkStage.setScene(new Scene(rightPane));
                homeworkViewController.setStage(homeworkStage);
                homeworkStage.initStyle(StageStyle.UNDECORATED);

            }catch(Exception e){}

            try {
                //grades stage init
                this.gradesStage = new Stage();
                FXMLLoader loaderGrades = new FXMLLoader(getClass().getResource("GradesFXMLView.fxml"));
                Pane gradePane =  loaderGrades.load();
                GradesViewController gradesViewController = loaderGrades.getController();
                gradesViewController.setSecurityService(this.securityService);
                gradesViewController.setService(teacherService);

                gradesStage.setScene(new Scene(gradePane));
                gradesViewController.setStage(gradesStage);
                gradesStage.initStyle(StageStyle.UNDECORATED);
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }

            try{
                this.reportStage = new Stage();
                FXMLLoader reportLoader = new FXMLLoader(getClass().getResource("ReportsFXMLView.fxml"));
                Pane reportPane = reportLoader.load();
                ReportsViewController reportsViewController = reportLoader.getController();
                reportsViewController.setSecurityService(this.securityService);
                reportsViewController.setService(teacherService);
                reportsViewController.setReportsService(this.reportService);

                reportStage.setScene(new Scene(reportPane));
                reportsViewController.setStage(reportStage);
                reportStage.initStyle(StageStyle.UNDECORATED);
            }
            catch(Exception e){

            }

            clock = new Clock(this.clockText);
            clock.start();



            //login for demo
            securityService.logIn("admin@cs.ubbcluj.ro","admin1234");
            this.securityService.notifyObserver(new SecurityEvent(null,"security"));


        }
        catch(Exception e){}
    }

    @FXML
    public void openStudentScene(){
        AccesRight[] neededRight = {ADMIN, FULL};

        if(securityService.grantAcces(neededRight)){
            studentStage.show();
        }
        else{
            handleError("You don't have the acces rights to enter this menu.");
        }
    }

    @FXML
    public void openHomeworkScene(){
        AccesRight[] neededRight = {ADMIN, FULL, RESTRICTED};

        if(securityService.grantAcces(neededRight)){
            homeworkStage.show();
        }
        else {
            handleError("Log in to enter this menu.");
        }
    }

    @FXML
    public void openGradesScene(){
        AccesRight[] neededRight = {ADMIN, FULL, RESTRICTED};

        if(securityService.grantAcces(neededRight)){
            gradesStage.show();
        }
        else{
            handleError("Log in to enter this menu.");
        }
    }

    @FXML
    public void openReportScene(){
        AccesRight[] neededRight = {ADMIN, FULL};

        if(securityService.grantAcces(neededRight)){
            reportStage.show();
        }
        else{
            handleError("You don't have the acces rights to enter this menu.");
        }
    }


    public void handleExit(MouseEvent mouseEvent) {
        this.clock.stopClock();
        this.quitStage();
    }


    ////////////////security controls
    @FXML
    public void handleLogin(){
        try{
            securityService.logIn(username.getText(),password.getText());
            loginStatusText = "Logged in as " + username.getText() + ".";
            String name = username.getText();
            initSecurityFeatures();
            handleConfirmation("Logged in as " + name + ".","Login confirmation");
            this.securityService.notifyObserver(new SecurityEvent(null,"security"));
        }
        catch (SecurityException e){
            handleError(e.getMessage());
        }
        catch (Exception e){
            handleError("Login error.");
        }
    }

    @FXML
    public void handleLogout(){
        try{
            securityService.logOut();
            loginStatusText = "Not logged in.";
            initSecurityFeatures();
            handleConfirmation("Successfully logged out.","Logout confirmation.");
            this.securityService.notifyObserver(new SecurityEvent(null,"security"));
        }
        catch (SecurityException e){
            handleError(e.getMessage());
        }
        catch (Exception e){
            handleError("Logout error.");
        }
    }

    @FXML
    public void handleCreateAccount(){
        try{
            securityService.addUser(username.getText(),password.getText());
            handleConfirmation("Successfully created " + username.getText()+ ".","Account created confirmation.");
        }
        catch (SecurityException e){
            handleError(e.getMessage());
        }
        catch (Exception e){
            handleError("Account create error.");
        }
    }

    private void handleError(String err){
        Alert msg = new Alert(Alert.AlertType.ERROR);
        msg.setTitle("Error message");
        msg.setContentText(err);
        msg.showAndWait();
    }

    public void handleConfirmation(String text,String title){
        Alert msg = new Alert(Alert.AlertType.CONFIRMATION);
        msg.setTitle(title);
        msg.setContentText(text);
        msg.showAndWait();
    }
}

























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
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private GradesViewController gradesViewController;

    private Stage thisStage;

    private Clock clock;

    @FXML
    private ImageView lockPic;

    @FXML
    private Text loginStatus,clockText;
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button logBtn;

    String loginStatusText;

    public void setStage(Stage stage){
        this.thisStage = stage;
    }

    public void quitStage(){
        this.thisStage.close();
    }

    public void setServices(TeacherService teacherService, SecurityService securityService, ReportService reportService){

        username.setStyle("-fx-text-inner-color: #e6e6e6;-fx-background-color: #333333;");
        password.setStyle("-fx-text-inner-color: #e6e6e6;-fx-background-color: #333333;");
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
                this.gradesViewController = loaderGrades.getController();
                gradesViewController.setSecurityService(this.securityService);
                gradesViewController.setService(teacherService);

                gradesStage.setScene(new Scene(gradePane));
                gradesViewController.setStage(gradesStage);
                gradesStage.initStyle(StageStyle.UNDECORATED);

                //login substage
                LoginController loginController = getGoogleLoginController();
                gradesViewController.setLoginController(loginController);
                if(loginController == null){
                    System.out.println("Error creating login scene.");
                }
                else{
                    loginController.setMailService(gradesViewController.getMailService());
                }
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
            this.logBtn.setText("Logout");


        }
        catch(Exception e){}
    }

    @FXML
    public void openStudentScene(){
        AccesRight[] neededRight = {ADMIN, FULL};
        try {
            if (securityService.grantAcces(neededRight)) {
                studentStage.show();
            } else {
                handleError("You don't have the acces rights to enter this menu.");
            }
        }
        catch(Exception e){}
    }

    @FXML
    public void openHomeworkScene(){
        AccesRight[] neededRight = {ADMIN, FULL, RESTRICTED};

        try {
            if (securityService.grantAcces(neededRight)) {
                homeworkStage.show();
            } else {
                handleError("Log in to enter this menu.");
            }
        }
        catch(Exception e){}
    }

    @FXML
    public void openGradesScene(){
        AccesRight[] neededRight = {ADMIN, FULL, RESTRICTED};

        try {
            if (securityService.grantAcces(neededRight)) {
                this.gradesViewController.onOpen();
                gradesStage.show();
            } else {
                handleError("Log in to enter this menu.");
            }
        }
        catch(Exception e){}
    }

    @FXML
    public void openReportScene(){
        AccesRight[] neededRight = {ADMIN, FULL};
        try {
            if (securityService.grantAcces(neededRight)) {
                reportStage.show();
            } else {
                handleError("You don't have the acces rights to enter this menu.");
            }
        }
        catch(Exception e){}
    }


    public void handleExit(MouseEvent mouseEvent) {
        this.clock.stopClock();
        this.quitStage();
    }


    ////////////////security controls
    @FXML
    public void handleLoginLogout(){

        if(!this.securityService.isLoggedIn()) {

            try {
                securityService.logIn(username.getText(), password.getText());
                this.logBtn.setText("Logout");
                loginStatusText = "Logged in as " + username.getText() + ".";
                String name = username.getText();
                initSecurityFeatures();
                this.lockPic.setImage(new Image("UI/Icons/pass2.png"));
                handleConfirmation("Logged in as " + name + ".", "Login confirmation");
                this.securityService.notifyObserver(new SecurityEvent(null, "security"));

            } catch (SecurityException e) {
                handleError(e.getMessage());
            } catch (Exception e) {
                handleError("Login error.");
            }
        }
        else{
            try{
                securityService.logOut();
                this.logBtn.setText("Login");
                loginStatusText = "Not logged in.";
                initSecurityFeatures();
                this.lockPic.setImage(new Image("UI/Icons/lock.png"));
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
    }

   /* @FXML
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
    }*/

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

























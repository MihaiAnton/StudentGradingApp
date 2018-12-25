package UI;

import Service.SecurityService;
import Service.TeacherService;
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

//'file:M:\School\Metode Avansate de Programare\StudentGradingApp\src\UI\Backgrounds\download.jpg'


public class MainController {

    private TeacherService teacherService;
    private SecurityService securityService;
    private Stage studentStage, homeworkStage, gradesStage;

    private Stage thisStage;

    @FXML
    private Text loginStatus;
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

    public void setServices(TeacherService teacherService, SecurityService securityService){

        this.teacherService = teacherService;
        this.securityService = securityService;
        loginStatusText = "Not logged in.";
        initComponents();
        initSecurityFeatures();

    }

    private void initSecurityFeatures() {
        loginStatus.setText(loginStatusText);
        clearFields();
    }



    private void clearFields(){
        username.clear();
        password.clear();
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
                studentViewController.setSecurityService(this.securityService);
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
                gradesViewController.setService(teacherService);
                gradesViewController.setSecurityService(this.securityService);
                gradesStage.setScene(new Scene(gradePane));
                gradesViewController.setStage(gradesStage);
                gradesStage.initStyle(StageStyle.UNDECORATED);
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


    ////////////////security controls
    @FXML
    public void handleLogin(){
        try{
            securityService.logIn(username.getText(),password.getText());
            loginStatusText = "Logged in as " + username.getText() + ".";
            String name = username.getText();
            initSecurityFeatures();
            handleConfirmation("Logged in as " + name + ".","Login confirmation");
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

























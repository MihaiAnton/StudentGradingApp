package UI;

import Service.MailService;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginController {


    private MailService mailSrv;
    @FXML
    TextField email;

    @FXML
    PasswordField passwordField;
    private Stage stage;


    public void setMailService(MailService mailService){
        this.mailSrv = mailService;
    }

    @FXML
    public void initialize(){
    }

    public String getPassword(){
        return this.passwordField.getText();

    }

    public String getEmail(){
        return this.email.getText();
    }

    @FXML
    public void handleLogin(){
        this.mailSrv.logIn(getEmail(),getPassword());
        //this.mailSrv.send("mihai_anton98@yahoo.com","Helllllooooo","text sample");
        this.stage.close();
    }

    public void setStage(Stage googleLoginStage) {
        this.stage = googleLoginStage;
    }

    public void show() {
        this.stage.show();
    }
}

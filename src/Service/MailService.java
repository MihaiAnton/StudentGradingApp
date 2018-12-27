package Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class MailService{

    private String userName = null;
    private String password = null;


    public MailService(){ }

    public void logIn(String mail,String password){
        this.password = password;
        this.userName = mail;
    }

    public boolean isLoggedIn(){
        if(password == null){
            return false;
        }
        return true;
    }


    public void send(String recipient, String title, String text){

        if(password == null){
            throw new RuntimeException("Not logged in.");
        }


        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(userName, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(userName));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject(title);
            message.setText(text);

            Transport.send(message);


        } catch (Exception e) {
            throw new RuntimeException("Error sending mail.");
        }

    }
}
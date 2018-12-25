package Service;

import Domain.AccesRight;
import Domain.Password;
import Domain.User;
import Repository.LoginInfo;
import Repository.UserRepository;
import Utils.Events.SecurityEvent;
import Utils.Observable;
import Utils.Observer;

import java.util.ArrayList;

public class SecurityService implements Observable<SecurityEvent> {

    private UserRepository userRepository;  //user repo
    private LoginInfo loginInfo;            //password repo

    private User loggedUser;
    private boolean loggedIn;
    private AccesRight accesRight;

    public SecurityService(UserRepository userRepository, LoginInfo loginInfo) {
        this.userRepository = userRepository;
        this.loginInfo = loginInfo;
        loggedIn = false;
        loggedUser = null;
        accesRight = AccesRight.GENERIC;
    }

    /***
     *  Called whenever a new user is created
     * @param userName
     * @param password
     */
    public String addUser(String userName,String password) {


        if (userName.matches("[a-zA-Z][a-zA-Z0-9]*@scs.ubbcluj.ro")) {        //student login
            if (loggedIn) {
                throw new SecurityException("Log out to create a student account.");
            } else {
                int id = userRepository.getNextId();
                if (userRepository.save(new User(userName, AccesRight.RESTRICTED)) == null) {

                    Password pass = new Password();
                    pass.setPassword(encryptPassword(password));
                    pass.setId(userName);
                    loginInfo.save(pass);

                    return "Account created";
                } else {
                    throw new SecurityException("Existing Account");
                }
            }
        } else if (userName.matches("[a-zA-Z][a-zA-Z0-9]*@cs.ubbcluj.ro")) {       //teacher login
            if (loggedIn && accesRight == AccesRight.ADMIN) {
                int id = userRepository.getNextId();
                if (userRepository.save(new User(userName, AccesRight.FULL)) == null) {
                    Password pass = new Password();
                    pass.setPassword(encryptPassword(password));
                    pass.setId(userName);
                    loginInfo.save(pass);

                    return "Account created";
                }
            } else {
                throw new SecurityException("Must be logged in as ADMIN to create teacher accounts.");
            }
        }

        else{
            throw new SecurityException("Invalid username.");
        }
        return "";
    }

    private void passwordCheck(String password){

    }

    private String encryptPassword(String pass){
        String rez = "";
        for (int i=0;i<pass.length();i++) {
            char b = pass.charAt(i);
            if(b >= 'a' && b <= 'z'){
                b = (char)(b - 'a');
                b = (char)((b + 16)%('z'-'a'+1) + 'a');
            }
            else if(b >= 'A' && b <= 'Z'){
                b = (char) (b - 'A');
                b = (char)((b + 21)%('Z'-'A'+1) + 'A');
            }
            else if(b >= '0' && b <= '9'){
                b = (char)(b - '0');
                b = (char)((b + 4)%('9'-'0'+1) + '0');
            }
            rez = rez + b;
        }
        return rez;
    }




    public void logIn(String userName,String password){
        if(loggedIn){
            throw new SecurityException("Already logged in.");
        }


        User user = (User)userRepository.findOne(userName);
        if(user == null){
            throw new SecurityException("Invalid username or password");
        }
        else{
            Password pass = (Password)loginInfo.findOne(user.getId());
            if(!pass.getPassword().equals(encryptPassword(password))){
                throw new SecurityException("Invalid username or password");
            }
            else{       //matching information
                this.loggedIn = true;
                this.loggedUser = user;
                this.accesRight = user.getRight();
            }
        }

    }

    public void logOut(){
        if(!loggedIn){
            throw  new SecurityException("Not logged in.");
        }
        this.accesRight = AccesRight.GENERIC;
        this.loggedUser = null;
        this.loggedIn = false;
    }


























    /////////////The observable part
    private ArrayList<Observer> observers = new ArrayList<>();


    @Override
    public void addObserver(Observer<SecurityEvent> observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<SecurityEvent> observer) {
        for(int i=0;i<observers.size();i++){
            if(observers.get(i) == observer){
                observers.remove(i);
                break;
            }
        }
    }

    @Override
    public void notifyObserver(SecurityEvent event) {
        for (Observer obs:observers) {
            obs.notify(event);
        }
    }
}
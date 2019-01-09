package Service;

import Domain.AccesRight;
import Domain.Password;
import Domain.User;
import Repository.LoginInfo;
import Repository.UserRepository;
import Utils.Events.Event;
import Utils.Events.SecurityEvent;
import Utils.Observable;
import Utils.Observer;

import java.util.ArrayList;

public class SecurityService implements Observable<Event> {

    private UserRepository userRepository;  //user repo
    private LoginInfo loginInfo;            //password repo

    private User loggedUser;
    private boolean loggedIn;
    private AccesRight accesRight;

    private int adminCount = 0;

    public SecurityService(UserRepository userRepository, LoginInfo loginInfo) {
        this.userRepository = userRepository;
        this.loginInfo = loginInfo;
        loggedIn = false;
        loggedUser = null;
        accesRight = AccesRight.GENERIC;

        adminCount = countAdmins();
    }

    private int countAdmins() {
        int nr = 0;
        for (Object user:
             userRepository.findAll()) {
            user = (User)user;
            if(((User) user).getRight().equals(AccesRight.ADMIN))
                nr++;
        }
        return nr;
    }

    /***
     *  Called whenever a new user is created
     * @param userName
     * @param password
     */
    public String addUser(String userName,String password) {


        if (userName.matches("[a-zA-Z][a-zA-Z0-9]*@scs.ubbcluj.ro")) {        //student login
            if (loggedIn && !accesRight.equals(AccesRight.ADMIN)) {
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

    public void updateUser(String userName,String passWord,AccesRight accesRight){

       // System.out.println("In update user: " + adminCount);
        if(this.userRepository.findOne(userName) == null){
            throw new SecurityException("User not found.");
        }

        else{
            User user = (User)userRepository.findOne(userName);
            if(!accesRight.equals(user.getRight())){
                if(accesRight.equals(AccesRight.ADMIN))
                    adminCount++;
                if(user.getRight().equals(AccesRight.ADMIN) && adminCount == 1 && !accesRight.equals(AccesRight.ADMIN)){
                    throw  new SecurityException("Can't deprecate the only ADMIN.");
                }
                if(user.getRight().equals(AccesRight.ADMIN) && !accesRight.equals(AccesRight.ADMIN)){
                    adminCount--;
                }

                user.setRight(accesRight);
                userRepository.update(user);
            }

            if(!passWord.equals("") && !loginInfo.findOne(userName).equals(encryptPassword(passWord))){
                Password pass = new Password();
                pass.setId(userName);
                pass.setPassword(encryptPassword(passWord));
                loginInfo.update(pass);
            }
        }
    }

    public void deleteUser(String userName){

        this.userRepository.delete(userName);
        this.loginInfo.delete(userName);

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



    public AccesRight getAccesRight(){
        return this.accesRight;
    }

    public boolean grantAcces(AccesRight[] rightsNeeded){
        //grants acces if the current right is in the list
        for(AccesRight right : rightsNeeded){
            if(getAccesRight().equals(right)){
                return true;
            }
        }
        return false;
    }





    public String getStudentId(){
        if(loggedUser == null){
            return null;
        }
        if(accesRight == AccesRight.RESTRICTED){
            char[] cset = loggedUser.getUserName().toCharArray();
            String id = "";
            for(int i =0;i<cset.length;i++){
                if(cset[i] == '@'){
                    return id;
                }
                else if(cset[i] >= (char)'0' && cset[i] <= (char)'9'){
                    id = id + cset[i];
                }
                else{
                    id = "";
                }
            }
            return id;
        }
        return null;
    }

    public boolean isLoggedIn(){
        return this.loggedIn;
    }
















    /////////////The observable part
    private ArrayList<Observer> observers = new ArrayList<>();


    @Override
    public void addObserver(Observer<Event> observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Event> observer) {
        for(int i=0;i<observers.size();i++){
            if(observers.get(i) == observer){
                observers.remove(i);
                break;
            }
        }
    }

    @Override
    public void notifyObserver(Event event) {
        for (Observer obs:observers) {
            obs.notify(event);
        }
    }

    public String getLogStatus() {
        if(loggedIn){
            return "Logged in as " + this.loggedUser.getUserName();
        }
        else{
            return "Not logged in.";
        }
    }

    public User findUser(String text) {
        return (User)this.userRepository.findOne(text);
    }


    public Iterable<User> findAllUsers(){
        return this.userRepository.findAll();
    }
}

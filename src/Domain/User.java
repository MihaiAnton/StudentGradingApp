package Domain;

public class User implements IdEntity<String> {

    String id;
    String userName;
    String password;
    AccesRight right;

   public User(String userName,AccesRight right){
       this.id = userName;
       this.userName = userName;
       this.right = right;
       this.password = null;
   }

    public AccesRight getRight() {
        return right;
    }

    public void setRight(AccesRight right) {
        this.right = right;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }
}

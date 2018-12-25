package Domain;

public class Password implements IdEntity<String> {

    String id;
    String Password = null;    //the encrypted password

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getPassword(){
        return Password;
    }

    public void setPassword(String password){
        this.Password = password;
    }

    @Override
    public String getId() {
        return id;
    }
}

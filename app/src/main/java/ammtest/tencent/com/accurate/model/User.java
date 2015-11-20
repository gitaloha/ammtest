package ammtest.tencent.com.accurate.model;

/**
 * Created by xiazeng on 2015/11/20.
 */
public class User {
    private static User instance = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private String password;

    public User() {
        name = "Anonymous";
    }

    static public User getInstance(){
        if(instance == null){
            instance = new User();
        }
        return instance;
    }
    public void save(){

    }

    public void update(){

    }

    public  boolean isValided(){
        boolean result = true;
        return  result;
    }
}

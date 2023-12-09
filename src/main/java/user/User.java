package user;

/**
 * @author Bu Xinran
 * @Description 用户
 * @date 2023/12/7 20:21
 */
public class User {
    private String username;
    private String password;
    public User(String username,String password){
        this.username=username;
        this.password=password;
    }
    public User(){}
    public String getUsername(){return username;}
    public String getPassword(){return password;}
    public void setUsername(String name){this.username=name;}
    public void setPassword(String password){this.password=password;}
}

package Database;
import user.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
/**
 * @author Bu Xinran
 * @Description 连接数据库
 * @date 2023/12/7 20:16
 */
public class DataInit {
    private final static String driver = "com.mysql.cj.jdbc.Driver";
    private final static String username = "root";
    private final static String password = "root";
    private final static String url = "jdbc:mysql://10.192.229.109/miaomiaodi";
    private final static String insert = "insert into user(username,password) values(?,?)";
    private final static String update = "update user set password = ? where username = ?";
    private final static String delete = "delete from user where username = ?";
    private final static String select = "select * from user where username = ?";
    private static Connection connection;
    static
    {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url,username,password);
        }
        catch(Exception e) {
            e.printStackTrace();
            connection = null;
        }
    }
    /***
     * @Description 判断该个用户名是否已被创建
     * @param user 用户
     * @return boolean
     * @author Bu Xinran
     * @date 2023/12/7 20:23
    **/
    public static boolean exists(User user) {
        try {
            PreparedStatement exist = connection.prepareStatement(select);
            exist.setString(1,user.getUsername());
            ResultSet existResult = exist.executeQuery();
            return existResult.next();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /***
     * @Description  向数据库中增加一条用户信息
     * @param user 用户
     * @return boolean
     * @author Bu Xinran
     * @date 2023/12/7 20:25
    **/
    public static boolean add(User user)
    {
        try {
            PreparedStatement add = connection.prepareStatement(insert);
            add.setString(1, user.getUsername());
            add.setString(2, user.getPassword());
            return add.executeUpdate() == 1;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /***
     * @Description  修改密码
     * @param user 用户信息
     * @return boolean
     * @author Bu Xinran
     * @date 2023/12/7 20:28
    **/
    public static boolean modify(User user)
    {
        try {
            PreparedStatement modify = connection.prepareStatement(update);
            modify.setString(1, user.getUsername());
            modify.setString(2, user.getPassword());
            return modify.executeUpdate() == 1;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }
    /***
     * @Description  注销用户信息
     * @param user 用户
     * @return boolean
     * @author Bu Xinran
     * @date 2023/12/7 20:28
    **/
    public static boolean delete(User user) {
        if(exists(user))
        {
            try
            {
                PreparedStatement del = connection.prepareStatement(delete);
                del.setString(1, user.getUsername());
                return del.executeUpdate() == 1;
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }
    /***
     * @Description 获取用户所有信息
     * @param name 用户名
     * @return user.User
     * @author Bu Xinran
     * @date 2023/12/7 20:29
    **/
    public static User getByName(String name) {
        try {
            PreparedStatement exist = connection.prepareStatement(select);
            exist.setString(1, name);
            ResultSet existResult = exist.executeQuery();
            if(existResult.next()) {
                User user = new User();
                user.setUsername(existResult.getString("username"));
                user.setPassword(existResult.getString("password"));
                return user;
            }
            return null;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

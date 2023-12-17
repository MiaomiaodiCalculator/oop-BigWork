package Database;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import user.User;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author Bu Xinran
 * @Description 连接数据库
 * @date 2023/12/7 20:16
 */
public class SqlUser {
    private final static String driver = "com.mysql.cj.jdbc.Driver";
    private final static String username = "root";
    private final static String password = "qwe1234!";
    private final static String url = "jdbc:mysql://bj-cynosdbmysql-grp-ac12urxu.sql.tencentcdb.com:29055/miaomiaodi";
    private final static String insert = "insert into user(username,password,avatar) values(?,?,?)";
    private final static String update = "update user set password = ? where username = ?";
    private final static String delete = "delete from user where username = ?";
    private final static String select = "select * from user where username = ?";
    private final static String updateImage = "update user set avatar=?  where username = ?";
    public static Connection connection;
    static
    {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url,username,password);
        }
        catch(Exception e) {
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
            add.setString(3, null);
            return add.executeUpdate() == 1;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /***
     * @Description  修改密码
     * @param name 用户名
     * @param password 密码
     * @author Bu Xinran
     * @date 2023/12/7 20:28
    **/
    public static void modify(String name, String password) {
        try {
            PreparedStatement modify = connection.prepareStatement(update);
            modify.setString(1, password);
            modify.setString(2, name);
            modify.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
    /***
     * @Description
     * @param fis 读入的图片信息
     * @param name   用户名
     * @author Bu Xinran
     * @date 2023/12/8 12:51
    **/
    public static void uploadAvatar(FileInputStream fis,String name) throws SQLException {
        PreparedStatement upload = connection.prepareStatement(updateImage);
        try {
            upload.setBinaryStream(1,fis, fis.available());
            upload.setString(2, name);
            upload.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /***
     * @Description  从数据库中获取头像
     * @param name 用户名
     * @return javafx.scene.image.Image
     * @author Bu Xinran
     * @date 2023/12/8 12:54
    **/
    public static Image loadAvatar(String name) {
        try {
            PreparedStatement exist = connection.prepareStatement(select);
            exist.setString(1, name);
            ResultSet result = exist.executeQuery();
            if (result.next()) {
                byte[] imageData = result.getBytes("avatar");
                if(imageData==null)return null;
                ByteArrayInputStream image = new ByteArrayInputStream(imageData);
                return new Image(image);
            }
            result.close();
            exist.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

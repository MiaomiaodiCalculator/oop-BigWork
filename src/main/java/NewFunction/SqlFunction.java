package NewFunction;

import com.calculator.calculation.LoginController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
/**
 * @author sxq
 * @Description 处理自定义函数与数据库的连接
 * @date 2023/12/7 21:51
**/

public class SqlFunction
{
    private final static String driver = "com.mysql.cj.jdbc.Driver";//固定？
    private final static String username = "root";
    private final static String password = "qwe1234!";
    private final static String url = "jdbc:mysql://bj-cynosdbmysql-grp-ac12urxu.sql.tencentcdb.com:29055/miaomiaodi";//最后一列为表名
    private final static String insert = "insert into userfunction(name,paraNum,formula,exp,username) values(?,?,?,?,?)";
    private final static String update = "update userfunction set formula = ?,exp = ? where name = ? and username = ?";
    private final static String delete = "delete from userfunction where name = ? and username = ?";
    private final static String select = "select * from userfunction where name = ? and username = ?";
    private final static String selectAll = "select * from userfunction where username = ?";
    private static Connection connection;

    static
    {
        try
        {
            Class.forName(driver);
            connection = DriverManager.getConnection(url,username,password);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            connection = null;
        }
    }
    /**
     * @Description 判断自定义函数是否存在（当前用户）
 * @param name 传入的自定义函数名
 * @return boolean       
     * @author sxq
     * @date 2023/12/7 21:51
    **/
    public static boolean exists(String name)
    {
        try
        {
            PreparedStatement exist = connection.prepareStatement(select);
            exist.setString(1,name);
            exist.setString(2,LoginController.userName);
            ResultSet existResult = exist.executeQuery();
            return existResult.next();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * @Description 添加自定义函数到数据库
 * @param f
 * @return boolean
     * @author sxq
     * @date 2023/12/7 22:11
    **/
    public static boolean add(UserFunction f)
    {
        try
        {
            PreparedStatement add = connection.prepareStatement(insert);
            add.setString(1, f.getName());
            add.setInt(2, f.getParaNum());
            add.setString(3, f.getFormula());
            add.setString(4,f.getExp());
            add.setString(5, LoginController.userName);
            return add.executeUpdate() == 1;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }
/**
 * @Description 修改自定义函数，只能修改表达式formula和exp
 * @param f
 * @return boolean
 * @author sxq
 * @date 2023/12/7 22:11
**/
    public static boolean modify(UserFunction f)
    {
        try
        {
            PreparedStatement modify = connection.prepareStatement(update);
            System.out.println("更新函数"+f.getName());
            modify.setString(1, f.getFormula());
            modify.setString(2, f.getExp());
            modify.setString(3, f.getName());
            modify.setString(4,LoginController.userName);
            return modify.executeUpdate() == 1;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }
/**
 * @Description 删除数据库中的某自定义函数
 * @param f
 * @return boolean
 * @author sxq
 * @date 2023/12/7 22:15
**/
    public static boolean delete(UserFunction f)
    {
        if(exists(f.getName()))
        {
            try
            {
                PreparedStatement del = connection.prepareStatement(delete);
                del.setString(1, f.getName());
                del.setString(2,LoginController.userName);
                return del.executeUpdate() == 1;
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }
    /**
     * @Description 根据函数名查询（全字匹配）
 * @param name 需查询的函数名
 * @return NewFunction.UserFunction
     * @author sxq
     * @date 2023/12/7 22:16
    **/
    public static UserFunction getByName(String name)
    {
        try
        {
            PreparedStatement exist = connection.prepareStatement(select);
            exist.setString(1, name);
            exist.setString(2,LoginController.userName);
            ResultSet existResult = exist.executeQuery();
            if(existResult.next())
            {
                UserFunction f = new UserFunction(existResult.getString("name"),existResult.getString("exp"),existResult.getString("formula"));
                f.setParaNum(existResult.getInt("paraNum"));
                return f;
            }
            return null;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    public static ArrayList<UserFunction> getAllFunction(){
        ArrayList<UserFunction> res=new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(selectAll);
            statement.setString(1, LoginController.userName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                UserFunction f = new UserFunction(resultSet.getString("name"),resultSet.getString("exp"),resultSet.getString("formula"));
                f.setParaNum(resultSet.getInt("paraNum"));
                // 加入list
                res.add(f);
                System.out.println(f.getName());
            }
            return res;
        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
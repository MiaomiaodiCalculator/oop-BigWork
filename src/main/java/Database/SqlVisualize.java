package Database;

import com.calculator.calculation.LoginController;
import com.calculator.calculation.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static Database.SqlUser.connection;

/**
 * @author sxq
 * @Description 保存函数图像绘制的历史记录到数据库
 * @date 2023/12/16 20:33
 */
public class SqlVisualize {
    private final static String insert = "insert into vishistory(dataShow,username,time) values(?,?,?)";
    private final static String delete = "delete from vishistory where time = ? and username = ?";
    private final static String select = "select * from vishistory where time = ? and username = ?";
    private final static String selectAll = "select * from vishistory where username = ? order by time";

    /**
     * @Description  查找某一历史记录是否存在（函数图像绘制） 
 * @return boolean       
     * @author sxq
     * @date 2023/12/16 20:53
    **/
    public static boolean exists(Timestamp time)
    {
        try
        {
            PreparedStatement exist = connection.prepareStatement(select);
            exist.setTimestamp(1,time);
            exist.setString(2, LoginController.userName);
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
 * @Description 向数据库中传入函数图像绘制的历史记录
 * @param dataShow 表达式
 * @return boolean
 * @author sxq
 * @date 2023/12/16 21:03
**/
    public static boolean add(String[] dataShow)
    {
        try
        {
            PreparedStatement add = connection.prepareStatement(insert);
            add.setString(1, Main.serializeStringList(List.of(dataShow)));
            add.setString(2,LoginController.userName);
            add.setTimestamp(3,new Timestamp(System.currentTimeMillis()));
            return add.executeUpdate() == 1;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * @Description 获取当前用户的全部历史记录
 * @return java.lang.String[]
     * @author sxq
     * @date 2023/12/16 21:07
    **/
    public static String[] getAllHis(){
        String[] res=null;
        try {
            PreparedStatement statement = connection.prepareStatement(selectAll);
            statement.setString(1, LoginController.userName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                res= Main.deserializeStringList(resultSet.getString("dataShow")).toArray(new String[0]);
            }
            return res;
        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

}

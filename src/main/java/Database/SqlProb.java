package Database;

import Probability.InputData;
import com.calculator.calculation.LoginController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import static Database.SqlUser.connection;

/**
 * @author sxq
 * @Description 概率统计的数据库保存
 * @date 2023/12/17 11:08
 */
public class SqlProb {
    private final static String insert = "insert into probhistory(data1,data2,userName,time) values(?,?,?,?)";
    private final static String delete = "delete from probhistory where time = ? and username = ?";
    private final static String select = "select * from probhistory where time = ? and username = ?";
    private final static String selectAll = "select * from probhistory where username = ? order by time";
    /**
     * @Description 判断某条历史记录是否存在
 * @param time 保存时间
 * @return boolean
     * @author sxq
     * @date 2023/12/17 11:14
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
     * @Description 添加新的历史记录
 * @param d inputdata保存类
 * @return boolean
     * @author sxq
     * @date 2023/12/17 11:23
    **/
    public static boolean add(InputData d)
    {
        try
        {
            PreparedStatement add = connection.prepareStatement(insert);
            add.setString(1,d.getData1());
            add.setString(2,d.getData2());
            add.setString(3, LoginController.userName);
            add.setTimestamp(4,d.getSaveTime());
            return add.executeUpdate() == 1;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }

    }
/**
 * @Description 删除数据库中某条记录
 * @param d 选中需要删除的记录
 * @return boolean
 * @author sxq
 * @date 2023/12/17 11:24
**/
    public static boolean delete(InputData d)
    {
        if(exists(d.getSaveTime()))
        {
            try
            {
                PreparedStatement del = connection.prepareStatement(delete);
                del.setTimestamp(1, d.getSaveTime());
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
     * @Description 获取用户的全部历史记录
 * @return java.util.ArrayList<Probability.InputData>
     * @author sxq
     * @date 2023/12/17 11:28
    **/
    public static ArrayList<InputData> getAllHis(){
        ArrayList<InputData> res=new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(selectAll);
            statement.setString(1, LoginController.userName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                InputData d = new InputData();
                d.setData1(resultSet.getString("data1"));
                d.setData2(resultSet.getString("data2"));
                d.setSaveTime(resultSet.getTimestamp("time"));
                // 加入list
                res.add(d);
            }
            return res;
        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }


}

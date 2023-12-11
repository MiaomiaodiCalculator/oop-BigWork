package Database;
import NewFunction.UserFunction;
import com.calculator.calculation.LoginController;
import com.calculator.calculation.Main;
import infinitesimal.InfinitesimalSolve;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static Database.SqlUser.connection;
/**
 * @author sxq
 * @Description 将微积分的计算过程保存至数据库中
 * @date 2023/12/8 11:13
 */
public class SqlInfinitesimal {
    private final static String insert = "insert into infhistory(upValue,downValue,formulaProcess,expProcess,result,userName,time) values(?,?,?,?,?,?,?)";
    private final static String delete = "delete from infhistory where time = ? and username = ?";
    private final static String select = "select * from infhistory where time = ? and username = ?";
    private final static String selectAll = "select * from infhistory where username = ? order by time";


   /**
    * @Description 判断条历史记录是否存在
 * @param time 保存该记录的时间
 * @return boolean
    * @author sxq
    * @date 2023/12/8 11:46
   **/
    public static boolean exists(Timestamp time)
    {
        try
        {
            PreparedStatement exist = connection.prepareStatement(select);
            exist.setTimestamp(1,time);
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
     * @Description 向数据库中传入微积分计算历史记录
 * @param ift 生成的微积分对象
 * @return boolean
     * @author sxq
     * @date 2023/12/8 11:55
    **/
    public static boolean add(InfinitesimalSolve ift)
    {
        try
        {
            PreparedStatement add = connection.prepareStatement(insert);
            add.setDouble(1,ift.getUpValue());
            add.setDouble(2, ift.getDownValue());
            add.setString(3, Main.serializeStringList(ift.getFormulaProcess()));
            add.setString(4,Main.serializeStringList(ift.getExpProcess()));
            add.setDouble(5,ift.getResult());
            add.setString(6, LoginController.userName);
            add.setTimestamp(7,new Timestamp(System.currentTimeMillis()));
            return add.executeUpdate() == 1;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
        
    }
    /**
     * @Description 删除某时刻保存的历史记录
 * @param ift 需要删除的微积分历史记录
 * @return boolean       
     * @author sxq
     * @date 2023/12/9 11:13
    **/
    public static boolean delete(InfinitesimalSolve ift)
    {
        if(exists(ift.getSaveTime()))
        {
            try
            {
                PreparedStatement del = connection.prepareStatement(delete);
                del.setTimestamp(1, ift.getSaveTime());
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
    /**
     * @Description 获取当前用户的全部历史记录
     * @return java.util.ArrayList<infinitesimal.InfinitesimalSolve>
     * @author sxq
     * @date 2023/12/9 11:14
     **/
    public static ArrayList<InfinitesimalSolve> getAllHis(){
        ArrayList<InfinitesimalSolve> res=new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(selectAll);
            statement.setString(1, LoginController.userName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                List<String> fP=Main.deserializeStringList(resultSet.getString("formulaProcess"));
                List<String> eP=Main.deserializeStringList(resultSet.getString("expProcess"));
                InfinitesimalSolve ift = new InfinitesimalSolve(resultSet.getDouble("upValue"),resultSet.getDouble("downValue"),fP.get(fP.size()-1),eP.get(eP.size()-1),fP,eP);
                ift.setResult(resultSet.getDouble("result"));
                ift.setSaveTime(resultSet.getTimestamp("time"));
                // 加入list
                res.add(ift);
            }
            return res;
        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}

package Database;

import Scientific.ErrorScientific;
import Scientific.ScientificSolve;
import com.calculator.calculation.LoginController;
import com.calculator.calculation.Main;
import java.sql.*;
import java.util.*;
import static Database.SqlUser.connection;

/**
 * @author Bu Xinran
 * @Description 用于管理科学计算器的数据库
 * @date 2023/12/8 9:59
 */
public class SqlScientific {
    private final static String insert = "insert into scihistory(formula,exp,formulaProcess,expProcess,result,username,time) values(?,?,?,?,?,?,?)";
    private final static String delete = "delete from scihistory where time = ? and userName = ?";
    private final static String select = "select * from scihistory where time = ? and userName = ?";
    private final static String selectAll = "select * from scihistory where userName = ? order by time";
    /***
     * @Description 向数据库中上传对象
     * @param sci 要增加的对象
     * @return boolean
     * @author Bu Xinran
     * @date 2023/12/9 17:06
    **/
    public static boolean add(ScientificSolve sci) {
        try {
            PreparedStatement add = connection.prepareStatement(insert);
            add.setString(1,sci.getFormula());
            add.setString(2, sci.getExp());
            add.setString(3, Main.serializeStringList(sci.getProcess()));
            add.setString(4,Main.serializeStringList(sci.getProcessExp()));
            add.setString(5,sci.getResult());
            add.setString(6, LoginController.userName);
            add.setTimestamp(7,new Timestamp(System.currentTimeMillis()));
            return add.executeUpdate() == 1;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /***
     * @Description  判断该对象是否存在
     * @param time 时间戳
     * @return boolean
     * @author Bu Xinran
     * @date 2023/12/9 17:05
    **/
    public static boolean exists(Timestamp time)
    {
        try {
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
    /***
     * @Description  从数据库中获取所有历史记录
     * @return java.util.ArrayList<Scientific.ScientificSolve>
     * @author Bu Xinran
     * @date 2023/12/9 17:05
    **/
    public static ArrayList<ScientificSolve> getAllHis(){
        ArrayList<ScientificSolve> res=new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(selectAll);
            statement.setString(1, LoginController.userName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                List<String> fP= Main.deserializeStringList(resultSet.getString("formulaProcess"));
                List<String> eP=Main.deserializeStringList(resultSet.getString("expProcess"));
                ScientificSolve ift = new ScientificSolve(resultSet.getString("formula"),resultSet.getString("result"), ErrorScientific.yes,fP,eP,resultSet.getString("exp"));
                ift.setSaveTime(resultSet.getTimestamp("time"));
                res.add(ift);
            }
            return res;
        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
    /***
     * @Description 删除科学计算器中制定对象
     * @param ift   要删除的对象
     * @author Bu Xinran
     * @date 2023/12/9 17:04
    **/
    public static void delete(ScientificSolve ift) {
        if(exists(ift.getSaveTime())) {
            try {
                PreparedStatement del = connection.prepareStatement(delete);
                del.setTimestamp(1, ift.getSaveTime());
                del.setString(2,LoginController.userName);
                del.executeUpdate();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

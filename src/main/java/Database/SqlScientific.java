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
    private final static String selectAll = "select * from scihistory where username = ? order by time";
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

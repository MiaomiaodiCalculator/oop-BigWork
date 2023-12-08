package Database;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author Bu Xinran
 * @Description 用于管理科学计算器的数据库
 * @date 2023/12/8 9:59
 */
public class SqlScientific {
    private final static String driver = "com.mysql.cj.jdbc.Driver";//固定？
    private final static String username = "root";
    private final static String password = "root";
    private final static String url = "jdbc:mysql://10.192.229.109/miaomiaodi";//最后一列为表名
    private final static String insert = "insert into userscientific(formula,result,id) values(?,?,?)";
    private final static String update = "update userscientific set formula = ?,result = ? where id=?";
    private final static String delete = "delete from userscientific where name = ? and username = ?";
    private final static String select = "select * from userscientific where name = ? and username = ?";
    private final static String selectAll = "select * from userscientific where username = ?";
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
}

package dao;
import com.mysql.cj.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    //静态语句块默认执行 但是只会执行一次
    //将加载驱动放在静态语句块中便只会执行一次（连接数据库的驱动只需要加载一次 便可以创建多个连接）
    static {
        try {
            DriverManager.registerDriver(new Driver());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public static Connection getConnection(){
        try {

            String url="jdbc:mysql://localhost:3306/test?serverTimezone=Asia/Shanghai";
            String username="root";
            String passworld="abc123456";
            Connection con = DriverManager.getConnection(url,username,passworld);
            //System.out.println("数据库连接成功");
            return con;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

package kylin;

import java.sql.*;

/**
 * @Auther: wxf
 * @Date: 2022/12/29 19:40:18
 * @Description: TestJDBC
 * @Version 1.0.0
 */
public class TestJDBC {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        // 注册驱动
        Class.forName("org.apache.kylin.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:kylin://hadoop103:7070/gmall", "admin", "KYLIN");

        // 准备sql
        String sql = "select * from xxxx";
        PreparedStatement ps = connection.prepareStatement(sql);
        // 执行查询
        ResultSet rs = ps.executeQuery();

        // 遍历结果
        System.out.println("========================== 结果 ===================");

        while (rs.next()) {
            System.out.println(rs.getString("列名"));
        }

        // 关闭资源
        rs.close();
        ps.close();
        connection.close();

    }
}
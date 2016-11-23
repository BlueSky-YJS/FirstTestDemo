package com.example.myfunctiontest.MySqlUtils;

/**
 * author: Administrator
 * created on: 2016/11/23 17:36
 * description:
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 数据库连接类
 * @author ITDragon
 *
 */
public class ConnectionUtil {

    /**
     * 第一步：加载驱动
     * 第二步：链接数据库
     * 第三步：一定要关闭流
     * 第四步：测试是否连接成功
     */

    private static String DRIVER = "com.mysql.jdbc.Driver"; 		// 数据库驱动
    private static String URL = "jdbc:mysql://localhost:3306/dianping";	// 访问数据库路径
    private static String NAME = "root";							// 数据库用户名
    private static String PASSWORD = "root";						// 数据库密码

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // 加载驱动
            Class.forName(DRIVER);
            // 连接数据库
            connection = DriverManager.getConnection(URL, NAME, PASSWORD);
            return connection;
        } catch (Exception e) {
            return null;
        }
    }

    // 关闭流
    public static void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeStatement(Statement statement) {
        try {
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closePreparedStatement(PreparedStatement pStatement) {
        try {
            pStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeResultSet(ResultSet rs) {
        try {
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 测试数据库是否链接成功
    public static void main(String[] args) {
        System.out.println(getConnection());
        System.out.println("测试成功");

    }

}

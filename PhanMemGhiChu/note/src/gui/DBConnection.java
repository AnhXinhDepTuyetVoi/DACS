package gui;

import java.sql.*;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/simplenote_db";
        String user = "root"; // XAMPP
        String password = ""; // mặc định rỗng
        return DriverManager.getConnection(url, user, password);
    }
}

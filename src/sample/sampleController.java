package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.sql.*;

public class sampleController {
    @FXML
    public Label l1;

    @FXML
    public void Bclick(MouseEvent event) {

        Connection con;
        //jdbc驱动
        String driver = "com.mysql.cj.jdbc.Driver";

        String url = "jdbc:mysql://42.192.73.17:3306/fileManage?&useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "lapis";
        try {
            //注册JDBC驱动程序
            Class.forName(driver);
            //建立连接
            con = DriverManager.getConnection(url, user, password);
            if (!con.isClosed()) {
                System.out.println("数据库连接成功");
            }
            Statement stmt = con.createStatement();
            String sql = "select * from book";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.println(rs.getString("bookname"));
                l1.setText(rs.getString("bookname"));
            }
            con.close();
        } catch (ClassNotFoundException e) {
            System.out.println("数据库驱动没有安装");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("数据库连接失败");
        }

//        String a = "poi";
//        String b = a + "p0";
//        b = b + a;
//        l1.setText(b + a);
    }
}
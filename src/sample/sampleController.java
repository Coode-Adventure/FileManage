package sample;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.sql.*;

public class sampleController extends Node {
    @FXML
    public Button B1;
    public TextArea T1;
    public Button CreateButton;
    public Button SelectButton;

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

                Statement stmt = con.createStatement();
                String sql = "select * from book";
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    System.out.println(rs.getString("bookname"));
                    T1.setText(rs.getString("bookname"));
                }
            }

            con.close();
        } catch (ClassNotFoundException e) {
            System.out.println("数据库驱动没有安装");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("数据库连接失败");
        }

    }

    public void CreateFile(MouseEvent event) throws IOException {
        File f1 = new File("src/sample/file/tes.txt");
        if (f1.exists()) {
            System.out.println("文件已存在");
            try (BufferedReader bis = new BufferedReader(new FileReader("src/sample/file/test.txt"))){

                StringBuilder data= new StringBuilder();
                while (bis.ready()) {
                    data.append(bis.readLine()+"\n");


                }
                T1.setText(String.valueOf(data));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                f1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("创建文件成功");
        }


    }

    public void SelectFile(MouseEvent event) {
    }
}
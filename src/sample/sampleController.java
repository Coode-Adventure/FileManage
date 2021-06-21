package sample;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.*;
import java.sql.*;

public class sampleController extends Node {
    @FXML
    public Button B1;
    public TextArea T1;
    public Button CreateButton;
    public Button SelectButton;
    public TextArea T2;
    public Pane P1;

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
                String sql = "select * from file";
                ResultSet rs = stmt.executeQuery(sql);
                StringBuilder a = new StringBuilder();
                while (rs.next()) {
                    System.out.println(rs.getString("text"));
                    a.append("\n").append(rs.getString("text"));
                }
                T2.setText(String.valueOf(a));
            }

            con.close();
        } catch (ClassNotFoundException e) {
            System.out.println("数据库驱动没有安装");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("数据库连接失败");
        }

    }

    // 读取文件功能
    public void CreateFile(MouseEvent event) throws IOException {
        File f1 = new File("src/sample/file/test.txt");
        if (f1.exists()) {
            System.out.println("文件已存在");
            try (BufferedReader bis = new BufferedReader(new FileReader("src/sample/file/test.txt"))) {

                StringBuilder data = new StringBuilder();
                while (bis.ready()) {
                    data.append(bis.readLine() + "\n");


                }
                T2.setText(String.valueOf(data));

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

    //添加按钮和事件
    public void SelectFile(MouseEvent event) {
        Button B2 = new Button();
        B2.setText("B2");
        B2.setLayoutX(150.0);

        //添加按钮点击事件
        B2.setOnMouseClicked(
                new EventHandler(){

                    @Override
                    public void handle(Event event) {
                        B2.setText("CLICK");
                    }
                }
        );
        P1.getChildren().add(B2);
    }

}
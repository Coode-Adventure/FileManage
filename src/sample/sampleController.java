package sample;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.awt.Dialog;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class sampleController extends Node {
    @FXML
    public Button B1;
    public TextArea T1;
    public Button CreateButton;
    public Button SelectButton;
    public TextArea Text1;
    public SplitPane mainView;

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
        File f1 = new File("src/sample/file/test.txt");
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


    public void fenYeClick(MouseEvent event) {
        TabPane pane1 = new TabPane();
        Tab tab1 = new Tab();
        tab1.setText("测试");
        TextArea area1 = new TextArea();
        tab1.setContent(area1);
        pane1.getTabs().add(tab1);
        mainView.getItems().add(pane1);
    }

    //打开文件
    public void openFile(Event event) {
        StringBuilder data = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tim  = sdf.format(new Date());
        int count = 0;
        FileDialog fd=new FileDialog((Dialog) null,"另存为",1);
        fd.setVisible(true);
        System.out.println(fd.getDirectory()+fd.getFile()+".txt");
        String name = fd.getFile();
        File file=new File(fd.getDirectory()+fd.getFile()+".txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileWriter writer=new FileWriter(file);
            String str=Text1.getText();
            System.out.println(str);
            writer.write(str);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }  File f1 = new File(fd.getDirectory()+fd.getFile()+".txt");
        if (f1.exists()) {
            try (BufferedReader bis = new BufferedReader(new FileReader(fd.getDirectory()+fd.getFile()+".txt"))) {

                while (bis.ready()) {
                    data.append(bis.readLine() + "\n");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //jdbc驱动
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://42.192.73.17:3306/fileManage?&useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "lapis";
        String a = data.toString();
        try {
            //注册JDBC驱动程序
            Class.forName(driver);
            //建立连接
            Connection con = DriverManager.getConnection(url, user, password);
            if (!con.isClosed()) {
                System.out.println("数据库连接成功");
                Statement stmt = con.createStatement();
                String sql = "select title from file";
                ResultSet rs = stmt.executeQuery(sql);

                while(rs.next()){
                  String  bi = rs.getString("title");
                  System.out.println(bi);
                  if (name.equals(bi)){
                      count ++;
                      System.out.println("文件名已经存在！");
                    }
                }
                if (count==0){
                    stmt = con.prepareStatement("insert into file (title,text,time) values(?,?,?)");
                ((PreparedStatement) stmt).setString(1,name);
                ((PreparedStatement) stmt).setString(2,a);
                ((PreparedStatement) stmt).setString(3,tim);
                ((PreparedStatement) stmt).executeUpdate();
            }
            else{
                    stmt = con.prepareStatement("update file  set text = ?,time = ? where title = ?");
                    ((PreparedStatement) stmt).setString(1,a);
                    ((PreparedStatement) stmt).setString(2,tim);
                    ((PreparedStatement) stmt).setString(3,name);
                    ((PreparedStatement) stmt).executeUpdate();
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


//        try {
//            FileOutputStream out =new FileOutputStream(fd.getDirectory()+ fd.getFile() +".txt");
//            String str="TEST";
//            try {
//                out.write(str.getBytes());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                out.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }




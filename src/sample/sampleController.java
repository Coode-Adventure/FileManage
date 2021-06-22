package sample;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.awt.Dialog;
import java.awt.*;
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
    public TabPane mainTab;
    public Button fenYe;
    public SplitPane mainView;
    public MenuItem openFile;
    public Tab Tab1;
    public TextArea Text1;
    //添加按钮和事件
    int id = 0;
    TabPane tabMain = null;

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

    //清空空元素TabPane
    public void deleteTabPane() {
        System.out.println(mainView.getItems()+"---");
        TabPane de=null;
        for (Node s : mainView.getItems()) {
            System.out.println(((TabPane) s).getTabs()+"++++");
            if((((TabPane) s).getTabs()).size()==0){
                System.out.println("有一个为空");
                de= (TabPane) s;
            }
        }
        //TabPane至少保留一个
        if(mainView.getItems().size()!=1){
            mainView.getItems().remove(de);
        }
        System.out.println(mainView.getItems()+"----");
    }

    public void SelectFile(MouseEvent event) {


        if (tabMain == null) {
            ObservableList<Node> l = mainView.getItems();
            tabMain = (TabPane) l.get(0);
        }

        Tab tab1 = new Tab();
        tab1.setText("测试");
        tab1.setId(String.valueOf(id++));
//        System.out.println(tab1.getId());
        TextArea area1 = new TextArea();
        tab1.setContent(area1);
        //获取焦点
        tab1.setOnSelectionChanged(event1 -> {
            tab1.setText("ooo");
            TextArea a = (TextArea) tab1.getContent();
            a.setText(tab1.getId());

        });
        tab1.setOnClosed(
                event1 -> {
                    TextArea a = (TextArea) tab1.getContent();
                    a.setText(tab1.getId());
//                    System.out.println(a.getText());
                    deleteTabPane();
                }
        );
        tabMain.getTabs().add(tab1);
//        System.out.println(tabMain.getTabs());


    }

    //添加TabPane
    public void fenYeClick(MouseEvent event) {
        TabPane pane1 = new TabPane();
        pane1.setOnMouseClicked(
                (EventHandler<Event>) event1 -> {
                    tabMain = pane1;
                });
        Tab tab1 = new Tab();
        tab1.setText("测试");
        tab1.setOnSelectionChanged(event1 -> {
            tab1.setText("ooo");
            TextArea a = (TextArea) tab1.getContent();
            a.setText(tab1.getId());

        });
        tab1.setOnClosed(
                event1 -> {
//                    System.out.println(a.getText());
                    deleteTabPane();
                }
        );
        TextArea area1 = new TextArea();
        tab1.setContent(area1);
        pane1.getTabs().add(tab1);
        mainView.getItems().add(pane1);
//        System.out.println(pane1);


    }

    //打开文件
    public void openFile(Event event) {
        FileDialog fd = new FileDialog((Dialog) null, "另存为", 1);
        fd.setVisible(true);
        System.out.println(fd.getDirectory() + fd.getFile() + ".txt");

        File file = new File(fd.getDirectory() + fd.getFile() + ".txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileWriter writer = new FileWriter(file);
            String str = Text1.getText();
            System.out.println(str);
            writer.write(str);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
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


    public void focusChange(MouseEvent event) {
        ObservableList<Node> l = mainView.getItems();
        tabMain = (TabPane) l.get(0);
    }
}
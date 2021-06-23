package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.awt.Dialog;
import java.awt.*;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class sampleController extends Node {
    @FXML
    public Button CreateButton;
    public Button SelectButton;
    public TextArea T2;
    public Pane P1;
    public SplitPane mainView;
    public MenuItem newFile, textPage, saveFile;
    public Menu openData;
    //添加按钮和事件
    int id = 0;
    TabPane tabMain = null;
    //获取当前text页面
    TextArea nowText = null;
    Tab nowTab = null;



    //清空空元素TabPane
    public void deleteTabPane() {
        System.out.println(mainView.getItems() + "---");
        TabPane de = null;
        for (Node s : mainView.getItems()) {
            System.out.println(((TabPane) s).getTabs() + "++++");
            if ((((TabPane) s).getTabs()).size() == 0) {
                System.out.println("有一个为空");
                de = (TabPane) s;
            }
        }
        //TabPane至少保留一个
        if (mainView.getItems().size() != 1) {
            mainView.getItems().remove(de);
        }
        System.out.println(mainView.getItems() + "----");
    }


    //新建空文本
    public void newFile(ActionEvent actionEvent) {
        addText(null, null);
    }



    public void addText(StringBuilder text, String name) {
        if (tabMain == null) {
            ObservableList<Node> l = mainView.getItems();
            if (l.size() == 0) {
                TabPane pane1 = new TabPane();
                pane1.setOnMouseClicked(
                        event1 -> {
                            tabMain = pane1;
                            System.out.println("clicked");
                        });
                tabMain = pane1;
                mainView.getItems().add(tabMain);
            } else {
                tabMain = (TabPane) l.get(0);
            }
        }

        Tab tab1 = new Tab();
        tab1.setId(String.valueOf(id++));
        TextArea area1 = new TextArea();
        if (text != null) {
            area1.setText(String.valueOf(text));
            tab1.setText(name + ".txt");
        } else {
            tab1.setText("unnamed.txt");
        }
        tab1.setContent(area1);
        //获取焦点
        tab1.setOnSelectionChanged(event1 -> {
            if (nowTab != tab1) {
                nowTab = tab1;
                nowText = (TextArea) tab1.getContent();
                System.out.println("焦点改变+" + tab1.getId());
            }
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

    //分页
    public void pagination(ActionEvent actionEvent) {
        TabPane pane1 = new TabPane();
        pane1.setOnMouseClicked(
                event1 -> {
                    tabMain = pane1;
                    System.out.println("clicked");
                });
        Tab tab1 = new Tab();
        tab1.setText("unnamed.txt");
        tab1.setOnSelectionChanged(event1 -> {
            TextArea a = (TextArea) tab1.getContent();
            a.setText(tab1.getId());

        });
        tab1.setOnClosed(
                event1 -> {
                    deleteTabPane();
                }
        );
        TextArea area1 = new TextArea();
        tab1.setContent(area1);
        pane1.getTabs().add(tab1);
        mainView.getItems().add(pane1);
    }


    //打开文件
    public void saveFile(Event event) {


        StringBuilder data = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tim = sdf.format(new Date());
        int count = 0;
        FileDialog fd = new FileDialog((Dialog) null, "另存为", 1);

        fd.setVisible(true);
        System.out.println(fd.getDirectory() + fd.getFile() + ".txt");
        String name = fd.getFile();
        File file = new File(fd.getDirectory() + fd.getFile() + ".txt");
        if (fd.getFile() == null) return;

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileWriter writer = new FileWriter(file);
            String str = nowText.getText();
            System.out.println(str);
            writer.write(str);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        nowTab.setText(fd.getFile() + ".txt");
        File f1 = new File(fd.getDirectory() + fd.getFile() + ".txt");

        if (f1.exists()) {
            try (BufferedReader bis = new BufferedReader(new FileReader(fd.getDirectory() + fd.getFile() + ".txt"))) {

                while (bis.ready()) {
                    data.append(bis.readLine()).append("\n");
                }
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

                while (rs.next()) {
                    String bi = rs.getString("title");
                    System.out.println(bi);
                    if (name.equals(bi)) {
                        count++;
                        System.out.println("文件名已经存在！");
                    }
                }
                if (count == 0) {
                    stmt = con.prepareStatement("insert into file (title,text,time) values(?,?,?)");
                    ((PreparedStatement) stmt).setString(1, name);
                    ((PreparedStatement) stmt).setString(2, a);
                    ((PreparedStatement) stmt).setString(3, tim);
                    ((PreparedStatement) stmt).executeUpdate();
                } else {
                    stmt = con.prepareStatement("update file  set text = ?,time = ? where title = ?");
                    ((PreparedStatement) stmt).setString(1, a);
                    ((PreparedStatement) stmt).setString(2, tim);
                    ((PreparedStatement) stmt).setString(3, name);
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


    public ArrayList<String> getList() {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://42.192.73.17:3306/fileManage?&useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "lapis";
        StringBuilder in = new StringBuilder();
        ArrayList<String> array = new ArrayList<>();
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

                for (int i = 0; rs.next(); i++) {
                    array.add(i, rs.getString("title"));
                }
//                while(rs.next()){
//                    in.append(rs.getString("title"));
//
//                }
//                String in1 = in.toString();
//                System.out.println(in1);
                System.out.println(array);
            }


            con.close();
        } catch (ClassNotFoundException e) {
            System.out.println("数据库驱动没有安装");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("数据库连接失败");
        }
        return array;
    }

    public StringBuilder readText(String name) {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://42.192.73.17:3306/fileManage?&useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "lapis";
        StringBuilder in = new StringBuilder();
        try {
            //注册JDBC驱动程序
            Class.forName(driver);
            //建立连接
            Connection con = DriverManager.getConnection(url, user, password);
            if (!con.isClosed()) {
                System.out.println("数据库连接成功");
                Statement stmt = con.createStatement();

                String sql1 = "select text from file where title ='" + name + "' ";
//                ((PreparedStatement) stmt).setString(1,in1);
                ResultSet rs1 = stmt.executeQuery(sql1);
                while (rs1.next()) {
                    in.append(rs1.getString("text"));
                }
//                System.out.println(in);
            }


            con.close();
        } catch (ClassNotFoundException e) {
            System.out.println("数据库驱动没有安装");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("数据库连接失败");
        }
        return in;
    }

    //显示数据库标题
    public void showText(Event event) {
        ArrayList<String> list = getList();
        for (String text : list) {
            MenuItem item = new MenuItem();

            item.setText(text + ".txt");
            item.setOnAction(
                    event1 -> {
                        System.out.println(item.getText());
                        String[] title = (item.getText()).split("\\.");
                        System.out.println(title[0]);
                        System.out.println(readText(title[0]));
                        addText(readText(title[0]), title[0]);

                    }
            );
            openData.getItems().add(item);
        }
    }
    //删除数据库标题
    public void deleteText(Event event){
        ObservableList<MenuItem> s = openData.getItems();
        System.out.println(s);
        for(int i=s.size()-1;i>0;i--){
            System.out.println(s.get(i)+""+i);
            openData.getItems().remove(s.get(i));
        }
//        System.out.println(openData.getItems());
    }


}
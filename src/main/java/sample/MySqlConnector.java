package sample;

import java.sql.*;

/**
 * Created by OskarPraca on 2017-04-26.
 */
public class MySqlConnector {


    private static final String DB = "jdbc:mysql://5.135.218.27:3306/justyna?useUnicode=true&characterEncoding=UTF-8";
    private static final String USER = "Justyna";
    private static final String USERPW = "Qwertyqwerty1!";
    private static final String DRIVER = "com.mysql.jdbc.Driver";

    private static MySqlConnector ourInstance = new MySqlConnector();

    public static MySqlConnector getInstance() {
        return ourInstance;
    }

    private Connection connection;

    private MySqlConnector() {
        try {
            Class.forName(DRIVER).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(DB, USER, USERPW);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Connected");
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement getNewStatement() {
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }public void register() {

        try {
            String sql = "INSERT INTO user(name, lastname, password, number) VALUES(?, ?, ?, ?)";
            PreparedStatement statement = MySqlConnector.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, "oskarx");
            statement.setString(2, "Kowalski");
            statement.setString(3, "10135886");
            statement.setString(4, "722277722");

            statement.execute();
            statement.close();

            //////////

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
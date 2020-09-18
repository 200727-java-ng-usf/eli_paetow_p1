package com.revature.util;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory { /*
 * create a singleton
 * A singleton creational pattern
 * restricts the object creation for a class to only one
 * instance.
 * */

    private static ConnectionFactory connFactory = new ConnectionFactory();


    private Properties props = new Properties();

    /*
     *set up the route for the application properties
     * this will help keep unwanted details off github
     * */
    private ConnectionFactory() {
        try {
            props.load(new FileReader("C:\\Users\\ep\\Desktop\\revature_git\\eli_paetow_p1\\eli_paetow_p1\\src\\main\\resources\\application.properties"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    /*
     * set up getters tp work with connection factory since it is private
     * */

    public static ConnectionFactory getInstance(){

        return connFactory;
    }




    public Connection getConnection(){

        Connection conn = null;
        /*
         *set up the connection to my dbeaver db
         * */

        try {

            // Force the JVM to load the PostGreSQL JDBC driver
            Class.forName("org.postgresql.Driver");

            conn = DriverManager.getConnection(
                    props.getProperty("url"),
                    props.getProperty("username"),
                    props.getProperty("password")
            );

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        if (conn == null) {
            try {
                conn = DriverManager.getConnection(
                        System.getenv("url"),
                        System.getenv("username"),
                        System.getenv("password")
                );
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return conn;

    }

    /*
     *overrides
     * */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }


}

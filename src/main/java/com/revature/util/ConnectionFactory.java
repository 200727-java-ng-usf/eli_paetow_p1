package com.revature.util;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
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
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream propsInput = loader.getResourceAsStream("application.properties");

            if (propsInput == null) {
                props.setProperty("url", System.getProperty("url"));
                props.setProperty("username", System.getProperty("username"));
                props.setProperty("password", System.getProperty("password"));
            } else {
                props.load(propsInput);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ConnectionFactory getInstance() {
        return connFactory;
    }

    public Connection getConnection() {

        Connection conn = null;

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

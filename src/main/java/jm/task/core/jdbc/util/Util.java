package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    public static Connection connectToDatabase() {
        Properties connProps = new Properties();
        Connection connection;

        try (InputStream input = Files.newInputStream(Paths.get("connection.properties"))) {
            connProps.load(input);
            connection = DriverManager.getConnection(
                    connProps.getProperty("url"),
                    connProps.getProperty("username"),
                    connProps.getProperty("password")
            );
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Ошибка соединения с базой данных.\n" + e.getMessage());
        }

        return connection;
    }

    public static SessionFactory getSessionFactory() {
        Properties connProps = new Properties();

        try (FileReader propsFile = new FileReader("connection.properties")) {
            connProps.load(propsFile);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось прочитать свойства подключения.\n" + e.getMessage());
        }

        return new Configuration()
                .setProperty("hibernate.connection.driver_class", connProps.getProperty("driver", "com.mysql.cj.jdbc.Driver"))
                .setProperty("hibernate.connection.url", connProps.getProperty("url"))
                .setProperty("hibernate.connection.username", connProps.getProperty("username"))
                .setProperty("hibernate.connection.password", connProps.getProperty("password"))
                .setProperty("hibernate.dialect", connProps.getProperty("dialect", "org.hibernate.dialect.MySQLDialect"))
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
    }
}

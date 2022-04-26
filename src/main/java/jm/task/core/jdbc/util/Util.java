package jm.task.core.jdbc.util;

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
}

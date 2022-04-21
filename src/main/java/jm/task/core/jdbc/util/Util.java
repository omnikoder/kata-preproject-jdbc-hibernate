package jm.task.core.jdbc.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    public static Connection connectToDatabase(Path propsFile) throws IOException, SQLException {
        Properties connProps = new Properties();
        try (InputStream input = Files.newInputStream(propsFile)) {
            connProps.load(input);
        }
        return DriverManager.getConnection(
                connProps.getProperty("url"),
                connProps.getProperty("username"),
                connProps.getProperty("password")
        );
    }
}

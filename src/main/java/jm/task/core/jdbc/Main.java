package jm.task.core.jdbc;

import jm.task.core.jdbc.util.Util;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try (Connection databaseConnection = Util.connectToDatabase(Paths.get("connection.properties"))) {
            System.out.println(databaseConnection.getCatalog());
        } catch (SQLException | IOException ex) {
            System.err.println("Не удалось установить соединение с базой данных.");
            ex.printStackTrace();
        }
    }
}

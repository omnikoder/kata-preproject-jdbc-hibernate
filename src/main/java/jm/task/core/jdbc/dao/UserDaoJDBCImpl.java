package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Connection connection = Util.connectToDatabase();

    public UserDaoJDBCImpl() {}

    public void createUsersTable() {
        try (Statement statement = this.connection.createStatement()) {
            String sql = String.format("CREATE TABLE IF NOT EXISTS users (%s, %s, %s, %s)",
                    "id BIGINT PRIMARY KEY AUTO_INCREMENT",
                    "name VARCHAR(20) NOT NULL",
                    "lastname VARCHAR(20) NOT NULL",
                    "age TINYINT UNSIGNED NOT NULL");
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("Не удалось создать таблицу пользователей.");
            System.err.println(e.getMessage());
        }
    }

    public void dropUsersTable() {
        try (Statement statement = this.connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS users");
        } catch (SQLException e) {
            System.err.println("Не удалось удалить таблицу пользователей.");
            System.err.println(e.getMessage());
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement prepared = this.connection.prepareStatement("INSERT users (name, lastname, age) VALUES (?, ?, ?)")) {
            prepared.setString(1, name);
            prepared.setString(2, lastName);
            prepared.setByte(3, age);
            prepared.executeUpdate();
        } catch (SQLException e) {
            System.err.printf("Не удалось сохранить пользователя - [%s, %s, %d]\n", name, lastName, age);
            System.err.println(e.getMessage());
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement prepared = this.connection.prepareStatement("DELETE FROM users WHERE id = ?")) {
            prepared.setLong(1, id);
            prepared.executeUpdate();
        } catch (SQLException e) {
            System.err.printf("Не удалось удалить пользователя с id: %d\n", id);
            System.err.println(e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (Statement statement = this.connection.createStatement()) {
            ResultSet set = statement.executeQuery("SELECT * FROM users");
            while (set.next()) {
                users.add(new User(set.getString(2), set.getString(3), set.getByte(4)));
            }
        } catch (SQLException e) {
            System.err.println("Не удалось получить ползователей.");
            System.err.println(e.getMessage());
        }

        return users;
    }

    public void cleanUsersTable() {
        try (Statement statement = this.connection.createStatement()) {
            statement.executeUpdate("DELETE FROM users");
        } catch (SQLException e) {
            System.err.println("Не удалось очистить таблицу пользователей.");
            System.err.println(e.getMessage());
        }
    }
}

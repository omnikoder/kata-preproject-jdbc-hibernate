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
                    "name VARCHAR(20) NOT NULL CHECK (name LIKE '__%')",
                    "lastname VARCHAR(20) NOT NULL CHECK (lastname != '')",
                    "age TINYINT UNSIGNED NOT NULL");
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось создать таблицу пользователей.\n" + e.getMessage());
        }
    }

    public void dropUsersTable() {
        try (Statement statement = this.connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS users");
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось удалить таблицу пользователей.\n" + e.getMessage());
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement prepared = this.connection.prepareStatement("INSERT users (name, lastname, age) VALUES (?, ?, ?)")) {
            prepared.setString(1, name);
            prepared.setString(2, lastName);
            prepared.setByte(3, age);
            prepared.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Не удалось сохранить пользователя - [%s, %s, %d]\n%s",
                    name, lastName, age, e.getMessage()));
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement prepared = this.connection.prepareStatement("DELETE FROM users WHERE id = ?")) {
            prepared.setLong(1, id);
            prepared.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось удалить пользователя с id: " + id + '\n' + e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (Statement statement = this.connection.createStatement()) {
            ResultSet set = statement.executeQuery("SELECT * FROM users");
            while (set.next()) {
                User user = new User(set.getString(2), set.getString(3), set.getByte(4));
                user.setId(set.getLong(1));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить ползователей.\n" + e.getMessage());
        }

        return users;
    }

    public void cleanUsersTable() {
        try (Statement statement = this.connection.createStatement()) {
            statement.executeUpdate("TRUNCATE TABLE users");
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось очистить таблицу пользователей.\n" + e.getMessage());
        }
    }
}

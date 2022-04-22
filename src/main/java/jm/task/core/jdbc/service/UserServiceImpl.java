package jm.task.core.jdbc.service;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl() {
        this.userDao = new UserDaoJDBCImpl();
    }

    public void createUsersTable() {
        this.userDao.createUsersTable();
    }

    public void dropUsersTable() {
        this.userDao.dropUsersTable();
    }

    public void saveUser(String name, String lastName, byte age) {
        this.userDao.saveUser(name, lastName, age);
    }

    public void removeUserById(long id) {
        this.userDao.removeUserById(id);
    }

    public List<User> getAllUsers() {
        return this.userDao.getAllUsers();
    }

    public void cleanUsersTable() {
        this.userDao.cleanUsersTable();
    }
}

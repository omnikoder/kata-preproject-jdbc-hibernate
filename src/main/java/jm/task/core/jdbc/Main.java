package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        User[] users = new User[] {
                new User("Tom", "Cruise", (byte) 59),
                new User("Иван", "Иванов", (byte) 255),
                new User("アレクサンドル", "プシキン", (byte) 35),
                new User("", "", (byte) -1)
        };
        UserService userService = new UserServiceImpl();
        userService.createUsersTable();
        for (User user : users) {
            userService.saveUser(user.getName(), user.getLastName(), user.getAge());
            System.out.printf("User с именем – %s добавлен в базу данных\n", user.getName());
        }
    }
}

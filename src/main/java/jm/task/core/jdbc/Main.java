package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("Rick", "Sanchez", (byte) 70);
        userService.saveUser("Morty", "Smith", (byte) 14);
        userService.saveUser("Bart", "Simpson", (byte) 11);
        userService.saveUser("Randy", "Marsh", (byte) 45);
        userService.saveUser(new User("test", "test",(byte)  1, 1));
        System.out.println(userService.getAllUsers());
        userService.test();
        userService.cleanUsersTable();
        userService.dropUsersTable();
        Util.closeSessionFactory();
    }
}

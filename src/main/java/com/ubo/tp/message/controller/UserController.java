package main.java.com.ubo.tp.message.controller;

import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.core.session.Session;
import main.java.com.ubo.tp.message.datamodel.User;

import java.util.Set;

public class UserController {

    private final DataManager dataManager;
    private final Session session;

    public UserController(DataManager dataManager, Session session) {
        this.dataManager = dataManager;
        this.session = session;
    }

    public Set<User> getAllUsers() {
        return dataManager.getUsers();
    }

    public User findUser(String tag) {
        return dataManager.getUser(tag);
    }

    public void deleteAccount() {

        User connected = session.getConnectedUser();

        if (connected != null) {

            connected.setOnline(false);

            dataManager.deleteUser(connected);

            session.disconnect();
        }
    }

    public String updateName(String newName) {

        if (newName == null || newName.trim().isEmpty()) {
            return "Nom invalide.";
        }

        User connected = session.getConnectedUser();

        connected.setName(newName.trim());

        dataManager.modifyUser(connected.getUuid(), connected);

        return "SUCCESS";
    }

    public Session getSession() {
        return session;
    }

    public void logout() {

        User connected = session.getConnectedUser();

        if (connected != null) {

            connected.setOnline(false);

            dataManager.modifyUser(connected.getUuid(), connected);

            session.disconnect();
        }
    }
}

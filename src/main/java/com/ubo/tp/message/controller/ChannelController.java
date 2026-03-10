package main.java.com.ubo.tp.message.controller;

import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.core.session.Session;
import main.java.com.ubo.tp.message.datamodel.Channel;
import main.java.com.ubo.tp.message.datamodel.User;

import java.util.Set;

public class ChannelController {

    private final DataManager dataManager;
    private final Session session;

    public ChannelController(DataManager dataManager, Session session) {
        this.dataManager = dataManager;
        this.session = session;
    }

    public Set<Channel> getAllChannels() {
        return dataManager.getChannels();
    }

    public void createChannel(String channelName, boolean isPrivate) {

        if (channelName == null || channelName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du canal est vide");
        }
        User creator = session.getConnectedUser();
        Channel channel = new Channel(creator, channelName.trim());
        channel.setPrivate(isPrivate);
        if (isPrivate) {
            channel.addUser(creator); // créateur membre du canal
        }
        dataManager.sendChannel(channel);
    }

    public void deleteChannel(Channel channel) {
        if (!canDeleteChannel(channel)) {
            throw new SecurityException(
                    "Vous ne pouvez supprimer que les canaux dont vous êtes le créateur."
            );
        }
        dataManager.deleteChannel(channel);
    }

    public boolean canDeleteChannel(Channel channel) {

        User user = session.getConnectedUser();

        return channel.isPrivate()
                && channel.getCreator().equals(user);
    }

    public void addUserToChannel(Channel channel, User user) {

        if (!channel.isPrivate()) {
            throw new IllegalStateException(
                    "Impossible d'ajouter un utilisateur à un canal public."
            );
        }

        if (!channel.getCreator().equals(session.getConnectedUser())) {
            throw new SecurityException(
                    "Seul le créateur peut modifier le canal."
            );
        }

        channel.addUser(user);

        dataManager.modifyChannel(channel);
    }

    public void removeUserFromChannel(Channel channel, User user) {

        if (!channel.isPrivate()) {
            throw new IllegalStateException(
                    "Impossible de supprimer un utilisateur d'un canal public."
            );
        }
        if (!channel.getCreator().equals(session.getConnectedUser())) {
            throw new SecurityException(
                    "Seul le créateur peut modifier le canal."
            );
        }
        if (user.equals(channel.getCreator())) {
            throw new IllegalArgumentException(
                    "Le créateur ne peut pas être supprimé."
            );
        }
        channel.removeUser(user);
        dataManager.modifyChannel(channel);
    }

    public Set<User> getAllUsers() {
        return dataManager.getUsers();
    }

    public void leaveChannel(Channel channel) {

        User connected = session.getConnectedUser();

        if (channel.getCreator().equals(connected)) {
            throw new IllegalStateException(
                    "Le créateur ne peut pas quitter son canal."
            );
        }

        if (!channel.isPrivate()) {
            throw new IllegalStateException(
                    "Impossible de quitter un canal public."
            );
        }

        channel.removeUser(connected);

        dataManager.modifyChannel(channel);
    }
    public boolean canLeaveChannel(Channel channel) {

        User connected = session.getConnectedUser();

        return channel.isPrivate()
                && !channel.getCreator().equals(connected)
                && channel.containsUser(connected);
    }

    public boolean canSeeChannel(Channel channel) {

        User user = session.getConnectedUser();

        if (!channel.isPrivate()) {
            return true;
        }

        return channel.getCreator().equals(user)
                || channel.containsUser(user);
    }
}
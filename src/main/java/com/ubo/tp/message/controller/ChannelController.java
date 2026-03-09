package main.java.com.ubo.tp.message.controller;

import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.core.session.Session;
import main.java.com.ubo.tp.message.datamodel.Channel;
import main.java.com.ubo.tp.message.datamodel.User;

import javax.swing.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

    public void createChannel(User creator, String channelName) {
        if (channelName == null || channelName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Le nom du canal ne peut pas être vide.",
                    "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Canal public — pas de liste d'utilisateurs
        Channel channel = new Channel(creator, channelName.trim());
        dataManager.sendChannel(channel);
    }

    public void createChannel(String channelName) {

        if (channelName == null || channelName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Le nom du canal ne peut pas être vide.",
                    "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User creator = session.getConnectedUser();

        Channel channel = new Channel(creator, channelName.trim());
        dataManager.sendChannel(channel);
    }

    public void deleteChannel(Channel channel) {

        User connectedUser = session.getConnectedUser();

        if (!channel.getCreator().equals(connectedUser)) {
            JOptionPane.showMessageDialog(null,
                    "Vous ne pouvez supprimer que les canaux dont vous êtes le créateur.",
                    "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(null,
                "Voulez-vous vraiment supprimer le canal \"" + channel.getName() + "\" ?",
                "Supprimer le canal",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        /*if (choice == JOptionPane.YES_OPTION) {
            dataManager.deleteChannel(channel);
        }*/
    }

    /*public void removeUser(Channel channel, User user) {

        if(channel.getCreator().equals(session.getConnectedUser())) {
            dataManager.removeUserFromChannel(channel, user);
        }
    }
         */
}
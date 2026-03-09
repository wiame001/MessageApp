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

    public void createChannel(String channelName) {

        if (channelName == null || channelName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du canal est vide");
        }

        User creator = session.getConnectedUser();

        Channel channel = new Channel(creator, channelName.trim());

        dataManager.sendChannel(channel);
    }

    public boolean canDeleteChannel(Channel channel) {

        User user = session.getConnectedUser();

        return channel.getCreator().equals(user);
    }

}
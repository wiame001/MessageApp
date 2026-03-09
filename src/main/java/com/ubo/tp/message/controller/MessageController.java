package main.java.com.ubo.tp.message.controller;

import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.core.session.Session;
import main.java.com.ubo.tp.message.datamodel.Message;
import java.util.UUID;

public class MessageController {
    private final DataManager dataManager;
    private final Session session;

    public MessageController(DataManager dataManager, Session session) {
        this.dataManager = dataManager;
        this.session = session;
    }

    /**
     * Logique d'envoi de message : crée l'objet et demande au model (DataManager) d'écrire le fichier.
     */
    public void sendMessage(String text, UUID recipientUuid) {
        if (text != null && !text.trim().isEmpty() && text.length() <= 200) {
            Message newMessage = new Message(session.getConnectedUser(), recipientUuid, text);
            dataManager.sendMessage(newMessage);
        }
    }
}

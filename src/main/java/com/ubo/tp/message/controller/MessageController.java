package main.java.com.ubo.tp.message.controller;

import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.core.session.Session;
import main.java.com.ubo.tp.message.datamodel.Message;
import main.java.com.ubo.tp.message.datamodel.User;

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

        if (session.getConnectedUser() == null) {
            return;
        }
        if (text == null || text.trim().isEmpty()) {
            return;
        }
        if (text.length() > 200) {
            throw new IllegalArgumentException(
                    "Un message ne peut pas dépasser 200 caractères."
            );
        }
        Message newMessage =
                new Message(session.getConnectedUser(), recipientUuid, text.trim());
        dataManager.sendMessage(newMessage);
        detectMentions(text);
    }

    public void deleteMessage(Message message) {

        if (!message.getSender().equals(session.getConnectedUser())) {

            throw new SecurityException(
                    "Vous ne pouvez supprimer que vos messages."
            );
        }

        dataManager.deleteMessage(message);
    }
    private void detectMentions(String text) {

        String[] words = text.split(" ");

        for (String word : words) {

            if (word.startsWith("@")) {

                String tag = word.substring(1);

                for (User user : dataManager.getUsers()) {

                    if (user.getUserTag().equals(tag)) {

                        System.out.println(
                                "Mention détectée pour @" + tag
                        );

                    }
                }
            }
        }
    }
}

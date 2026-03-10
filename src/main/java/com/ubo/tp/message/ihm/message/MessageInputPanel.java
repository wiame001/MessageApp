package com.ubo.tp.message.ihm.message;

import main.java.com.ubo.tp.message.controller.MessageController;
import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class MessageInputPanel extends JPanel {
    private JTextArea messageArea;
    private JButton sendButton;
    private MessageController controller;
    private UUID currentRecipient;

    public MessageInputPanel(MessageController controller) {
        this.controller = controller;
        this.currentRecipient = null;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createTitledBorder("Nouveau message"));

        messageArea = new JTextArea(3, 20);
        messageArea.setLineWrap(true);
        // Limite visuelle (on pourrait ajouter un DocumentFilter pour bloquer à 200)

        sendButton = new JButton("Envoyer");
        sendButton.addActionListener(e -> {
            if (currentRecipient == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Veuillez sélectionner un canal.",
                        "Erreur",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            controller.sendMessage(messageArea.getText(), currentRecipient);
            messageArea.setText("");
        });

        add(new JScrollPane(messageArea), BorderLayout.CENTER);
        add(sendButton, BorderLayout.EAST);
    }

    public void setRecipient(UUID recipient) {
        this.currentRecipient = recipient;
    }
}
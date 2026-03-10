package main.java.com.ubo.tp.message.ihm.canal;

import main.java.com.ubo.tp.message.controller.ChannelController;
import main.java.com.ubo.tp.message.datamodel.Channel;
import main.java.com.ubo.tp.message.datamodel.User;

import javax.swing.*;
import java.awt.*;

public class ChannelEditDialog extends JDialog {

    private final Channel channel;
    private final ChannelController controller;

    private DefaultListModel<User> membersModel;
    private JList<User> membersList;

    private JComboBox<User> userCombo;

    public ChannelEditDialog(Channel channel, ChannelController controller) {

        this.channel = channel;
        this.controller = controller;

        setTitle("Modifier canal : " + channel.getName());
        setSize(400,300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {

        // Liste membres
        membersModel = new DefaultListModel<>();
        for (User u : channel.getUsers()) {
            membersModel.addElement(u);
        }

        membersList = new JList<>(membersModel);

        add(new JScrollPane(membersList), BorderLayout.CENTER);

        // bas
        JPanel bottom = new JPanel(new BorderLayout());

        userCombo = new JComboBox<>();

        for (User user : controller.getAllUsers()) {
            userCombo.addItem(user);
        }

        bottom.add(userCombo, BorderLayout.CENTER);

        JButton addButton = new JButton("Ajouter");

        addButton.addActionListener(e -> {

            User user = (User) userCombo.getSelectedItem();

            try {

                controller.addUserToChannel(channel, user);

                membersModel.addElement(user);

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE
                );

            }

        });

        bottom.add(addButton, BorderLayout.EAST);

        add(bottom, BorderLayout.SOUTH);

        // bouton supprimer
        JButton removeButton = new JButton("Supprimer");

        removeButton.addActionListener(e -> {

            User selected = membersList.getSelectedValue();

            if (selected != null) {

                try {

                    controller.removeUserFromChannel(channel, selected);

                    membersModel.removeElement(selected);

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(
                            this,
                            ex.getMessage(),
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE
                    );

                }

            }

        });

        add(removeButton, BorderLayout.NORTH);
    }
}
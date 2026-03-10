package main.java.com.ubo.tp.message.ihm.message;

import main.java.com.ubo.tp.message.controller.MessageController;
import main.java.com.ubo.tp.message.datamodel.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;

public class MessagePanel extends JPanel {

    private final Message message;
    private final MessageController controller;

    public MessagePanel(Message message, MessageController controller) {
        this.message = message;
        this.controller = controller;

        initComponents();
    }

    private void initComponents() {

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        // Auteur
        JLabel authorLabel = new JLabel(
                "<html><b>@" + message.getSender().getUserTag() + "</b></html>"
        );

        gbc.gridx = 0;
        gbc.gridy = 0;

        add(authorLabel, gbc);

        // Date
        String dateStr =
                new SimpleDateFormat("HH:mm")
                        .format(message.getEmissionDate());

        JLabel dateLabel = new JLabel(dateStr);

        dateLabel.setForeground(Color.GRAY);
        dateLabel.setFont(new Font("Arial", Font.ITALIC, 10));

        gbc.gridx = 1;

        add(dateLabel, gbc);

// Message
        String text = message.getText();

// coloration des mentions @user
        text = text.replaceAll(
                "@(\\w+)",
                "<font color='blue'>@$1</font>"
        );

        JLabel contentLabel = new JLabel(
                "<html><body style='width:250px'>"
                        + text
                        + "</body></html>"
        );

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        add(contentLabel, gbc);

        // Menu clic droit
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {

                if (SwingUtilities.isRightMouseButton(e)) {

                    JPopupMenu menu = new JPopupMenu();

                    JMenuItem deleteItem =
                            new JMenuItem("Supprimer");

                    deleteItem.addActionListener(ev -> {

                        try {

                            controller.deleteMessage(message);

                        }
                        catch (Exception ex) {

                            JOptionPane.showMessageDialog(
                                    MessagePanel.this,
                                    ex.getMessage(),
                                    "Erreur",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }

                    });

                    menu.add(deleteItem);

                    menu.show(
                            MessagePanel.this,
                            e.getX(),
                            e.getY()
                    );
                }
            }
        });
    }
}
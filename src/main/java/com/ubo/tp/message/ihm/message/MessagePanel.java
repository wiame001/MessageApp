package main.java.com.ubo.tp.message.ihm.message;

import main.java.com.ubo.tp.message.datamodel.Message;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class MessagePanel extends JPanel {
    private final Message message;

    public MessagePanel(Message message) {
        this.message = message;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        // Style du cadre pour distinguer les messages [cite: 58]
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // 1. Auteur (en gras)
        JLabel authorLabel = new JLabel("<html><b>@" + message.getSender().getUserTag() + "</b></html>");
        gbc.gridx = 0; gbc.gridy = 0;
        add(authorLabel, gbc);

        // 2. Date
        String dateStr = new SimpleDateFormat("HH:mm").format(message.getEmissionDate());
        JLabel dateLabel = new JLabel(dateStr);
        dateLabel.setForeground(Color.GRAY);
        dateLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        gbc.gridx = 1;
        add(dateLabel, gbc);

        // 3. Corps du message
        JLabel contentLabel = new JLabel("<html><body style='width: 250px'>" + message.getText() + "</body></html>");
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(contentLabel, gbc);
    }
}
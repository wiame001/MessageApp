package main.java.com.ubo.tp.message.ihm.user;

import main.java.com.ubo.tp.message.datamodel.User;
import javax.swing.*;
import java.awt.*;

public class UserPanel extends JPanel {
    private final User user;

    public UserPanel(User user, UserSelectionListener listener) {
        this.user = user;

        addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {

                if (listener != null) {
                    listener.onUserSelected(user);
                }
            }
        });
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Avatar
        JLabel lblAvatar = new JLabel("👤");
        lblAvatar.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridheight = 2;
        add(lblAvatar, gbc);

        // Nom
        JLabel lblName = new JLabel(user.getName());
        lblName.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridheight = 1; gbc.weightx = 1.0;
        add(lblName, gbc);

        // Tag
        JLabel lblTag = new JLabel("@" + user.getUserTag());
        lblTag.setForeground(Color.GRAY);
        gbc.gridx = 1; gbc.gridy = 1;
        add(lblTag, gbc);

        // Indicateur en ligne (CHN-010)
        JLabel lblOnline;

        if (user.isOnline()) {
            lblOnline = new JLabel("🟢");
            lblOnline.setToolTipText("En ligne");
        } else {
            lblOnline = new JLabel("⚫");
            lblOnline.setToolTipText("Hors ligne");
        }
        gbc.gridx = 2; gbc.gridy = 0; gbc.gridheight = 2;
        gbc.weightx = 0;
        add(lblOnline, gbc);
    }

    /** Nécessaire pour le filtre dans UserListPanel */
    public User getUser() { return user; }
}
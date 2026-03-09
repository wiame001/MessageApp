package main.java.com.ubo.tp.message.ihm.canal;

import main.java.com.ubo.tp.message.datamodel.Channel;
import javax.swing.*;
import java.awt.*;

public class ChannelPanel extends JPanel {

    private Channel channel;

    public ChannelPanel(Channel channel) {
        this.channel = channel;

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblHash = new JLabel("#");
        lblHash.setFont(new Font("Arial", Font.BOLD, 18));
        lblHash.setForeground(new Color(100, 100, 255));
        gbc.gridx = 0; gbc.gridy = 0;
        add(lblHash, gbc);

        JLabel lblName = new JLabel(channel.getName());
        lblName.setFont(new Font("Arial", Font.BOLD, 13));
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        add(lblName, gbc);

        JLabel lblDesc = new JLabel(channel.getName());
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 11));
        lblDesc.setForeground(Color.DARK_GRAY);
        gbc.gridx = 1; gbc.gridy = 1;
        add(lblDesc, gbc);

        // Listener de clic
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                System.out.println("Canal sélectionné : " + channel.getName());
            }
        });
    }

}
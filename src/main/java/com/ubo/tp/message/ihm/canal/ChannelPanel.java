package main.java.com.ubo.tp.message.ihm.canal;

import main.java.com.ubo.tp.message.controller.ChannelController;
import main.java.com.ubo.tp.message.datamodel.Channel;
import com.ubo.tp.message.ihm.canal.ChannelSelectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChannelPanel extends JPanel {

    private final Channel channel;
    private final ChannelController controller;

    public ChannelPanel(Channel channel,
                        ChannelSelectionListener selectionListener,
                        ChannelController controller) {
        this.channel = channel;
        this.controller = controller;

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

        JLabel lblDesc = new JLabel("Créé par : " + channel.getCreator().getName());
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 11));
        lblDesc.setForeground(Color.DARK_GRAY);
        gbc.gridx = 1; gbc.gridy = 1;
        add(lblDesc, gbc);

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    // Sélectionner le canal
                    if (selectionListener != null) {
                        selectionListener.onChannelSelected(channel);
                    }
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    // Menu contextuel
                    showContextMenu(e.getX(), e.getY());
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(new Color(220, 220, 240));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(new Color(245, 245, 245));
            }
        });
    }

    private void showContextMenu(int x, int y) {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem itemSupprimer = new JMenuItem("Supprimer le canal");

        // Grisé si l'utilisateur connecté n'est pas le créateur
        itemSupprimer.setEnabled(controller.canDeleteChannel(channel));

        itemSupprimer.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Supprimer le canal \"" + channel.getName() + "\" ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    controller.deleteChannel(channel);
                    // La ChannelListPanel sera rafraîchie via notifyChannelDeleted -> MainPanel
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
        JMenuItem editItem = new JMenuItem("Modifier le canal");

        editItem.setEnabled(controller.canDeleteChannel(channel));

        editItem.addActionListener(e -> {

            ChannelEditDialog dialog =
                    new ChannelEditDialog(channel, controller);

            dialog.setVisible(true);

        });

        JMenuItem leaveItem = new JMenuItem("Quitter le canal");

        leaveItem.setEnabled(controller.canLeaveChannel(channel));

        leaveItem.addActionListener(e -> {

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Quitter le canal \"" + channel.getName() + "\" ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {

                try {

                    controller.leaveChannel(channel);

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
        menu.add(leaveItem);
        menu.add(editItem);
        menu.add(itemSupprimer);
        menu.show(this, x, y);
    }
    public Channel getChannel() {
        return channel;
    }
}
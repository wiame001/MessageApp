package main.java.com.ubo.tp.message.ihm.canal;

import main.java.com.ubo.tp.message.controller.ChannelController;
import main.java.com.ubo.tp.message.datamodel.Channel;

import javax.swing.*;
import java.awt.*;

public class ChannelListPanel extends JPanel {

    private final ChannelController controller;
    private final JPanel listContainer;

    // callback vers MainPanel
    private final com.ubo.tp.message.ihm.canal.ChannelSelectionListener selectionListener;

    public ChannelListPanel(ChannelController controller, com.ubo.tp.message.ihm.canal.ChannelSelectionListener listener) {

        this.controller = controller;
        this.selectionListener = listener;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Canaux"));

        // bouton création canal
        JButton createButton = new JButton("+");

        createButton.addActionListener(e -> {

            String name = JOptionPane.showInputDialog(
                    this,
                    "Nom du canal :",
                    "Créer un canal",
                    JOptionPane.PLAIN_MESSAGE
            );

            if (name != null) {
                try {
                    controller.createChannel(name);
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

        add(createButton, BorderLayout.NORTH);

        // liste des canaux
        listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));

        add(new JScrollPane(listContainer), BorderLayout.CENTER);

        refreshList();
    }

    public void refreshList() {

        listContainer.removeAll();

        for (Channel channel : controller.getAllChannels()) {

            ChannelPanel panel = new ChannelPanel(channel, selectionListener);

            listContainer.add(panel);
        }

        listContainer.revalidate();
        listContainer.repaint();
    }
}
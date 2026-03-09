package main.java.com.ubo.tp.message.ihm.canal;

import main.java.com.ubo.tp.message.controller.ChannelController;
import main.java.com.ubo.tp.message.datamodel.Channel;
import javax.swing.*;
import java.awt.*;

public class ChannelListPanel extends JPanel {
    private final ChannelController controller;
    private JPanel listContainer;

    public ChannelListPanel(ChannelController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Canaux"));

        listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));

        add(new JScrollPane(listContainer), BorderLayout.CENTER);
        refreshList();
    }

    public void refreshList() {
        listContainer.removeAll();
        for (Channel channel : controller.getAllChannels()) {
            listContainer.add(new ChannelPanel(channel));
        }
        listContainer.revalidate();
        listContainer.repaint();
    }
}
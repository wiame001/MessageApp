package main.java.com.ubo.tp.message.ihm.canal;

import main.java.com.ubo.tp.message.controller.ChannelController;
import main.java.com.ubo.tp.message.datamodel.Channel;
import com.ubo.tp.message.ihm.canal.ChannelSelectionListener;

import javax.swing.*;
import java.awt.*;

public class ChannelListPanel extends JPanel {

    private final ChannelController controller;
    private final JPanel listContainer;
    private final ChannelSelectionListener selectionListener;
    private JTextField searchField;

    public ChannelListPanel(ChannelController controller, ChannelSelectionListener listener) {
        this.controller = controller;
        this.selectionListener = listener;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Canaux"));


        // Bouton "+"
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
                            this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        add(createButton, BorderLayout.NORTH);
        searchField = new JTextField();
        searchField.putClientProperty("JTextField.placeholderText", "Rechercher un canal...");

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {

            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterList();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterList();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterList();
            }

        });

        add(searchField, BorderLayout.NORTH);
        // Liste
        listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        add(new JScrollPane(listContainer), BorderLayout.CENTER);

        refreshList();
    }

    private void filterList() {

        String query = searchField.getText().toLowerCase().trim();

        for (Component c : listContainer.getComponents()) {

            if (c instanceof ChannelPanel cp) {

                boolean match =
                        cp.getChannel().getName()
                                .toLowerCase()
                                .contains(query);

                c.setVisible(query.isEmpty() || match);
            }
        }

        listContainer.revalidate();
        listContainer.repaint();
    }
    public void refreshList() {
        listContainer.removeAll();

        for (Channel channel : controller.getAllChannels()) {
            // On passe maintenant aussi le controller pour le menu contextuel
            ChannelPanel panel = new ChannelPanel(channel, selectionListener, controller);
            listContainer.add(panel);
        }

        listContainer.revalidate();
        listContainer.repaint();
    }
}
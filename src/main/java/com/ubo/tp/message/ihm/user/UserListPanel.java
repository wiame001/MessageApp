package main.java.com.ubo.tp.message.ihm.user;

import main.java.com.ubo.tp.message.controller.UserController;
import main.java.com.ubo.tp.message.datamodel.User;
import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class UserListPanel extends JPanel {
    private final UserController controller;
    private JPanel    listContainer;
    private JTextField searchField;

    public UserListPanel(UserController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Utilisateurs enregistrés"));

        // --- Barre de recherche (USR-008) ---
        searchField = new JTextField();
        searchField.putClientProperty("JTextField.placeholderText", "Rechercher un utilisateur...");
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { filterList(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { filterList(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterList(); }
        });
        add(searchField, BorderLayout.NORTH);

        // --- Liste ---
        listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        add(new JScrollPane(listContainer), BorderLayout.CENTER);

        refreshList();
    }

    public void refreshList() {
        listContainer.removeAll();
        Set<User> users = controller.getAllUsers();
        for (User user : users) {
            listContainer.add(new UserPanel(user));
        }
        listContainer.revalidate();
        listContainer.repaint();
        // Réappliquer le filtre si une recherche est en cours
        filterList();
    }

    /** Filtre les UserPanel selon le texte saisi (USR-008) */
    private void filterList() {
        String query = searchField.getText().toLowerCase().trim();
        for (Component c : listContainer.getComponents()) {
            if (c instanceof UserPanel up) {
                boolean match = up.getUser().getName().toLowerCase().contains(query)
                        || up.getUser().getUserTag().toLowerCase().contains(query);
                c.setVisible(query.isEmpty() || match);
            }
        }
        listContainer.revalidate();
        listContainer.repaint();
    }
}

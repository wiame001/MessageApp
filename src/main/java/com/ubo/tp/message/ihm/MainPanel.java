package main.java.com.ubo.tp.message.ihm;

import main.java.com.ubo.tp.message.controller.ChannelController;
import main.java.com.ubo.tp.message.controller.LoginController;
import main.java.com.ubo.tp.message.controller.MessageController;
import main.java.com.ubo.tp.message.controller.UserController;
import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.core.database.IDatabaseObserver;
import main.java.com.ubo.tp.message.datamodel.Channel;
import main.java.com.ubo.tp.message.datamodel.Message;
import main.java.com.ubo.tp.message.datamodel.User;
import main.java.com.ubo.tp.message.ihm.canal.ChannelListPanel;
import com.ubo.tp.message.ihm.message.MessageInputPanel;
import main.java.com.ubo.tp.message.ihm.message.MessagePanel;
import main.java.com.ubo.tp.message.ihm.user.UserListPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Set;
import java.util.UUID;

public class MainPanel extends JPanel implements IDatabaseObserver {

    protected MessageController mMessageController;
    protected DataManager mDataManager;
    protected JPanel mMessagesListPanel;
    protected UserListPanel mUserListPanel;
    protected UserController mUserController;
    protected ChannelController mChannelController;
    protected ChannelListPanel mChannelListPanel;
    private UUID currentChannel;
    private MessageInputPanel messageInputPanel;
    private UUID currentPrivateRecipient;
    private UUID myUserId;
    private JTextField messageSearchField;
    private JLabel connectedUserLabel;

    public MainPanel(User user, MessageController msgCtrl, UserController userCtrl, ChannelController chanCtrl, DataManager dataManager) {
        this.mMessageController = msgCtrl;
        this.mUserController = userCtrl;
        this.mDataManager = dataManager;
        this.mChannelController = chanCtrl;
        myUserId = user.getUuid();

        // 1. D'ABORD : On crée les composants (mMessagesListPanel, mUserListPanel, etc.)
        this.initComponents(user);

        // 2. ENSUITE : On affiche les données déjà présentes en base
        this.refreshMessages();
        // (Le UserListPanel se rafraîchit déjà tout seul dans son constructeur normalement)

        // 3. ENFIN : On s'enregistre pour les futures modifications
        this.mDataManager.addObserver(this);
    }

    private void initComponents(User user) {

        setLayout(new BorderLayout());

        // ===== BARRE UTILISATEUR =====
        JPanel userBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnEditName = new JButton("Modifier nom");
        JButton btnDelete = new JButton("Supprimer compte");
        JButton btnLogout = new JButton("Déconnexion");

        connectedUserLabel = new JLabel("Connecté : " + user.getName());
        userBar.add(connectedUserLabel);        userBar.add(btnEditName);
        userBar.add(btnDelete);
        userBar.add(btnLogout);

        // ===== BARRE RECHERCHE MESSAGE =====
        messageSearchField = new JTextField();
        messageSearchField.putClientProperty("JTextField.placeholderText", "Rechercher un message...");

        messageSearchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {

            public void insertUpdate(javax.swing.event.DocumentEvent e) { refreshMessages(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { refreshMessages(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { refreshMessages(); }

        });

        // ===== PANEL HAUT =====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(userBar, BorderLayout.NORTH);
        topPanel.add(messageSearchField, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // ===== ACTIONS BOUTONS =====
        btnEditName.addActionListener(e -> {

            String newName = JOptionPane.showInputDialog(this, "Nouveau nom :");

            if (newName != null) {

                String result = mUserController.updateName(newName);

                if (!result.equals("SUCCESS")) {
                    JOptionPane.showMessageDialog(this, result);
                } else {

                    connectedUserLabel.setText(
                            "Connecté : " +
                                    mUserController.getSession().getConnectedUser().getName()
                    );

                    mChannelListPanel.refreshList();
                }
            }
        });

        btnDelete.addActionListener(e -> {

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Supprimer votre compte ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                mUserController.deleteAccount();
            }
        });

        btnLogout.addActionListener(e -> {
            mUserController.logout();
        });

        // ===== CANAUX (OUEST) =====
        mChannelListPanel = new ChannelListPanel(
                mChannelController,
                this::setSelectedChannel
        );

        mChannelListPanel.setPreferredSize(new Dimension(180, 0));

        add(mChannelListPanel, BorderLayout.WEST);

        // ===== UTILISATEURS (EST) =====
        mUserListPanel = new UserListPanel(
                mUserController,
                this::setSelectedUser
        );

        mUserListPanel.setPreferredSize(new Dimension(180, 0));

        add(mUserListPanel, BorderLayout.EAST);

        // ===== MESSAGES (CENTRE) =====
        mMessagesListPanel = new JPanel();
        mMessagesListPanel.setLayout(new BoxLayout(mMessagesListPanel, BoxLayout.Y_AXIS));

        add(new JScrollPane(mMessagesListPanel), BorderLayout.CENTER);

        // ===== SAISIE MESSAGE (BAS) =====
        messageInputPanel = new MessageInputPanel(mMessageController);

        add(messageInputPanel, BorderLayout.SOUTH);
    }


    // Dans l'implémentation de IDatabaseObserver du MainPanel
    @Override
    public void notifyUserAdded(User user) {
        mUserListPanel.refreshList(); // Mise à jour auto quand un nouvel utilisateur est détecté
    }

    /**
     * Méthode pour vider et reconstruire la liste des messages
     */
    private void refreshMessages() {

        String query = messageSearchField.getText().toLowerCase().trim();

        SwingUtilities.invokeLater(() -> {

            mMessagesListPanel.removeAll();

            Set<Message> messages = mDataManager.getMessages();

            for (Message msg : messages) {

                // ---- Messages de canal ----
                if (currentChannel != null &&
                        msg.getRecipient().equals(currentChannel) &&
                        msg.getText().toLowerCase().contains(query)) {

                    mMessagesListPanel.add(
                            new MessagePanel(msg, mMessageController)
                    );                }

                // ---- Messages privés ----
                else if (currentPrivateRecipient != null) {

                    boolean sentByMe =
                            msg.getSender().getUuid().equals(myUserId)
                                    && msg.getRecipient().equals(currentPrivateRecipient);

                    boolean sentToMe =
                            msg.getSender().getUuid().equals(currentPrivateRecipient)
                                    && msg.getRecipient().equals(myUserId);

                    if ((sentByMe || sentToMe) &&
                            msg.getText().toLowerCase().contains(query)) {

                        mMessagesListPanel.add(
                                new MessagePanel(msg, mMessageController)
                        );
                    }
                }
            }

            mMessagesListPanel.revalidate();
            mMessagesListPanel.repaint();
        });
    }
    // --- Implémentation de IDatabaseObserver ---

    @Override
    public void notifyMessageAdded(Message message) {
        refreshMessages(); // On rafraîchit quand un message arrive
    }

    @Override
    public void notifyChannelAdded(main.java.com.ubo.tp.message.datamodel.Channel channel) {
        if (mChannelListPanel != null) {
            mChannelListPanel.refreshList();
        }
    }


    public void setSelectedChannel(Channel channel) {

        currentChannel = channel.getUuid();
        currentPrivateRecipient = null;

        messageInputPanel.setRecipient(currentChannel);

        refreshMessages();
    }

    public void setSelectedUser(User user) {

        currentPrivateRecipient = user.getUuid();
        currentChannel = null;

        messageInputPanel.setRecipient(currentPrivateRecipient);

        refreshMessages();

    }

    @Override
    public void notifyMessageDeleted(Message message) {
        refreshMessages();
    }

    @Override
    public void notifyChannelDeleted(Channel deletedChannel) {
        if (mChannelListPanel != null) {
            mChannelListPanel.refreshList();
        }
    }

    @Override
    public void notifyChannelModified(Channel channel) {
        mChannelListPanel.refreshList();
    }

    @Override
    public void notifyMessageModified(Message modifiedMessage) {}

    @Override
    public void notifyUserDeleted(User user) { }

    @Override
    public void notifyUserModified(User user) { }

}
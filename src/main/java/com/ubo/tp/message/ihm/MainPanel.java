package main.java.com.ubo.tp.message.ihm;

import main.java.com.ubo.tp.message.controller.ChannelController;
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

public class MainPanel extends JPanel implements IDatabaseObserver {

    protected MessageController mMessageController;
    protected DataManager mDataManager;
    protected JPanel mMessagesListPanel;
    protected UserListPanel mUserListPanel;
    protected UserController mUserController;
    protected ChannelController mChannelController;
    protected ChannelListPanel mChannelListPanel;

    private MessageInputPanel messageInputPanel;

    public MainPanel(User user, MessageController msgCtrl, UserController userCtrl, ChannelController chanCtrl, DataManager dataManager) {
        this.mMessageController = msgCtrl;
        this.mUserController = userCtrl;
        this.mDataManager = dataManager;
        this.mChannelController = chanCtrl;

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

        // --- OUEST : Canaux ---
        mChannelListPanel = new ChannelListPanel(
                mChannelController,
                this::setSelectedChannel
        );
        mChannelListPanel.setPreferredSize(new Dimension(180, 0));
        add(mChannelListPanel, BorderLayout.WEST);

        // --- EST : Utilisateurs ---
        mUserListPanel = new UserListPanel(mUserController);
        mUserListPanel.setPreferredSize(new Dimension(180, 0));
        add(mUserListPanel, BorderLayout.EAST);

        // --- CENTRE : Messages ---
        mMessagesListPanel = new JPanel();
        mMessagesListPanel.setLayout(new BoxLayout(mMessagesListPanel, BoxLayout.Y_AXIS));
        add(new JScrollPane(mMessagesListPanel), BorderLayout.CENTER);

        // --- BAS : Saisie ---
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
        // On s'assure de modifier l'IHM dans le thread Swing
        SwingUtilities.invokeLater(() -> {
            mMessagesListPanel.removeAll();

            Set<Message> messages = mDataManager.getMessages();
            // Optionnel : Trier les messages par date ici

            for (Message msg : messages) {
                mMessagesListPanel.add(new MessagePanel(msg));
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
        messageInputPanel.setRecipient(channel.getUuid());
    }
    @Override
    public void notifyMessageDeleted(Message message) {
        refreshMessages();
    }

    @Override
    public void notifyChannelDeleted(Channel deletedChannel) {}

    @Override
    public void notifyChannelModified(Channel modifiedChannel) {}

    @Override
    public void notifyMessageModified(Message modifiedMessage) {}

    @Override
    public void notifyUserDeleted(User user) { }

    @Override
    public void notifyUserModified(User user) { }
}
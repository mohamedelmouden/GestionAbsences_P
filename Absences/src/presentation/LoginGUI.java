package presentation;

import dao.UtilisateurDAO;
import entities.Utilisateur;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class LoginGUI extends JFrame {

    private static final Color BG_DARK      = new Color(13,  17,  23);
    private static final Color BG_PANEL     = new Color(22,  27,  34);
    private static final Color BG_CARD      = new Color(30,  37,  46);
    private static final Color ACCENT_BLUE  = new Color(56,  139, 253);
    private static final Color ACCENT_GREEN = new Color(63,  185, 80);
    private static final Color ACCENT_RED   = new Color(248, 81,  73);
    private static final Color TEXT_PRIMARY = new Color(230, 237, 243);
    private static final Color TEXT_MUTED   = new Color(110, 118, 129);
    private static final Color BORDER_COLOR = new Color(48,  54,  61);

    private ModernTextField txtUsername;
    private JPasswordField  txtPassword;
    private JLabel          lblStatus;

    public LoginGUI() {
        super("Connexion — Gestion RH");
        setSize(420, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout());
        add(createLoginPanel(), BorderLayout.CENTER);
    }

    private JPanel createLoginPanel() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(BG_DARK);

        // Carte centrale
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(BORDER_COLOR);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(36, 36, 36, 36));
        card.setPreferredSize(new Dimension(340, 420));

        // Logo + Titre
        JLabel icon = new JLabel("\uD83C\uDFE2");
        icon.setFont(new Font("Dialog", Font.PLAIN, 48));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(icon);
        card.add(Box.createVerticalStrut(12));

        JLabel title = new JLabel("Gestion des Absences");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);

        JLabel sub = new JLabel("Connectez-vous pour continuer");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(TEXT_MUTED);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(sub);
        card.add(Box.createVerticalStrut(28));

        // Champ username
        JLabel lblUser = new JLabel("NOM D'UTILISATEUR");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblUser.setForeground(TEXT_MUTED);
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblUser);
        card.add(Box.createVerticalStrut(5));

        txtUsername = new ModernTextField("ex: admin");
        txtUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        card.add(txtUsername);
        card.add(Box.createVerticalStrut(14));

        // Champ mot de passe
        JLabel lblPwd = new JLabel("MOT DE PASSE");
        lblPwd.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblPwd.setForeground(TEXT_MUTED);
        lblPwd.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblPwd);
        card.add(Box.createVerticalStrut(5));

        txtPassword = new JPasswordField();
        txtPassword.setBackground(new Color(20, 25, 32));
        txtPassword.setForeground(TEXT_PRIMARY);
        txtPassword.setCaretColor(TEXT_PRIMARY);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtPassword.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(8, 12, 8, 12)));
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(txtPassword);
        card.add(Box.createVerticalStrut(6));

        // Lien mot de passe oublié
        JLabel lblOublie = new JLabel("Mot de passe oublié ?");
        lblOublie.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblOublie.setForeground(ACCENT_BLUE);
        lblOublie.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblOublie.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblOublie.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { afficherMotDePasseOublie(); }
            public void mouseEntered(MouseEvent e) { lblOublie.setText("<html><u>Mot de passe oublié ?</u></html>"); }
            public void mouseExited (MouseEvent e) { lblOublie.setText("Mot de passe oublié ?"); }
        });
        card.add(lblOublie);
        card.add(Box.createVerticalStrut(20));

        // Bouton connexion
        ModernButton btnLogin = new ModernButton("Se connecter", ACCENT_BLUE, new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        card.add(btnLogin);
        card.add(Box.createVerticalStrut(12));

        // Status
        lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblStatus.setForeground(ACCENT_RED);
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblStatus);

        // Action connexion
        btnLogin.addActionListener(e -> actionConnecter());
        txtPassword.addActionListener(e -> actionConnecter()); // Enter = login

        outer.add(card);
        return outer;
    }

    private void actionConnecter() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            lblStatus.setText("Veuillez remplir tous les champs.");
            return;
        }

        Utilisateur user = new UtilisateurDAO().connecter(username, password);
        if (user != null) {
            lblStatus.setForeground(ACCENT_GREEN);
            lblStatus.setText("Connexion réussie !");
            // Ouvrir l'application principale
            Timer t = new Timer(600, e -> {
                MainAppGUI app = new MainAppGUI();
                app.actionEmpActualiser();
                app.actionAbsActualiser();
                app.actionTypeActualiser();
                app.setVisible(true);
                dispose();
            });
            t.setRepeats(false);
            t.start();
        } else {
            lblStatus.setForeground(ACCENT_RED);
            lblStatus.setText("Identifiants incorrects.");
            txtPassword.setText("");
        }
    }

    // ── Fenêtre mot de passe oublié ───────────────────────────────────────
    private void afficherMotDePasseOublie() {
        JDialog dlg = new JDialog(this, "Mot de passe oublié", true);
        dlg.setSize(380, 280);
        dlg.setLocationRelativeTo(this);
        dlg.getContentPane().setBackground(BG_DARK);
        dlg.setLayout(new BorderLayout());

        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(28, 32, 28, 32));

        JLabel title = new JLabel("Réinitialiser le mot de passe");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(title);
        p.add(Box.createVerticalStrut(6));

        JLabel desc = new JLabel("<html>Entrez votre nom d'utilisateur.<br>Un nouveau mot de passe sera envoyé à votre email.</html>");
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        desc.setForeground(TEXT_MUTED);
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(desc);
        p.add(Box.createVerticalStrut(18));

        JLabel lbl = new JLabel("NOM D'UTILISATEUR");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(TEXT_MUTED);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(lbl);
        p.add(Box.createVerticalStrut(5));

        ModernTextField txtUser = new ModernTextField("ex: admin");
        txtUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtUser.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        p.add(txtUser);
        p.add(Box.createVerticalStrut(14));

        JLabel lblMsg = new JLabel(" ");
        lblMsg.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblMsg.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(lblMsg);
        p.add(Box.createVerticalStrut(10));

        ModernButton btnEnvoyer = new ModernButton("Envoyer le nouveau mot de passe", ACCENT_BLUE, new Font("Segoe UI", Font.BOLD, 12));
        btnEnvoyer.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnEnvoyer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        p.add(btnEnvoyer);

        btnEnvoyer.addActionListener(e -> {
            String username = txtUser.getText().trim();
            if (username.isEmpty()) {
                lblMsg.setForeground(ACCENT_RED);
                lblMsg.setText("Entrez votre nom d'utilisateur.");
                return;
            }

            lblMsg.setForeground(TEXT_MUTED);
            lblMsg.setText("Recherche en cours...");

            Utilisateur user = new UtilisateurDAO().trouverParUsername(username);
            if (user == null) {
                lblMsg.setForeground(ACCENT_RED);
                lblMsg.setText("Utilisateur introuvable.");
                return;
            }

            // Générer nouveau mot de passe
            String nouveauMdp = EmailService.genererMotDePasse();

            // Envoyer l'email
            lblMsg.setText("Envoi en cours...");
            boolean envoye = EmailService.envoyerNouveauMotDePasse(user.getEmail(), username, nouveauMdp);

            if (envoye) {
                // Sauvegarder le nouveau mot de passe en BDD
                new UtilisateurDAO().mettreAJourMotDePasse(username, nouveauMdp);
                lblMsg.setForeground(ACCENT_GREEN);
                lblMsg.setText("Email envoyé à " + user.getEmail());
                btnEnvoyer.setEnabled(false);
            } else {
                lblMsg.setForeground(ACCENT_RED);
                lblMsg.setText("Échec de l'envoi. Vérifiez EmailService.java");
            }
        });

        dlg.add(p, BorderLayout.CENTER);
        dlg.setVisible(true);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new LoginGUI().setVisible(true));
    }

    public void setVisible(boolean b) {
    super.setVisible(b);
}}


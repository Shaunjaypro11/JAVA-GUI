import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private List<Student> students;

    // Color palette
    private static final Color BG = new Color(15, 23, 42);
    private static final Color PANEL_BG = new Color(30, 41, 59);
    private static final Color ACCENT = new Color(99, 179, 237);
    private static final Color ACCENT2 = new Color(129, 140, 248);
    private static final Color TEXT = new Color(226, 232, 240);
    private static final Color SUBTEXT = new Color(148, 163, 184);
    private static final Color FIELD_BG = new Color(51, 65, 85);
    private static final Color SUCCESS = new Color(74, 222, 128);

    public LoginFrame() {
        students = FileHandler.loadStudents();
        setupUI();
    }

    private void setupUI() {
        setTitle("Study Group Organizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 560);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, BG, getWidth(), getHeight(), new Color(23, 37, 84));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        root.setBorder(new EmptyBorder(40, 50, 40, 50));

        // Header
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel icon = new JLabel("📚", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 52));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Study Group Organizer", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Group 3 – Collaborative Learning System", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(SUBTEXT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(icon);
        header.add(Box.createVerticalStrut(8));
        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);

        // Login card
        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PANEL_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        card.setOpaque(false);
        card.setLayout(new GridBagLayout());
        card.setBorder(new EmptyBorder(28, 28, 28, 28));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 0);

        JLabel loginTitle = new JLabel("Welcome Back");
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginTitle.setForeground(TEXT);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        card.add(loginTitle, gbc);

        // Username
        gbc.gridy = 1; gbc.gridwidth = 2;
        card.add(makeLabel("Username"), gbc);

        usernameField = makeTextField();
        gbc.gridy = 2;
        card.add(usernameField, gbc);

        // Password
        gbc.gridy = 3;
        card.add(makeLabel("Password"), gbc);

        passwordField = new JPasswordField();
        styleTextField(passwordField);
        gbc.gridy = 4;
        card.add(passwordField, gbc);

        // Login button
        JButton loginBtn = makeButton("Login", ACCENT, Color.WHITE);
        gbc.gridy = 5; gbc.insets = new Insets(14, 0, 6, 0);
        card.add(loginBtn, gbc);

        // Divider
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(71, 85, 105));
        gbc.gridy = 6; gbc.insets = new Insets(4, 0, 4, 0);
        card.add(sep, gbc);

        // Register button
        JButton registerBtn = makeButton("Create New Account", FIELD_BG, SUBTEXT);
        gbc.gridy = 7; gbc.insets = new Insets(6, 0, 0, 0);
        card.add(registerBtn, gbc);

        // Actions
        loginBtn.addActionListener(e -> handleLogin());
        registerBtn.addActionListener(e -> openRegister());

        passwordField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) handleLogin();
            }
        });

        root.add(header, BorderLayout.NORTH);
        root.add(Box.createVerticalStrut(24), BorderLayout.CENTER);
        root.add(card, BorderLayout.SOUTH);

        add(root);
    }

    private JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(SUBTEXT);
        return lbl;
    }

    private JTextField makeTextField() {
        JTextField tf = new JTextField();
        styleTextField(tf);
        return tf;
    }

    private void styleTextField(JTextField tf) {
        tf.setBackground(FIELD_BG);
        tf.setForeground(TEXT);
        tf.setCaretColor(ACCENT);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(71, 85, 105), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        tf.setPreferredSize(new Dimension(0, 40));
    }

    private JButton makeButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? bg.darker() : getModel().isRollover() ? bg.brighter() : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 42));
        return btn;
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showMsg("Please fill in all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Admin login check
        if (username.equals("admin") && password.equals("admin123")) {
            dispose();
            new AdminFrame().setVisible(true);
            return;
        }

        for (Student s : students) {
            if (s.getUsername().equals(username) && s.getPassword().equals(password)) {
                dispose();
                new DashboardFrame(s).setVisible(true);
                return;
            }
        }
        showMsg("Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
    }

    private void openRegister() {
        new RegisterFrame(students, this).setVisible(true);
        setVisible(false);
    }

    public void refreshStudents() {
        students = FileHandler.loadStudents();
    }

    private void showMsg(String msg, String title, int type) {
        JOptionPane.showMessageDialog(this, msg, title, type);
    }
}
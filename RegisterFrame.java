import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

public class RegisterFrame extends JFrame {
    private JTextField fullNameField, usernameField, emailField;
    private JPasswordField passwordField, confirmPasswordField;
    private List<Student> students;
    private LoginFrame loginFrame;

    private static final Color BG = new Color(15, 23, 42);
    private static final Color PANEL_BG = new Color(30, 41, 59);
    private static final Color ACCENT = new Color(99, 179, 237);
    private static final Color TEXT = new Color(226, 232, 240);
    private static final Color SUBTEXT = new Color(148, 163, 184);
    private static final Color FIELD_BG = new Color(51, 65, 85);

    public RegisterFrame(List<Student> students, LoginFrame loginFrame) {
        this.students = students;
        this.loginFrame = loginFrame;
        setupUI();
    }

    private void setupUI() {
        setTitle("Create Account");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(480, 620);
        setLocationRelativeTo(null);
        setResizable(false);

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent e) {
                loginFrame.setVisible(true);
                loginFrame.refreshStudents();
            }
        });

        JPanel root = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, BG, getWidth(), getHeight(), new Color(23, 37, 84));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        root.setBorder(new EmptyBorder(30, 50, 30, 50));

        // Header
        JLabel title = new JLabel("Create Your Account");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel sub = new JLabel("Join the Study Group Organizer");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(SUBTEXT);
        sub.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(sub);

        // Form card
        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PANEL_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        card.setOpaque(false);
        card.setLayout(new GridBagLayout());
        card.setBorder(new EmptyBorder(24, 28, 24, 28));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 0, 4, 0);
        gbc.gridwidth = 2;

        fullNameField = addField(card, gbc, "Full Name", 0);
        usernameField = addField(card, gbc, "Username", 2);
        emailField = addField(card, gbc, "Email", 4);
        passwordField = addPasswordField(card, gbc, "Password", 6);
        confirmPasswordField = addPasswordField(card, gbc, "Confirm Password", 8);

        JButton registerBtn = makeButton("Create Account");
        gbc.gridy = 10; gbc.insets = new Insets(14, 0, 6, 0);
        card.add(registerBtn, gbc);

        JButton backBtn = makeOutlineButton("Back to Login");
        gbc.gridy = 11; gbc.insets = new Insets(4, 0, 0, 0);
        card.add(backBtn, gbc);

        registerBtn.addActionListener(e -> handleRegister());
        backBtn.addActionListener(e -> { dispose(); });

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(Box.createVerticalStrut(20), BorderLayout.NORTH);
        center.add(card, BorderLayout.CENTER);

        root.add(header, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);
        add(root);
    }

    private JTextField addField(JPanel panel, GridBagConstraints gbc, String label, int row) {
        gbc.gridy = row;
        panel.add(makeLabel(label), gbc);
        JTextField tf = new JTextField();
        styleField(tf);
        gbc.gridy = row + 1;
        panel.add(tf, gbc);
        return tf;
    }

    private JPasswordField addPasswordField(JPanel panel, GridBagConstraints gbc, String label, int row) {
        gbc.gridy = row;
        panel.add(makeLabel(label), gbc);
        JPasswordField pf = new JPasswordField();
        styleField(pf);
        gbc.gridy = row + 1;
        panel.add(pf, gbc);
        return pf;
    }

    private void styleField(JTextField tf) {
        tf.setBackground(FIELD_BG);
        tf.setForeground(TEXT);
        tf.setCaretColor(ACCENT);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(71, 85, 105), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        tf.setPreferredSize(new Dimension(0, 38));
    }

    private JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(SUBTEXT);
        return lbl;
    }

    private JButton makeButton(String text) {
        JButton btn = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? ACCENT.darker() : ACCENT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 42));
        return btn;
    }

    private JButton makeOutlineButton(String text) {
        JButton btn = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(FIELD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(SUBTEXT);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 38));
        return btn;
    }

    private void handleRegister() {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmPasswordField.getPassword());

        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (Student s : students) {
            if (s.getUsername().equals(username)) {
                JOptionPane.showMessageDialog(this, "Username already taken.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Student newStudent = new Student(username, password, fullName, email);
        students.add(newStudent);
        FileHandler.saveStudents(students);
        JOptionPane.showMessageDialog(this, "Account created successfully! Please log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}

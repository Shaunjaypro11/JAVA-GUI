import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class AdminFrame extends JFrame {

    private static final Color BG         = new Color(10, 10, 20);
    private static final Color SIDEBAR_BG  = new Color(20, 10, 40);
    private static final Color PANEL_BG   = new Color(30, 20, 55);
    private static final Color ACCENT     = new Color(180, 100, 255);
    private static final Color TEXT       = new Color(230, 220, 255);
    private static final Color SUBTEXT    = new Color(150, 130, 200);
    private static final Color FIELD_BG   = new Color(50, 35, 80);
    private static final Color DANGER     = new Color(248, 113, 113);
    private static final Color SUCCESS    = new Color(74, 222, 128);

    private JPanel contentPanel;
    private List<Student> students;
    private List<Group> groups;

    public AdminFrame() {
        refreshData();
        setupUI();
    }

    private void refreshData() {
        students = FileHandler.loadStudents();
        groups   = FileHandler.loadGroups();
    }

    private void setupUI() {
        setTitle("Study Group Organizer - Admin Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 680);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);
        root.add(buildSidebar(), BorderLayout.WEST);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BG);
        showPanel("OVERVIEW");
        root.add(contentPanel, BorderLayout.CENTER);
        add(root);
    }

    private void showPanel(String name) {
        refreshData();
        contentPanel.removeAll();
        switch (name) {
            case "OVERVIEW": contentPanel.add(buildOverviewPanel(),  BorderLayout.CENTER); break;
            case "USERS":    contentPanel.add(buildUsersPanel(),     BorderLayout.CENTER); break;
            case "GROUPS":   contentPanel.add(buildGroupsPanel(),    BorderLayout.CENTER); break;
            case "SESSIONS": contentPanel.add(buildSessionsPanel(),  BorderLayout.CENTER); break;
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, SIDEBAR_BG, 0, getHeight(), BG);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sidebar.setPreferredSize(new Dimension(210, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 0, 20, 0));

        JLabel icon = new JLabel("\uD83D\uDEE1", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Admin Panel");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(TEXT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("System Administrator");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sub.setForeground(SUBTEXT);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(icon);
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(title);
        sidebar.add(Box.createVerticalStrut(2));
        sidebar.add(sub);
        sidebar.add(Box.createVerticalStrut(20));

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(80, 50, 120));
        sep.setMaximumSize(new Dimension(180, 1));
        sep.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(sep);
        sidebar.add(Box.createVerticalStrut(16));

        String[][] nav = {
            {"\uD83D\uDCCA", "Overview",         "OVERVIEW"},
            {"\uD83D\uDC65", "Manage Users",     "USERS"},
            {"\uD83D\uDCDA", "Manage Groups",    "GROUPS"},
            {"\uD83D\uDCC5", "Manage Sessions",  "SESSIONS"},
        };
        for (String[] n : nav) {
            sidebar.add(makeSideBtn(n[0], n[1], n[2]));
            sidebar.add(Box.createVerticalStrut(4));
        }
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(makeSideBtn("\uD83D\uDEAA", "Logout", "LOGOUT"));
        return sidebar;
    }

    private JButton makeSideBtn(String icon, String label, String card) {
        JButton btn = new JButton(icon + "  " + label) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover() || getModel().isPressed()) {
                    g2.setColor(new Color(255, 255, 255, 25));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(card.equals("LOGOUT") ? DANGER : SUBTEXT);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(190, 38));
        btn.setPreferredSize(new Dimension(190, 38));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBorder(new EmptyBorder(0, 20, 0, 20));
        btn.addActionListener(e -> {
            if (card.equals("LOGOUT")) { dispose(); new LoginFrame().setVisible(true); }
            else showPanel(card);
        });
        return btn;
    }

    // OVERVIEW
    private JPanel buildOverviewPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 24));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel stats = new JPanel(new GridLayout(1, 3, 16, 0));
        stats.setOpaque(false);
        stats.add(makeStatCard("\uD83D\uDC65", "Total Users",    String.valueOf(students.size()), ACCENT));
        stats.add(makeStatCard("\uD83D\uDCDA", "Total Groups",   String.valueOf(groups.size()), new Color(99, 179, 237)));
        long totalSessions = groups.stream().mapToLong(g -> g.getSessions().size()).sum();
        stats.add(makeStatCard("\uD83D\uDCC5", "Total Sessions", String.valueOf(totalSessions), SUCCESS));

        JLabel recentLabel = new JLabel("All Registered Users");
        recentLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        recentLabel.setForeground(TEXT);

        String[] cols = {"#", "Full Name", "Username", "Email"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        int i = 1;
        for (Student s : students) tm.addRow(new Object[]{i++, s.getFullName(), s.getUsername(), s.getEmail()});
        JTable table = makeTable(tm);

        JPanel bottom = new JPanel(new BorderLayout(0, 8));
        bottom.setOpaque(false);
        bottom.add(recentLabel, BorderLayout.NORTH);
        bottom.add(makeScroll(table), BorderLayout.CENTER);

        panel.add(makeTitle("\uD83D\uDCCA  System Overview"), BorderLayout.NORTH);
        panel.add(stats,  BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    // USERS
    private JPanel buildUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        String[] cols = {"#", "Full Name", "Username", "Email"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            tm.addRow(new Object[]{i + 1, s.getFullName(), s.getUsername(), s.getEmail()});
        }
        JTable table = makeTable(tm);

        JButton deleteBtn = makeBtn("Delete Selected User", DANGER);
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Please select a user.", "Warning", JOptionPane.WARNING_MESSAGE); return; }
            String username = (String) tm.getValueAt(row, 2);
            if (username.equals("admin")) { JOptionPane.showMessageDialog(this, "Cannot delete the admin account.", "Error", JOptionPane.ERROR_MESSAGE); return; }
            int confirm = JOptionPane.showConfirmDialog(this,
                "Delete user \"" + username + "\"?\nThis will also remove them from all groups.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) return;
            for (Group g : groups) g.getMemberUsernames().remove(username);
            FileHandler.saveGroups(groups);
            FileHandler.deleteStudent(username);
            JOptionPane.showMessageDialog(this, "User deleted.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
            showPanel("USERS");
        });

        panel.add(makeTitle("\uD83D\uDC65  Manage Users"), BorderLayout.NORTH);
        panel.add(makeScroll(table), BorderLayout.CENTER);
        panel.add(deleteBtn, BorderLayout.SOUTH);
        return panel;
    }

    // GROUPS
    private JPanel buildGroupsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Create group form at the top
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(PANEL_BG);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 50, 120), 1, true),
            new EmptyBorder(14, 16, 14, 16)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 6, 4, 6);

        JLabel formTitle = new JLabel("Create New Group");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formTitle.setForeground(ACCENT);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        form.add(formTitle, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1;
        gbc.gridx = 0; form.add(makeFormLbl("Group Name"), gbc);
        gbc.gridx = 1; form.add(makeFormLbl("Subject"), gbc);
        gbc.gridx = 2; form.add(makeFormLbl("Description"), gbc);

        JTextField nameF    = makeField();
        JTextField subjectF = makeField();
        JTextField descF    = makeField();
        gbc.gridy = 2;
        gbc.gridx = 0; form.add(nameF, gbc);
        gbc.gridx = 1; form.add(subjectF, gbc);
        gbc.gridx = 2; form.add(descF, gbc);

        JButton createBtn = makeBtn("+ Create Group", ACCENT);
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 3;
        gbc.insets = new Insets(10, 6, 4, 6);
        form.add(createBtn, gbc);

        createBtn.addActionListener(e -> {
            String name    = nameF.getText().trim();
            String subject = subjectF.getText().trim();
            String desc    = descF.getText().trim();
            if (name.isEmpty() || subject.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Group Name and Subject are required.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            for (Group g : groups) {
                if (g.getGroupName().equalsIgnoreCase(name)) {
                    JOptionPane.showMessageDialog(this, "A group with this name already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            Group newGroup = new Group(name, subject, desc, "admin");
            groups.add(newGroup);
            FileHandler.saveGroups(groups);
            nameF.setText(""); subjectF.setText(""); descF.setText("");
            JOptionPane.showMessageDialog(this, "Group \"" + name + "\" created!", "Success", JOptionPane.INFORMATION_MESSAGE);
            showPanel("GROUPS");
        });

        // Groups table
        String[] cols = {"#", "Group Name", "Subject", "Creator", "Members", "Sessions"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        for (int i = 0; i < groups.size(); i++) {
            Group g = groups.get(i);
            tm.addRow(new Object[]{i + 1, g.getGroupName(), g.getSubject(), g.getCreatorUsername(), g.getMemberUsernames().size(), g.getSessions().size()});
        }
        JTable table = makeTable(tm);

        JButton deleteBtn = makeBtn("Delete Selected Group", DANGER);
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Please select a group.", "Warning", JOptionPane.WARNING_MESSAGE); return; }
            Group selected = groups.get(row);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Delete group \"" + selected.getGroupName() + "\"?\nThis will remove " +
                selected.getMemberUsernames().size() + " members and " + selected.getSessions().size() + " sessions.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) return;
            FileHandler.deleteGroup(selected);
            groups.remove(selected);
            JOptionPane.showMessageDialog(this, "Group deleted.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
            showPanel("GROUPS");
        });

        JPanel center = new JPanel(new BorderLayout(0, 10));
        center.setOpaque(false);
        center.add(makeScroll(table), BorderLayout.CENTER);

        panel.add(makeTitle("\uD83D\uDCDA  Manage Groups"), BorderLayout.NORTH);
        panel.add(form,       BorderLayout.CENTER);
        panel.add(center,     BorderLayout.SOUTH);

        // Resize south to take more space
        JPanel wrapper = new JPanel(new BorderLayout(0, 12));
        wrapper.setOpaque(false);
        wrapper.add(form, BorderLayout.NORTH);
        wrapper.add(makeScroll(table), BorderLayout.CENTER);
        wrapper.add(deleteBtn, BorderLayout.SOUTH);

        panel.remove(form);
        panel.remove(center);
        panel.add(wrapper, BorderLayout.CENTER);
        return panel;
    }

    // SESSIONS
    private JPanel buildSessionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        String[] cols = {"#", "Group", "Topic", "Date", "Time", "Location", "Created By"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        java.util.List<Object[]> refs = new java.util.ArrayList<>();
        int i = 1;
        for (Group g : groups) {
            for (StudySession s : g.getSessions()) {
                tm.addRow(new Object[]{i++, g.getGroupName(), s.getTopic(), s.getDate(), s.getTime(), s.getLocation(), s.getCreatorUsername()});
                refs.add(new Object[]{g, s});
            }
        }
        JTable table = makeTable(tm);

        JButton deleteBtn = makeBtn("Delete Selected Session", DANGER);
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Please select a session.", "Warning", JOptionPane.WARNING_MESSAGE); return; }
            Object[] ref = refs.get(row);
            Group g = (Group) ref[0];
            StudySession s = (StudySession) ref[1];
            int confirm = JOptionPane.showConfirmDialog(this,
                "Delete session \"" + s.getTopic() + "\" from group \"" + g.getGroupName() + "\"?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) return;
            g.getSessions().remove(s);
            FileHandler.saveGroups(groups);
            JOptionPane.showMessageDialog(this, "Session deleted.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
            showPanel("SESSIONS");
        });

        panel.add(makeTitle("\uD83D\uDCC5  Manage Sessions"), BorderLayout.NORTH);
        panel.add(makeScroll(table), BorderLayout.CENTER);
        panel.add(deleteBtn, BorderLayout.SOUTH);
        return panel;
    }

    // HELPERS
    private JLabel makeTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lbl.setForeground(TEXT);
        lbl.setBorder(new EmptyBorder(0, 0, 10, 0));
        return lbl;
    }

    private JLabel makeFormLbl(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(SUBTEXT);
        return lbl;
    }

    private JTextField makeField() {
        JTextField tf = new JTextField();
        tf.setBackground(FIELD_BG);
        tf.setForeground(TEXT);
        tf.setCaretColor(ACCENT);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 50, 120), 1, true),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        tf.setPreferredSize(new Dimension(160, 36));
        return tf;
    }

    private JPanel makeStatCard(String icon, String label, String value, Color accent) {
        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PANEL_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(accent);
                g2.fillRoundRect(0, getHeight() - 4, getWidth(), 4, 4, 4);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel iconL = new JLabel(icon); iconL.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28)); iconL.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel valL  = new JLabel(value); valL.setFont(new Font("Segoe UI", Font.BOLD, 32)); valL.setForeground(accent); valL.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblL  = new JLabel(label); lblL.setFont(new Font("Segoe UI", Font.PLAIN, 12)); lblL.setForeground(SUBTEXT); lblL.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(iconL); card.add(Box.createVerticalStrut(8)); card.add(valL); card.add(lblL);
        return card;
    }

    private JTable makeTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setBackground(FIELD_BG);
        table.setForeground(TEXT);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(34);
        table.setGridColor(new Color(70, 50, 100));
        table.setSelectionBackground(new Color(180, 100, 255, 80));
        table.setSelectionForeground(TEXT);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.getTableHeader().setBackground(PANEL_BG);
        table.getTableHeader().setForeground(SUBTEXT);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        return table;
    }

    private JScrollPane makeScroll(JTable table) {
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(70, 50, 100)));
        scroll.getViewport().setBackground(FIELD_BG);
        return scroll;
    }

    private JButton makeBtn(String text, Color color) {
        JButton btn = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? color.darker() : getModel().isRollover() ? color.brighter() : color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 52));
        btn.setMargin(new Insets(10, 20, 10, 20));
        return btn;
    }
}
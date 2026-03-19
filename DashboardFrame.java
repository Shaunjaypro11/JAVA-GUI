import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class DashboardFrame extends JFrame {
    private Student currentStudent;
    private List<Student> students;
    private List<Group> groups;

    private static final Color BG = new Color(15, 23, 42);
    private static final Color SIDEBAR_BG = new Color(23, 37, 84);
    private static final Color PANEL_BG = new Color(30, 41, 59);
    private static final Color ACCENT = new Color(99, 179, 237);
    private static final Color ACCENT2 = new Color(129, 140, 248);
    private static final Color TEXT = new Color(226, 232, 240);
    private static final Color SUBTEXT = new Color(148, 163, 184);
    private static final Color FIELD_BG = new Color(51, 65, 85);
    private static final Color SUCCESS = new Color(74, 222, 128);
    private static final Color DANGER = new Color(248, 113, 113);

    private JPanel contentPanel;

    public DashboardFrame(Student student) {
        this.currentStudent = student;
        this.students = FileHandler.loadStudents();
        this.groups = FileHandler.loadGroups();
        setupUI();
    }

    private void setupUI() {
        setTitle("Study Group Organizer – Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 640);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        // Sidebar
        JPanel sidebar = buildSidebar();
        root.add(sidebar, BorderLayout.WEST);

        // Content — use BorderLayout so we can swap panels dynamically
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BG);
        showPanel("HOME");

        root.add(contentPanel, BorderLayout.CENTER);
        add(root);
    }

    /** Replace the content area with a freshly built panel. */
    private void showPanel(String name) {
        refreshData();
        contentPanel.removeAll();
        switch (name) {
            case "HOME":     contentPanel.add(buildHomePanel(),       BorderLayout.CENTER); break;
            case "CREATE":   contentPanel.add(buildCreateGroupPanel(), BorderLayout.CENTER); break;
            case "JOIN":     contentPanel.add(buildJoinGroupPanel(),   BorderLayout.CENTER); break;
            case "MEMBERS":  contentPanel.add(buildViewMembersPanel(), BorderLayout.CENTER); break;
            case "SCHEDULE": contentPanel.add(buildSchedulePanel(),    BorderLayout.CENTER); break;
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, SIDEBAR_BG, 0, getHeight(), new Color(15, 23, 42));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sidebar.setPreferredSize(new Dimension(210, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 0, 20, 0));

        // Logo area
        JLabel logo = new JLabel("📚", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel appName = new JLabel("Study Groups");
        appName.setFont(new Font("Segoe UI", Font.BOLD, 15));
        appName.setForeground(TEXT);
        appName.setAlignmentX(Component.CENTER_ALIGNMENT);

        // User info
        JLabel userLabel = new JLabel("👤 " + currentStudent.getFullName());
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        userLabel.setForeground(SUBTEXT);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userLabel.setBorder(new EmptyBorder(0, 10, 0, 10));

        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(appName);
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(userLabel);
        sidebar.add(Box.createVerticalStrut(24));

        // Separator
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(55, 65, 81));
        sep.setMaximumSize(new Dimension(180, 1));
        sep.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(sep);
        sidebar.add(Box.createVerticalStrut(16));

        // Nav buttons
        String[][] navItems = {
            {"🏠", "Dashboard", "HOME"},
            {"➕", "Create Group", "CREATE"},
            {"🔗", "Join Group", "JOIN"},
            {"👥", "View Members", "MEMBERS"},
            {"📅", "Schedule Session", "SCHEDULE"}
        };

        for (String[] item : navItems) {
            sidebar.add(makeSidebarBtn(item[0], item[1], item[2]));
            sidebar.add(Box.createVerticalStrut(4));
        }

        sidebar.add(Box.createVerticalGlue());

        // Logout
        sidebar.add(makeSidebarBtn("🚪", "Logout", "LOGOUT"));
        return sidebar;
    }

    private JButton makeSidebarBtn(String icon, String label, String card) {
        JButton btn = new JButton(icon + "  " + label) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover() || getModel().isPressed()) {
                    g2.setColor(new Color(255, 255, 255, 20));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(card.equals("LOGOUT") ? DANGER : SUBTEXT);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
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
            if (card.equals("LOGOUT")) {
                dispose();
                new LoginFrame().setVisible(true);
            } else {
                showPanel(card);
            }
        });
        return btn;
    }

    private void refreshData() {
        groups = FileHandler.loadGroups();
        students = FileHandler.loadStudents();
    }

    // ─── HOME PANEL ────────────────────────────────────────────────────────────
    private JPanel buildHomePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel welcome = new JLabel("Welcome back, " + currentStudent.getFullName() + "! 👋");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcome.setForeground(TEXT);

        JLabel sub = new JLabel("Here's an overview of your study groups and activities.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sub.setForeground(SUBTEXT);

        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.add(welcome);
        headerPanel.add(Box.createVerticalStrut(4));
        headerPanel.add(sub);

        // Stats cards
        JPanel stats = new JPanel(new GridLayout(1, 3, 16, 0));
        stats.setOpaque(false);

        long myGroups = groups.stream().filter(g -> g.getMemberUsernames().contains(currentStudent.getUsername())).count();
        long createdGroups = groups.stream().filter(g -> g.getCreatorUsername().equals(currentStudent.getUsername())).count();
        long totalSessions = groups.stream()
            .filter(g -> g.getMemberUsernames().contains(currentStudent.getUsername()))
            .mapToLong(g -> g.getSessions().size()).sum();

        stats.add(makeStatCard("📚", "My Groups", String.valueOf(myGroups), ACCENT));
        stats.add(makeStatCard("🏆", "Created by Me", String.valueOf(createdGroups), ACCENT2));
        stats.add(makeStatCard("📅", "Total Sessions", String.valueOf(totalSessions), SUCCESS));

        // My groups list
        JLabel myGroupsLabel = new JLabel("My Study Groups");
        myGroupsLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        myGroupsLabel.setForeground(TEXT);

        java.util.List<Group> myGroupList = new java.util.ArrayList<>();
        DefaultListModel<String> model = new DefaultListModel<>();
        for (Group g : groups) {
            if (g.getMemberUsernames().contains(currentStudent.getUsername())) {
                myGroupList.add(g);
                String tag = g.getCreatorUsername().equals(currentStudent.getUsername()) ? "  [creator]" : "";
                model.addElement("📖  " + g.getGroupName() + "  [" + g.getSubject() + "]  •  " + g.getMemberUsernames().size() + " members" + tag);
            }
        }
        if (model.isEmpty()) model.addElement("You haven't joined any groups yet.");

        JList<String> list = makeStyledList(model);
        JScrollPane scroll = makeScrollPane(list);

        // Leave button (for non-creators)
        JButton leaveBtn = makeAccentButton("Leave Group", DANGER);
        leaveBtn.setEnabled(!myGroupList.isEmpty());
        leaveBtn.addActionListener(e -> {
            int idx = list.getSelectedIndex();
            if (idx < 0 || idx >= myGroupList.size()) {
                JOptionPane.showMessageDialog(this, "Please select a group from the list first.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Group selected = myGroupList.get(idx);
            if (selected.getCreatorUsername().equals(currentStudent.getUsername())) {
                JOptionPane.showMessageDialog(this,
                    "You are the creator of \"" + selected.getGroupName() + "\"." +
                    "\nCreators cannot leave. Use the Delete button instead.",
                    "Cannot Leave", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to leave \"" + selected.getGroupName() + "\"?\nYou can rejoin later.",
                "Leave Group", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) return;
            selected.getMemberUsernames().remove(currentStudent.getUsername());
            FileHandler.saveGroups(groups);
            JOptionPane.showMessageDialog(this, "You have left \"" + selected.getGroupName() + "\".", "Left Group", JOptionPane.INFORMATION_MESSAGE);
            showPanel("HOME");
        });

        // Delete button (only for creators)
        JButton deleteBtn = makeAccentButton("Delete Group", new Color(180, 40, 40));
        deleteBtn.setEnabled(!myGroupList.isEmpty());
        deleteBtn.addActionListener(e -> {
            int idx = list.getSelectedIndex();
            if (idx < 0 || idx >= myGroupList.size()) {
                JOptionPane.showMessageDialog(this, "Please select a group from the list first.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Group selected = myGroupList.get(idx);
            if (!selected.getCreatorUsername().equals(currentStudent.getUsername())) {
                JOptionPane.showMessageDialog(this,
                    "Only the creator of \"" + selected.getGroupName() + "\" can delete it.\nYou can only Leave this group.",
                    "Cannot Delete", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to PERMANENTLY DELETE \"" + selected.getGroupName() + "\"?\n" +
                "This will remove all " + selected.getMemberUsernames().size() + " members and " + selected.getSessions().size() + " sessions.\n" +
                "This action CANNOT be undone!",
                "Delete Group", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) return;
            groups.remove(selected);
            FileHandler.saveGroups(groups);
            JOptionPane.showMessageDialog(this, "Group \"" + selected.getGroupName() + "\" has been deleted.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
            showPanel("HOME");
        });

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 8, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(leaveBtn);
        btnPanel.add(deleteBtn);

        JPanel bottom = new JPanel(new BorderLayout(0, 8));
        bottom.setOpaque(false);
        bottom.add(myGroupsLabel, BorderLayout.NORTH);
        bottom.add(scroll, BorderLayout.CENTER);
        bottom.add(btnPanel, BorderLayout.SOUTH);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(stats, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    // ─── CREATE GROUP PANEL ────────────────────────────────────────────────────
    private JPanel buildCreateGroupPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel title = sectionTitle("➕  Create Study Group");

        JPanel card = makeCard();
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.gridwidth = 2;

        JTextField nameField = addFormField(card, gbc, "Group Name", 0);
        JTextField subjectField = addFormField(card, gbc, "Subject", 2);

        gbc.gridy = 4;
        card.add(makeFormLabel("Description"), gbc);
        JTextArea descArea = new JTextArea(4, 20);
        descArea.setBackground(FIELD_BG);
        descArea.setForeground(TEXT);
        descArea.setCaretColor(ACCENT);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(71, 85, 105), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setBorder(BorderFactory.createEmptyBorder());
        gbc.gridy = 5;
        card.add(descScroll, gbc);

        JButton createBtn = makeAccentButton("Create Group", SUCCESS);
        gbc.gridy = 6; gbc.insets = new Insets(16, 0, 0, 0);
        card.add(createBtn, gbc);

        createBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String subject = subjectField.getText().trim();
            String desc = descArea.getText().trim();
            if (name.isEmpty() || subject.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in the Group Name and Subject.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            for (Group g : groups) {
                if (g.getGroupName().equalsIgnoreCase(name)) {
                    JOptionPane.showMessageDialog(this, "A group with this name already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            Group newGroup = new Group(name, subject, desc, currentStudent.getUsername());
            groups.add(newGroup);
            FileHandler.saveGroups(groups);
            nameField.setText(""); subjectField.setText(""); descArea.setText("");
            JOptionPane.showMessageDialog(this, "Group \"" + name + "\" created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        panel.add(title, BorderLayout.NORTH);
        panel.add(Box.createVerticalStrut(16), BorderLayout.CENTER);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        wrapper.setOpaque(false);
        card.setPreferredSize(new Dimension(500, 380));
        wrapper.add(card);
        panel.add(wrapper, BorderLayout.SOUTH);
        return panel;
    }

    // ─── JOIN GROUP PANEL ─────────────────────────────────────────────────────
    private JPanel buildJoinGroupPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header section
        JPanel headerSection = new JPanel();
        headerSection.setOpaque(false);
        headerSection.setLayout(new BoxLayout(headerSection, BoxLayout.Y_AXIS));

        JLabel title = sectionTitle("🔗  Join Study Group");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel infoLabel = new JLabel("ℹ  Only groups created by OTHER students appear here. You cannot join groups you already belong to.");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        infoLabel.setForeground(new Color(129, 140, 248));
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerSection.add(title);
        headerSection.add(infoLabel);
        headerSection.add(Box.createVerticalStrut(8));

        // Build available groups list
        DefaultListModel<Group> model = new DefaultListModel<>();
        for (Group g : groups) {
            if (!g.getMemberUsernames().contains(currentStudent.getUsername())) {
                model.addElement(g);
            }
        }

        panel.add(headerSection, BorderLayout.NORTH);

        if (model.isEmpty()) {
            // Empty state
            JPanel emptyState = new JPanel(new GridBagLayout());
            emptyState.setBackground(FIELD_BG);
            emptyState.setBorder(BorderFactory.createLineBorder(new Color(55, 65, 81)));

            JPanel inner = new JPanel();
            inner.setOpaque(false);
            inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

            JLabel eIcon = new JLabel("🔍");
            eIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
            eIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel eMsg = new JLabel("No groups available to join.");
            eMsg.setFont(new Font("Segoe UI", Font.BOLD, 15));
            eMsg.setForeground(TEXT);
            eMsg.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel eHint = new JLabel("Groups you created or already joined won't appear here.");
            eHint.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            eHint.setForeground(SUBTEXT);
            eHint.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel eHint2 = new JLabel("Have a classmate register and create a group, then come back.");
            eHint2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            eHint2.setForeground(SUBTEXT);
            eHint2.setAlignmentX(Component.CENTER_ALIGNMENT);

            inner.add(eIcon);
            inner.add(Box.createVerticalStrut(10));
            inner.add(eMsg);
            inner.add(Box.createVerticalStrut(6));
            inner.add(eHint);
            inner.add(Box.createVerticalStrut(2));
            inner.add(eHint2);

            emptyState.add(inner);
            panel.add(emptyState, BorderLayout.CENTER);
        } else {
            JList<Group> list = new JList<>(model);
            list.setBackground(FIELD_BG);
            list.setForeground(TEXT);
            list.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            list.setSelectionBackground(new Color(99, 179, 237, 80));
            list.setSelectionForeground(TEXT);
            list.setCellRenderer(new DefaultListCellRenderer() {
                public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean sel, boolean focus) {
                    JLabel lbl = (JLabel) super.getListCellRendererComponent(l, v, i, sel, focus);
                    Group g = (Group) v;
                    lbl.setText("  📖 " + g.getGroupName() + "  [" + g.getSubject() + "]  •  " + g.getMemberUsernames().size() + " members");
                    lbl.setBorder(new EmptyBorder(8, 8, 8, 8));
                    lbl.setBackground(sel ? new Color(99, 179, 237, 60) : (i % 2 == 0 ? FIELD_BG : new Color(45, 55, 72)));
                    return lbl;
                }
            });

            JTextArea detail = new JTextArea();
            detail.setBackground(PANEL_BG);
            detail.setForeground(SUBTEXT);
            detail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            detail.setEditable(false);
            detail.setLineWrap(true);
            detail.setWrapStyleWord(true);
            detail.setBorder(new EmptyBorder(10, 12, 10, 12));
            detail.setText("Select a group above to see its details.");

            list.addListSelectionListener(e -> {
                Group g = list.getSelectedValue();
                if (g != null) {
                    detail.setText("Group: " + g.getGroupName()
                        + "\nSubject: " + g.getSubject()
                        + "\nMembers: " + g.getMemberUsernames().size()
                        + "\nCreated by: " + g.getCreatorUsername()
                        + "\nDescription: " + (g.getDescription().isEmpty() ? "(none)" : g.getDescription()));
                }
            });

            JButton joinBtn = makeAccentButton("Join Selected Group", ACCENT);
            joinBtn.addActionListener(e -> {
                Group selected = list.getSelectedValue();
                if (selected == null) {
                    JOptionPane.showMessageDialog(this, "Please select a group first.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                selected.addMember(currentStudent.getUsername());
                FileHandler.saveGroups(groups);
                model.removeElement(selected);
                detail.setText("Select a group above to see its details.");
                JOptionPane.showMessageDialog(this, "You joined \"" + selected.getGroupName() + "\"!", "Joined!", JOptionPane.INFORMATION_MESSAGE);
            });

            JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, makeScrollPane(list), new JScrollPane(detail));
            split.setDividerLocation(250);
            split.setBackground(BG);
            split.setBorder(BorderFactory.createEmptyBorder());

            panel.add(split, BorderLayout.CENTER);
            panel.add(joinBtn, BorderLayout.SOUTH);
        }

        return panel;
    }

    // ─── VIEW MEMBERS PANEL ───────────────────────────────────────────────────
    private JPanel buildViewMembersPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel title = sectionTitle("👥  View Group Members");

        // Group selector
        JComboBox<Group> groupCombo = new JComboBox<>();
        groupCombo.setBackground(FIELD_BG);
        groupCombo.setForeground(TEXT);
        groupCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        for (Group g : groups) {
            if (g.getMemberUsernames().contains(currentStudent.getUsername())) {
                groupCombo.addItem(g);
            }
        }

        // Members table
        String[] cols = {"#", "Full Name", "Username", "Email"};
        DefaultTableModel tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        styleTable(table);

        groupCombo.addActionListener(e -> {
            tableModel.setRowCount(0);
            Group selected = (Group) groupCombo.getSelectedItem();
            if (selected == null) return;
            int i = 1;
            for (String uname : selected.getMemberUsernames()) {
                for (Student s : students) {
                    if (s.getUsername().equals(uname)) {
                        tableModel.addRow(new Object[]{i++, s.getFullName(), s.getUsername(), s.getEmail()});
                    }
                }
            }
        });

        if (groupCombo.getItemCount() > 0) groupCombo.setSelectedIndex(0);

        JPanel top = new JPanel(new BorderLayout(10, 0));
        top.setOpaque(false);
        JLabel selectLabel = new JLabel("Select Group: ");
        selectLabel.setForeground(SUBTEXT);
        selectLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        top.add(selectLabel, BorderLayout.WEST);
        top.add(groupCombo, BorderLayout.CENTER);

        panel.add(title, BorderLayout.NORTH);
        panel.add(top, BorderLayout.CENTER);
        panel.add(makeScrollPane(table), BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel(new BorderLayout(0, 10));
        bottomPanel.setOpaque(false);
        bottomPanel.add(top, BorderLayout.NORTH);
        bottomPanel.add(makeScrollPane(table), BorderLayout.CENTER);

        panel.add(title, BorderLayout.NORTH);
        panel.add(bottomPanel, BorderLayout.CENTER);
        return panel;
    }

    // ─── SCHEDULE PANEL ───────────────────────────────────────────────────────
    private JPanel buildSchedulePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel title = sectionTitle("📅  Schedule Study Session");

        JPanel left = new JPanel(new BorderLayout(0, 12));
        left.setOpaque(false);

        // Schedule form
        JPanel form = makeCard();
        form.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridwidth = 2;

        // Group selector
        gbc.gridy = 0;
        form.add(makeFormLabel("Select Group"), gbc);
        JComboBox<Group> groupCombo = new JComboBox<>();
        groupCombo.setBackground(FIELD_BG);
        groupCombo.setForeground(TEXT);
        groupCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        for (Group g : groups) {
            if (g.getMemberUsernames().contains(currentStudent.getUsername())) {
                groupCombo.addItem(g);
            }
        }
        gbc.gridy = 1;
        form.add(groupCombo, gbc);

        JTextField topicField = addFormField(form, gbc, "Topic", 2);
        JTextField dateField = addFormField(form, gbc, "Date (e.g. 2025-06-15)", 4);
        JTextField timeField = addFormField(form, gbc, "Time (e.g. 2:00 PM)", 6);
        JTextField locationField = addFormField(form, gbc, "Location / Platform", 8);

        JButton scheduleBtn = makeAccentButton("Schedule Session", ACCENT2);
        gbc.gridy = 10; gbc.insets = new Insets(14, 0, 0, 0);
        form.add(scheduleBtn, gbc);

        // Sessions list
        JLabel sessLabel = new JLabel("Upcoming Sessions");
        sessLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        sessLabel.setForeground(TEXT);

        DefaultListModel<String> sessModel = new DefaultListModel<>();
        JList<String> sessList = makeStyledList(sessModel);

        Runnable refreshSessions = () -> {
            sessModel.clear();
            for (Group g : groups) {
                if (g.getMemberUsernames().contains(currentStudent.getUsername())) {
                    for (StudySession s : g.getSessions()) {
                        sessModel.addElement("📖 " + g.getGroupName() + "  |  " + s.toString());
                    }
                }
            }
            if (sessModel.isEmpty()) sessModel.addElement("No sessions scheduled yet.");
        };
        refreshSessions.run();

        scheduleBtn.addActionListener(e -> {
            Group selected = (Group) groupCombo.getSelectedItem();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "No group selected.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String topic = topicField.getText().trim();
            String date = dateField.getText().trim();
            String time = timeField.getText().trim();
            String loc = locationField.getText().trim();
            if (topic.isEmpty() || date.isEmpty() || time.isEmpty() || loc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            selected.addSession(new StudySession(topic, date, time, loc));
            FileHandler.saveGroups(groups);
            topicField.setText(""); dateField.setText(""); timeField.setText(""); locationField.setText("");
            refreshSessions.run();
            JOptionPane.showMessageDialog(this, "Session scheduled!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel sessPanel = new JPanel(new BorderLayout(0, 8));
        sessPanel.setOpaque(false);
        sessPanel.add(sessLabel, BorderLayout.NORTH);
        sessPanel.add(makeScrollPane(sessList), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, form, sessPanel);
        split.setDividerLocation(380);
        split.setBackground(BG);
        split.setBorder(BorderFactory.createEmptyBorder());

        panel.add(title, BorderLayout.NORTH);
        panel.add(split, BorderLayout.CENTER);
        return panel;
    }

    // ─── HELPERS ──────────────────────────────────────────────────────────────
    private JLabel sectionTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lbl.setForeground(TEXT);
        lbl.setBorder(new EmptyBorder(0, 0, 10, 0));
        return lbl;
    }

    private JPanel makeCard() {
        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PANEL_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(20, 24, 20, 24));
        return card;
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

        JLabel iconL = new JLabel(icon);
        iconL.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconL.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valL = new JLabel(value);
        valL.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valL.setForeground(accent);
        valL.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblL = new JLabel(label);
        lblL.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblL.setForeground(SUBTEXT);
        lblL.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(iconL);
        card.add(Box.createVerticalStrut(8));
        card.add(valL);
        card.add(lblL);
        return card;
    }

    private JTextField addFormField(JPanel panel, GridBagConstraints gbc, String label, int row) {
        gbc.gridy = row;
        panel.add(makeFormLabel(label), gbc);
        JTextField tf = new JTextField();
        tf.setBackground(FIELD_BG);
        tf.setForeground(TEXT);
        tf.setCaretColor(ACCENT);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(71, 85, 105), 1, true),
            BorderFactory.createEmptyBorder(7, 12, 7, 12)
        ));
        tf.setPreferredSize(new Dimension(0, 36));
        gbc.gridy = row + 1;
        panel.add(tf, gbc);
        return tf;
    }

    private JLabel makeFormLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(SUBTEXT);
        return lbl;
    }

    private JButton makeAccentButton(String text, Color color) {
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
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 40));
        return btn;
    }

    private <T> JList<T> makeStyledList(ListModel<T> model) {
        JList<T> list = new JList<>(model);
        list.setBackground(FIELD_BG);
        list.setForeground(TEXT);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        list.setSelectionBackground(new Color(99, 179, 237, 80));
        list.setSelectionForeground(TEXT);
        list.setFixedCellHeight(36);
        list.setBorder(new EmptyBorder(4, 8, 4, 8));
        return list;
    }

    private JScrollPane makeScrollPane(Component comp) {
        JScrollPane scroll = new JScrollPane(comp);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(55, 65, 81), 1));
        scroll.getViewport().setBackground(FIELD_BG);
        scroll.setPreferredSize(new Dimension(0, 240));
        return scroll;
    }

    private void styleTable(JTable table) {
        table.setBackground(FIELD_BG);
        table.setForeground(TEXT);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(34);
        table.setGridColor(new Color(55, 65, 81));
        table.setSelectionBackground(new Color(99, 179, 237, 80));
        table.setSelectionForeground(TEXT);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.getTableHeader().setBackground(PANEL_BG);
        table.getTableHeader().setForeground(SUBTEXT);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBorder(BorderFactory.createLineBorder(new Color(55, 65, 81)));
    }
}

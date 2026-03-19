import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    // ─── DATABASE CONFIG ──────────────────────────────────────────────────────
    private static final String DB_URL      = "jdbc:mysql://tramway.proxy.rlwy.net:18300/railway?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "LlFxoBQWbOIWLXhQEvTtSJqYRRjLykGj";

    // ─── GET CONNECTION ───────────────────────────────────────────────────────
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // ─── INITIALIZE TABLES ────────────────────────────────────────────────────
    public static void initializeDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS students (" +
                "  username VARCHAR(50) PRIMARY KEY," +
                "  password VARCHAR(100) NOT NULL," +
                "  full_name VARCHAR(100) NOT NULL," +
                "  email VARCHAR(100) NOT NULL" +
                ")"
            );

            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS study_groups (" +
                "  group_name VARCHAR(100) PRIMARY KEY," +
                "  subject VARCHAR(100) NOT NULL," +
                "  description TEXT," +
                "  creator_username VARCHAR(50) NOT NULL" +
                ")"
            );

            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS group_members (" +
                "  group_name VARCHAR(100) NOT NULL," +
                "  username VARCHAR(50) NOT NULL," +
                "  PRIMARY KEY (group_name, username)" +
                ")"
            );

            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS study_sessions (" +
                "  id INT AUTO_INCREMENT PRIMARY KEY," +
                "  group_name VARCHAR(100) NOT NULL," +
                "  topic VARCHAR(200) NOT NULL," +
                "  session_date VARCHAR(50) NOT NULL," +
                "  session_time VARCHAR(50) NOT NULL," +
                "  location VARCHAR(200) NOT NULL," +
                "  creator_username VARCHAR(50) DEFAULT \'\'" +
                ")"
            );

            System.out.println("Database initialized successfully.");

        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
        }
    }

    // ─── STUDENT METHODS ──────────────────────────────────────────────────────
    public static List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM students")) {

            while (rs.next()) {
                students.add(new Student(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("full_name"),
                    rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error loading students: " + e.getMessage());
        }
        return students;
    }

    public static void saveStudents(List<Student> students) {
        String sql = "INSERT IGNORE INTO students (username, password, full_name, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Student s : students) {
                ps.setString(1, s.getUsername());
                ps.setString(2, s.getPassword());
                ps.setString(3, s.getFullName());
                ps.setString(4, s.getEmail());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error saving students: " + e.getMessage());
        }
    }

    // ─── GROUP METHODS ────────────────────────────────────────────────────────
    public static List<Group> loadGroups() {
        List<Group> groups = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM study_groups")) {

            while (rs.next()) {
                Group g = new Group(
                    rs.getString("group_name"),
                    rs.getString("subject"),
                    rs.getString("description"),
                    rs.getString("creator_username")
                );
                loadMembers(conn, g);
                loadSessions(conn, g);
                groups.add(g);
            }
        } catch (SQLException e) {
            System.err.println("Error loading groups: " + e.getMessage());
        }
        return groups;
    }

    private static void loadMembers(Connection conn, Group g) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT username FROM group_members WHERE group_name = ?")) {
            ps.setString(1, g.getGroupName());
            ResultSet rs = ps.executeQuery();
            g.getMemberUsernames().clear();
            while (rs.next()) g.getMemberUsernames().add(rs.getString("username"));
        }
    }

    private static void loadSessions(Connection conn, Group g) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM study_sessions WHERE group_name = ?")) {
            ps.setString(1, g.getGroupName());
            ResultSet rs = ps.executeQuery();
            g.getSessions().clear();
            while (rs.next()) {
                g.getSessions().add(new StudySession(
                    rs.getString("topic"),
                    rs.getString("session_date"),
                    rs.getString("session_time"),
                    rs.getString("location"),
                    rs.getString("creator_username")
                ));
            }
        }
    }

    public static void saveGroups(List<Group> groups) {
        try (Connection conn = getConnection()) {
            for (Group g : groups) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO study_groups (group_name, subject, description, creator_username) " +
                        "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE subject=VALUES(subject), description=VALUES(description)")) {
                    ps.setString(1, g.getGroupName());
                    ps.setString(2, g.getSubject());
                    ps.setString(3, g.getDescription());
                    ps.setString(4, g.getCreatorUsername());
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM group_members WHERE group_name = ?")) {
                    ps.setString(1, g.getGroupName()); ps.executeUpdate();
                }
                for (String member : g.getMemberUsernames()) {
                    try (PreparedStatement ps = conn.prepareStatement(
                            "INSERT IGNORE INTO group_members (group_name, username) VALUES (?, ?)")) {
                        ps.setString(1, g.getGroupName());
                        ps.setString(2, member);
                        ps.executeUpdate();
                    }
                }
                try (PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM study_sessions WHERE group_name = ?")) {
                    ps.setString(1, g.getGroupName()); ps.executeUpdate();
                }
                for (StudySession s : g.getSessions()) {
                    try (PreparedStatement ps = conn.prepareStatement(
                            "INSERT INTO study_sessions (group_name, topic, session_date, session_time, location, creator_username) VALUES (?, ?, ?, ?, ?, ?)")) {
                        ps.setString(1, g.getGroupName());
                        ps.setString(2, s.getTopic());
                        ps.setString(3, s.getDate());
                        ps.setString(4, s.getTime());
                        ps.setString(5, s.getLocation());
                        ps.setString(6, s.getCreatorUsername());
                        ps.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving groups: " + e.getMessage());
        }
    }

    public static void deleteStudent(String username) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM students WHERE username = ?")) {
            ps.setString(1, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting student: " + e.getMessage());
        }
    }

    public static void deleteGroup(Group group) {
        try (Connection conn = getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM study_sessions WHERE group_name = ?")) {
                ps.setString(1, group.getGroupName()); ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM group_members WHERE group_name = ?")) {
                ps.setString(1, group.getGroupName()); ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM study_groups WHERE group_name = ?")) {
                ps.setString(1, group.getGroupName()); ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error deleting group: " + e.getMessage());
        }
    }
}
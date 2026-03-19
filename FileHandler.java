import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final String STUDENTS_FILE = "students.dat";
    private static final String GROUPS_FILE = "groups.dat";

    @SuppressWarnings("unchecked")
    public static List<Student> loadStudents() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(STUDENTS_FILE))) {
            return (List<Student>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static void saveStudents(List<Student> students) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(STUDENTS_FILE))) {
            oos.writeObject(students);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Group> loadGroups() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(GROUPS_FILE))) {
            return (List<Group>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static void saveGroups(List<Group> groups) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(GROUPS_FILE))) {
            oos.writeObject(groups);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Group implements Serializable {
    private static final long serialVersionUID = 1L;

    private String groupName;
    private String subject;
    private String description;
    private String creatorUsername;
    private List<String> memberUsernames;
    private List<StudySession> sessions;

    public Group(String groupName, String subject, String description, String creatorUsername) {
        this.groupName = groupName;
        this.subject = subject;
        this.description = description;
        this.creatorUsername = creatorUsername;
        this.memberUsernames = new ArrayList<>();
        this.sessions = new ArrayList<>();
        this.memberUsernames.add(creatorUsername);
    }

    public String getGroupName() { return groupName; }
    public String getSubject() { return subject; }
    public String getDescription() { return description; }
    public String getCreatorUsername() { return creatorUsername; }
    public List<String> getMemberUsernames() { return memberUsernames; }
    public List<StudySession> getSessions() { return sessions; }

    public boolean addMember(String username) {
        if (!memberUsernames.contains(username)) {
            memberUsernames.add(username);
            return true;
        }
        return false;
    }

    public void addSession(StudySession session) {
        sessions.add(session);
    }

    @Override
    public String toString() {
        return groupName + " [" + subject + "]";
    }
}

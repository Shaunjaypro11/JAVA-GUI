import java.io.Serializable;

public class StudySession implements Serializable {
    private static final long serialVersionUID = 2L;

    private String topic;
    private String date;
    private String time;
    private String location;
    private String creatorUsername;

    public StudySession(String topic, String date, String time, String location, String creatorUsername) {
        this.topic = topic;
        this.date = date;
        this.time = time;
        this.location = location;
        this.creatorUsername = creatorUsername;
    }

    public String getTopic() { return topic; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getLocation() { return location; }
    public String getCreatorUsername() { return creatorUsername != null ? creatorUsername : ""; }

    @Override
    public String toString() {
        return topic + " | " + date + " " + time + " | " + location;
    }
}
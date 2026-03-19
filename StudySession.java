import java.io.Serializable;

public class StudySession implements Serializable {
    private static final long serialVersionUID = 1L;

    private String topic;
    private String date;
    private String time;
    private String location;

    public StudySession(String topic, String date, String time, String location) {
        this.topic = topic;
        this.date = date;
        this.time = time;
        this.location = location;
    }

    public String getTopic() { return topic; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getLocation() { return location; }

    @Override
    public String toString() {
        return topic + " | " + date + " " + time + " | " + location;
    }
}

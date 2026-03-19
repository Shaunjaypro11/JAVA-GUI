# Study Group Organizer System
## Group 3 – Java Swing Application

### Files Overview
| File | Purpose |
|------|---------|
| `Main.java` | Entry point |
| `Student.java` | Student model (OOP) |
| `Group.java` | Group model (OOP) |
| `StudySession.java` | Session model (OOP) |
| `FileHandler.java` | File I/O (saves/loads data) |
| `LoginFrame.java` | Login & registration UI |
| `RegisterFrame.java` | New account creation UI |
| `DashboardFrame.java` | Main dashboard with all features |

### How to Compile & Run

1. Make sure Java JDK is installed (Java 8 or higher)
2. Place all `.java` files in the same folder
3. Open a terminal/command prompt in that folder
4. Run:

```bash
javac *.java
java Main
```

### Features
- ✅ Student Registration / Login
- ✅ Create Study Group (name, subject, description)
- ✅ Join Study Group
- ✅ View Group Members (table view)
- ✅ Schedule Study Sessions (topic, date, time, location)
- ✅ Persistent data (saved to `.dat` files)

### Data Persistence
Data is saved automatically to:
- `students.dat` — registered accounts
- `groups.dat` — groups and sessions

These files are created in the same directory you run the program from.

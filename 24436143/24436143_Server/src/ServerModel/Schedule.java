package ServerModel;
import java.util.HashMap;

/**
 * cannot corrupt the HashMap or observe a half-updated state (e.g. a check-then-put
 * race in addLecture where two clients book the same slot simultaneously).
 */
public class Schedule {
    // HashMap to store lecture slots
    // Key: The day + timeslot of the lecture. E.g. "Monday-09:00-10:00"
    // Value: The Lecture object with lecture details
    private final HashMap<String, Lecture> lectureSlots;

    public Schedule() {
        lectureSlots = new HashMap<>();
    }


 // * Every method that reads or mutates lectureSlots is synchronized
 // * on the Schedule instance. This guarantees that concurrent ClientHandler threads

    public synchronized HashMap<String, ServerModel.Lecture> getLectureSlots() {
        return new HashMap<>(lectureSlots);
    }

    public synchronized boolean addLecture(Lecture lecture) {
        String uniqueKey = lecture.getScheduleKey();

        // Check-then-put must be atomic so two threads can't both see the slot as
        // free and each insert their lecture
        if (!lectureSlots.containsKey(uniqueKey)) {
            lectureSlots.put(uniqueKey, lecture);
            return true;
        } else {
            return false;
        }
    }

    public synchronized Lecture getLecture(String scheduleKey) {
        // Gets lecture at specified day + time
        return lectureSlots.get(scheduleKey);
    }

    public synchronized boolean removeLecture(Lecture lecture) {
        String uniqueKey = lecture.getScheduleKey();

        if(!lectureSlots.containsKey(uniqueKey)) {
            return false;
        } else {
            lectureSlots.remove(uniqueKey);
            return true;
        }
    }
}
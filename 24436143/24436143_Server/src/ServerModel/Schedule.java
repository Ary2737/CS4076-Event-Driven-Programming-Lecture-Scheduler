package ServerModel;
import java.util.HashMap;

public class Schedule {
    // HashMap to store lecture slots
    // Key: The day + timeslot of the lecture. E.g. "Monday-09:00"
    // Value: The Lecture object with lecture details
    private HashMap<String, Lecture> lectureSlots;

    public Schedule() {
        lectureSlots = new HashMap<>();
    }


    public HashMap<String, ServerModel.Lecture> getLectureSlots() {
        return new HashMap<>(lectureSlots);
    }

    public boolean addLecture(Lecture lecture) {
        String uniqueKey = lecture.getScheduleKey();

        // Check if the slot is free
        // If free: add lecture + return true
        // If not: return false
        if (!lectureSlots.containsKey(uniqueKey)) {
            lectureSlots.put(uniqueKey, lecture);
            return true;
        } else {
            return false;
        }
    }

    public Lecture getLecture(String scheduleKey) {
        // Gets lecture at specified day + time
        return lectureSlots.get(scheduleKey);
    }

    public boolean removeLecture(Lecture lecture) {
        String uniqueKey = lecture.getScheduleKey();

        if(!lectureSlots.containsKey(uniqueKey)) {
            return false;
        } else {
            lectureSlots.remove(uniqueKey);
            return true;
        }
    }
}
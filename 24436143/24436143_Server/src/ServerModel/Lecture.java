package ServerModel;

/**
 * This is a class which is part of the Server Model
 * This represents the lecture slot itself with details such as roomCode , timeSlot etc.
 */

public class Lecture {
    private String timeSlot;
    private String dayOfWeek;
    private String moduleCode;
    private String roomCode;


    public Lecture(String dayOfWeek, String timeSlot, String moduleCode, String roomCode) {
        this.dayOfWeek = dayOfWeek;
        this.timeSlot = timeSlot;
        this.moduleCode = moduleCode;
        this.roomCode = roomCode;
    }

    // Getters
    public String getTimeSlot() { return timeSlot; }
    public String getDayOfWeek() { return dayOfWeek; }
    public String getModuleCode() { return moduleCode; }
    public String getRoomCode() { return roomCode; }

    // Setters
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public void setModuleCode(String moduleCode) { this.moduleCode = moduleCode; }
    public void setRoomCode(String roomCode) { this.roomCode = roomCode; }

    // Helper method to generate a unique key for this specific slot
    public String getScheduleKey() {
        return dayOfWeek + "-" + timeSlot;
    }

}


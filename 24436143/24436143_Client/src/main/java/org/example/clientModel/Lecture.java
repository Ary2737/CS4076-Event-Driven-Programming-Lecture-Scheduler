package org.example.clientModel;

public class Lecture {
    private String dayOfWeek;
    private String timeSlot;
    private String moduleCode;
    private String roomCode;

    public Lecture(String dayOfWeek, String timeSlot, String moduleCode, String roomCode) {
        this.dayOfWeek = dayOfWeek;
        this.timeSlot = timeSlot;
        this.moduleCode = moduleCode;
        this.roomCode = roomCode;
    }

    public String getDayOfWeek() { return dayOfWeek; }
    public String getTimeSlot() { return timeSlot; }
    public String getModuleCode() { return moduleCode; }
    public String getRoomCode() { return roomCode; }

    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }
    public void setModuleCode(String moduleCode) { this.moduleCode = moduleCode; }
    public void setRoomCode(String roomCode) { this.roomCode = roomCode; }
}
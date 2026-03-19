package ServerController;
import ServerModel.Schedule;
import ServerModel.Lecture;


import java.util.HashMap;
import java.lang.StringBuilder;


/**
 * This class focuses on processing the Strings sent between the Client GUI and Console Server
 * This class will implement the logic for the user's options
 * It will return a String to client outlining status of the request
 */

public class ServerController {
    // Schedule of lectures for Schedule
    private Schedule timeTableSlots;

    public ServerController() {
        timeTableSlots = new Schedule();
    }


    /**
     * This method processes the client's string and returns a suitable string back.
     * Message Format: ACTION|(DAY)|(TIMESLOT)|(MODULE CODE)|(ROOM CODE)
     * E.g. ADD|Monday|09:00|CS101|C105
     */

    public String processClientRequest(String request) throws IncorrectActionException {
        // Splitting String to get relevant details
        // Splits String by "|". -1 splits the string into exactly 5 parts.
        String[] details = request.split("\\|",-1);
        String action = details[0];


        if(action.equals("ADD")) {
            // Form new Lecture object and populate with Strings from details array
            Lecture newLecture = new Lecture(details[1],details[2],details[3],details[4]);
            boolean success = timeTableSlots.addLecture(newLecture);

            if(success) return "SERVER: SUCCESS! Lecture added";
            else return "SERVER: FAILURE! Lecture could not be added. Clash found !";

        } else if(action.equals("REMOVE")) {

            // Create model of the Lecture to remove
            Lecture lectureToRemove = new Lecture(details[1],details[2],details[3],details[4]);
            boolean success = timeTableSlots.removeLecture(lectureToRemove);

            if(success) return "SERVER: SUCCESS! Lecture removed. " +
                               "Room: " + lectureToRemove.getRoomCode() +
                              " free, TimeSlot: " + lectureToRemove.getTimeSlot() + "free";
            else return "SERVER: FAILURE! Lecture doesn't already exist";

        } else if(action.equals("DISPLAY")) {

            // Get Lectures currently in HashMap
            var scheduledLectures = timeTableSlots.getLectureSlots().values();

            // Check if there are lecture slots in the schedule
            if(scheduledLectures.isEmpty()) {
                return "SCHEDULE_EMPTY|No lectures scheduled yet.";

            }

            // Creating StringBuilder to format the display output string
            // ':' = Divides lecture objects
            // ',' = Divides columns representing attributes of lecture

            StringBuilder scheduleData= new StringBuilder("LECTURE_DATA|");


            // Add lectures to formatted StringBuilder
            for(Lecture l: scheduledLectures) {
                // Append lecture details
                scheduleData.append(l.getDayOfWeek()).append(",");
                scheduleData.append(l.getTimeSlot()).append(",");
                scheduleData.append(l.getModuleCode()).append(",");
                scheduleData.append(l.getRoomCode()).append(",");

                // Append divider of lecture slots
                scheduleData.append(":");
            }

            return "SERVER: Success !Displaying schedule: " + scheduleData;

        } else if(action.equals("QUIT")) {
            System.out.println("Client requested to end server connection....");

            // Message to confirm the closure of the server connection
            return "SERVER: Connection with client has ended. Goodbye";
        }
        else {
            // If all else fails throw our own custom exception (if action is undefined)
            throw new IncorrectActionException("The server cannot process this action" + action);
        }

    }

}

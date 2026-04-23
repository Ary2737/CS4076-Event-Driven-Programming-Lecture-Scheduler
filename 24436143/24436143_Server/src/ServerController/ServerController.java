package ServerController;
import java.util.Iterator;
import java.util.concurrent.ForkJoinPool;

import ServerModel.Lecture;
import ServerModel.Schedule;


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
     * E.g. ADD|Monday|09:00-10:00|CS101|C105
     */

    public String processClientRequest(String request) throws IncorrectActionException {
        // Splitting String to get relevant details
        // Splits String by "|". -1 splits the string into exactly 5 parts.
        String[] details = request.split("\\|",-1);
        String action = details[0];

        // Checking if the request has all 5 parts before trying to read them!
        if ((action.equals("ADD") || action.equals("REMOVE")) && details.length < 5) {
            return "SERVER: ERROR! Missing lecture details. Please fill all fields.";
        }

        switch(action) {
            case "ADD":

                if (details.length < 5) {
                    return "ERROR|Missing lecture details";
                }
                // Form new Lecture object and populate with Strings from details array
                Lecture newLecture = new Lecture(details[1],details[2],details[3],details[4]);
                boolean addSuccess = timeTableSlots.addLecture(newLecture);


                if(addSuccess) return "SUCCESS|Lecture added";
                else return "ERROR|Clash with existing lecture found !";

            case "REMOVE":
                if (details.length < 5) {
                    return "ERROR|Missing lecture details";
                }

                Lecture lectureToRemove = new Lecture(details[1], details[2], details[3], details[4]);
                boolean removeSuccess = timeTableSlots.removeLecture(lectureToRemove);

                if (removeSuccess) return "SUCCESS|Lecture removed";
                else return "ERROR|Lecture not found";

            case "DISPLAY":

                var scheduledLectures = timeTableSlots.getLectureSlots().values();

                if (scheduledLectures.isEmpty()) {
                    return "SCHEDULE_EMPTY";
                }

                StringBuilder sb = new StringBuilder("LECTURE_DATA|");

                Iterator<Lecture> iterator = scheduledLectures.iterator();
                while (iterator.hasNext()) {
                    Lecture l = iterator.next();
                    sb.append(l.getDayOfWeek()).append(",")
                            .append(l.getTimeSlot()).append(",")
                            .append(l.getModuleCode()).append(",")
                            .append(l.getRoomCode()).append(";");
                }

                // remove trailing record separator
                sb.setLength(sb.length() - 1);

                return sb.toString();

            case "EARLY_LECTURES":
                // Divide-and-conquer: one ForkJoin subtask per weekday shifts
                // that day's lectures toward the earliest available slots.
                // invoke() blocks until all five day-tasks have joined.
                ForkJoinPool.commonPool().invoke(new EarlyLecturesAction(timeTableSlots));
                return "SUCCESS|Lectures shifted to earliest available slots";

            case "QUIT":
                System.out.println("Client requested to end server connection....");
                return "SUCCESS|Connection closed";

            default:
                throw new IncorrectActionException("Unknown action: " + action);
        }
    }

}

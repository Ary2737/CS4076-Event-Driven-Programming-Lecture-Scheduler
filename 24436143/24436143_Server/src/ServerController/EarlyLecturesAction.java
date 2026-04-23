package ServerController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

import ServerModel.Lecture;
import ServerModel.Schedule;

/**
 * Fork-Join worker algorithm for the 'Early Lectures' feature.
 * This feature shifts lectures towards free morning slots.
 *
 * The top-level action forks one child per weekday (Mon–Fri), so each day is
 * compacted on its own thread in parallel. Each leaf only touches its own day.
 */
public class EarlyLecturesAction extends RecursiveAction {

    // Ordered list of valid timeslots, earliest first.
    private static final List<String> TIME_SLOTS = List.of(
            "09:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00",
            "13:00-14:00", "14:00-15:00", "15:00-16:00", "16:00-17:00",
            "17:00-18:00"
    );

    private static final List<String> WEEKDAYS = List.of(
            "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"
    );

    private final Schedule schedule;

    // null = top-level action;
    // non-null = leaf for that day
    private final String day;

    /** Top-level action: split across weekdays. */
    public EarlyLecturesAction(Schedule schedule) {
        this(schedule, null);
    }

    private EarlyLecturesAction(Schedule schedule, String day) {
        this.schedule = schedule;
        this.day = day;
    }

    @Override
    protected void compute() {
        if (day == null) {
            // Divide: one subtask per weekday.
            // InvokeAll forks all but the last,
            // runs the last on the current thread, then joins.
            List<EarlyLecturesAction> subtasks = new ArrayList<>(WEEKDAYS.size());
            for (String d : WEEKDAYS) {
                subtasks.add(new EarlyLecturesAction(schedule, d));
            }
            invokeAll(subtasks);
        } else {
            shiftDay();
        }
    }

    /**
     * Compact this day's lectures toward 09:00, preserving their existing
     * relative order. Atomic against other mutations to the same Schedule.
     */
    private void shiftDay() {
        synchronized (schedule) {
            var snapshot = schedule.getLectureSlots();

            List<Lecture> dayLectures = new ArrayList<>();
            for (String slot : TIME_SLOTS) {
                Lecture l = snapshot.get(day + "-" + slot);
                if (l != null) dayLectures.add(l);
            }

            if (dayLectures.isEmpty()) return;

            // Clear this day's slots, then re-insert packed from 09:00 upward.
            for (Lecture l : dayLectures) {
                schedule.removeLecture(l);
            }
            for (int i = 0; i < dayLectures.size(); i++) {
                Lecture original = dayLectures.get(i);
                Lecture shifted = new Lecture(
                        day,
                        TIME_SLOTS.get(i),
                        original.getModuleCode(),
                        original.getRoomCode()
                );
                schedule.addLecture(shifted);
            }

            System.out.println("[ForkJoin " + Thread.currentThread().getName()
                    + "] " + day + ": shifted " + dayLectures.size() + " lecture(s) early");
        }
    }
}

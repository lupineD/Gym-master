import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class TimetableTest1 {

    @Test
    void testMultipleSessionsSameTime() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петрова", "Ольга", "Сергеевна");

        Group group1 = new Group("Йога", Age.ADULT, 60);
        Group group2 = new Group("Пилатес", Age.ADULT, 60);

        // Два занятия в одно время
        TrainingSession session1 = new TrainingSession(group1, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(18, 0));
        TrainingSession session2 = new TrainingSession(group2, coach2,
                DayOfWeek.MONDAY, new TimeOfDay(18, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        // Проверяем, что в одно время вернулось два занятия
        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(18, 0));

        assertEquals(2, sessions.size());

        // Проверяем, что оба занятия присутствуют
        Set<String> groupTitles = new HashSet<>();
        for (TrainingSession session : sessions) {
            groupTitles.add(session.getGroup().getTitle());
        }

        assertTrue(groupTitles.contains("Йога"));
        assertTrue(groupTitles.contains("Пилатес"));
    }

    @Test
    void testMixedSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Сидоров", "Алексей", "Петрович");

        Group group1 = new Group("Детская гимнастика", Age.CHILD, 45);
        Group group2 = new Group("Взрослая гимнастика", Age.ADULT, 90);

        // Несколько занятий в разное время
        TrainingSession morningSession1 = new TrainingSession(group1, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0));
        TrainingSession morningSession2 = new TrainingSession(group2, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)); // В то же время!
        TrainingSession eveningSession = new TrainingSession(group2, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(19, 0));

        timetable.addNewTrainingSession(morningSession1);
        timetable.addNewTrainingSession(morningSession2);
        timetable.addNewTrainingSession(eveningSession);

        // Проверяем общее количество занятий в среду
        List<TrainingSession> wednesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.WEDNESDAY);
        assertEquals(3, wednesdaySessions.size());

        // Проверяем занятия в 10:00
        List<TrainingSession> morningSessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0));
        assertEquals(2, morningSessions.size());

        // Проверяем занятия в 19:00
        List<TrainingSession> eveningSessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.WEDNESDAY, new TimeOfDay(19, 0));
        assertEquals(1, eveningSessions.size());
    }
}
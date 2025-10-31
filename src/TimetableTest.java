import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        //Проверить, что за вторник не вернулось занятий
        assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());
        assertEquals(0, timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).size());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        // Проверить, что за вторник не вернулось занятий

        assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());

        List<TrainingSession> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);

        assertEquals(2, thursdaySessions.size());
        // Проверяем порядок: сначала 13:00, потом 20:00
        assertEquals(13, thursdaySessions.get(0).getTimeOfDay().getHours());
        assertEquals(20, thursdaySessions.get(1).getTimeOfDay().getHours());

        assertEquals(0, timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).size());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        //Проверить, что за понедельник в 14:00 не вернулось занятий

        assertEquals(1, timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0)).size());
        assertEquals(0, timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0)).size());
    }

    // 1 Тест на несколько занятий в одно и то же время
    @Test
    void testMultipleSessionsAtSameTime() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coach2 = new Coach("Петрова", "Ольга", "Ивановна");

        Group group1 = new Group("Акробатика для детей", Age.CHILD, 60);
        Group group2 = new Group("Гимнастика для детей", Age.CHILD, 60);

        // Два занятия в одно и то же время с разными группами и тренерами
        TrainingSession session1 = new TrainingSession(group1, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        TrainingSession session2 = new TrainingSession(group2, coach2,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        // Проверяем, что оба занятия возвращаются для этого времени
        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        assertEquals(2, sessions.size());

        // Проверяем, что оба занятия есть в списке за весь день
        List<TrainingSession> daySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(2, daySessions.size());
    }

    // 2 Тест на граничные случаи времени (полночь, конец дня)
    @Test
    void testBoundaryTimeCases() {
        Timetable timetable = new Timetable();

        Group group = new Group("Ночная йога", Age.ADULT, 90);
        Coach coach = new Coach("Сидоров", "Алексей", "Петрович");

        // Занятия в граничное время
        TrainingSession lateSession = new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(23, 30));
        TrainingSession earlySession = new TrainingSession(group, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(0, 15));
        TrainingSession middaySession = new TrainingSession(group, coach,
                DayOfWeek.SUNDAY, new TimeOfDay(12, 0));

        timetable.addNewTrainingSession(lateSession);
        timetable.addNewTrainingSession(earlySession);
        timetable.addNewTrainingSession(middaySession);

        // Проверяем корректность работы с граничными значениями времени
        assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.FRIDAY).size());
        assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.SATURDAY).size());
        assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.SUNDAY).size());

        // Проверяем конкретное время
        assertEquals(1, timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.FRIDAY, new TimeOfDay(23, 30)).size());
        assertEquals(0, timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.FRIDAY, new TimeOfDay(23, 0)).size());
    }

    // 3 Тест на порядок занятий при разном времени
    @Test
    void testChronologicalOrder() {
        Timetable timetable = new Timetable();

        Group group = new Group("Плавание", Age.ADULT, 45);
        Coach coach = new Coach("Иванов", "Дмитрий", "Викторович");

        // Добавляем занятия в разном порядке
        TrainingSession session3 = new TrainingSession(group, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(15, 30));
        TrainingSession session1 = new TrainingSession(group, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(9, 0));
        TrainingSession session4 = new TrainingSession(group, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 45));
        TrainingSession session2 = new TrainingSession(group, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(11, 15));

        // Добавляем не в хронологическом порядке
        timetable.addNewTrainingSession(session3);
        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session4);
        timetable.addNewTrainingSession(session2);

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDay(DayOfWeek.WEDNESDAY);

        // Проверяем, что занятия возвращаются в правильном хронологическом порядке
        assertEquals(4, sessions.size());
        assertEquals(new TimeOfDay(9, 0), sessions.get(0).getTimeOfDay());
        assertEquals(new TimeOfDay(11, 15), sessions.get(1).getTimeOfDay());
        assertEquals(new TimeOfDay(15, 30), sessions.get(2).getTimeOfDay());
        assertEquals(new TimeOfDay(18, 45), sessions.get(3).getTimeOfDay());
    }

}
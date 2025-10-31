package com.yandex.app.service;

import com.yandex.app.model.*;
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
    // Этот тест гарантирует, что пользователи всегда видят расписание в логичном,
    // упорядоченном виде, а не в том порядке, в котором данные были загружены в систему
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

        // Сравниваем по значениям времени, а не по объектам
        assertEquals(9, sessions.get(0).getTimeOfDay().getHours());
        assertEquals(0, sessions.get(0).getTimeOfDay().getMinutes());

        assertEquals(11, sessions.get(1).getTimeOfDay().getHours());
        assertEquals(15, sessions.get(1).getTimeOfDay().getMinutes());

        assertEquals(15, sessions.get(2).getTimeOfDay().getHours());
        assertEquals(30, sessions.get(2).getTimeOfDay().getMinutes());

        assertEquals(18, sessions.get(3).getTimeOfDay().getHours());
        assertEquals(45, sessions.get(3).getTimeOfDay().getMinutes());
    }

    // Тесты для нового метода getCountByCoaches:
    // 1 Тест на количество тренировок в неделю
    @Test
    void testGetCountByCoaches() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coach2 = new Coach("Петрова", "Ольга", "Ивановна");
        Coach coach3 = new Coach("Сидоров", "Алексей", "Петрович");

        Group group1 = new Group("Акробатика для детей", Age.CHILD, 60);
        Group group2 = new Group("Гимнастика", Age.ADULT, 90);
        Group group3 = new Group("Плавание", Age.ADULT, 45);

        // Тренер 1: 4 тренировки
        timetable.addNewTrainingSession(new TrainingSession(group1, coach1, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group1, coach1, DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group2, coach1, DayOfWeek.FRIDAY, new TimeOfDay(15, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group2, coach1, DayOfWeek.SATURDAY, new TimeOfDay(11, 0)));

        // Тренер 2: 2 тренировки
        timetable.addNewTrainingSession(new TrainingSession(group3, coach2, DayOfWeek.TUESDAY, new TimeOfDay(14, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group3, coach2, DayOfWeek.THURSDAY, new TimeOfDay(14, 0)));

        // Тренер 3: 3 тренировки
        timetable.addNewTrainingSession(new TrainingSession(group1, coach3, DayOfWeek.MONDAY, new TimeOfDay(16, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group2, coach3, DayOfWeek.WEDNESDAY, new TimeOfDay(16, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group3, coach3, DayOfWeek.FRIDAY, new TimeOfDay(16, 0)));

        Map<Coach, Integer> result = timetable.getCountByCoaches();

        // Проверяем порядок: coach1(4) -> coach3(3) -> coach2(2)
        List<Coach> coaches = new ArrayList<>(result.keySet());
        List<Integer> counts = new ArrayList<>(result.values());

        assertEquals(coach1, coaches.get(0));
        assertEquals(4, counts.get(0));

        assertEquals(coach3, coaches.get(1));
        assertEquals(3, counts.get(1));

        assertEquals(coach2, coaches.get(2));
        assertEquals(2, counts.get(2));
    }

    // Тесты для нового метода getCountByCoaches:
    // 2 поведение метода когда в расписании нет ни одного занятия.
    @Test
    void testGetCountByCoachesEmptyTimetable() {
        Timetable timetable = new Timetable();

        Map<Coach, Integer> result = timetable.getCountByCoaches();

        assertTrue(result.isEmpty());
    }

    // Тесты для нового метода getCountByCoaches:
    // 3 проверяет крайний случай, когда в расписании есть тренировки только у одного тренера.
    @Test
    void testGetCountByCoachesSingleCoach() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Иванов", "Дмитрий", "Викторович");
        Group group = new Group("Йога", Age.ADULT, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));

        Map<Coach, Integer> result = timetable.getCountByCoaches();

        assertEquals(1, result.size());
        assertEquals(2, result.get(coach));
    }
}
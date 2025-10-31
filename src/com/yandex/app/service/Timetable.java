package com.yandex.app.service;

import com.yandex.app.model.Coach;
import com.yandex.app.model.DayOfWeek;
import com.yandex.app.model.TimeOfDay;
import com.yandex.app.model.TrainingSession;

import java.util.*;

public class Timetable {
    private final Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        this.timetable = new HashMap<>();

        // Инициализируем все дни недели
        for (DayOfWeek day : DayOfWeek.values()) {
            timetable.put(day, new TreeMap<>(new TimeOfDayComparator()));
        }
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        Map<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(day);

        // Если для данного времени еще нет списка занятий, создаем его
        if (!daySchedule.containsKey(time)) {
            daySchedule.put(time, new ArrayList<>());
        }

        // Добавляем занятие в список для данного времени
        // Теперь в одно время может быть несколько тренировок
        daySchedule.get(time).add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        Map<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        List<TrainingSession> result = new ArrayList<>();

        // Собираем все занятия за день в хронологическом порядке
        for (Map.Entry<TimeOfDay, List<TrainingSession>> entry : daySchedule.entrySet()) {
            result.addAll(entry.getValue());
        }

        return Collections.unmodifiableList(result);
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        Map<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        List<TrainingSession> sessions = daySchedule.get(timeOfDay);

        // Возвращаем неизменяемый список
        return sessions != null ?
                Collections.unmodifiableList(sessions) :
                Collections.emptyList();
    }

    public Map<Coach, Integer> getCountByCoaches() {
        Map<Coach, Integer> coachCounts = new HashMap<>();

        // Собираем статистику по всем дням недели
        for (DayOfWeek day : DayOfWeek.values()) {
            Map<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(day);
            for (List<TrainingSession> sessions : daySchedule.values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    coachCounts.put(coach, coachCounts.getOrDefault(coach, 0) + 1);
                }
            }
        }

        // Сортируем по убыванию количества тренировок
        return CounterOfTrainings(coachCounts);
    }

    private Map<Coach, Integer> CounterOfTrainings(Map<Coach, Integer> map) {
        List<Map.Entry<Coach, Integer>> entries = new ArrayList<>(map.entrySet());

        // Сортируем по убыванию значения (количества тренировок)
        entries.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // Создаем LinkedHashMap для сохранения порядка
        Map<Coach, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Coach, Integer> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}

class TimeOfDayComparator implements Comparator<TimeOfDay> {
    @Override
    public int compare(TimeOfDay time1, TimeOfDay time2) {
        if (time1.getHours() != time2.getHours()) {
            return Integer.compare(time1.getHours(), time2.getHours());
        }
        return Integer.compare(time1.getMinutes(), time2.getMinutes());
    }
}
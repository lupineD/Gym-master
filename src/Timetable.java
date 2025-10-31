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

//    // Дополнительный метод для получения всех занятий по времени с группировкой
//    public Map<TimeOfDay, List<TrainingSession>> getTrainingSessionsByTimeForDay(DayOfWeek dayOfWeek) {
//        Map<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
//        // Возвращаем неизменяемую копию
//        Map<TimeOfDay, List<TrainingSession>> result = new TreeMap<>(new TimeOfDayComparator());
//        for (Map.Entry<TimeOfDay, List<TrainingSession>> entry : daySchedule.entrySet()) {
//            result.put(entry.getKey(), Collections.unmodifiableList(entry.getValue()));
//        }
//        return Collections.unmodifiableMap(result);
//    }
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
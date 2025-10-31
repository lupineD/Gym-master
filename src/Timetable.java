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
        daySchedule.get(time).add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        Map<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        List<TrainingSession> result = new ArrayList<>();

        // Собираем все занятия за день в хронологическом порядке
        for (List<TrainingSession> sessions : daySchedule.values()) {
            result.addAll(sessions);
        }

        return result;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        Map<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        List<TrainingSession> sessions = daySchedule.get(timeOfDay);

        // Возвращаем копию списка, чтобы избежать модификации исходных данных
        if (sessions != null) {
            return new ArrayList<>(sessions);  // возвращаем копию списка
        } else {
            return new ArrayList<>();  // возвращаем пустой список
        }
    }
}

// Компаратор для сравнения времени
class TimeOfDayComparator implements Comparator<TimeOfDay> {
    @Override
    public int compare(TimeOfDay time1, TimeOfDay time2) {
        if (time1.getHours() != time2.getHours()) {
            return Integer.compare(time1.getHours(), time2.getHours());
        }
        return Integer.compare(time1.getMinutes(), time2.getMinutes());
    }
}

//аааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааа
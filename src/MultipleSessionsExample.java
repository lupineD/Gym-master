import java.util.*;

public class MultipleSessionsExample {
    public static void main(String[] args) {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Петр", "Сергеевич");
        Coach coach2 = new Coach("Сидорова", "Мария", "Ивановна");

        // Две разные группы начинают тренировки в одно время
        Group group1 = new Group("Йога для начинающих", Age.ADULT, 60);
        Group group2 = new Group("Пилатес", Age.ADULT, 60);

        // Оба занятия в понедельник в 18:00
        TrainingSession yogaSession = new TrainingSession(group1, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(18, 0));
        TrainingSession pilatesSession = new TrainingSession(group2, coach2,
                DayOfWeek.MONDAY, new TimeOfDay(18, 0));

        timetable.addNewTrainingSession(yogaSession);
        timetable.addNewTrainingSession(pilatesSession);

        // Получаем все занятия в понедельник в 18:00
        List<TrainingSession> sessionsAt6pm = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(18, 0));

        System.out.println("Занятий в 18:00: " + sessionsAt6pm.size()); // Выведет: 2
        for (TrainingSession session : sessionsAt6pm) {
            System.out.println("- " + session.getGroup().getTitle() +
                    " с тренером " + session.getCoach().getSurname());
        }
    }
}

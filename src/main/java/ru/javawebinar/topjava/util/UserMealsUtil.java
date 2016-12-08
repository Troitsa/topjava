package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        //1. Мар для собирание милсов по дням
        Map<LocalDate, List<UserMeal>> sumByDay = new TreeMap<>();

        //2. Собрать в этом сете имеющиеся дни в mealList
        Set<LocalDate> days = new LinkedHashSet<>();

        for (UserMeal meal : mealList) {
            days.add(meal.getDateTime().toLocalDate()); // кладу в days дни
        }

        for (LocalDate day : days) {
            List<UserMeal> userMeals = new ArrayList<>(); // 3. промежуточная коллекция UserMeal
            for (UserMeal m : mealList){
                if (day.equals(m.getDateTime().toLocalDate())){
                    userMeals.add(m); // в userMeals кладу отсортированные по дням итемы
                }
            }
            sumByDay.put(day, userMeals); // получаю итем sumByDay от day и userMeals
        }

        //4. Результирующий лист UserMealWithExceed
        List<UserMealWithExceed> mealWithExceeds = new ArrayList<>();

        for (Map.Entry<LocalDate, List<UserMeal>> entry : sumByDay.entrySet()) {

            int amount = 0; // счетчик каллории за день

            for (UserMeal m : entry.getValue()) {
                amount += m.getCalories();
            }

            if (amount >= caloriesPerDay)
            {
                for (UserMeal m : entry.getValue()) {
                    if (TimeUtil.isBetween(m.getDateTime().toLocalTime(),startTime, endTime)){
                        mealWithExceeds.add(
                                new UserMealWithExceed( m.getDateTime(), m.getDescription(), m.getCalories(), true)
                        );
                    }
                }
            }
            else {
                for (UserMeal m : entry.getValue()) {
                    if (TimeUtil.isBetween(m.getDateTime().toLocalTime(),startTime, endTime)){
                        mealWithExceeds.add(
                                new UserMealWithExceed( m.getDateTime(), m.getDescription(), m.getCalories(), false)
                        );
                    }
                }
            }
        }

        return mealWithExceeds;
    }
}

package com.example.samsungschoolproject.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class CalendarUtils {
    public static LocalDate selectedDate;

    private static HashMap<String, String> parseMonth = new HashMap<String, String>() {{
            put("января", "Январь");
            put("февраля", "Февраль");
            put("марта", "Март");
            put("апреля", "Апрель");
            put("мая", "Май");
            put("июня", "Июнь");
            put("июля", "Июль");
            put("августа", "Август");
            put("сентября", "Сентябрь");
            put("октября", "Октябрь");
            put("ноября", "Ноябрь");
            put("декабря", "Декабрь");
    }};

    public static String monthYearFromDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        String[] unparsedDate = date.format(formatter).split(" ");
        return parseMonth.get(unparsedDate[0]) + " " + unparsedDate[1];
    }

    public static ArrayList<LocalDate> daysInMonthArray(LocalDate date){
        ArrayList<LocalDate> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstOfMonth = CalendarUtils.selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 2; i <= 41; i++){
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek){
                daysInMonthArray.add(null);
            }
            else{
                daysInMonthArray.add(LocalDate.of(selectedDate.getYear(), selectedDate.getMonth(), i-dayOfWeek));
            }
        }

        return daysInMonthArray;
    }

    public static ArrayList<LocalDate> daysInWeekArray(LocalDate date){
        ArrayList<LocalDate> days = new ArrayList<>();
        LocalDate current = mondayForDate(selectedDate);
        LocalDate endDate = current.plusWeeks(1);

        while (current.isBefore(endDate)){
            days.add(current);
            current = current.plusDays(1);
        }

        return days;
    }

    private static LocalDate mondayForDate(LocalDate current) {
        LocalDate oneWeekAgo = current.minusWeeks(1);

        while (current.isAfter(oneWeekAgo)){
            if (current.getDayOfWeek() == DayOfWeek.MONDAY){
                return current;
            }
            current = current.minusDays(1);
        }

        return null;
    }
}

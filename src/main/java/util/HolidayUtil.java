package util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import static java.time.temporal.TemporalAdjusters.firstInMonth;

public class HolidayUtil {

    /*
    * Holidays in the calendar:
    *
    * Labor Day (First Monday in September)
    * Independence Day (July 4th
    *
     */
    public static boolean isHoliday(LocalDate date) {
        // Labor Day
        if(date.isEqual(date.with(firstInMonth(DayOfWeek.MONDAY))) && date.getMonth() == Month.SEPTEMBER) {
            return true;
        }

        // July 4th
        if(date.getMonth() == Month.JULY && date.getDayOfMonth() == 4) {
            return true;
        }

        // July 4th (observed)
        LocalDate datePrior = date.minusDays(1);
        LocalDate dateAfter = date.plusDays(1);
        if((dateAfter.getDayOfMonth() == 4 || datePrior.getDayOfMonth() == 4)
                && (dateAfter.getMonth() == Month.JULY || datePrior.getMonth() == Month.JULY)) {
            if(dateAfter.getDayOfWeek() == DayOfWeek.SATURDAY || datePrior.getDayOfWeek() == DayOfWeek.SUNDAY) {
                return true;
            }
        }

        return false;
    }
}

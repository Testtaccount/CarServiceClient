package am.gsoft.carserviceclient.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.Months;

public final class DateUtils {
    //Allowed date formats
    public enum DateType {
        DMY,
        MDY,
        YMD,
        SQL,
        DMY_HM,
        DMY_HMS,
        MDY_HM,
        MDY_HMS,
        YMD_HM,
        YMD_HMS,
        SQL_HM,
        SQL_HMS,
    }

    /**
     * Return string format from DataType
     * @param format    DataType format
     * @return          String representing DataType format
     */
    public static String getDateFormat(DateType format) {
        String str_format;
        switch (format) {
            case DMY:
                str_format = "dd/MM/yyyy";
                break;
            case MDY:
                str_format = "MM/dd/yyyy";
                break;
            case YMD:
                str_format = "MM/dd/yyyy";
                break;
            case SQL:
                str_format = "yyyy-MM-dd";
                break;
            case DMY_HM:
                str_format = "dd/MM/yyyy HH:mm";
                break;
            case DMY_HMS:
                str_format = "dd/MM/yyyy HH:mm:ss";
                break;
            case MDY_HM:
                str_format = "MM/dd/yyyy HH:mm";
                break;
            case MDY_HMS:
                str_format = "MM/dd/yyyy HH:mm:ss";
                break;
            case YMD_HM:
                str_format = "MM/dd/yyyy HH:mm";
                break;
            case YMD_HMS:
                str_format = "MM/dd/yyyy HH:mm:ss";
                break;
            case SQL_HM:
                str_format = "yyyy-MM-dd HH:mm";
                break;
            case SQL_HMS:
                str_format = "yyyy-MM-dd HH:mm:ss";
                break;
            default:
                str_format = "";
        }

        return str_format;
    }


    public static String dateToString(Date data, String pattern) {
        return new SimpleDateFormat(pattern).format(data);
    }

    public static long dateToLong(Date date) {
        return date.getTime();
    }

    public static Date stringToDate(String text, String pattern)
        throws ParseException {
        return new SimpleDateFormat(pattern).parse(text);
    }

    public static Date longToDate(long millSec) {
        return new Date(millSec);
    }

    public static String longToString(long millSec, String pattern) {
        Date date = longToDate(millSec);
        String text = dateToString(date, pattern);
        return text;
    }

    public static long stringToLong(String text, String pattern)  throws ParseException {
        Date date = stringToDate(text, pattern);
        long millSec = dateToLong(date);
        return millSec;
    }


    public static String formatDateToLongStyle(Date date) {
        DateFormat format = SimpleDateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
        return format.format(date);
    }

    public static int monthsBetweenDates(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        int diff = 0;
        if (c2.after(c1)) {
            while (c2.after(c1)) {
                c1.add(Calendar.MONTH, 1);
                if (c2.after(c1)) {
                    diff++;
                }
            }
        } else if (c2.before(c1)) {
            while (c2.before(c1)) {
                c1.add(Calendar.MONTH, -1);
                if (c1.before(c2)) {
                    diff--;
                }
            }
        }
        Logger.d("testt","monthsBetweenDates: "+diff);
        return diff;
    }

    public static int monthsBetweenDates1(Date startDate, Date endDate){

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);

        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        DateTime startDateTime = new DateTime(start.get(Calendar.YEAR), start.get(Calendar.MONTH), start.get(Calendar.DAY_OF_MONTH), 0, 0, 0, 0);
        DateTime endDateTime = new DateTime(end.get(Calendar.YEAR), end.get(Calendar.MONTH), end.get(Calendar.DAY_OF_MONTH), 0, 0, 0, 0);

        Months months = Months.monthsBetween(startDateTime, endDateTime);

        int monthsInYear = months.getMonths();

        Logger.d("testt","monthsBetweenDates(1): " + monthsInYear);

        return  monthsInYear;

    }

    public static int monthsBetweenDates2(Date startDate, Date endDate){

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);

        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        int monthsBetween = 0;
        int dateDiff = end.get(Calendar.DAY_OF_MONTH)-start.get(Calendar.DAY_OF_MONTH);

        if(dateDiff<0) {
            int borrrow = end.getActualMaximum(Calendar.DAY_OF_MONTH);
            dateDiff = (end.get(Calendar.DAY_OF_MONTH)+borrrow)-start.get(Calendar.DAY_OF_MONTH);
            monthsBetween--;

            if(dateDiff>0) {
                monthsBetween++;
            }
        }
        else {
            monthsBetween++;
        }
        monthsBetween += end.get(Calendar.MONTH)-start.get(Calendar.MONTH);
        monthsBetween  += (end.get(Calendar.YEAR)-start.get(Calendar.YEAR))*12;

        Logger.d("testt","monthsBetweenDates(2): "+monthsBetween);

        return monthsBetween;
    }

    public static int getDateDifferenceInDDMMYYYY(Date from, Date to) {
        Calendar fromDate=Calendar.getInstance();
        Calendar toDate=Calendar.getInstance();
        fromDate.setTime(from);
        toDate.setTime(to);
        int increment = 0;
        int year,month,day;
        System.out.println(fromDate.getActualMaximum(Calendar.DAY_OF_MONTH));
        if (fromDate.get(Calendar.DAY_OF_MONTH) > toDate.get(Calendar.DAY_OF_MONTH)) {
            increment =fromDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        System.out.println("increment"+increment);
// DAY CALCULATION
        if (increment != 0) {
            day = (toDate.get(Calendar.DAY_OF_MONTH) + increment) - fromDate.get(Calendar.DAY_OF_MONTH);
            increment = 1;
        } else {
            day = toDate.get(Calendar.DAY_OF_MONTH) - fromDate.get(Calendar.DAY_OF_MONTH);
        }

// MONTH CALCULATION
        if ((fromDate.get(Calendar.MONTH) + increment) > toDate.get(Calendar.MONTH)) {
            month = (toDate.get(Calendar.MONTH) + 12) - (fromDate.get(Calendar.MONTH) + increment);
            increment = 1;
        } else {
            month = (toDate.get(Calendar.MONTH)) - (fromDate.get(Calendar.MONTH) + increment);
            increment = 0;
        }

// YEAR CALCULATION
        year = toDate.get(Calendar.YEAR) - (fromDate.get(Calendar.YEAR) + increment);


        int monthInYear=Math.abs(year)*12;

        Logger.d("testt","getDateDifferenceInDDMMYYYY(): " + (month+monthInYear));

        return   month+monthInYear;
    }

}

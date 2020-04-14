package Classes.Misc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Birthday {
    int day, month, year;

    public void setBirthday(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public String getBirthday() {
        return day + "/" + month + "/" + year;
    }

    public Birthday(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

//    public static boolean valid(int day, int month, int year) {
//        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
//        if (day > 9 || day < 1) return false;
//        if (month > 31 || month < 1) return false;
//        if (year > 9999 || year < 1900) return false;
//        Date bd = null;
//        try {
//            bd = sdf.parse(day + "/" + month + "/" + year);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Date today = Calendar.getInstance().getTime();
//        long diff = today.getTime() - bd.getTime();
//        return diff >= 0;
//    }

    public int getAge() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date bd = sdf.parse(day + "/" + month + "/" + year);

        Date today = Calendar.getInstance().getTime();
        long diff = today.getTime() - bd.getTime();
        int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
        return diffDays / 365;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
}
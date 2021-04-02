package bank_system;

import java.time.Instant;

/**
 * A class that translate Instant into groups of integers.
 */
public class MyDate implements Comparable<MyDate> {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    public MyDate(Instant now){
        changeTime(now);
    }


    public MyDate(String time) {
        year = Integer.parseInt(time.substring(0, 4));
        month = Integer.parseInt(time.substring(5, 7));
        day = Integer.parseInt(time.substring(8, 10));
        hour = Integer.parseInt(time.substring(11, 13));
        minute = Integer.parseInt(time.substring(14, 16));
        second = Integer.parseInt(time.substring(17, 19));
        //"2019-01-01 00:00:00"
    }

    /**
     * Update time ot current instant.
     * @param now a instant represents the current time.
     */
    public void changeTime(Instant now) {
        String line = now.toString();
        year = Integer.parseInt(line.substring(0, 4));
        month = Integer.parseInt(line.substring(5, 7));
        day = Integer.parseInt(line.substring(8, 10));
        hour = Integer.parseInt(line.substring(11, 13));
        minute = Integer.parseInt(line.substring(14, 16));
        second = Integer.parseInt(line.substring(17, 19));
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    @Override
    public int compareTo(MyDate o) {
        if(o.getYear() != this.year){
            return Integer.compare(o.getYear(), this.year);
        }
        if(o.getMonth() != this.month) {
            return Integer.compare(o.getMonth(), this.month);
        }
        if(o.getDay() != this.day) {
            return Integer.compare(o.getDay(), this.day);
        }
        if(o.getHour() != this.hour) {
            return Integer.compare(o.getHour(), this.hour);
        }
        if(o.getMinute() != this.minute) {
            return Integer.compare(o.getMinute(), this.minute);
        }
        if(o.getSecond() != this.second) {
            return Integer.compare(o.getSecond(), this.second);
        }
        return 0;
    }

    public boolean isEarlier(MyDate o){
        return this.compareTo(o) < 0;
    }

    public boolean isLater(MyDate o) {
        return compareTo(o) > 0;
    }

    /**
     * Output the current time.
     * @return String in format of Year-Mo-Da Ho:Mi:Se
     */
    @Override
    public String toString() {
        return String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, minute, second);
    }
}

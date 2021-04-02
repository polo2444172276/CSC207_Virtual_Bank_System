package bank_system;

import java.io.IOException;
import java.time.Clock;
import java.time.ZoneId;

class Timer implements Runnable{
    private final String threadName;
    private Thread myThread;
    private boolean isRunning;

    /**
     * Clock that track the current time in UTC time zone.
     */
    private final Clock clock;

    /**
     * Stores the current time.
     */
    private final MyDate time;
    private int currentMonth;
    private int currentDay;
    private int currentHour;
    private int currentMinute;
    private int currentSecond;

    public Timer(String threadName){
        this.threadName = threadName;
        clock = Clock.system(ZoneId.of("-05:00"));
        time = new MyDate(clock.instant());
        currentMonth = time.getMonth();
        currentHour = time.getHour();
        currentDay = time.getDay();
        currentMinute = time.getMinute();
        currentSecond = time.getSecond();
    }

    public void start(){
        if(myThread == null) {
            myThread = new Thread(this, threadName);
            myThread.start();
        }
        isRunning = true;
    }

    public void stop(){
        if(myThread != null) {
            myThread = null;
        }
        isRunning = false;
    }

    /**
     * For each seconds, the time updates based on the current time.
     */
    public void run(){
        try{
            while(isRunning) {
                time.changeTime(clock.instant());

                if(currentMonth != time.getMonth()) {
                    currentMonth = time.getMonth();
                    updatePerMonth();
                }

                if(currentMinute != time.getMinute()) {
                    currentMinute = time.getMinute();
                    updatePerMinute();
                }

                if(currentDay != time.getDay()){
                    currentDay = time.getDay();
                    updatePerDay();
                }

                if (currentSecond != time.getSecond()){
                    currentSecond = time.getSecond();
                    updatePerSecond();
                }

                if(currentHour != time.getHour()){
                    currentHour = time.getHour();
                }

                Thread.sleep(1000);
            }
        } catch(InterruptedException e) {
            System.out.println("ERROR");
        }
    }

    /**
     * Things that needs to be updated per month.
     */
    private void updatePerMonth(){
        Bank.updateInterest();
    }

    /**
     * Things that needs to be updated per day.
     */
    private void updatePerDay(){
        Bank.checkFunding();
    }

    /**
     * Things that needs to be updated per second.
     */
    private void updatePerSecond() {
        Bank.checkStock();
    }

    private void updatePerMinute(){
        FileLoader fileLoader = new FileLoader();
        try{
            fileLoader.saveData();
        } catch (IOException e){
            e.printStackTrace();
        }

        // check if bank is open
        Bank.isOpen = !(
                (time.getHour() == 0 && time.getMinute() <= 3) ||
                (time.getHour() == 23 && time.getMinute() >= 57));
    }

    /**
     * Return the current time.
     *
     * @return current time in string representation.
     */
    public MyDate getTime() {
        return new MyDate(clock.instant());
    }
}
package Utils;


import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Clock extends Thread
{

    private Text clockText;
    private boolean running = true;

    public Clock(Text text){
        this.clockText = text;
    }

    public void run()
    {

        while(running) {
            LocalDateTime localDateTime = LocalDateTime.now();




            Date d = new Date();
            int minutes = localDateTime.getMinute();
            int hours = localDateTime.getHour();

            String mins = minutes + "";
            if(minutes < 10){
                mins = "0" + minutes;
            }

            String hrs = hours + "";
            if (hours < 10){
                hrs = "0" + hours;
            }

            clockText.setText(hrs + ":" + mins);

        }
    }

    public void stopClock(){
        this.running = false;
    }
}
package act;

import people.*;
import people.Wolf;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by geyao on 16/7/5.
 */
public class Clock {



    public void timeBegin( Player player ,int sec ){
        final Timer clock1 = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {


                clock1.cancel();
            }
        };
        clock1.schedule(tt, sec*000); // sec秒
        System.out.println("开始计时");

    }


}

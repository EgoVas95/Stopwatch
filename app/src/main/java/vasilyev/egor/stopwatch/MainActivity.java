package vasilyev.egor.stopwatch;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Button startStop, reset;
    private TextView stopwatch;
    private Timer timer;
    private TimerTask stopwatchTimerTask;
    private long systemUpTime = 0, millis;

    class StopwatchTimerTask extends TimerTask{

        @Override
        public void run(){
            millis = System.currentTimeMillis();
            if(systemUpTime != 0 && millis > systemUpTime){
                millis = millis - systemUpTime;
            } else{
                millis = 0;
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final String timeString = millisToString(millis);
                    stopwatch.setText(timeString);
                }
            });
        }

    }

    private String millisToString(long time){
        SimpleDateFormat outputTimeFormat = new SimpleDateFormat(
                getString(R.string.time_format_pattern)
        );
        return outputTimeFormat.format(time);
    }

    private void resetSetClickable(boolean clickable){
        if(clickable){
            reset.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else{
            reset.setTextColor(getResources().getColor(R.color.colorNotChecked));
        }
        reset.setClickable(clickable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        startStop = (Button)findViewById(R.id.stopStartButton);
        reset = (Button)findViewById(R.id.resetButton);
        stopwatch = (TextView)findViewById(R.id.stopwatchTextBox);

        stopwatch.setText( millisToString(0) );

        reset.setClickable(true);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                systemUpTime = 0;
                stopwatch.setText(millisToString(0));

                startStop.setText(R.string.start_button);
                startStop.setTextColor( getResources().getColor(R.color.colorPrimary));
            }
        });

        startStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startStop.getText().toString().equals(getResources().getString(R.string.start_button)))
                {
                    resetSetClickable(false);

                    startStop.setText(R.string.stop_button);
                    startStop.setTextColor( getResources().getColor(R.color.colorAccent));

                    systemUpTime = System.currentTimeMillis();

                    timer = new Timer();
                    stopwatchTimerTask = new StopwatchTimerTask();

                    timer.schedule(stopwatchTimerTask, 1, 1);
                }
                else if(startStop.getText().toString().equals(getResources().getString(R.string.stop_button)))
                {
                    if(timer != null)
                    {
                        timer.cancel();
                        timer = null;
                    }

                    resetSetClickable(true);
                    startStop.setText(R.string.resume_button);
                    startStop.setTextColor( getResources().getColor(R.color.colorResume));

                }
                else if(startStop.getText().toString().equals(getResources().getString(R.string.resume_button)))
                {
                    startStop.setText(R.string.stop_button);
                    startStop.setTextColor( getResources().getColor(R.color.colorAccent));

                    timer = new Timer();
                    stopwatchTimerTask = new StopwatchTimerTask();

                    timer.schedule(stopwatchTimerTask, 1, 1);
                    resetSetClickable(false);
                }
            }
        });
    }
}

package com.prography.progrpahy_pizza.src.record;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.prography.progrpahy_pizza.R;
import com.prography.progrpahy_pizza.src.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RecordActivity extends BaseActivity implements View.OnClickListener{

    TextView tvCountTime;
    Button btnStartRecord;

    Thread timerThread;

    private boolean running = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        /* findViewByID */
        tvCountTime = findViewById(R.id.tv_cur_time_record);
        btnStartRecord = findViewById(R.id.btn_start_record);

        /* Set On Click Listener */

        final Handler timerHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                tvCountTime.setText(sdf.format(new Date((long) msg.obj)));
            }
        };

        timerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                while (running) {
                    Message message = new Message();
                    message.obj = System.currentTimeMillis() - start;
                    timerHandler.sendMessage(message);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        /* Set on Click Listener */
        btnStartRecord.setOnClickListener(this);

        /* Init View */

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_record:
                if (!running) {
                    running = true;
                    timerThread.start();
                    btnStartRecord.setText("일시정지");
                } else {
                    running = false;
                }
                break;
        }
    }
}

package com.prography.progrpahy_pizza.src.record;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.prography.progrpahy_pizza.R;
import com.prography.progrpahy_pizza.src.BaseActivity;
import com.prography.progrpahy_pizza.src.record.interfaces.RecordActivityView;

import java.util.Date;

import static com.prography.progrpahy_pizza.src.ApplicationClass.TIME_FORMAT;

public class RecordActivity extends BaseActivity implements RecordActivityView {

    private TextView tvCountTime;
    private Button btnStartRecord;
    private Button btnSubmitRecord;

    private Thread timerThread;


    private Handler timerHandler;

    private boolean TIMER_RUNNING = false;
    private long startTime;
    private long totalLeftTime = 0;
    private long leftTime = 0;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        /* findViewByID */
        tvCountTime = findViewById(R.id.tv_cur_time_record);
        btnStartRecord = findViewById(R.id.btn_start_record);
        btnSubmitRecord = findViewById(R.id.btn_submit_record);

        /* Set on Click Listener */
        btnStartRecord.setOnClickListener(this);
        btnSubmitRecord.setOnClickListener(this);


        /* Set Timer Handler */
        timerHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                Date date = new Date((long) msg.obj);
                tvCountTime.setText(TIME_FORMAT.format(date));
            }
        };
    }

    private void tryPostRecord() {
        RecordService recordService = new RecordService(this);
        recordService.postRecord();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_record:
                if (!TIMER_RUNNING) {
                    TIMER_RUNNING = true;
                    startTime = System.currentTimeMillis(); // 초기 시간 기록
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            totalLeftTime += leftTime; // 가장 최근 leftTime 더하기.
                            while (TIMER_RUNNING) {
                                Message message = new Message();
                                leftTime = System.currentTimeMillis() - startTime; // 지속시간 누적
                                message.obj = totalLeftTime + leftTime;
                                timerHandler.sendMessage(message);
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();

                    btnStartRecord.setText("일시정지");
                    btnSubmitRecord.setVisibility(View.INVISIBLE);
                } else {

                    TIMER_RUNNING = false;
                    btnStartRecord.setText("계속");
                    btnSubmitRecord.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_submit_record:
                new AlertDialog.Builder(this).setMessage("이대로 제출하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create().show();
                break;
        }
    }
}

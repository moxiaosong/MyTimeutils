package com.dlc.mytimeutils;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dyhdyh.support.countdowntimer.CountDownTimerSupport;
import com.dyhdyh.support.countdowntimer.OnCountDownTimerListener;
import com.dyhdyh.support.countdowntimer.TimerState;


public class MainActivity extends AppCompatActivity {
    TextView tv;
    TextView tv_state;
    EditText ed_future;
    EditText ed_interval;

    public static final String IN_RUNNING = "IN_RUNNING";
    public static final String END_RUNNING = "END_RUNNING";

    private CountDownTimerSupport mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        tv_state = (TextView) findViewById(R.id.tv_state);
        ed_future = (EditText) findViewById(R.id.ed_future);
        ed_interval = (EditText) findViewById(R.id.ed_interval);

        // 注册广播
        registerReceiver(mUpdateReceiver, updateIntentFilter());

        //开启倒计时
        int time = (int) (60 * 1000 );
        Intent countDownIntent = new Intent(MainActivity.this, CodeTimerService.class);
        countDownIntent.putExtra(CodeTimerService.TIME, time);
        startService(countDownIntent);
    }

    //广播接收者
    private final BroadcastReceiver mUpdateReceiver = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            assert action != null;
            switch (action) {
                case IN_RUNNING://正在倒计时

                    String time1 = intent.getStringExtra("time");
                    Log.i("lgq","倒计时回调===="+time1);
                    break;
                case END_RUNNING://倒计时完成

                    finish();
                    break;
            }
        }
    };

    //注册广播
    private static IntentFilter updateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(IN_RUNNING);
        intentFilter.addAction(END_RUNNING);
        return intentFilter;
    }


    public void clickStart(View v) {
        if (mTimer == null) {
            long millisInFuture = Long.parseLong(ed_future.getText().toString());
            long countDownInterval = Long.parseLong(ed_interval.getText().toString());
            mTimer = new CountDownTimerSupport(millisInFuture, countDownInterval);
            mTimer.setOnCountDownTimerListener(new OnCountDownTimerListener() {
                @Override
                public void onTick(long millisUntilFinished) {
                    tv.setText(millisUntilFinished + "ms\n" + millisUntilFinished / 1000 + "s");//倒计时
//                    textView.setText((60 * 1000 - millisUntilFinished) / 1000 + "S");//正计时
                    Log.d("CountDownTimerSupport", "onTick : " + millisUntilFinished + "ms");
                }

                @Override
                public void onFinish() {
                    tv.setText("已停止");
                    Log.d("CountDownTimerSupport", "onFinish");
                }
            });
        }
        mTimer.start();
        tv_state.setText(getStateText());
    }

    public void clickPause(View v) {
        if (mTimer != null) {
            mTimer.pause();

            tv_state.setText(getStateText());
        }
    }

    public void clickResume(View v) {
        if (mTimer != null) {
            mTimer.resume();

            tv_state.setText(getStateText());
        }
    }

    public void clickCancel(View v) {
        if (mTimer != null) {
            mTimer.stop();

            tv_state.setText(getStateText());
        }
    }

    public void clickResetStart(View v) {
        if (mTimer != null) {
            mTimer.reset();//重复启动
//            mTimer.setMillisInFuture(30000);//重设时长
            mTimer.start();//重复启动

            tv_state.setText(getStateText());
        }
    }

    public void clickList(View v) {
        startActivity(new Intent(this, RecyclerViewActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTimer != null) {
            mTimer.resume();
            tv_state.setText(getStateText());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mTimer != null) {
            mTimer.pause();
            tv_state.setText(getStateText());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.stop();
            tv_state.setText(getStateText());
        }
    }

    private String getStateText() {
        TimerState state = mTimer.getTimerState();
        if (TimerState.START == state) {
            return "正在倒计时";
        } else if (TimerState.PAUSE == state) {
            return "倒计时暂停";
        } else if (TimerState.FINISH == state) {
            return "倒计时闲置";
        }
        return "";
    }

}

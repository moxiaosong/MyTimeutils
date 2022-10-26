package com.dlc.mytimeutils;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import static com.dlc.mytimeutils.MainActivity.END_RUNNING;
import static com.dlc.mytimeutils.MainActivity.IN_RUNNING;

/**
 * @author : LGQ
 * @date : 2020/06/01 11
 * @desc :
 */
public class CodeTimerService extends Service {

    public static final String TIME = "time";

    public CountDownTimer mCodeTimer;
    public int time;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        time = intent.getIntExtra(TIME, 600000);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
//        LogPlus.e("开启服务 = " + startId);

        //第一个参数是总时间， 第二个参数是间隔
        mCodeTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //                LogPlus.e("time=" + millisUntilFinished);
                //通过广播发送剩余时间
                //                SpFileUtil.setWorkStatus(getApplicationContext(), true);
                Log.i("lgq","倒计时服务在运行====="+millisUntilFinished / 1000 + "");
                broadcastUpdate(IN_RUNNING, millisUntilFinished / 1000 + "");
            }

            @Override
            public void onFinish() {

                //广播倒计时结束
                broadcastUpdate(END_RUNNING);
                //                SpFileUtil.setWorkStatus(getApplicationContext(), false);
                //停止服务
                stopSelf();
            }
        };
        // 开始倒计时
        mCodeTimer.start();
        super.onStart(intent, startId);
    }
    // 发送广播
    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }
    // 发送带有数据的广播
    private void broadcastUpdate(final String action, String time) {
        final Intent intent = new Intent(action);
        intent.putExtra("time", time);
        sendBroadcast(intent);
    }
    @Override
    public void onDestroy() {
        mCodeTimer.cancel();
        //        SpFileUtil.setWorkStatus(getApplicationContext(), false);
        //        SpFileUtil.saveIsOpen(getApplicationContext(), "false");
        //        SpFileUtil.saveMode(getApplicationContext(), "0");
        //        SpFileUtil.setMacState(this, "");
//        LogPlus.e("服务销毁");
        super.onDestroy();
    }
}
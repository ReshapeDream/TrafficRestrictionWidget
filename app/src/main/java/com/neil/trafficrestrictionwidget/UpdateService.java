package com.neil.trafficrestrictionwidget;

import android.app.AlarmManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * Service在华为手机息屏2分钟左右会被kill掉，故不再使用
 */

public class UpdateService extends Service {
    private static final String TAG="UpdateService";

    private static final int UPDATE_DURATION = 10 * 1000;     // Widget 更新间隔
    private static final int UPDATE_MESSAGE  = 1000;
    private Context mContext;

    UpdateHandler updateHandler;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: " );
//        AlarmManagerUtil.sendPeriodicAlarm(this,1, AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),5*60*1000,UpdateService.class);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: " );
        updateHandler=new UpdateHandler();
        Message message = updateHandler.obtainMessage();
        message.what = UPDATE_MESSAGE;
        updateHandler.sendMessage(message);
        mContext = this.getApplicationContext();
    }

    private void sendBroadcast(){
        AppWidgetUtil.sendBroadcast(this,"com.neil.traffic.update",TrafficRestrictionAppWidget.class);
    }

    protected final class UpdateHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_MESSAGE:
                    sendBroadcast();
                    Message message = obtainMessage();
                    message.what = UPDATE_MESSAGE;
                    sendMessageDelayed(message,UPDATE_DURATION);
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy: " );
        updateHandler.removeMessages(UPDATE_MESSAGE);
        super.onDestroy();
    }
}

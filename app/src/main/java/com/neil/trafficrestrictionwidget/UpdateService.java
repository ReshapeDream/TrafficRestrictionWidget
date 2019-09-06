package com.neil.trafficrestrictionwidget;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import androidx.annotation.Nullable;


public class UpdateService extends Service {
    private static final String TAG="UpdateService";

    private static final int UPDATE_DURATION = 2*60*60 * 1000;     // Widget 更新间隔
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
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
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
        updateHandler.removeMessages(UPDATE_MESSAGE);
        super.onDestroy();
    }
}

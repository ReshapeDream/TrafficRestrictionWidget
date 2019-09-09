package com.neil.trafficrestrictionwidget;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmManagerUtil {
/**
 AlarmManager.ELAPSED_REALTIME：休眠后停止，相对开机时间
 AlarmManager.ELAPSED_REALTIME_WAKEUP：休眠状态仍可唤醒cpu继续工作，相对开机时间
 AlarmManager.RTC：同1，但时间相对于绝对时间
 AlarmManager.RTC_WAKEUP：同2，但时间相对于绝对时间
 AlarmManager.POWER_OFF_WAKEUP：关机后依旧可用，相对于绝对时间
 */
    /**
     * 获取 AlarmManager
     *
     * @param context
     * @return [AlarmManager]
     */
    public static AlarmManager getAlarmManager(Context context) {
        return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    /**
     * 发送定时Alarm
     *
     * @param context
     * @param type          Alarm启动类型
     * @param triggerAtTime 定时任务开启的时间
     * @param action        Intent action
     * @param requestCode   请求码，用于区分不同任务
     * @param cls           广播接收器的class
     */
    public static void sendAlarm(Context context,int type, long triggerAtTime,String action, int requestCode,  Class<?> cls) {
        getAlarmManager(context).set(type, triggerAtTime, createPendingIntent(context, action,requestCode, cls));
    }

    /**
     * 发送周期 Alarm广播
     *
     * @param context
     * @param requestCode     请求码，用于区分不同任务
     * @param type            Alarm启动类型
     * @param triggerAtMillis 定时任务开启的时间
     * @param intervalMillis  间隔周期
     * @param cls             广播接收者的class
     */
    public static void sendPeriodicAlarm(Context context, int type, long triggerAtMillis, long intervalMillis, String action, int requestCode,Class<?> cls) {
        getAlarmManager(context).setRepeating(type, triggerAtMillis, intervalMillis, createPendingIntent(context,action, requestCode, cls));
    }

    /**
     * 精准事件，不会被系统改时间，但是在华为手机上，息屏后会被暂停！
     * @param context
     * @param type
     * @param triggerAtMillis
     * @param action
     * @param requestCode
     * @param cls
     */
    public static void sendExactAlarm(Context context, int type, long triggerAtMillis, String action, int requestCode, Class<?> cls){
        getAlarmManager(context).setExact(type,triggerAtMillis,createPendingIntent(context,action,requestCode,cls));
    }

    /**
     *
     * @param context
     * @param requestCode
     * @param cls
     * @return
     */
    private static PendingIntent createPendingIntent(Context context,String action, int requestCode, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        if(action!=null){
            intent.setAction(action);
        }
        if(BroadcastReceiver.class.isAssignableFrom(cls)){
            return PendingIntent.getBroadcast(context,requestCode,intent,0);
        }else if(Service.class.isAssignableFrom(cls)){
            return PendingIntent.getService(context,requestCode,intent,0);
        }else if(Activity.class.isAssignableFrom(cls)){
            return PendingIntent.getActivity(context,requestCode,intent,0);
        }else{
            return null;
        }
    }



    /**
     * 取消Alarm
     *
     * @param context
     * @param requestCode
     * @param cls
     */
    public static void cancelAlarmBroadcast(Context context,String action, int requestCode, Class<?> cls) {
        getAlarmManager(context).cancel(createPendingIntent(context,action, requestCode, cls));
    }
}

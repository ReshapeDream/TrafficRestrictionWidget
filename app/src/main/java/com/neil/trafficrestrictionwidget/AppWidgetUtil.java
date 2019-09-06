package com.neil.trafficrestrictionwidget;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;

public class AppWidgetUtil {
    /**
     * RemoteViews内容设置好之后，提交更新
     * @param context      上下文，用于获取AppWidgetManager
     * @param remoteViews  要更新的RemoteView
     * @param appWidgetProviderClass AppWidgetProvider类
     */
    public static void updateAppWidget(Context context, RemoteViews remoteViews, Class<? extends AppWidgetProvider> appWidgetProviderClass){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, appWidgetProviderClass));
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    /**
     * 创建点击事件
     * @param context  上下文
     * @param remoteViews 通过layout布局文件创建的 RemoteViews
     * @param action  广播Action
     * @param viewId  View 的id
     * @param appWidgetProviderClass AppWidgetProvider类
     */
    public static void createClickBroadcast(Context context, String action, @IdRes int viewId, RemoteViews remoteViews, Class<? extends AppWidgetProvider> appWidgetProviderClass){
        Intent intent = new Intent(action);
        intent.setComponent(new ComponentName(context,appWidgetProviderClass));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, viewId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(viewId, pendingIntent);
    }


    /**
     * 创建点击广播
     * @param context  上下文
     * @param layoutId RemoteViews 的布局文件
     * @param action  广播Action
     * @param viewId  View 的id
     * @param appWidgetProviderClass AppWidgetProvider类
     */
    public static void createClickBroadcast(Context context, String action, @IdRes int viewId, @LayoutRes int layoutId, Class<? extends AppWidgetProvider> appWidgetProviderClass){
        RemoteViews remoteViews=new RemoteViews(context.getPackageName(),layoutId);
        Intent intent = new Intent(action);
        intent.setComponent(new ComponentName(context,appWidgetProviderClass));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, viewId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(viewId, pendingIntent);
    }

    /**
     * 发送广播
     * @param context
     * @param action
     * @param appWidgetProviderClass
     */
    public static void sendBroadcast(Context context,String action,Class<? extends AppWidgetProvider> appWidgetProviderClass){
        Intent intent=new Intent(action);
        intent.setComponent(new ComponentName(context,appWidgetProviderClass));
        context.sendBroadcast(intent);
    }

    /**
     * 在Broadcast中启动某个Activity
     * @param context
     * @param activityClass
     */
    public static void startActivity(Context context, Class<? extends Activity> activityClass){
        Intent intent = new Intent(context, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}

package com.neil.trafficrestrictionwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class TrafficRestrictionAppWidget extends AppWidgetProvider {
    private static final String TAG = "TRWidget";
    //自动更新
    private static final String action_update = "com.neil.traffic.update";
    //手动更新
    private static final String action_update_manual = "com.neil.traffic.update_manual";
    //启动activity
    private static final String action_start_activity = "com.neil.traffic.startMainActivity";
    //切换深色浅色主题
    private static final String action_change_style = "com.neil.traffic.changeStyleMode";


    OkHttpClient httpClient = new OkHttpClient();
    //设置颜色

    int[] lightColors=new int[]{0xffff6699,0xffeeeeee,0xffdddddd,R.mipmap.update};
    int[] darkColors=new int[]{0xffff6633,0xff555555,0xff222222,R.mipmap.update_dark};
    private SharedPreferences sharedPreferences;

    private static final int STYLE_MODE_LIGHT=0;
    private static final int STYLE_MODE_DARK=1;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.traffic);

        //手动更新事件
        AppWidgetUtil.createClickBroadcast(context,action_update_manual,R.id.update,remoteViews,TrafficRestrictionAppWidget.class);

        //点击启动Activity
        AppWidgetUtil.createClickBroadcast(context,action_start_activity,R.id.tr_layout,remoteViews,TrafficRestrictionAppWidget.class);

        appWidgetManager.updateAppWidget(appWidgetIds,remoteViews);
        //启动service
        context.startService(new Intent(context, UpdateService.class));
        setStyleMode(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        context.startService(new Intent(context, UpdateService.class));
        setStyleMode(context);
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        Log.e(TAG, "onDisabled: " );
        context.stopService(new Intent(context, UpdateService.class));
        super.onDisabled(context);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.e(TAG, "onReceive: "+intent.getAction() );
        switch (intent.getAction()) {
            case action_update:
                updateWidget(context,null);
                break;
            case action_update_manual:
                updateWidget(context,"更新成功");
                break;
            case action_start_activity:
                AppWidgetUtil.startActivity(context,MainActivity.class);
                break;
            case action_change_style:
                setStyleMode(context);
                break;
        }
    }


    /**
     * @param context
     * @param msg 手动更新传递的消息
     */
    private void updateWidget(final Context context,@Nullable final String msg) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                Request request = new Request.Builder()
                        .url("http://59.110.140.90:7979/updatetr/update")
                        .build();
                try {
                    Response execute = httpClient.newCall(request).execute();
                    emitter.onNext(execute.body().string());
                    emitter.onComplete();
                } catch (IOException e) {
                    e.printStackTrace();
                    emitter.onError(new Throwable("更新出错,请检查网络"));
                }

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e(TAG, "accept: "+s );
                        if (TextUtils.isEmpty(s) || s.contains("html")) {
                            return;
                        }
                        if(!s.startsWith("{")){
                            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Gson gson = new Gson();
                        TrMsg trMsg = gson.fromJson(s, TrMsg.class);
                        if (trMsg.getCode() != 0) {
                            return;
                        }
                        update(context,trMsg);
                        if(msg!=null){
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void update(Context context, TrMsg trMsg) {
        TrMsg.MsgBean.TrBean tr = trMsg.getMsg().getTr();
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.traffic);
        remoteViews.setTextViewText(R.id.today, "今天" + tr.getT0().getDw());
        remoteViews.setTextViewText(R.id.today_tr, tr.getT0().getTr());
        remoteViews.setTextViewText(R.id.tomorrow, "明天" + tr.getT1().getDw());
        remoteViews.setTextViewText(R.id.tomorrow_tr,  tr.getT1().getTr());
        remoteViews.setTextViewText(R.id.third_day, "后天" + tr.getT2().getDw());
        remoteViews.setTextViewText(R.id.third_day_tr,tr.getT2().getTr());

        TrMsg.MsgBean.PmBean pm = trMsg.getMsg().getPm();
        remoteViews.setTextViewText(R.id.pm, "空气:" + pm.getPmt() + " AQI:" + pm.getPm());

        AppWidgetUtil.updateAppWidget(context,remoteViews,TrafficRestrictionAppWidget.class);
    }

    private void setStyleMode(Context context){
        sharedPreferences = context.getSharedPreferences("style",0);
        int mode = sharedPreferences.getInt("mode", 0);
        if(mode==STYLE_MODE_LIGHT){
            changeAppWidgetColor(context,lightColors);
        }else if(mode==STYLE_MODE_DARK){
            changeAppWidgetColor(context,darkColors);
        }
    }

    private void changeAppWidgetColor(Context context,int[] colors) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.traffic);
        remoteViews.setTextColor(R.id.today, colors[0]);
        remoteViews.setTextColor(R.id.today_tr, colors[0]);
        remoteViews.setTextColor(R.id.tomorrow, colors[1]);
        remoteViews.setTextColor(R.id.tomorrow_tr,  colors[1]);
        remoteViews.setTextColor(R.id.third_day, colors[1]);
        remoteViews.setTextColor(R.id.third_day_tr,colors[1]);
        remoteViews.setTextColor(R.id.third_day_tr,colors[1]);
        remoteViews.setTextColor(R.id.pm, colors[1]);
        remoteViews.setTextColor(R.id.tr_tip,colors[2]);
        remoteViews.setInt(R.id.update,"setBackgroundResource",colors[3]);

        AppWidgetUtil.updateAppWidget(context,remoteViews,TrafficRestrictionAppWidget.class);
    }

}

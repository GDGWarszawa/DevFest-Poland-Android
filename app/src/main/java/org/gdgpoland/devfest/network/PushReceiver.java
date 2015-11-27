package org.gdgpoland.devfest.network;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.parse.ParsePushBroadcastReceiver;

import org.gdgpoland.devfest.PushActivity;
import org.gdgpoland.devfest.R;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tajchert on 27.11.2015.
 */
public class PushReceiver extends ParsePushBroadcastReceiver {
    private static final String TAG = PushReceiver.class.getSimpleName();

    protected void onPushReceive(Context mContext, Intent intent) {
        if("com.parse.push.intent.RECEIVE".equals(intent.getAction())) {
            try {
                JSONObject pushData = new JSONObject(intent.getStringExtra("com.parse.Data"));
                if(pushData.has("show_notif") && "true".equals(pushData.getString("show_notif"))) {
                    String title = pushData.getString("title");
                    String content = pushData.getString("content");
                    String subTitle = pushData.getString("subTitle");
                    String longText = pushData.getString("longText");
                    boolean isOpenable = "true".equals(pushData.getString("isOpenable"));
                    boolean isUrl = "true".equals(pushData.getString("isUrl"));
                    boolean isRatingForm  = "true".equals(pushData.getString("isRatingForm"));
                    String ratingUrl = "";
                    if(isRatingForm) {
                        ratingUrl = pushData.getString("ratingUrl");
                    }
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(mContext)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
                                    .setLargeIcon(BitmapFactory.decodeResource(
                                            mContext.getResources(), R.mipmap.ic_launcher))
                                    .setOnlyAlertOnce(true)
                                    .setAutoCancel(true)
                                    .setContentTitle(title)
                                    .setSubText(subTitle)
                                    .setContentText(content);
                    if(isOpenable) {
                        Intent notificationIntent = new Intent(mContext, PushActivity.class);
                        notificationIntent.putExtra("title", title);
                        notificationIntent.putExtra("content", longText);
                        notificationIntent.putExtra("isUrl", isUrl);
                        notificationIntent.putExtra("ratingUrl", ratingUrl);
                        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(mContext, 123, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        mBuilder.setContentIntent(pendingNotificationIntent);
                    }
                    Notification notification = mBuilder.build();
                    notification.flags |= Notification.FLAG_AUTO_CANCEL;
                    int mNotificationId = 001;
                    NotificationManager mNotifyMgr = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotifyMgr.notify(mNotificationId, mBuilder.build());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

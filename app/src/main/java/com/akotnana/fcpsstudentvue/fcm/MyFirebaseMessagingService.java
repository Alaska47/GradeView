package com.akotnana.fcpsstudentvue.fcm;

/**
 * Created by anees on 11/21/2017.
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.akotnana.fcpsstudentvue.R;
import com.akotnana.fcpsstudentvue.activities.AssignmentViewActivity;
import com.akotnana.fcpsstudentvue.utils.gson.Course;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification

        String title = "";
        String body = "";
        String extra = "";
        if(remoteMessage.getData() != null) {
            Map<String, String> payload = remoteMessage.getData();
            title = payload.get("title");
            body = payload.get("body");
            extra = payload.get("extra");
            Log.d(TAG, "title: " + title);
            Log.d(TAG, "message: " + body);
            Log.d(TAG, "extra: " + extra);
            sendNotification(new String[]{title, body, extra});
        }

        /*
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(new String[]{remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody()});
        }
        */

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String[] messageBody) {

        Intent intent = new Intent(this, AssignmentViewActivity.class);
        Gson gson = null;
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gson = gsonBuilder.create();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Course course = gson.fromJson(messageBody[2], Course.class);

        intent.putExtra("currentQuarter", course.getGrades().getCurrentQuarterString());
        intent.putExtra("period", course.getPeriodNumber());
        intent.putExtra("assignments", messageBody[2]);
        intent.putExtra("fromNotification", "1");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int)(Math.random()*100) /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification notification = new Notification();
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setContentTitle(messageBody[0])
                        .setContentText(messageBody[1])
                        .setSmallIcon(R.drawable.ic_stat_logo)
                        .setPriority(NotificationManager.IMPORTANCE_MAX)
                        .setAutoCancel(true)
                        .setDefaults(notification.defaults)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
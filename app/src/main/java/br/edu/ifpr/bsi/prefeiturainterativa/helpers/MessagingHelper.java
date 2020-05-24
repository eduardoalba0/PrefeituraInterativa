package br.edu.ifpr.bsi.prefeiturainterativa.helpers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.controller.ActivitySplash;

public class MessagingHelper extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {

            String channelId = getString(R.string.default_notification_client_id);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Tramitação das Solicitações",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            Intent intent = new Intent(this, ActivitySplash.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            notificationManager.notify(((int) remoteMessage.getSentTime()),
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.mipmap.ic_launcher_round)
                            .setContentTitle(remoteMessage.getNotification().getTitle())
                            .setContentText(remoteMessage.getNotification().getBody())
                            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                            .setColorized(true)
                            .setAutoCancel(true)
                            .setColor(getResources().getColor(R.color.colorRed))
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setContentIntent(PendingIntent.getActivity(this, 0, intent,
                                    PendingIntent.FLAG_ONE_SHOT)).build());
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.e("Token", token);
    }
}

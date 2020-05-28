package br.edu.ifpr.bsi.prefeiturainterativa.helpers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import androidx.core.app.NotificationCompat;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.controller.ActivitySolicitacaoVisualizar;
import br.edu.ifpr.bsi.prefeiturainterativa.model.Aviso;

import static br.edu.ifpr.bsi.prefeiturainterativa.model.Aviso.CATEGORIA_PADRAO;

public class NotificationHelper extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage message) {
        if (message.getNotification() != null && FirebaseAuth.getInstance().getCurrentUser() != null) {

            Intent intent = new Intent(this, ActivitySolicitacaoVisualizar.class);
            String channelId = getString(R.string.default_notification_client_id);
            String categoria = CATEGORIA_PADRAO;

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (message.getData() != null) {
                Aviso aviso = new Aviso();
                aviso.setTitulo(message.getNotification().getTitle());
                aviso.setCorpo(message.getNotification().getBody());
                aviso.setSolicitacao_ID(message.getData().get("Solicitacao"));
                aviso.setData(new Date());
                aviso.setCategoria(message.getData().get("Categoria"));
                intent.putExtra("Aviso", aviso);
                categoria = aviso.getCategoria();
                new SharedPreferencesHelper(this).insertAviso(aviso);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        categoria.trim().equals("") ? CATEGORIA_PADRAO : categoria,
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(((int) message.getSentTime()),
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.ic_notificacao)
                            .setContentTitle(message.getNotification().getTitle())
                            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(message.getNotification().getBody()))
                            .setColorized(true)
                            .setAutoCancel(true)
                            .setColor(getResources().getColor(R.color.colorRed))
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setContentIntent(PendingIntent.getActivity(this, 0, intent,
                                    PendingIntent.FLAG_ONE_SHOT)).build());

        }
    }
}

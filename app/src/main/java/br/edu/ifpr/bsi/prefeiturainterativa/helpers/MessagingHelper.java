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

import java.util.Map;

import androidx.core.app.NotificationCompat;
import br.edu.ifpr.bsi.prefeiturainterativa.R;
import br.edu.ifpr.bsi.prefeiturainterativa.controller.ActivitySolicitacaoVisualizar;

public class MessagingHelper extends FirebaseMessagingService {
    public static final String CATEGORIA_PADRAO = "Avisos",
            CATEGORIA_AVALIACAO = "Avaliar Atendimento",
            CATEGORIA_TRAMITACAO = "Atualizações";

    @Override
    public void onMessageReceived(RemoteMessage message) {
        if (message.getNotification() != null && FirebaseAuth.getInstance().getCurrentUser() != null) {

            String channelId = getString(R.string.default_notification_client_id);
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            String categoria = CATEGORIA_PADRAO;

            if (message.getData() != null) {
                Map<String, String> dados = message.getData();
                String aux = dados.get("Categoria");
                if (aux != null && !aux.equals("")) {
                    categoria = aux;
                    if (aux.equals(CATEGORIA_AVALIACAO) || aux.equals(CATEGORIA_TRAMITACAO)) {
                        PreferenceManager.getDefaultSharedPreferences(this).edit()
                                .putString("Categoria", dados.get("Categoria"))
                                .putString("Solicitacao", dados.get("Solicitacao"))
                                .apply();
                    }
                }
            }

            Intent intent = new Intent(this, ActivitySolicitacaoVisualizar.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        categoria,
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(((int) message.getSentTime()),
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.ic_notificacao)
                            .setContentTitle(message.getNotification().getTitle())
                            .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
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

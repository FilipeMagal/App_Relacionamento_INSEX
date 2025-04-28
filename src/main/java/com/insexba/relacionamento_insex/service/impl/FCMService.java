package com.insexba.relacionamento_insex.service.impl;

import com.insexba.relacionamento_insex.entity.PushNotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service
public class FCMService {
    private Logger logger = LoggerFactory.getLogger(FCMService.class);

    // Metodo para enviar mensagem sem redirecionamento
    public void sendMessageToToken(PushNotificationRequest request)
            throws InterruptedException, ExecutionException {
        // Mensagem personalizada com base no evento (curtida)
        String title = request.getTitle() != null ? request.getTitle() : "Novo evento!";
        String message = request.getMessage() != null ? request.getMessage() : "Você tem uma nova notificação.";

        // Atualize a mensagem para refletir a curtida (Ex: "Usuário X curtiu seu perfil")
        if ("like".equals(request.getTopic())) {  // Supondo que o topic seja "like" para curtidas
            title = "Curtida de " + request.getTitle();
            message = "Você recebeu uma curtida de " + request.getTitle();
        }

        // Atualiza a requisição com a mensagem
        request.setTitle(title);
        request.setMessage(message);

        // Criando a notificação com base na requisição
        Message messageToSend = getPreconfiguredMessageToToken(request);

        // Serializando para log
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(messageToSend);

        // Enviando a notificação
        String response = sendAndGetResponse(messageToSend);
        logger.info("Sent message to token. Device token: " + request.getToken() + ", " + response + " msg " + jsonOutput);
    }

    private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }

    private AndroidConfig getAndroidConfig(String topic) {
        return AndroidConfig.builder()
                .setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic)
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(AndroidNotification.builder()
                        .setTag(topic).build()).build();
    }

    private ApnsConfig getApnsConfig(String topic) {
        return ApnsConfig.builder()
                .setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build();
    }

    private Message getPreconfiguredMessageToToken(PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).setToken(request.getToken())
                .build();
    }

    private Message getPreconfiguredMessageWithoutData(PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).setTopic(request.getTopic())
                .build();
    }

    private Message getPreconfiguredMessageWithData(Map<String, String> data, PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).putAllData(data).setToken(request.getToken())
                .build();
    }

    private Message.Builder getPreconfiguredMessageBuilder(PushNotificationRequest request) {
        AndroidConfig androidConfig = getAndroidConfig(request.getTopic());
        ApnsConfig apnsConfig = getApnsConfig(request.getTopic());

        String title = request.getTitle() != null ? request.getTitle() : "Sem título";
        String message = request.getMessage() != null ? request.getMessage() : "Sem mensagem";

        // Usando o metodo estático builder() da classe Notification
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(message)
                .build();

        return Message.builder()
                .setApnsConfig(apnsConfig)
                .setAndroidConfig(androidConfig)
                .setNotification(notification);
                // Não estamos configurando a ação de clique, logo ele apenas exibirá a notificação.
    }
}
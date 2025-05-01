package com.insexba.relacionamento_insex.notification.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

import java.io.FileInputStream;
import java.io.IOException;

public class FcmService {

    public void sendNotification(String token, String title, String body) throws IOException, FirebaseMessagingException {
        FileInputStream serviceAccount =
                new FileInputStream("path/to/serviceAccountKey.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);

        Message message = Message.builder()
                .setToken(token) // token do dispositivo que vai receber a notificação
                .putData("title", title)
                .putData("body", body)
                .build();

        String response = FirebaseMessaging.getInstance().send(message);
        System.out.println("Successfully sent message: " + response);
    }
}

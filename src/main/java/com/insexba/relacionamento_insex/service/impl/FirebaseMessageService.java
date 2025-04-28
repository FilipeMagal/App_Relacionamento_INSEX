package com.insexba.relacionamento_insex.service.impl;

import com.google.firebase.FirebaseException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.insexba.relacionamento_insex.entity.NotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FirebaseMessageService {
    @Autowired
    private FirebaseMessaging firebaseMessaging;

    public String sendNotificationByToken(NotificationMessage notificationMessage){
        Notification notification = Notification
                .builder()
                .setTitle(notificationMessage.getTitle())
                .setBody(notificationMessage.getBody())
                .setImage(notificationMessage.getImage())
                .build();

        Message message = Message
                .builder()
                .setToken(notificationMessage.getRecipientToken())
                .setNotification(notification)
                .putAllData(notificationMessage.getData())
                .build();

        try{
            firebaseMessaging.send(message);
            return "Success Sending Notification";
        }catch (FirebaseMessagingException e){
            e.printStackTrace();
            return "Error Sending Notification";
        }
    }
}

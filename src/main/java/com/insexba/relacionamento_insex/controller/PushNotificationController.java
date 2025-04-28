package com.insexba.relacionamento_insex.controller;

import com.insexba.relacionamento_insex.entity.NotificationMessage;
import com.insexba.relacionamento_insex.entity.PushNotificationResponse;
import com.insexba.relacionamento_insex.service.impl.FirebaseMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
public class PushNotificationController {

    @Autowired
    FirebaseMessageService firebaseMessageService;





    @PostMapping("/notification/token")
    public ResponseEntity sendTokenNotification(@RequestBody NotificationMessage request) {
        firebaseMessageService.sendNotificationByToken(request);
        System.out.println("princr");
        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
    }

}

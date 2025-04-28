package com.insexba.relacionamento_insex;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@SpringBootApplication
public class RelacionamentoInsexApplication {

	@Bean
	FirebaseMessaging firebaseMessaging() throws IOException {
		GoogleCredentials googleCredentials = GoogleCredentials.fromStream(
				new ClassPathResource("novoprojetos-92ea7-firebase-adminsdk-fbsvc-996485ac11.json").getInputStream());
		FirebaseOptions firebaseOptions = FirebaseOptions.builder()
				.setCredentials(googleCredentials).build();
		FirebaseApp firebaseApp = FirebaseApp.initializeApp(firebaseOptions, "my-app");
		return FirebaseMessaging.getInstance(firebaseApp);
	}


	public static void main(String[] args) {
		SpringApplication.run(RelacionamentoInsexApplication.class, args);
	}

}

package com.example.gongdal.config.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.IOException;

@Configuration
@Slf4j
public class FirebaseConfig {

    @PostConstruct
    public void init() {
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath*:fcm/GongDal2 Firebase Service Account.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(resources[0].getInputStream()))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase application has been initialized");
            } else {
                log.info("Firebase application is already initialized");
            }

        } catch (FileNotFoundException e) {
            log.error("Firebase ServiceAccountKey FileNotFoundException: " + e.getMessage());
        } catch (IOException e) {
            log.error("FirebaseOptions IOException: " + e.getMessage());
        }
    }
}

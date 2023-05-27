package com.example.mediaServices.config;

import com.google.api.client.util.Value;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@ConfigurationProperties
@RequiredArgsConstructor
public class FirebaseConfig {

    @Value("${firebase.config.path}")
    private String firebaseConfigPath;

    /*@Bean
    public FileInputStream firebaseConfig() throws FileNotFoundException {
        return new FileInputStream(new File(firebaseConfigPath));
    }*/

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        //System.out.println("111"+firebaseConfigPath);
        //FileInputStream serviceAccount = new FileInputStream(firebaseConfigPath);


        /*FileInputStream serviceAccount =
                new FileInputStream("C:\\Users\\Albert\\Desktop\\4eme\\Spring boot\\PFA_backendCorr\\media-servives\\src\\main\\java\\com\\example\\mediaServices\\config\\firebase-adminsdk.json");
        System.out.println(serviceAccount);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        System.out.println("22: "+options.getProjectId());
        FirebaseApp.initializeApp(options);*/


        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(getClass().getResourceAsStream("/firebase-adminsdk.json")))
                .build();
        System.out.println(options.getProjectId());
        //System.out.println(options.t);
        FirebaseApp.initializeApp(options);

        return FirebaseApp.getInstance();
    }

    @Bean
    public Storage storage() throws IOException {
        return StorageOptions.newBuilder().setProjectId(firebaseApp().getOptions().getProjectId()).build().getService();
    }

}


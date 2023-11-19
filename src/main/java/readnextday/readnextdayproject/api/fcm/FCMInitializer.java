package readnextday.readnextdayproject.api.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
@Slf4j
public class FCMInitializer {

    private static final String FIREBASE_CONFIG_PATH = "test-297f3-firebase-adminsdk-8cotw-873b7cd07b.json";


    /**
     * ClassPathResource()는 resources 폴더 아래에 있는 괄호 안 경로를 찾는다.
     * 우리가 이동시킨 json파일을 찾아서 맞는 정보인지 확인한 후 FirebaseApp.initializeApp()을 통해서 실행한다.
     */
    @PostConstruct
    public void initialize() {
        try {
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(FIREBASE_CONFIG_PATH).getInputStream());
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(googleCredentials)
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            log.info(">>>>>>>>FCM error");
            log.error(">>>>>>FCM error message : " + e.getMessage());
        }
    }
}

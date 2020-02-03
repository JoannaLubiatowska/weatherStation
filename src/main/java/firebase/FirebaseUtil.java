package firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.SneakyThrows;
import model.hibernate.entity.WeatherCondition;

import java.io.IOException;

public final class FirebaseUtil {

    private static FirebaseUtil instance;

    public static FirebaseUtil getInstance() {
        return instance == null ? (instance = new FirebaseUtil()) : instance;
    }

    private final FirebaseApp app;

    @SneakyThrows
    private FirebaseUtil() {
        this.app = initializeApp();
    }

    private FirebaseApp initializeApp() throws IOException {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .build();

        return FirebaseApp.initializeApp(options);
    }

    public String send(WeatherCondition weatherCondition) throws FirebaseMessagingException {
        Notification weatherConditionNotification = Notification.builder()
                .setTitle("Powiadomienie pogodowe")
                .setBody(weatherCondition.getInferenceResult().getInferenceResultsDescription())
                .build();
        Message message = Message.builder()
                .setNotification(weatherConditionNotification)
                .setTopic(FirebaseAppTopic.weather.name())
                .build();
        return FirebaseMessaging.getInstance(app).send(message);
    }

//    public String send
}

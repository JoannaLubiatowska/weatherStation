package notifier;

import com.google.firebase.messaging.FirebaseMessagingException;
import controller.DataController;
import firebase.FirebaseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.hibernate.entity.WeatherCondition;

import java.util.Date;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class NotifierService {

    public void notifyLastConditionBefore(Date fireTime) throws FirebaseMessagingException {
        Optional<WeatherCondition> lastCondition = DataController.findLastClassifiedConditionBefore(fireTime);
        if (lastCondition.isPresent()) {
            WeatherCondition weatherCondition = lastCondition.get();
            String send = FirebaseUtil.getInstance().send(weatherCondition);
            log.info("Notification sent with response:\n {}", send);
        } else {
            log.info("Any conditions not found. Notification is not sent.");
        }
    }
}

package authservice.eventProducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class UserInfoProducer {
    private final KafkaTemplate<String, UserInfoEvent> kafkaTemplate;

    @Value("${spring.kafka.topic-json.name}")
    private String topicName;

    @Autowired
    UserInfoProducer(KafkaTemplate<String, UserInfoEvent> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEventToKafka(UserInfoEvent userInfoEvent){
        try {
            System.out.println("=== SENDING KAFKA MESSAGE ===");
            System.out.println("Topic: " + topicName);
            System.out.println("Event: " + userInfoEvent);
            
            kafkaTemplate.send(topicName, userInfoEvent).get();
            System.out.println("=== MESSAGE SENT SUCCESSFULLY ===");
        } catch (Exception e) {
            System.err.println("=== FAILED TO SEND KAFKA MESSAGE ===");
            e.printStackTrace();
        }
    }
}

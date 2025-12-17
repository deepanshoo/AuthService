package authservice.serializer;

import authservice.eventProducer.UserInfoEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class UserInfoEventSerializer implements Serializer<UserInfoEvent> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> map, boolean b) {
        // No configuration needed
    }

    @Override
    public byte[] serialize(String topic, UserInfoEvent userInfoEvent) {
        try {
            if (userInfoEvent == null) {
                return null;
            }
            System.out.println("=== SERIALIZING MESSAGE ===");
            System.out.println("Event: " + userInfoEvent);
            
            byte[] result = objectMapper.writeValueAsBytes(userInfoEvent);
            System.out.println("Serialized to: " + new String(result));
            return result;
        } catch (Exception ex) {
            System.err.println("=== SERIALIZATION FAILED ===");
            ex.printStackTrace();
            throw new RuntimeException("Failed to serialize UserInfoEvent", ex);
        }
    }

    @Override
    public void close() {
        // No resources to close
    }
}
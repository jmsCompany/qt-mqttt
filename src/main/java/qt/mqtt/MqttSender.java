package qt.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MqttSender {

    @Autowired private MqttClient client;

    @Value("${mqtt.qos}") private int qos;

    @Value("${mqtt.topic.prefix}")
    private String topicPrefix;

    public void sendMessage(final String topic, final Object content) {

        final String message = seralizeMessage(topic, content);
        System.out.println("Sending message: " + message);
        if (message != null) {
            sendMessage(topic, message);
        }
    }

    public static String seralizeMessage(final String topic, final Object content) {
        final Message message = new Message(content);
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
        	System.out.println("Error serializing message on topic: "+topic+"  with content: "+content +", error: " + e.getMessage());
        }
        return null;
    }

    private void sendMessage(final String topic, final String content) {
        final String targetTopic = topicPrefix + topic;
        System.out.println("Publishing message to "+targetTopic+": " + content);
        final MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(qos);
        try {
            client.publish(targetTopic, message);
        } catch (MqttException e) {
        	System.out.println("Error sending message: " + e.getMessage());
        }
        System.out.println("Message published");
    }
}
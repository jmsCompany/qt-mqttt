package qt.mqtt;

import java.util.UUID;
import javax.annotation.PreDestroy;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class MqttConfiguration {

 
    private MemoryPersistence persistence = new MemoryPersistence();
    private MqttClient client;

    @Value("${mqtt.broker}")
    private String broker;
    @Value("${mqtt.client.id}")
    private String clientId;
    @Value("${mqtt.username}")
    private String username;
    @Value("${mqtt.password}")
    private String password;

    @Bean
    public MqttClient getMqttClient() {
        try {
            if (clientId == null) {
                clientId = UUID.randomUUID().toString();
                System.out.println("Using a client: " + clientId);
            }
            client = new MqttClient(broker, clientId, persistence);
         
            final MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(username);
            connOpts.setPassword(password.toCharArray());
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: "+broker+" ...");
            client.connect(connOpts);
            System.out.println("Connected.");
        } catch (MqttException e) {
        	System.out.println("Error establishing MQTT connection: " + e.getMessage());
        }
        return client;
    }

    /**
     * Disconnect on destroy.
     */
    @PreDestroy
    public void destroy() {
        try {
            if (client != null) {
                client.disconnect();
                System.out.println("Disconnected from broker: " + broker);
            }
        } catch (MqttException e) {
        	System.out.println("Error disconnecting "+ e.getMessage());
        }
    }
}
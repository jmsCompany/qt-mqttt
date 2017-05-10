package qt.mqtt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MqttReceiver extends MqttCallbackAdapter {

	private Map<String, Collection<MqttCallback>> listeners = new HashMap<>();

	@Autowired
	private MqttClient client;

	@Value("${mqtt.qos}")
	private int qos;

	@Value("${mqtt.topic.prefix}")
	private String topicPrefix;

	@PostConstruct
	public void init() {
		//client.setCallback(this);
		System.out.println("MQTT receiver initialized.");
	}

	/**
	 * Registers a callback for receiving MQTT messages.
	 * @param topic topic to listen for messages.
	 * @param callback listener to register.
	 */
	public void addListener(final String topic, final MqttCallback callback) {
	    final String fullTopic = topicPrefix + topic;
	    System.out.println("Adding listener"+callback.getClass().getSimpleName()+" to topic '"+ fullTopic);
		Collection<MqttCallback> callbacksForTopic = this.listeners.get(fullTopic);
		if (callbacksForTopic == null) {
			callbacksForTopic = new ArrayList<MqttCallback>();
			try {
				this.listeners.put(fullTopic, callbacksForTopic);
				this.client.subscribe(fullTopic);
				System.out.println("Subscribed to topic " + fullTopic);
			} catch (MqttException e) {
				System.out.println("Error registering listener: " + e.getMessage());
			}
		}
		callbacksForTopic.add(callback);
	}

	@Override
	public void messageArrived(final String topic, final MqttMessage message) throws Exception {
		System.out.println("Message arrived at "+topic+" with payload: " + message);
		final Collection<MqttCallback> callbacks = this.listeners.get(topic);
		if (callbacks != null) {
			for (final MqttCallback mqttCallback : callbacks) {
				try {
					mqttCallback.messageArrived(topic, message);
				} catch (Exception e) {
					System.out.println("Exception in message processing: " + e.getMessage());
				}
			}
		}
	}

}
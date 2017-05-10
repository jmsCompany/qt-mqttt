package qt.mqtt;


import javax.annotation.PostConstruct;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeviceReceiver extends MqttReceiver {

	@Autowired private ToohotCallback toohotCallback;
	@Autowired private MqttClient client;
	
	@PostConstruct
	public void init() {
		 client.setCallback(this);
		 addListener("temp",toohotCallback);
	}



}
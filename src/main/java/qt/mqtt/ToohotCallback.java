package qt.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class ToohotCallback implements MqttCallback {

	@Autowired private MqttSender mqttSender;
	private ObjectMapper mapper = new ObjectMapper();
	@Override
	public void connectionLost(Throwable arg0) {
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {

	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		
		Device device= mapper.readValue(message.toString(), Device.class);
		if(device.getTemp()>200f)
		{
			Msg msg = new Msg(device.getId(),"too hot!");
			String returnMsg = mapper.writeValueAsString(msg);
			mqttSender.sendMessage(""+device.getId(), returnMsg);
		}
        
	}

}

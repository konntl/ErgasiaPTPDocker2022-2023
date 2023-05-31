package gr.upatras.httpmqttpublish;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    private final MqttClient mqttClient;

    public MessageController(@Value("${mqtt.broker.url}") String brokerUrl) throws MqttException {
        mqttClient = new MqttClient(brokerUrl, MqttClient.generateClientId());
        mqttClient.connect();
    }

    @PostMapping("/messages")
    public ResponseEntity<String> publishMessage(@RequestBody String message) {
        try {
            // Publish the message to an MQTT topic
            mqttClient.publish("topic", message.getBytes(), 0, false);
            return ResponseEntity.ok("Message published successfully.");
        } catch (MqttException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to publish message.");
        }
    }
}

package at.tgm.ac.mom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    // Topic Lagerdaten
    private static final String TOPIC ="warehouse-data";

    @GetMapping("/warehouse/send")
    public String sendMessage(@RequestParam(value = "data", defaultValue = "") String data) {
        if (data.isEmpty()) return "No data provided";
        kafkaTemplate.send(TOPIC, data); //Senden der Nachricht an Kafka
        return "SUCCESS"; // Bei Erfolg Rückmeldung
    }

}
package com.example.acctsearch.producer;

import com.example.acctsearch.config.KafkaTopicsProperties;
import com.example.acctsearch.event.SearchEvent;
import com.example.acctsearch.exception.EventPublishException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountSearchEventProducer {

    private final KafkaTemplate<String, SearchEvent> kafkaTemplate;
    private final KafkaTopicsProperties topics;

    public void publish(SearchEvent event) {
        String topic = topics.accountSearchEventsTopic();
        String key = event.accountId();

        try {
            kafkaTemplate.send(topic, key, event).get(3, TimeUnit.SECONDS);
            log.info("Published SearchEvent to topic='{}' with key='{}', correlationId='{}'", topic, key, event.correlationId());
        } catch (Exception ex) {
            log.error("Primary publish failed for topic='{}', key='{}', correlationId='{}'. Trying DLT.",
                    topic, key, event.correlationId(), ex);
            publishToDltBestEffort(event);
            throw new EventPublishException("Failed to publish search event to Kafka", ex);
        }
    }

    private void publishToDltBestEffort(SearchEvent event) {
        String dltTopic = topics.accountSearchEventsDltTopic();
        String key = event.accountId();

        kafkaTemplate.send(dltTopic, key, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("DLT publish also failed for topic='{}', key='{}', correlationId='{}'",
                                dltTopic, key, event.correlationId(), ex);
                        return;
                    }
                    log.warn("Event routed to DLT topic='{}' with key='{}', correlationId='{}'",
                            dltTopic, key, event.correlationId());
                });
    }
}

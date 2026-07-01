package com.example.recentsearch.consumer;

import com.example.recentsearch.event.SearchEvent;
import com.example.recentsearch.service.RecentSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "recentsearch.kafka", name = "consumer-enabled", havingValue = "true", matchIfMissing = true)
public class RecentSearchEventConsumer {

    private final RecentSearchService recentSearchService;

    @KafkaListener(
            topics = "${recentsearch.kafka.account-search-events-topic}",
            groupId = "${recentsearch.kafka.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(
            SearchEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.OFFSET) long offset
    ) {
        log.info("Consumed SearchEvent key='{}', offset='{}', correlationId='{}'", key, offset, event.correlationId());
        recentSearchService.saveFromEvent(event);
    }
}

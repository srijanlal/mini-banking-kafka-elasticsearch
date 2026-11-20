package com.example.acctsearch.producer;

import com.example.acctsearch.config.KafkaTopicsProperties;
import com.example.acctsearch.event.SearchEvent;
import com.example.acctsearch.exception.EventPublishException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountSearchEventProducerTest {

    @Mock
    private KafkaTemplate<String, SearchEvent> kafkaTemplate;

    @Mock
    private KafkaTopicsProperties topics;

    @InjectMocks
    private AccountSearchEventProducer producer;

    private SearchEvent event;

    @BeforeEach
    void setUp() {
        when(topics.accountSearchEventsTopic()).thenReturn("acct-search-events");
        event = new SearchEvent(
                "acct-1", "ACC001", "Account One", "SN001",
                "OFF01", "CUST01", Instant.now(), "system", "corr-id-1");
    }

    @Test
    @SuppressWarnings("unchecked")
    void publish_succeeds_whenKafkaSendCompletes() {
        // Arrange
        SendResult<String, SearchEvent> sendResult = mock(SendResult.class);
        CompletableFuture<SendResult<String, SearchEvent>> future =
                CompletableFuture.completedFuture(sendResult);
        when(kafkaTemplate.send(anyString(), anyString(), any(SearchEvent.class))).thenReturn(future);

        // Act + Assert — no exception thrown
        assertThatCode(() -> producer.publish(event)).doesNotThrowAnyException();
        verify(kafkaTemplate, times(1)).send("acct-search-events", "acct-1", event);
    }

    @Test
    @SuppressWarnings("unchecked")
    void publish_throwsEventPublishException_andRoutesDltOnKafkaFailure() {
        // Arrange — primary send fails; DLT send succeeds
        when(topics.accountSearchEventsDltTopic()).thenReturn("acct-search-events.DLT");
        CompletableFuture<SendResult<String, SearchEvent>> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("Kafka unavailable"));

        SendResult<String, SearchEvent> dltResult = mock(SendResult.class);
        CompletableFuture<SendResult<String, SearchEvent>> dltFuture =
                CompletableFuture.completedFuture(dltResult);

        when(kafkaTemplate.send(anyString(), anyString(), any(SearchEvent.class)))
                .thenReturn(failedFuture)
                .thenReturn(dltFuture);

        // Act + Assert
        assertThatThrownBy(() -> producer.publish(event))
                .isInstanceOf(EventPublishException.class)
                .hasMessageContaining("Failed to publish search event to Kafka");

        // DLT send must have been attempted
        verify(kafkaTemplate, times(2)).send(anyString(), anyString(), any(SearchEvent.class));
    }
}

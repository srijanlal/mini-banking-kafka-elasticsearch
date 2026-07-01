package com.example.recentsearch;

import com.example.recentsearch.repository.RecentSearchDocumentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(properties = {
    "spring.kafka.listener.auto-startup=false",
    "recentsearch.kafka.consumer-enabled=false"
})
class RecentSearchApplicationTests {

    @MockBean
    private RecentSearchDocumentRepository recentSearchDocumentRepository;

    @Test
    void contextLoads() {
    }
}

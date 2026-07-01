package com.example.acctsearch;

import com.example.acctsearch.repository.AccountDocumentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(properties = "acctsearch.data-loader.enabled=false")
class AcctSearchApplicationTests {

    @MockBean
    private AccountDocumentRepository accountDocumentRepository;

    @Test
    void contextLoads() {
    }
}

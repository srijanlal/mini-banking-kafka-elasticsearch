package com.example.recentsearch.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "recentsearch-index")
public class RecentSearchDocument {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String accountId;

    @Field(type = FieldType.Keyword)
    private String accountNumber;

    @Field(type = FieldType.Text)
    private String accountName;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private Instant clickedAt;

    @Field(type = FieldType.Keyword)
    private String username;
}

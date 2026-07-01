package com.example.acctsearch.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "acctsearch-index")
public class AccountDocument {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String accountNumber;

    @Field(type = FieldType.Keyword)
    private String shortName;

    @Field(type = FieldType.Text)
    private String accountName;

    @Field(type = FieldType.Keyword)
    private String customerId;

    @Field(type = FieldType.Keyword)
    private String officeCode;

    @Field(type = FieldType.Keyword)
    private String status;
}

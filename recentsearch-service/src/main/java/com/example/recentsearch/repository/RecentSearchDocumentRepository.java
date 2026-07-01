package com.example.recentsearch.repository;

import com.example.recentsearch.document.RecentSearchDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RecentSearchDocumentRepository extends ElasticsearchRepository<RecentSearchDocument, String> {

    Page<RecentSearchDocument> findAllByOrderByClickedAtDesc(Pageable pageable);
}
